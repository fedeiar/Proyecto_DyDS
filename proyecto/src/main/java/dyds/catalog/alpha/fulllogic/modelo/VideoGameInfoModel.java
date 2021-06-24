package dyds.catalog.alpha.fulllogic.modelo;

import java.sql.SQLException;
import java.util.LinkedList;

import dyds.catalog.alpha.fulllogic.modelo.repositorio.DataBase;

public interface VideoGameInfoModel {
    
    public void setVideoGameInfoRepository(DataBase dataBase);

    public void setWikipediaSearchInfoListener(WikipediaSearchedInfoListener wikipediaSearchInfoListener);
    public void setStoredSearchedInformationListener(StoredSearchedInfoListener storedSearchedInfoListener);
    public void setDeletedInfoListener(DeletedInfoListener deletedInfoListener);
    public void setSuccesfullySavedLocalInfoListener(SuccesfullySavedInfoListener succesfullySavedLocalInfoListener);
    public void setNoResultsToSaveListener(NoResultsToSaveListener unsuccesfullySavedLocalInfoListener);
    
    public WikipediaPage getLastWikiPageSearched();
    public WikipediaPage getLastLocallyStoredWikiPageSearched();
    
    public Object[] getTotalTitulosRegistrados() throws Exception;

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
    public LinkedList<SuccesfullySavedInfoListener> getListOfSuccesfullySavedInfoListenerList();

    public LinkedList<NoResultsToSaveListener> getListNoResultsToSaveListener();
}
