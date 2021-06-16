package dyds.catalog.alpha.fulllogic.modelo;

public interface VideoGameInfoModel {
    
    public String getLastSearchedPageIntroText();

    public String getLastSearchedPageTitle();

    public String getLastSearchedTerm();

    public void searchTermInWikipedia(String searchTerm);

    public String getLastLocalSearchedPage();
    public String getLastLocalSearchedTitle();

    public Object[] getTotalTitulosRegistrados();
    
    public void setWikipediaSearchInfoListener(WikipediaSearchInfoListener wikipediaSearchInfoListener);
    public void setStoredInformationListener(StoredInfoListener storedInformationListener);

    public void storeLastSearchedPage();

    public void searchInLocalStorage(String videoGameTitle);
    public void deleteFromLocalStorage(String videoGameTitle);

}
