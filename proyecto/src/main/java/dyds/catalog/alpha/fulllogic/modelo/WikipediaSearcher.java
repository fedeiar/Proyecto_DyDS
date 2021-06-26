package dyds.catalog.alpha.fulllogic.modelo;


public interface WikipediaSearcher {

    public boolean searchPage(String searchedTerm) throws Exception;

    public String getLastSearchedTitle();
    
    public String getLastSearchedPageIntro();
}
