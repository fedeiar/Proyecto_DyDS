package dyds.catalog.alpha.fulllogic.modelo;

import dyds.catalog.alpha.fulllogic.vista.*;

public interface WikipediaSearcher {

    public boolean searchPage(String searchedTerm) throws Exception;

    public String getLastSearchedTitle();
    
    public String getLastSearchedPageIntro();

    //for testing

    public void setValues(String title, String extract, boolean searchedPage);
}
