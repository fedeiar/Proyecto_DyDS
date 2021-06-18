package dyds.catalog.alpha.fulllogic.modelo;

import dyds.catalog.alpha.fulllogic.modelo.repositorio.DataBase;

public interface VideoGameInfoModel {
    
    public void setVideoGameInfoRepository(DataBase dataBase);

    public void setWikipediaSearchInfoListener(WikipediaSearchedInfoListener wikipediaSearchInfoListener);
    public void setStoredSearchedInformationListener(StoredSearchedInfoListener storedSearchedInfoListener);
    public void setStoredTitlesListener(StoredTitlesListener storedTitlesListener);
    public void setSavedLocallyInfoListener(SavedLocallyInfoListener savedLocallyInfoListener);
    public void setDeletedInfoListener(DeletedInfoListener deletedInfoListener);
    
    public WikipediaPage getLastWikiPageSearched();
    public WikipediaPage getLastLocallyStoredWikiPageSearched();
    public Object[] getTotalTitulosRegistrados();

    public void searchTermInWikipedia(String searchTerm);

    public void storeLastSearchedPage();

    public void searchInLocalStorage(String videoGameTitle);
    
    public void deleteFromLocalStorage(String videoGameTitle);

}
