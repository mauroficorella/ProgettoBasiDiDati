package DB;

import controller.Utility.Utility;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class RicercaFilPerIDeNomeQuery {
    private static DataSource dataSource;
    public RicercaFilPerIDeNomeQuery() throws IOException {
        dataSource = new DataSource();
    }

    public ArrayList<Double> findCentroide(String id_nomeFil, String satellite) throws SQLException {
        ArrayList<Double> centroide = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;

        try {
            connection = this.dataSource.getConnection();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String query;
            if(Utility.isInteger(id_nomeFil)){
                query =  "SELECT avg(\"GLON_CONT\") as long, avg(\"GLAT_CONT\") as lat " +
                        " FROM punti_contorni_filamenti as punti JOIN filamenti as fil on punti.\"IDFIL\" = fil.\"IDFIL\" " +
                        " WHERE punti.\"IDFIL\" = '" + id_nomeFil + "' AND \"SATELLITE\" = '" + satellite + "'";
            } else {
                query =  "SELECT avg(\"GLON_CONT\") as long, avg(\"GLAT_CONT\") as lat " +
                        " FROM punti_contorni_filamenti as punti JOIN filamenti as fil on punti.\"IDFIL\" = fil.\"IDFIL\"" +
                        " WHERE \"NAME\" = '" + id_nomeFil + "' AND \"SATELLITE\" = '" + satellite + "'";
            }


            result = statement.executeQuery(query);

            while (result.next()) {
                Double avgLong = result.getDouble("long");
                Double avgLat = result.getDouble("lat");

                centroide.add(avgLong);
                centroide.add(avgLat);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            // release resources
            if(result != null){
                result.close();
            }
            // release resources
            if(statement != null){
                statement.close();
            }
            if(connection  != null){
                connection.close();
            }
        }
        return centroide;
    }

    public ArrayList<Double> findPerimeterExtension(String id_nomeFil, String satellite) throws SQLException {
        ArrayList<Double> extension = new ArrayList<>();
        ArrayList<Double> maxLong = new ArrayList<>();
        ArrayList<Double> minLong = new ArrayList<>();
        ArrayList<Double> maxLat = new ArrayList<>();
        ArrayList<Double> minLat = new ArrayList<>();
        Connection connection = null;
        Statement statement = null;
        ResultSet result= null;

        try {
            connection = this.dataSource.getConnection();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String queryForMaxLong;
            String queryForMinLong;
            String queryForMaxLat;
            String queryForMinLat;

            if(Utility.isInteger(id_nomeFil)){
                queryForMaxLong = "SELECT \"GLAT_CONT\" as lat , \"GLON_CONT\" as long " +
                        " FROM punti_contorni_filamenti as punti JOIN filamenti as fil on punti.\"IDFIL\" = fil.\"IDFIL\" " +
                        " WHERE punti.\"IDFIL\" ='" + id_nomeFil +"' and \"GLON_CONT\" =(SELECT (max(\"GLON_CONT\")) " +
                        " FROM punti_contorni_filamenti as punti JOIN filamenti as fil on punti.\"IDFIL\" = fil.\"IDFIL\"" +
                        " WHERE punti.\"IDFIL\" ='" + id_nomeFil + "' AND \"SATELLITE\" = '" + satellite + "' )";

                queryForMinLong = "SELECT \"GLAT_CONT\" as lat , \"GLON_CONT\" as long " +
                        " FROM punti_contorni_filamenti as punti JOIN filamenti as fil on punti.\"IDFIL\" = fil.\"IDFIL\"" +
                        " WHERE punti.\"IDFIL\" ='" + id_nomeFil +"' and \"GLON_CONT\" =(SELECT (min(\"GLON_CONT\")) " +
                        " FROM punti_contorni_filamenti as punti JOIN filamenti as fil on punti.\"IDFIL\" = fil.\"IDFIL\"" +
                        " WHERE punti.\"IDFIL\" ='" + id_nomeFil + "' AND \"SATELLITE\" = '" + satellite + "' )";

                queryForMaxLat = "SELECT \"GLAT_CONT\" as lat, \"GLON_CONT\" as long " +
                        " FROM punti_contorni_filamenti as punti JOIN filamenti as fil on punti.\"IDFIL\" = fil.\"IDFIL\"" +
                        " WHERE punti.\"IDFIL\" ='" + id_nomeFil +"' and \"GLAT_CONT\" =(SELECT (max(\"GLAT_CONT\")) " +
                        " FROM punti_contorni_filamenti as punti JOIN filamenti as fil on punti.\"IDFIL\" = fil.\"IDFIL\"" +
                        " WHERE punti.\"IDFIL\" ='" + id_nomeFil + "' AND \"SATELLITE\" = '" + satellite + "' )";

                queryForMinLat = "SELECT \"GLAT_CONT\" as lat, \"GLON_CONT\" as long " +
                        " FROM punti_contorni_filamenti as punti JOIN filamenti as fil on punti.\"IDFIL\" = fil.\"IDFIL\" " +
                        " WHERE punti.\"IDFIL\" ='" + id_nomeFil +"' and \"GLAT_CONT\" =(SELECT (min(\"GLAT_CONT\")) " +
                        " FROM punti_contorni_filamenti as punti JOIN filamenti as fil on punti.\"IDFIL\" = fil.\"IDFIL\"" +
                        " WHERE punti.\"IDFIL\" ='" + id_nomeFil + "' AND \"SATELLITE\" = '" + satellite + "' )";

            } else {
                queryForMaxLong = "SELECT \"GLAT_CONT\" as lat, \"GLON_CONT\" as long " +
                        " FROM punti_contorni_filamenti as punti JOIN filamenti as fil on punti.\"IDFIL\" = fil.\"IDFIL\"" +
                        " WHERE \"NAME\" ='" + id_nomeFil +"' and \"GLON_CONT\" =(SELECT (max(\"GLON_CONT\")) " +
                        " FROM punti_contorni_filamenti as punti JOIN filamenti as fil on punti.\"IDFIL\" = fil.\"IDFIL\"" +
                        " WHERE \"NAME\" ='" + id_nomeFil + "' AND \"SATELLITE\" = '" + satellite + "' )";

                queryForMinLong = "SELECT \"GLAT_CONT\" as lat , \"GLON_CONT\" as long " +
                        " FROM punti_contorni_filamenti as punti JOIN filamenti as fil on punti.\"IDFIL\" = fil.\"IDFIL\"" +
                        " WHERE \"NAME\" ='" + id_nomeFil +"' and \"GLON_CONT\" =(SELECT (min(\"GLON_CONT\")) " +
                        " FROM punti_contorni_filamenti as punti JOIN filamenti as fil on punti.\"IDFIL\" = fil.\"IDFIL\"" +
                        " WHERE \"NAME\" ='" + id_nomeFil + "' AND \"SATELLITE\" = '" + satellite + "' )";

                queryForMaxLat = "SELECT \"GLAT_CONT\" as lat, \"GLON_CONT\" as long " +
                        " FROM punti_contorni_filamenti as punti JOIN filamenti as fil on punti.\"IDFIL\" = fil.\"IDFIL\" " +
                        " WHERE \"NAME\" ='" + id_nomeFil +"' and \"GLAT_CONT\" =(SELECT (max(\"GLAT_CONT\")) " +
                        " FROM punti_contorni_filamenti as punti JOIN filamenti as fil on punti.\"IDFIL\" = fil.\"IDFIL\"" +
                        " WHERE \"NAME\" ='" + id_nomeFil + "' AND \"SATELLITE\" = '" + satellite + "' )";

                queryForMinLat = "SELECT \"GLAT_CONT\" as lat , \"GLON_CONT\" as long " +
                        " FROM punti_contorni_filamenti as punti JOIN filamenti as fil on punti.\"IDFIL\" = fil.\"IDFIL\" " +
                        " WHERE \"NAME\" ='" + id_nomeFil +"' and \"GLAT_CONT\" =(SELECT (min(\"GLAT_CONT\")) " +
                        " FROM punti_contorni_filamenti as punti JOIN filamenti as fil on punti.\"IDFIL\" = fil.\"IDFIL\"" +
                        " WHERE \"NAME\" ='" + id_nomeFil + "' AND \"SATELLITE\" = '" + satellite + "' )";
            }

            result = statement.executeQuery(queryForMaxLong);
            while(result.next()) {
                maxLong.add(result.getDouble("long"));
                maxLong.add(result.getDouble("lat"));
            }
            result.close();

            result = statement.executeQuery(queryForMinLong);
            while(result.next()) {
                minLong.add(result.getDouble("long"));
                minLong.add(result.getDouble("lat"));
            }
            result.close();

            result= statement.executeQuery(queryForMaxLat);
            while(result.next()) {
                maxLat.add(result.getDouble("long"));
                maxLat.add(result.getDouble("lat"));
            }
            result.close();

            result=statement.executeQuery(queryForMinLat);
            while(result.next()) {
                minLat.add(result.getDouble("long"));
                minLat.add(result.getDouble("lat"));
            }
            result.close();

            double avgLong = sqrt(pow((maxLong.get(0)-minLong.get(0)),2)+pow((maxLong.get(1)
                    -minLong.get(1)),2));
            double avgLat = sqrt(pow((maxLat.get(0)-minLat.get(0)),2)+pow((maxLat.get(1)
                    -minLat.get(1)),2));
            extension.add(avgLong);
            extension.add(avgLat);

        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            // release resources
            if(result != null){
                result.close();
            }
            // release resources
            if(statement != null){
                statement.close();
            }
            if(connection  != null){
                connection.close();
            }
        }
        return extension;
    }

    public int findNumberSegments(String id_nomeFil, String satellite) throws SQLException {
        int numSeg = 0;
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;

        try {
            connection = this.dataSource.getConnection();
            statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
            String query;
            if(Utility.isInteger(id_nomeFil)){
                query =  "SELECT count(DISTINCT \"IDBRANCH\") as numerosegmenti" +
                        " FROM punti_segmenti as segm JOIN filamenti as fil on segm.\"IDFIL\" = fil.\"IDFIL\" " +
                        " WHERE segm.\"IDFIL\" = '" + id_nomeFil + "' AND \"SATELLITE\" = '" + satellite + "'";
            } else {
                query =  "SELECT count(DISTINCT \"IDBRANCH\") as numerosegmenti" +
                        " FROM punti_segmenti as segm JOIN filamenti as fil ON segm.\"IDFIL\" = fil.\"IDFIL\"" +
                        " WHERE fil.\"NAME\" = '" + id_nomeFil + "' AND \"SATELLITE\" = '" + satellite + "'";
            }
            result = statement.executeQuery(query);

            while (result.next()) {
                numSeg = result.getInt("numerosegmenti");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            // release resources
            if(result != null){
                result.close();
            }
            // release resources
            if(statement != null){
                statement.close();
            }
            if(connection  != null){
                connection.close();
            }
        }
        return numSeg;
    }

    public boolean searchFilament(String id_nomeFil, String satellite) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        boolean esito = false;

        try {
            final String query;
            if(Utility.isInteger(id_nomeFil)){
                query =  "SELECT DISTINCT \"IDFIL\" " +
                        "FROM filamenti " +
                        "WHERE \"IDFIL\" = '" + id_nomeFil +"' AND \"SATELLITE\" = '" + satellite + "'";
            } else {
                query =  "SELECT DISTINCT \"NAME\" " +
                        "FROM filamenti " +
                        "WHERE \"NAME\" = '" + id_nomeFil +"' AND \"SATELLITE\" = '" + satellite + "'";
            }
            connection = this.dataSource.getConnection();
            statement = connection.prepareStatement(query);
            result = ((PreparedStatement) statement).executeQuery();

            if(result.next())
                esito = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            // release resources
            if(result != null){
                result.close();
            }
            // release resources
            if(statement != null){
                statement.close();
            }
            if(connection  != null){
                connection.close();
            }
        }
        return esito;
    }


}
