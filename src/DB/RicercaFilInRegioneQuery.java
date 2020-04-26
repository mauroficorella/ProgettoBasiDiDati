package DB;

import Entity.PuntoContorno;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class RicercaFilInRegioneQuery {
    private DataSource dataSource;

    public RicercaFilInRegioneQuery() throws IOException {
        dataSource = new DataSource();
    }

    public ArrayList<PuntoContorno> ricercaFilamentiCerchio(double lat, double longi, double raggio) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        ArrayList<PuntoContorno> fil = new ArrayList<>();

        try {
            connection = this.dataSource.getConnection();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

            String query =  "SELECT \"IDFIL\" as idfil " +
                    " FROM punti_contorni_filamenti " +
                    " EXCEPT " +
                    " SELECT \"IDFIL\" as idfil1 " +
                    " FROM punti_contorni_filamenti" +
                    " GROUP BY idfil1, \"GLON_CONT\", \"GLAT_CONT\" " +
                    " HAVING " + raggio + " <= sqrt(power((\"GLON_CONT\" - " + longi + "),2) + power((\"GLAT_CONT\" - " + lat + "),2)) ";

            rs = statement.executeQuery(query);
            while (rs.next()) {
                PuntoContorno id = new PuntoContorno(rs.getInt("idfil"));
                fil.add(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // release resources
            if (rs != null) {
                rs.close();
            }
            // release resources
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return fil;
    }

    public ArrayList<PuntoContorno> ricercaFilamentiQuadrato(double lat, double longi, double lato) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet rs = null;
        ArrayList<PuntoContorno> fil = new ArrayList<>();


        try {
            connection = this.dataSource.getConnection();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

            double limit1 = longi -(lato/2);
            double limit2 = longi +(lato/2);
            double limit3 = lat - (lato/2);
            double limit4 = lat + (lato/2);
            String query =  "SELECT \"IDFIL\" as idfil " +
                    " FROM punti_contorni_filamenti " +
                    " GROUP BY idfil " +
                    " HAVING min(\"GLON_CONT\")>= " + limit1 + " and max(\"GLON_CONT\")<= " + limit2 +
                    " and min(\"GLAT_CONT\")>= " + limit3 + " and max(\"GLAT_CONT\")<= " + limit4 +
                    " ORDER BY idfil";

            rs = statement.executeQuery(query);
            while (rs.next()) {
                PuntoContorno id = new PuntoContorno(rs.getInt("idfil"));
                fil.add(id);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // release resources
            if (rs != null) {
                rs.close();
            }
            // release resources
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return fil;
    }

}
