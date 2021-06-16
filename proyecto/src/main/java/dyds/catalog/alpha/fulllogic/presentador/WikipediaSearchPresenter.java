package dyds.catalog.alpha.fulllogic.presentador;

import dyds.catalog.alpha.fulllogic.vista.WikipediaSearchView;
import dyds.catalog.alpha.fulllogic.vista.*;

public interface WikipediaSearchPresenter {

    public void onEventSearchInWikipedia();
    
    public void onEventSaveSearchLocally();

    public void setView(WikipediaSearchView view);
}
