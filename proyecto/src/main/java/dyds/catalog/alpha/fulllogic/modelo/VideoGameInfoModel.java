package dyds.catalog.alpha.fulllogic.modelo;

import java.util.LinkedList;

import dyds.catalog.alpha.fulllogic.modelo.repositorio.DataBase;

public interface VideoGameInfoModel {
    
    public void setVideoGameInfoRepository(DataBase dataBase);

    public void setPageFoundInWikipediaListener(Listener PageFoundInWikipediaListener);
    public void setPageNotFoundInWikipediaListener(Listener PageNotFoundInWikipediaListener);
    public void setSearchedStoredInfoListenerList(Listener storedSearchedInfoListener);
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
    public LinkedList<Listener> getListOfSuccesfullySavedInfoListenerList();

    public LinkedList<Listener> getListNoResultsToSaveListener();
}
