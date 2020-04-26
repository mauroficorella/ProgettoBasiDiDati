package DB;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class InclusioneQuery {

    public static boolean isStarInInclusione(int idstar) throws IOException, SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        DataSource dataSource = new DataSource();

        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);


            final String query = "SELECT DISTINCT idstella FROM inclusione WHERE idstella = '" + idstar + "' ;";

            //System.out.println(query);
            rs = statement.executeQuery(query);

            if(rs.first()) {
                rs.close();
                statement.close();
                connection.close();
                return true;
            }

        }catch(SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            // release resources
            if(rs != null){
                rs.close();
            }
            // release resources
            if(statement != null){
                statement.close();
            }
            if(connection  != null){
                connection.close();
            }
        }
        return false;
    }

    public static boolean updateInclusione(int idstar, int idfilamento) throws IOException, SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        DataSource dataSource = new DataSource();

        try {
            connection = dataSource.getConnection();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);


            final String query = "INSERT INTO inclusione VALUES ("+ idfilamento + ", " + idstar +") ON CONFLICT (idfilamento, idstella) DO NOTHING ;";

            System.out.println(query);
            statement.executeUpdate(query);


        }catch(SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        finally{
            // release resources
            if(rs != null){
                rs.close();
            }
            // release resources
            if(statement != null){
                statement.close();
            }
            if(connection  != null){
                connection.close();
            }
        }

        return false;
    }
}
