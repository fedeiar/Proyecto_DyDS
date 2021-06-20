package dyds.catalog.alpha.fulllogic.modelo.repositorio;

import java.sql.*;
import java.util.ArrayList;

import javax.xml.crypto.Data;

public class DataBaseImplementation implements DataBase{

    private final int TIMEOUT_SEGUNDOS = 30;

    private static DataBaseImplementation instance;

    private Connection connection;
    private Statement statement;
    

    private DataBaseImplementation(){

    }

    public static DataBaseImplementation getInstance(){
        if (instance == null){
            instance = new DataBaseImplementation();
        }
        return instance;
    }

    //TODO: agregarle a cada método de la BD un throws, entonces en el modelo debemos usar try, catch, y en el catch podemos progpagar la excpecion hasta la vista.
    public void loadDatabase() {

        try{
            initConnectionToDataBase();
  
            ResultSet table = connection.getMetaData().getTables(null, null, "catalog", null);
            if(tableDoesntExists(table)){
                statement.executeUpdate("create table catalog (id INTEGER, title string PRIMARY KEY, extract string, source integer)");
            }
            
        } 
        catch (SQLException e) {
            //TODO: habría algún error que capturar acá?
            System.out.println(e.getMessage());
        }
        finally{
            closeConnectionToDataBase();
        }
    }

    private boolean tableDoesntExists(ResultSet table) throws SQLException{
        return !table.next();
    }

    private void initConnectionToDataBase() throws SQLException{
        String url = "jdbc:sqlite:./dictionary.db";
        connection = DriverManager.getConnection(url);
        statement = connection.createStatement();
        statement.setQueryTimeout(TIMEOUT_SEGUNDOS);  
    }

    private void closeConnectionToDataBase(){
        try{
            if(connection != null){
                connection.close();
            }
        }
        catch(SQLException e){
            //TODO: habría algún error que capturar acá?
            System.err.println("fallo el cierre de la conexion");
        }
    }
    

    public ArrayList<String> getTitles() throws SQLException {
        ArrayList<String> titles = new ArrayList<>();
        
        initConnectionToDataBase();

        ResultSet rs = statement.executeQuery("select * from catalog");
        while(rs.next()){
            titles.add(rs.getString("title"));
        }
        
        closeConnectionToDataBase();
        
        return titles;
    }

    public String getExtract(String title) throws SQLException {
        String extract = "";
        
        initConnectionToDataBase();

        ResultSet rs = statement.executeQuery("select * from catalog WHERE title = '" + title + "'" );
        rs.next();
        extract = rs.getString("extract");
        
        closeConnectionToDataBase();
        
        return extract;
    }

    public void saveInfo(String title, String extract) throws SQLException{
        initConnectionToDataBase();

        statement.executeUpdate("replace into catalog values(null, '"+ title + "', '"+ extract + "', 1)");
        
        closeConnectionToDataBase();
    }

    public void deleteEntry(String title) throws SQLException{
        try {
            initConnectionToDataBase();

            statement.executeUpdate("DELETE FROM catalog WHERE title = '" + title + "'" );
        }
        catch(SQLException e){
            System.err.println("Get title error " + e.getMessage());
        }
        finally{
            closeConnectionToDataBase();
        }
    }


/*
  public static void testDB() {

    Connection connection = null;
    try
    {
      // create a database connection
      connection = DriverManager.getConnection("jdbc:sqlite:./dictionary.db");
      Statement statement = connection.createStatement();
      statement.setQueryTimeout(30);  // set timeout to 30 sec.

      //statement.executeUpdate("drop table if exists person");
      //statement.executeUpdate("create table person (id integer, name string)");
      //statement.executeUpdate("insert into person values(1, 'leo')");
      //statement.executeUpdate("insert into person values(2, 'yui')");
      ResultSet rs = statement.executeQuery("select * from catalog");
      while(rs.next())
      {
        // read the result set
        System.out.println("id = " + rs.getInt("id"));
        System.out.println("title = " + rs.getString("title"));
        System.out.println("extract = " + rs.getString("extract"));
        System.out.println("source = " + rs.getString("source"));

      }
    }
    catch(SQLException e)
    {
      // if the error message is "out of memory",
      // it probably means no database file is found
      System.err.println(e.getMessage());
    }
    finally
    {
      try
      {
        if(connection != null)
          connection.close();
      }
      catch(SQLException e)
      {
        // connection close failed.
        System.err.println(e);
      }
    }
  }
  */
}
