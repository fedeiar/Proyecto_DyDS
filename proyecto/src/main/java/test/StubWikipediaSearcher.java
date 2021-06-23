package test;

import dyds.catalog.alpha.fulllogic.modelo.WikipediaSearcher;

public class StubWikipediaSearcher implements WikipediaSearcher {
    String title = null;
    String extract = null;

    @Override
    public boolean searchPage(String terminoDeBusqueda) {
        title = "Fifa 21";
        extract = "Fifa 21 is a game of ...";
        return true;
    }

    @Override
    public String getLastSearchedTitle() {
        return title;
    }

    @Override
    public String getLastSearchedPageIntro() {
        return extract;
    }
}
