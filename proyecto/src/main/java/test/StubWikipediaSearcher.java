package test;

import dyds.catalog.alpha.fulllogic.modelo.WikipediaSearcher;

public class StubWikipediaSearcher implements WikipediaSearcher {
    private String title = null;
    private String extract = null;
    private boolean searchedPage;

    @Override
    public boolean searchPage(String terminoDeBusqueda) throws Exception {
        return searchedPage;
    }

    @Override
    public String getLastSearchedTitle() {
        return title;
    }

    @Override
    public String getLastSearchedPageIntro() {
        return extract;
    }

    //for testing

    public void setValues(String title, String extract, boolean searchedPage){
        this.title = title;
        this.extract = extract;
        this.searchedPage = searchedPage;
    }
}
