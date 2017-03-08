
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {

    Connection connection = null;
    Statement statement = null;

    public static String dbFile = "C:\\Tomcat\\webapps\\ODIN\\resources\\db\\ppktest-1.db";
    private static Database instance = null;

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    private Database() {
        Boolean dbExists = true;
        if (!(new File(dbFile)).exists()) {
            dbExists = false;
        }
        init();
        createTables();
    }

    private void init() {
        System.out.println("Database.init() start ......");
        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dbFile);
            statement = connection.createStatement();
            statement.setQueryTimeout(30);
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error in Database.init(): " + e.toString());
        }
    }

    private void createTables() {
        System.out.println("Database.createTables() start ......");
        try {
            // blocks
            executeUpdate("CREATE TABLE IF NOT EXISTS blocks(block_index INTEGER PRIMARY KEY, block_hash TEXT UNIQUE, block_time INTEGER,block_nonce BIGINT)");
            executeUpdate("CREATE INDEX IF NOT EXISTS blocks_block_index_idx ON blocks (block_index)");

            // transactions
            executeUpdate("CREATE TABLE IF NOT EXISTS transactions(tx_index INTEGER PRIMARY KEY, tx_hash TEXT UNIQUE, block_index INTEGER, block_time INTEGER, source TEXT, destination TEXT, btc_amount INTEGER, fee INTEGER, data BLOB, supported BOOL DEFAULT 1)");
            executeUpdate("CREATE INDEX IF NOT EXISTS transactions_block_index_idx ON transactions (block_index)");
            executeUpdate("CREATE INDEX IF NOT EXISTS transactions_tx_index_idx ON transactions (tx_index)");
            executeUpdate("CREATE INDEX IF NOT EXISTS transactions_tx_hash_idx ON transactions (tx_hash)");

            // balances
            executeUpdate("CREATE TABLE IF NOT EXISTS balances(address TEXT, asset TEXT, amount INTEGER)");
            executeUpdate("CREATE INDEX IF NOT EXISTS address_idx ON balances (address)");
            executeUpdate("CREATE INDEX IF NOT EXISTS asset_idx ON balances (asset)");

            // sends
            executeUpdate("CREATE TABLE IF NOT EXISTS sends(tx_index INTEGER PRIMARY KEY, tx_hash TEXT UNIQUE, block_index INTEGER, source TEXT, destination TEXT, asset TEXT, amount INTEGER, validity TEXT)");
            executeUpdate("CREATE INDEX IF NOT EXISTS sends_block_index_idx ON sends (block_index)");

            // messages
            executeUpdate("CREATE TABLE IF NOT EXISTS messages(message_index INTEGER PRIMARY KEY, block_index INTEGER, command TEXT, category TEXT, bindings TEXT)");
            executeUpdate("CREATE INDEX IF NOT EXISTS block_index_idx ON messages (block_index)");

            // sys_parameters
            executeUpdate("CREATE TABLE IF NOT EXISTS sys_parameters (para_name VARCHAR(32) PRIMARY KEY, para_value TEXT )");

            // odins
            executeUpdate("CREATE TABLE IF NOT EXISTS odins (tx_index INTEGER PRIMARY KEY, tx_hash TEXT UNIQUE,block_index INTEGER,full_odin TEXT UNIQUE,short_odin INTEGER UNIQUE , register TEXT, admin TEXT,odin_set TEXT, validity TEXT)");
            executeUpdate("CREATE INDEX IF NOT EXISTS block_index_idx ON odins (block_index)");
            executeUpdate("CREATE INDEX IF NOT EXISTS tx_index ON odins (tx_index)");
            executeUpdate("CREATE INDEX IF NOT EXISTS full_odin ON odins (full_odin)");
            executeUpdate("CREATE INDEX IF NOT EXISTS short_odin ON odins (short_odin)");

            // odin_update_logs
            executeUpdate("CREATE TABLE IF NOT EXISTS odin_update_logs (log_id TEXT, tx_index INTEGER PRIMARY KEY,block_index INTEGER,full_odin TEXT, updater TEXT,destination TEXT,update_set TEXT, validity TEXT,required_confirmer TEXT);");
            executeUpdate("CREATE INDEX IF NOT EXISTS logid_idx ON odin_update_logs (log_id);");
            executeUpdate("CREATE INDEX IF NOT EXISTS odin_idx ON odin_update_logs (full_odin);");

        } catch (Exception e) {
            System.err.println("Error in Database.createTables(): " + e.toString());
        }
    }

    public void executeUpdate(String query) {
        try {
            (connection.createStatement()).executeUpdate(query);
            System.out.println("Execute Insert/Update query: " + query);
        } catch (SQLException e) {
            System.err.println("Error in Database.executeUpdate(): " + e.toString());
            System.err.println("Offending query: " + query);
        }
    }

    public ResultSet executeQuery(String query) {
        try {
            ResultSet rs = (connection.createStatement()).executeQuery(query);
            System.out.println("Execute Query query: " + query);
            return rs;
        } catch (SQLException e) {
            System.err.println("Error in Database.executeQuery(): " + e.toString());
            System.err.println("Offending query: " + query);
        }
        return null;
    }

}
