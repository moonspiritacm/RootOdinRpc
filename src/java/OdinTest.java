
import java.util.Scanner;
import java.net.URL;
import java.net.MalformedURLException;

import com.googlecode.jsonrpc4j.JsonRpcHttpClient;
import com.googlecode.jsonrpc4j.ProxyUtil;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OdinTest {

    public static void main(String[] args) throws MalformedURLException, Throwable {

        JsonRpcHttpClient client = new JsonRpcHttpClient(new URL("http://10.3.240.199:8080/ODIN"));
        OdinRpcService service = ProxyUtil.createClientProxy(OdinRpcService.class.getClassLoader(), OdinRpcService.class, client);
        Scanner sc = new Scanner(System.in);
        String odin = sc.nextLine();
        System.out.println(service.testHello());
        System.out.println(service.testHelloToXX("fy"));
        System.err.println(service.getSimpleRegister(odin));
        System.err.println(service.getSimpleAP(odin));
        System.err.println(service.getSimpleVD(odin));
        System.err.println(service.getAllAP());
        System.err.println(service.getAllVD());
    }

}
