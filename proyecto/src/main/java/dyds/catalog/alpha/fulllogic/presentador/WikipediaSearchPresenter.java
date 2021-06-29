package dyds.catalog.alpha.fulllogic.presentador;

import dyds.catalog.alpha.fulllogic.vista.WikipediaSearchView;

public interface WikipediaSearchPresenter {

    public void onEventSearchInWikipedia();
    
    public void onEventSaveSearchLocally();

    public void setView(WikipediaSearchView view);


    //methods for testing
    
    public boolean isActivellyWorking();

    public WikipediaSearchView getView();
}
