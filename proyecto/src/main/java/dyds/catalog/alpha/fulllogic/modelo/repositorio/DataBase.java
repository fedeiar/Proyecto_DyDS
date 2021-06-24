package dyds.catalog.alpha.fulllogic.modelo.repositorio;

import java.sql.SQLException;
import java.util.ArrayList;

public interface DataBase{
    
    public void loadDatabase();

    public ArrayList<String> getTitles() throws Exception;

    public String getExtract(String title) throws Exception;

    public void saveInfo(String title, String extract) throws Exception;

    public void deleteEntry(String title) throws Exception;

}
