package starter.gradle;

import com.google.common.io.Files;

import org.jdbi.v3.core.Jdbi;
import org.jooby.Request;
import org.jooby.Results;
import org.jooby.Route;
import org.jooby.Upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

public class HandlerForUpload implements Route.OneArgHandler {

    private App app;

    public HandlerForUpload(App _app) {
        this.app = _app;
    }

    @Override
    public Object handle(Request req) throws Throwable {
        /** database interface */
        Jdbi jdbi = this.app.require(Jdbi.class);
        /** parameters from request and generate unique url */
        Upload upload = req.file("file");
        String url = String.format("/upload/%s-%s", System.currentTimeMillis(), upload.name());
        /** create the file */
        File file = new File(String.format("./public%s", url));
        file.getParentFile().mkdirs();
        file.createNewFile();
        /** copy through channels */
        FileChannel inputChannel = null, outputChannel = null;
        try {
            inputChannel = new FileInputStream(upload.file()).getChannel();
            outputChannel = new FileOutputStream(file).getChannel();
            outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
        } finally {
            inputChannel.close();
            outputChannel.close();
        }
        if (Files.getFileExtension(file.getAbsolutePath()).toLowerCase().equals("zip")) {
            /** zip - extract and insert all entries to database */
            String destDirAbsolutePath = file.getAbsolutePath().replaceFirst("[.][^.]+$", "");
            Utils.unzip(file.getAbsolutePath(), destDirAbsolutePath, "");
            jdbi.useHandle(h -> {
                String sqlFormat = "INSERT INTO files (name, url) VALUES ('%s', '%s')";
                String publicDirAbsolutePath = new File("./public").getAbsolutePath();
                for(String path : Utils.getAbsolutePathListUnderDir(destDirAbsolutePath)) {
                    if(new File(path).isDirectory())    continue;
                    String entryName = path.replace(destDirAbsolutePath, upload.name().replaceFirst("[.][^.]+$", ""));
                    String entryURL = path.replace(publicDirAbsolutePath, "");
                    h.createUpdate(String.format(sqlFormat, entryName, entryURL)).execute();
                }
            });
        } else {
            /** not zip - insert to database */
            jdbi.useHandle(h -> {
                String sqlFormat = "INSERT INTO files (name, url) VALUES ('%s', '%s')";
                h.createUpdate(String.format(sqlFormat, upload.name(), url)).execute();
            });
        }
        /** redirect */
        return Results.redirect("/images");
    }

}
