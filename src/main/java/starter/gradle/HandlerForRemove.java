package starter.gradle;

import org.jdbi.v3.core.Jdbi;
import org.jooby.Request;
import org.jooby.Route;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class HandlerForRemove implements Route.OneArgHandler {

    private App app;

    public HandlerForRemove(App _app) {
        this.app = _app;
    }

    @Override
    public Object handle(Request req) {
        /** database interface */
        Jdbi jdbi = this.app.require(Jdbi.class);
        /** create response */
        Map<String, String> rsp = new HashMap<>();
        /** parameters from request */
        String url = req.param("url").value();
        /** remove from database */
        jdbi.useHandle(h -> h.createUpdate(String.format("DELETE FROM files WHERE url LIKE '%s'", url)).execute());
        /** destroy the file */
        if (new File(String.format("./public%s", url)).delete())    rsp.put("status", "removed");
        else    rsp.put("status", "error");
        return rsp;
    }

}
