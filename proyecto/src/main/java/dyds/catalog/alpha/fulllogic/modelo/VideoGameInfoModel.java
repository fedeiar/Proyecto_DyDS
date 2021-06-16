package dyds.catalog.alpha.fulllogic.modelo;

public interface VideoGameInfoModel {
    
    public String getLastSearchedPageIntroText();

    public String getLastSearchedPageTitle();

    public String getLastSearchedTerm();

    public void searchTermInWikipedia(String searchTerm);

    public String getUltimaBusquedaLocal();

    public Object[] getTotalTitulosRegistrados();
    
    public void setWikipediaSearchInfoListener(WikipediaSearchInfoListener wikipediaSearchInfoListener);
    public void setStoredInformationListener(StoredInfoListener storedInformationListener);

    public void storeLastSearchedPage();

    public void searchInLocalStorage(String termSearched);
    public void deleteFromLocalStorage(String termSearched);

}
