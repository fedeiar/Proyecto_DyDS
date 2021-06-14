package dyds.catalog.alpha.fulllogic.modelo.repositorio;

import java.util.ArrayList;

public interface DataBase{
    
    public void loadDatabase();

    public ArrayList<String> getTitles();

    public String getExtract(String title);

    public void saveInfo(String title, String extract);

    public void deleteEntry(String title);

}
