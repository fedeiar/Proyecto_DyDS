package dyds.catalog.alpha.fulllogic.modelo;

import dyds.catalog.alpha.fulllogic.vista.*;

public interface WikipediaSearcher {

    public boolean searchPage(String terminoDeBusqueda) throws Exception;

    String getLastSearchedTitle();
    
    String getLastSearchedPageIntro();

    //for testing

    public void setValues(String title, String extract, boolean searchedPage);
}
