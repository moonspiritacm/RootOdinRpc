
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.jsonrpc4j.JsonRpcServer;

public class OdinRpcServer extends HttpServlet {

    private JsonRpcServer rpcServer = null;

    public OdinRpcServer() {
        super();
        rpcServer = new JsonRpcServer(new OdinRpcServiceImpl(), OdinRpcService.class);
    }

    @Override
    protected void service(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        rpcServer.handle(request, response);
    }

}
