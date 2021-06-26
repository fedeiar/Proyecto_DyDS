package dyds.catalog.alpha.fulllogic.modelo.repositorio;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseImplementation implements Database{

    private final int TIMEOUT_SEGUNDOS = 30;

    private static DatabaseImplementation instance;

    private Connection connection;
    private Statement statement;
    

    private DatabaseImplementation(){

    }

    public static DatabaseImplementation getInstance(){
        if (instance == null){
            instance = new DatabaseImplementation();
        }
        return instance;
    }

    @Override public void loadDatabase(){
        try{
            initConnectionToDataBase();
  
            ResultSet table = connection.getMetaData().getTables(null, null, "catalog", null);
            if(tableDoesntExists(table)){
                statement.executeUpdate("create table catalog (id INTEGER, title string PRIMARY KEY, extract string, source integer)");
            }

            closeConnectionToDataBase();
            
        } 
        catch (SQLException e) {
            e.printStackTrace();
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

    private void closeConnectionToDataBase() throws SQLException{
        if(connection != null){
            connection.close();
        }
    }
    
    @Override public List<String> getTitles() throws SQLException {
        ArrayList<String> titles = new ArrayList<>();
        
        initConnectionToDataBase();

        ResultSet rs = statement.executeQuery("select * from catalog");
        while(rs.next()){
            titles.add(rs.getString("title"));
        }
        
        closeConnectionToDataBase();
        
        return titles;
    }

    @Override public String getExtract(String title) throws SQLException {
        String extract = "";
        
        initConnectionToDataBase();

        ResultSet rs = statement.executeQuery("select * from catalog WHERE title = '" + title + "'" );
        rs.next();
        extract = rs.getString("extract");
        
        closeConnectionToDataBase();
        
        return extract;
    }

    @Override public void saveInfo(String title, String extract) throws SQLException{
        initConnectionToDataBase();

        statement.executeUpdate("replace into catalog values(null, '"+ title + "', '"+ extract + "', 1)");
        
        closeConnectionToDataBase();
    }

    @Override public void deleteEntry(String title) throws SQLException{
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

}
