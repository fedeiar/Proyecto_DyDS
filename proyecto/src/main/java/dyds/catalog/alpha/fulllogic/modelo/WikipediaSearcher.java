package dyds.catalog.alpha.fulllogic.modelo;

import dyds.catalog.alpha.fulllogic.vista.*;

public interface WikipediaSearcher {

    public boolean searchPage(String terminoDeBusqueda);

    String getLastSearchedTitle();
    
    String getLastSearchedPageIntro();
}
