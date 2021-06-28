package dyds.catalog.alpha.fulllogic.modelo;

import java.util.LinkedList;

import dyds.catalog.alpha.fulllogic.modelo.repositorio.Database;

public interface VideoGameInfoModel {
    
    public void setVideoGameInfoRepository(Database database);

    public void setPageFoundInWikipediaListener(Listener PageFoundInWikipediaListener);
    public void setPageNotFoundInWikipediaListener(Listener PageNotFoundInWikipediaListener);
    public void setSearchedStoredInfoListener(Listener storedSearchedInfoListener);
    public void setDeletedInfoListener(Listener deletedInfoListener);
    public void setSuccesfullySavedLocalInfoListener(Listener succesfullySavedLocalInfoListener);
    public void setNoResultsToSaveListener(Listener noResultsToSaveListener);
    
    public WikipediaPage getLastWikiPageSearched();
    public WikipediaPage getLastLocallyStoredWikiPageSearched();
    
    public Object[] getAllStoredTitles() throws Exception;

    public void searchTermInWikipedia(String searchTerm) throws Exception;

    public void storeLastSearchedPage() throws Exception;

    public void searchInLocalStorage(String videoGameTitle) throws Exception;
    
    public void deleteFromLocalStorage(String videoGameTitle) throws Exception;

    //setters for testing
    public void setWikipediaSearcher(WikipediaSearcher wikipediaSearcher);

    public void setLastPageSearchedWithSuccessInWiki(boolean value);

    public void setLastPageTitleSearchedInWiki(String lastPageTitleSearchedInWiki);

    public void setLastIntroPageSearchedInWiki(String lastIntroPageSearchedInWiki);

    //getters for testing

    public LinkedList<Listener> getListPageFoundInWikipediaListenerListenerList();

    public LinkedList<Listener> getListPageNotFoundInWikipediaListenerList();

    public LinkedList<Listener> getListSearchedStoredInfoListenerList();

    public LinkedList<Listener> getListDeletedInfoListenerList();

    public LinkedList<Listener> getListSuccesfullySavedInfoListenerList();

    public LinkedList<Listener> getListNoResultsToSaveListener();
}
