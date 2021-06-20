package dyds.catalog.alpha.fulllogic.modelo.repositorio;

import java.sql.SQLException;
import java.util.ArrayList;

public interface DataBase{
    
    public void loadDatabase();

    public ArrayList<String> getTitles() throws SQLException;

    public String getExtract(String title) throws SQLException;

    public void saveInfo(String title, String extract);

    public void deleteEntry(String title);

}
