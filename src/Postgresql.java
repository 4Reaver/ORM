import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Postgresql {
	private static Connection connection = null;
	private static String user = "java";
	private static String password = "dtVK32.01H";
	private static String url = "jdbc:postgresql://localhost/ormdb";
	
	private static void createConnection() {
		try {
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection(url, user, password);
		} catch (ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getConnection() {
		if ( connection == null ) {
			createConnection();
		}
		
		return connection;
	}
	
	public static void closeConnection() {
		try {
			connection.close();
			connection = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
