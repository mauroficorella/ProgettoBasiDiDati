package DB;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DataSource {

    String driver;
    String dbURL;
    String user;
    String password;

    public DataSource() throws IOException {
        Properties props = new Properties();
        FileInputStream in = new FileInputStream("logging.properties"); //apro il file properties creato per una gestione
                                                                              //più efficiente e aggiornabile del driver/url/username/password
        props.load(in);
        in.close();
        driver = props.getProperty("driver");
        dbURL = props.getProperty("url");
        user = props.getProperty("username");
        password = props.getProperty("password");
    }

    public Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName(driver);
        Connection connection = DriverManager.getConnection(dbURL,user, password);
        return connection;
    }

}
