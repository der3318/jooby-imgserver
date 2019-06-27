package starter.gradle;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {

    public static List<String> getAbsolutePathListUnderDir(String _dirPath) {
        /** use walk api */
        try (Stream<Path> paths = Files.walk(Paths.get(_dirPath))) {
            return paths.map(Path::toAbsolutePath).map(Path::toString).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        /** return empty list */
        return Arrays.asList();
    }

    public static void unzip(String _zipFilePath, String _destDirPath, String _password) {
        /** use zip4j api */
        try {
            ZipFile zipFile = new ZipFile(_zipFilePath);
            if (zipFile.isEncrypted()) {
                zipFile.setPassword(_password);
            }
            zipFile.extractAll(_destDirPath);
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

}
