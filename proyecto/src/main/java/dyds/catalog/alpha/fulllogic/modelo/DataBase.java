package dyds.catalog.alpha.fulllogic.modelo;

import java.sql.*;
import java.util.ArrayList;

public class DataBase {

  private Connection connection;
  Statement statement;

  public static void loadDatabase() {
    //If the database doesnt exists we create it
    String url = "jdbc:sqlite:./dictionary.db";

    try (Connection connection = DriverManager.getConnection(url)) {
      if (connection != null) {

        DatabaseMetaData meta = connection.getMetaData();
        System.out.println("The driver name is " + meta.getDriverName());
        //System.out.println("A new database has been created.");

        Statement statement = connection.createStatement();
        statement.setQueryTimeout(30);  // set timeout to 30 sec.

        statement.executeUpdate("create table catalog (id INTEGER, title string PRIMARY KEY, extract string, source integer)");
        //If the DB was created before, a SQL error is reported but it is not harmfull...
      }

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

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

  public ArrayList<String> getTitles() {
    ArrayList<String> titulosRecuperados = null;
    try {
      initConnectionDataBase();
      titulosRecuperados = recuperarTitulos();
      closeConnectionDataBase();
    }
    catch (SQLException e) {
      System.err.println("error in titles" + e.getMessage());
    }
    return titulosRecuperados;
  }

  public void saveInfo(String title, String extract) {
    try {
      initConnectionDataBase();
      guardarInformacion(title, extract);
      closeConnectionDataBase();
    }
    catch(SQLException e) {
      System.out.println("Error in saveinfo " + e.getMessage());
    }
  }

  public String getExtract(String title) {
    String informacionRecuperada = "";
    try {
      initConnectionDataBase();
      informacionRecuperada = recuperarInformacion(title);
      closeConnectionDataBase();
    }
    catch(SQLException e){
      System.err.println("Get title error " + e.getMessage());
    }
    return informacionRecuperada;
  }

  public void deleteEntry(String title) {
    try {
      initConnectionDataBase();
      eliminarInformacion(title);
      closeConnectionDataBase();
    }
    catch(SQLException e){
      System.err.println("Get title error " + e.getMessage());
    }
  }

  private void initConnectionDataBase() throws SQLException{
      connection = DriverManager.getConnection("jdbc:sqlite:./dictionary.db");
      statement = connection.createStatement();
      statement.setQueryTimeout(30);  // set timeout to 30 sec.
  }
  private void closeConnectionDataBase() throws SQLException{
      if(connection != null)
        connection.close();
  }

  private void guardarInformacion(String title, String extract) throws SQLException{
      statement.executeUpdate("replace into catalog values(null, '"+ title + "', '"+ extract + "', 1)");
  }
  private String recuperarInformacion(String title) throws SQLException{
    ResultSet rs = statement.executeQuery("select * from catalog WHERE title = '" + title + "'" );
    rs.next();
    return rs.getString("extract");
  }
  private void eliminarInformacion(String title) throws SQLException{
    statement.executeUpdate("DELETE FROM catalog WHERE title = '" + title + "'" );
  }
  private ArrayList<String> recuperarTitulos() throws SQLException {
    ArrayList<String> titles = new ArrayList<>();
    ResultSet rs = statement.executeQuery("select * from catalog");
    while(rs.next()) titles.add(rs.getString("title"));
    return titles;
  }
}
