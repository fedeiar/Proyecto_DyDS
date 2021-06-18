package dyds.catalog.alpha.fulllogic.modelo;

import dyds.catalog.alpha.fulllogic.modelo.repositorio.DataBase;

public interface VideoGameInfoModel {
    
    public void setVideoGameInfoRepository(DataBase dataBase);
    public void setWikipediaSearchInfoListener(WikipediaSearchInfoListener wikipediaSearchInfoListener);
    public void setStoredInformationListener(StoredInfoListener storedInformationListener);
    
    public void searchTermInWikipedia(String searchTerm);

    public WikipediaPage getLastWikiPageSearched();
    public WikipediaPage getLastLocallyStoredWikiPageSearched();

    public Object[] getTotalTitulosRegistrados();

    public void storeLastSearchedPage();

    public void searchInLocalStorage(String videoGameTitle);
    public void deleteFromLocalStorage(String videoGameTitle);

}
