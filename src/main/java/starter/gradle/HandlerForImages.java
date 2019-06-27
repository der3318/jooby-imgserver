package starter.gradle;

import org.jdbi.v3.core.Jdbi;
import org.jooby.Request;
import org.jooby.Results;
import org.jooby.Route;

import java.util.List;

public class HandlerForImages implements Route.OneArgHandler {

    /** information of a file */
    public static class FileInfo {
        public String name, url;
        public FileInfo(String _name, String _url) {
            this.name = _name;
            this.url = _url;
        }
    }

    private App app;

    public HandlerForImages(App _app) {
        this.app = _app;
    }

    @Override
    public Object handle(Request req) {
        /** database interface */
        Jdbi jdbi = this.app.require(Jdbi.class);
        /** query images from database */
        String search = req.param("search").value("");
        List<FileInfo> fileInfoList = jdbi.withHandle( h -> {
            String token = "%" + search + "%";
            String sqlFormat = "SELECT name, url FROM files WHERE name LIKE '%s'";
            return h.createQuery(String.format(sqlFormat, token)).mapTo(FileInfo.class).list();
        });
        return Results.html("images").put("fileInfoList", fileInfoList).put("search", search);
    }

}
