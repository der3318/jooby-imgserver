package starter.gradle;

import org.jdbi.v3.core.Jdbi;
import org.jooby.Jooby;
import org.jooby.jdbc.Jdbc;
import org.jooby.jdbi.Jdbi3;
import org.jooby.json.Jackson;
import org.jooby.pebble.Pebble;

import java.util.List;
import java.util.stream.Collectors;

/**
 * image server application
 */
public class App extends Jooby {

    {
        /** static images */
        assets("/images/**");

        /** javascripts */
        assets("/javascripts/**");

        /** pebble */
        use(new Pebble("templates", ".html"));

        /** database */
        use(new Jdbc());
        use(new Jdbi3());

        /** json */
        use(new Jackson());

        onStart(() -> {
            /** registry mapper */
            Jdbi jdbi = require(Jdbi.class);
            jdbi.registerRowMapper(HandlerForImages.FileInfo.class,
                    (rs, ctx) -> new HandlerForImages.FileInfo(rs.getString("name"), rs.getString("url")));
            /** create database tables if needed */
            List<String> tableNameList = jdbi.withHandle(h -> {
                String sql = "SELECT name FROM sqlite_master WHERE type='table' ORDER BY name";
                return h.createQuery(sql).mapTo(String.class).stream().map(String::toLowerCase).collect(Collectors.toList());
            });
            if (tableNameList.contains("files")) return;
            jdbi.useHandle(h -> {
                String sql = "CREATE TABLE files (id INTEGER PRIMARY KEY, name VARCHAR(255), url VARCHAR(255))";
                h.createUpdate(sql).execute();
            });
        });

        /** GET, POST, PUT and DELETE - images */
        use("/images", new HandlerForImages(this));

        /** GET and POST - uploaded and upload */
        get("/upload/{suffix:**}", new HandlerForUploadedImg());
        post("/upload", new HandlerForUpload(this));

        /** DELETE - remove */
        delete("/remove", new HandlerForRemove(this));
    }

    public static void main(String[] args) {
        run(App::new, args);
    }
}
