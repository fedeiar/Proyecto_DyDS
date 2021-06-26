package dyds.catalog.alpha.fulllogic.modelo.repositorio;

import java.util.List;

public interface Database{
    
    public void loadDatabase();

    public List<String> getTitles() throws Exception;

    public String getExtract(String title) throws Exception;

    public void saveInfo(String title, String extract) throws Exception;

    public void deleteEntry(String title) throws Exception;

}
