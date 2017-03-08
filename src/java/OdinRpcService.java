
import java.util.ArrayList;
import java.util.HashMap;

public interface OdinRpcService {

    public String testHello();

    public String testHelloToXX(String xx);

    public String getSimpleRegister(String odin);

    public ArrayList<String> getSimpleAP(String odin);

    public HashMap<String, String> getSimpleVD(String odin);

    public HashMap<String, ArrayList<String>> getAllAP();

    public HashMap<String, HashMap<String, String>> getAllVD();
}
