package DB;

import Entity.Filamento;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class RicercaPerContrastoEllitticitaQuery {
	private static DataSource dataSource;

	public RicercaPerContrastoEllitticitaQuery() throws IOException {
		dataSource = new DataSource();
	}


	public ArrayList<Filamento> getNumerofilamenti(double brillanza, double min, double max, String satellite) throws SQLException {
		Connection connection = null;
		Statement statement = null;
		ResultSet rs = null;
		ArrayList<Filamento> numeroFilamenti = new ArrayList<>();
		Double contrast = 1+(brillanza/100);

		try{
			connection = this.dataSource.getConnection();
			statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);

			final String query_idFil = "SELECT DISTINCT (\"IDFIL\") as idfil FROM filamenti WHERE \"CONTRAST\" > '" +
					contrast + "' AND \"ELLIPTICITY\" >= '" + min + "' AND \"ELLIPTICITY\" <= '" + max + "' AND \"SATELLITE\" = '" + satellite + "'";
			rs = statement.executeQuery(query_idFil);
			while (rs.next()) {
				Filamento fil = new Filamento(rs.getInt("idfil"));
				fil.setIdfil(rs.getInt("idfil"));
				numeroFilamenti.add(fil);
			}
		}catch(Exception e){
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
		return numeroFilamenti;
	}
}