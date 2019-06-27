package starter.gradle;

import org.jooby.Request;
import org.jooby.Response;
import org.jooby.Route;

import java.io.File;

public class HandlerForUploadedImg implements Route.Handler {

    @Override
    public void handle(Request req, Response rsp) throws Throwable {
        rsp.download(new File(String.format("./public/upload/%s", req.param("suffix").value())));
    }

}
