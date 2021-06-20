package dyds.catalog.alpha.fulllogic.modelo;

import java.sql.SQLException;

import dyds.catalog.alpha.fulllogic.modelo.repositorio.DataBase;

public interface VideoGameInfoModel {
    
    public void setVideoGameInfoRepository(DataBase dataBase);

    public void setWikipediaSearchInfoListener(WikipediaSearchedInfoListener wikipediaSearchInfoListener);
    public void setStoredSearchedInformationListener(StoredSearchedInfoListener storedSearchedInfoListener);
    public void setDeletedInfoListener(DeletedInfoListener deletedInfoListener);
    public void setSuccesfullySavedLocalInfoListener(SuccesfullySavedLocalInfoListener succesfullySavedLocalInfoListener);
    public void setUnsuccesfullySavedLocalInfoListener(UnsuccesfullySavedLocalInfoListener unsuccesfullySavedLocalInfoListener);
    
    public WikipediaPage getLastWikiPageSearched();
    public WikipediaPage getLastLocallyStoredWikiPageSearched();
    public Object[] getTotalTitulosRegistrados() throws SQLException;

    public void searchTermInWikipedia(String searchTerm);

    public void storeLastSearchedPage();

    public void searchInLocalStorage(String videoGameTitle) throws SQLException;
    
    public void deleteFromLocalStorage(String videoGameTitle);

}
