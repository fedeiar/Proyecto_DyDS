package dyds.catalog.alpha.fulllogic.modelo;

import dyds.catalog.alpha.fulllogic.vista.MainWindow;

public interface WikipediaSearcher {

    public boolean searchPage(String terminoDeBusqueda);

    String getLastSearchedTitle();
    
    String getLastSearchedPageIntro();
}
