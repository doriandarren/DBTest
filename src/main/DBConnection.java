package main;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Base de Datos
 *  CREATE TABLE comments (
        id INT NOT NULL AUTO_INCREMENT,
        MYUSER VARCHAR(30) NOT NULL,
        EMAIL VARCHAR(30),
        WEBPAGE VARCHAR(100) NOT NULL,
        DATUM DATE NOT NULL,
        SUMMARY VARCHAR(40) NOT NULL,
        COMMENTS VARCHAR(400) NOT NULL,
        PRIMARY KEY (ID)
     );
 */



public class DBConnection {

	private static final String DB_TABLE = "comments";
	private final String name;
	private Connection connect;
	private Statement statement;
	private ResultSet resultSet;
	private PreparedStatement preparedStatement;
		
	
	public DBConnection(String name){
		this.name = name;
	}
	
	
	/**
	 * Abre la conexion a la base da datos
	 * @return 
	 * @throws Exception 
	 */
	public boolean connection() throws Exception{
				
		try {
			//Carga el driver Mysql
			Class.forName("com.mysql.jdbc.Driver");
			//
			connect = DriverManager
					.getConnection("jdbc:mysql://localhost/"+ name +"?"
							+ "user=root&password=1234");
						
			statement = connect.createStatement();
			
		} catch (Exception e) {
			close();
			throw e;
		}
		
		return statement!=null;		
	}

	
	/**
	 * INSERT INTO table values(default,?,?,?,?,?,?) ()
	 * 
	 * @param user
	 * @param email
	 * @param sumary
	 * @param comment
	 * @return 
	 * @throws SQLException 
	 */
	public int insert(String user, String email, String webPage, String sumary, String comment) 
						throws SQLException {
		
		int lastInsertId = -1;
		String strSql = "INSERT INTO "+ DB_TABLE + " values (default,?,?,?,?,?,?)";
		
		try {
			preparedStatement = connect.prepareStatement(strSql, Statement.RETURN_GENERATED_KEYS);
						
			preparedStatement.setString(1, user);
			preparedStatement.setString(2, email);
			preparedStatement.setString(3, webPage);
			preparedStatement.setDate(4, new java.sql.Date(System.currentTimeMillis()));
			preparedStatement.setString(5, sumary);
			preparedStatement.setString(6, comment);
			
			preparedStatement.executeUpdate();
			ResultSet rs = preparedStatement.getGeneratedKeys();
			
			if(rs.next()){
				lastInsertId = rs.getInt(1);
			}
			return lastInsertId;
		} catch (SQLException e) {
			close();
			throw e;
		} 
	}	
	
	
	
	public void delete(int id) throws SQLException{
		try {			
			preparedStatement = connect.prepareStatement("DELETE FROM " + DB_TABLE + " WHERE=?;");
			preparedStatement.setInt(1,id);
			preparedStatement.executeUpdate();			
		} catch (SQLException e) {
			close();
			throw e;
		}	
	}
	
	/**
	 * Elimina todos los registros de la tabla y reinicia la cuenta del id
	 * @throws SQLException
	 */
	public void deleteAll() throws SQLException{
		try {			
			
			preparedStatement = connect.prepareStatement("TRUNCATE " + DB_TABLE);
			preparedStatement.executeUpdate();
			
		} catch (SQLException e) {
			close();
			throw e;
		}
	}
	
	
	
	public void update(){
		
		
		
		
		
	}
	
	
	
	
	
	
	
	public ArrayList<Map> select(int id) throws SQLException{
		
		String strSql = "SELECT * FROM "+ DB_TABLE + " WHERE id=?";
		ArrayList<Map> map = null;
				
		try {			
						
			preparedStatement = connect.prepareStatement(strSql);						
			preparedStatement.setInt(1, id);
			resultSet = preparedStatement.executeQuery();
			map = resultSetToCollection(resultSet);
			
		} catch (SQLException e) {
			close();
			throw e;
		}
		return map;
	}	
	
	
	
	
	
	@SuppressWarnings("rawtypes")
	private ArrayList<Map> resultSetToCollection(ResultSet resultSet) throws SQLException { 
        // ResultSet is initially before the first data set
    	ArrayList<Map> list = new ArrayList<Map>(); 
    	
        while (resultSet.next()) {
        	HashMap<String,String> hashMap = new HashMap<String,String>(); 
        	String id = resultSet.getString("id");
            String user = resultSet.getString("myuser");
            String email = resultSet.getString("email");
            String webpage = resultSet.getString("webpage");
            String summary = resultSet.getString("summary");
            Date date = resultSet.getDate("datum");
            String comments = resultSet.getString("comments");
       
            hashMap.put("id",id);
            hashMap.put("user",user);
            hashMap.put("email",email);
            hashMap.put("webpage",webpage);
            hashMap.put("summary",summary);
            hashMap.put("date",date.toString());
            hashMap.put("comments",comments);
            
            list.add(hashMap);  
        }
        
        return list; 
    }

	
	
	
	
	
	/**
	 * Cierra la conexcion
	 */
	public void close() {		
		try {
			if(resultSet!=null){
				resultSet.close();
				resultSet=null;
			}
			if(statement !=null){
				statement.close();
				statement=null;
			}
			if(connect!=null){
				connect.close();
				connect=null;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}		
	}	
	
}
