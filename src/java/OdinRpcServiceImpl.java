
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OdinRpcServiceImpl implements OdinRpcService {

    @Override
    public String testHello() {
        return "hello";
    }

    @Override
    public String testHelloToXX(String xx) {
        return "hello " + xx;
    }

    @Override
    public String getSimpleRegister(String odin) {
        Database db = Database.getInstance();
        if (odin != null) {
            System.out.println("invoke getSimpleRegister(), odin=" + odin);
            ResultSet rs = db.executeQuery("SELECT register FROM odins WHERE full_odin='" + odin + "' or short_odin='" + odin + "'");
            try {
                if (rs.next()) {
                    String register = rs.getString("register");
                    System.out.println("invoke getSimpleRegister(String odin), result=" + register);
                    return register;
                } else {
                    return null;
                }
            } catch (SQLException e) {
                System.err.println("Error in getSimpleRegister(): " + e.toString());
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public ArrayList<String> getSimpleAP(String odin) {
        ArrayList<String> strAP = new ArrayList<>();
        Database db = Database.getInstance();
        Integer count = 0;
        strAP.add("ERROR");
        if (odin != null) {
            System.out.println("invoke getSimpleAP(), odin=" + odin);
            ResultSet rs = db.executeQuery("SELECT odin_set FROM odins WHERE full_odin='" + odin + "' or short_odin='" + odin + "'");
            try {
                if (rs.next()) {
                    String odin_set = rs.getString("odin_set");
                    System.out.println("invoke getSimpleAP(String odin), result=" + odin_set);

                    JSONObject tmp = new JSONObject(odin_set);
                    JSONObject ap = tmp.getJSONObject("ap_set");
                    Iterator it = ap.keys();
                    while (it.hasNext()) {
                        String key = (String) it.next();
                        String value = ap.getJSONObject(key).getString("url");
                        strAP.add(value);
                        count++;
                    }
                    strAP.set(0, count.toString());
                }
            } catch (SQLException | JSONException e) {
                System.err.println("Error in getSimpleAP(): " + e.toString());
            }
        }
        return strAP;
    }

    public HashMap<String, String> getSimpleVD(String odin) {
        HashMap<String, String> mapVD = new HashMap<>();
        Database db = Database.getInstance();
        if (odin != null) {
            System.out.println("invoke getSimpleVD(), odin=" + odin);
            ResultSet rs = db.executeQuery("SELECT odin_set FROM odins WHERE full_odin='" + odin + "' or short_odin='" + odin + "'");
            try {
                if (rs.next()) {
                    String odin_set = rs.getString("odin_set");
                    System.out.println("invoke getSimpleVD(String odin), result=" + odin_set);

                    JSONObject tmp = new JSONObject(odin_set);
                    JSONObject jsonVD = tmp.getJSONObject("vd_set");
                    Iterator it = jsonVD.keys();
                    while (it.hasNext()) {
                        String key = (String) it.next();
                        String value = (String) jsonVD.get(key);
                        mapVD.put(key, value);
                    }
                }
            } catch (SQLException | JSONException e) {
                System.err.println("Error in getSimpleIP(): " + e.toString());
            }
        }
        return mapVD;
    }

    @Override
    public HashMap<String, ArrayList<String>> getAllAP() {
        HashMap<String, ArrayList<String>> mapAP = new HashMap<>();
        Database db = Database.getInstance();
        ResultSet rs = db.executeQuery("SELECT * FROM odins");
        try {
            while (rs.next()) {
                ArrayList<String> strAP = new ArrayList<>();
                strAP.add("ERROR");
                Integer count = 0;
                String odin = rs.getString("full_odin");
                String odin_set = rs.getString("odin_set");
                JSONObject tmp = new JSONObject(odin_set);
                if (!tmp.isNull("ap_set")) {
                    JSONObject ap = tmp.getJSONObject("ap_set");
                    Iterator it = ap.keys();
                    while (it.hasNext()) {
                        String key = (String) it.next();
                        String value = ap.getJSONObject(key).getString("url");
                        strAP.add(value);
                        count++;
                    }
                }
                strAP.set(0, count.toString());
                mapAP.put(odin, strAP);
            }
        } catch (SQLException | JSONException e) {
            System.err.println("Error in getSimpleIP(): " + e.toString());
        }
        return mapAP;
    }

    public HashMap<String, HashMap<String, String>> getAllVD() {
        HashMap<String, HashMap<String, String>> mapVD = new HashMap<>();
        Database db = Database.getInstance();
        ResultSet rs = db.executeQuery("SELECT * FROM odins");
        try {
            while (rs.next()) {
                HashMap<String, String> strVD = new HashMap<>();
                String odin = rs.getString("full_odin");
                String odin_set = rs.getString("odin_set");
                JSONObject tmp = new JSONObject(odin_set);
                if (!tmp.isNull("vd_set")) {
                    JSONObject vd = tmp.getJSONObject("vd_set");
                    Iterator it = vd.keys();
                    while (it.hasNext()) {
                        String key = (String) it.next();
                        String value = (String) vd.get(key);
                        strVD.put(key, value);
                    }
                }
                mapVD.put(odin, strVD);
            }
        } catch (SQLException | JSONException e) {
            System.err.println("Error in getSimpleIP(): " + e.toString());
        }
        return mapVD;
    }

}
