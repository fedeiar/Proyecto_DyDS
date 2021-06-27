package test;

import dyds.catalog.alpha.fulllogic.modelo.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import dyds.catalog.alpha.fulllogic.modelo.repositorio.Database;
import dyds.catalog.alpha.fulllogic.modelo.repositorio.DatabaseImplementation;
import dyds.catalog.alpha.fulllogic.presentador.PresenterModule;
import dyds.catalog.alpha.fulllogic.presentador.StoredInfoPresenter;
import dyds.catalog.alpha.fulllogic.presentador.WikipediaSearchPresenter;
import dyds.catalog.alpha.fulllogic.utils.Utilidades;
import dyds.catalog.alpha.fulllogic.vista.StoredInfoView;
import dyds.catalog.alpha.fulllogic.vista.ViewModule;
import dyds.catalog.alpha.fulllogic.vista.WikipediaSearchView;
import org.junit.Test;

import javax.swing.*;

public class tests {
    VideoGameInfoModel videoGameInfoModel;
    WikipediaSearchPresenter wikipediaSearchPresenter;
    StoredInfoPresenter storedInfoPresenter;

    WikipediaSearchView wikipediaSearchView;
    StoredInfoView storedInfoView;

    //tests unitarios

    @Test(timeout = 4000)
    public void testNewSearchInWikipediaAndFound() throws Exception {
        //Mock wikipediaSearcher
        WikipediaSearcher mockWikipediaSearcher = mock(WikipediaSearcher.class);

        //Mocking methods in wikipediaSearcher
        //TODO tengo que poner un valor al parametro del searchPage?
        when(mockWikipediaSearcher.searchPage("League of Legends")).thenReturn(true);
        when(mockWikipediaSearcher.getLastSearchedTitle()).thenReturn("League of Legends");
        when(mockWikipediaSearcher.getLastSearchedPageIntro()).thenReturn("League of Legends is a game ...");

        //setup model
        videoGameInfoModel = ModelModule.getInstance().setUpModel(new StubDataBase(), mockWikipediaSearcher);

        //set at least one listener
        Listener listener = mock(Listener.class);
        doNothing().when(listener).notifyListener();
        videoGameInfoModel.setPageFoundInWikipediaListener(listener);

        //init test
        videoGameInfoModel.searchTermInWikipedia("League of Legends");

        //check results
        verify(videoGameInfoModel.getListPageFoundInWikipediaListenerListenerList().getFirst()).notifyListener();
        assertEquals("League of Legends", videoGameInfoModel.getLastWikiPageSearched().getTitle());
    }

    @Test(timeout = 4000)
    public void testNewSearchInWikipediaButNotFound() throws Exception {
        //Mock wikipediaSearcher
        WikipediaSearcher mockWikipediaSearcher = mock(WikipediaSearcher.class);

        //Mocking methods in wikipediaSearcher
        //TODO tengo que poner un valor al parametro del searchPage?
        when(mockWikipediaSearcher.searchPage("League of Legends")).thenReturn(false);
        when(mockWikipediaSearcher.getLastSearchedTitle()).thenReturn("No results");
        when(mockWikipediaSearcher.getLastSearchedPageIntro()).thenReturn("No results");

        videoGameInfoModel = ModelModule.getInstance().setUpModel(new StubDataBase(), mockWikipediaSearcher);

        //set at least one listener
        Listener listener = mock(Listener.class);
        doNothing().when(listener).notifyListener();
        videoGameInfoModel.setPageNotFoundInWikipediaListener(listener);

        //init test
        videoGameInfoModel.searchTermInWikipedia("League of Legends");

        //check results
        verify(videoGameInfoModel.getListPageNotFoundInWikipediaListenerList().getFirst()).notifyListener();
        assertNull(videoGameInfoModel.getLastWikiPageSearched().getPageIntro());
    }

    @Test
    public void testSuccesfullySaveLocally() throws Exception {
        //Stub dataBase
        Database stubDataBase = new StubDataBase();
        videoGameInfoModel = new VideoGameInfoModelImpl(stubDataBase, mock(WikipediaSearcher.class));

        //Supongo que la ultima busqueda fue realizada exitosamente
        //seteo una ultima busqueda
        videoGameInfoModel.setLastPageSearchedWithSuccessInWiki(true);
        videoGameInfoModel.setLastPageTitleSearchedInWiki("League of Legends");
        videoGameInfoModel.setLastIntroPageSearchedInWiki("League of Legends is a game of ...");

        //set at least one listener
        Listener listener = mock(Listener.class);
        doNothing().when(listener).notifyListener();
        videoGameInfoModel.setSuccesfullySavedLocalInfoListener(listener);

        //metodo guardar informacion localmente
        videoGameInfoModel.storeLastSearchedPage();

        //revisar si el resultado es el esperado
        verify(videoGameInfoModel.getListSuccesfullySavedInfoListenerList().getFirst()).notifyListener();
        assertEquals("League of Legends is a game of ...", stubDataBase.getExtract(null));
    }

    @Test
    public void testNoResultsToSaveLocally() throws Exception {
        //Stub database
        Database stubDataBase = new StubDataBase();
        videoGameInfoModel = new VideoGameInfoModelImpl(stubDataBase,mock(WikipediaSearcher.class));

        //Supongo que la ultima busqueda fue realizada exitosamente
        //seteo una ultima busqueda
        videoGameInfoModel.setLastPageSearchedWithSuccessInWiki(false);
        videoGameInfoModel.setLastPageTitleSearchedInWiki("League of Legends");
        videoGameInfoModel.setLastIntroPageSearchedInWiki("League of Legends is a game of ...");

        //set at least one listener
        Listener listener = mock(Listener.class);
        doNothing().when(listener).notifyListener();
        videoGameInfoModel.setNoResultsToSaveListener(listener);

        //metodo guardar informacion localmente
        videoGameInfoModel.storeLastSearchedPage();

        //revisar si el resultado es el esperado
        verify(videoGameInfoModel.getListNoResultsToSaveListener().getFirst()).notifyListener();
    }

    @Test
    public void testDeleteFromLocalStorage() throws Exception {
        //Stub database
        Database stubDataBase = new StubDataBase();
        //set something in database
        stubDataBase.saveInfo("Something", "Something");

        //set up stubDatabase
        videoGameInfoModel = new VideoGameInfoModelImpl(stubDataBase,mock(WikipediaSearcher.class));

        //set at least one listener
        Listener listener = mock(Listener.class);
        doNothing().when(listener).notifyListener();
        videoGameInfoModel.setDeletedInfoListener(listener);

        //metodo eliminar informacion localmente
        videoGameInfoModel.deleteFromLocalStorage("Something");

        //check results
        verify(videoGameInfoModel.getListDeletedInfoListenerList().getFirst()).notifyListener();
        assertNull(stubDataBase.getExtract(null));
    }

    //tests integracion
    @Test
    public void testIntegracionNuevaBusquedaExitosa() throws Exception{
        initSystem();

        String title = "League of legends";
        String extract = "League of legends is a game of";
        String term = "league of legends";

        //Stub WikipediaSearcher and set values
        WikipediaSearcher stubWikipediaSearcher = new StubWikipediaSearcher();
        stubWikipediaSearcher.setValues(title,extract,true);

        //set StubWikipediaSearcher
        videoGameInfoModel.setWikipediaSearcher(stubWikipediaSearcher);

        //Simular el ingreso de datos a la vista
        wikipediaSearchView.setTermOfSearch(term);

        //search button
        wikipediaSearchPresenter.onEventSearchInWikipedia();

        //wait for search
        waitForControllerTaskInSearchPresenter();

        //simulate JtextPane
        JTextPane JTP = new JTextPane();
        JTP.setContentType("text/html");
        JTP.setText(Utilidades.formatData(title,extract,term));

        //check results
        assertEquals(JTP.getText(),wikipediaSearchView.getActualSearch());
    }

    @Test
    public void testIntegracionNuevaBusquedaSinExito() throws Exception{
        initSystem();

        //mock WikipediaSearcher
        WikipediaSearcher mockWikipediaSearcher = mock(WikipediaSearcher.class);
        when(mockWikipediaSearcher.searchPage("Something")).thenReturn(false);
        videoGameInfoModel.setWikipediaSearcher(mockWikipediaSearcher);

        //Simular el ingreso de datos a la vista
        wikipediaSearchView.setPageIntroText("League of Legends");
        wikipediaSearchPresenter.onEventSearchInWikipedia();

        //simulate JtextPane
        JTextPane JTP = new JTextPane();
        JTP.setContentType("text/html");
        JTP.setText("No Results");

        //check results
        assertEquals(JTP.getText(),wikipediaSearchView.getActualSearch());
    }

    @Test
    public void testIntegracionSuccesfullySaveLocally() throws Exception {
        initSystem();

        //setting last search
        String title = "League of legends";
        String extract = "League of legends is a game of";

        //simulate sucessfully last search
        videoGameInfoModel.setLastPageSearchedWithSuccessInWiki(true);
        videoGameInfoModel.setLastPageTitleSearchedInWiki(title);
        videoGameInfoModel.setLastIntroPageSearchedInWiki(extract);

        //calling event
        wikipediaSearchPresenter.onEventSaveSearchLocally();

        //waiting for save
        waitForControllerTaskInSearchPresenter();

        //check results
        assertEquals(extract,DatabaseImplementation.getInstance().getExtract(title));
        assertEquals(true,DatabaseImplementation.getInstance().getTitles().contains(title));
    }

    @Test
    public void testIntegracionNoResultsToSaveLocally() throws Exception {
        initSystem();

        //setting last search
        String title = "League of Legends";
        String extract = "No results";

        //simulate last failed search
        videoGameInfoModel.setLastPageSearchedWithSuccessInWiki(false);

        //calling event
        wikipediaSearchPresenter.onEventSaveSearchLocally();

        //waiting for save
        waitForControllerTaskInSearchPresenter();

        //check results
        assertEquals(false,DatabaseImplementation.getInstance().getTitles().contains("League of Legends"));
    }

    @Test
    public void testIntegracionGetPageIntroFromLocalStorage() throws Exception{
        initSystem();

        //setting last search
        String title = "Half-Life (series)";
        String extract = "Half-Life is a series of first-person shooter (FPS) games developed and published by Valve. The games combine shooting combat, puzzles, and storytelling. The original Half-Life, Valve`s first product, was released in 1998 for Windows to critical and commercial success. Players control Gordon Freeman, a scientist who must survive an alien invasion. The innovative scripted sequences were influential on the FPS genre, and the game inspired numerous community-developed mods, including the multiplayer games Counter-Strike and Day of Defeat. Half-Life was followed by the expansions Opposing Force (1999), Blue Shift (2001) and Decay (2001), developed by Gearbox Software. In 2004, Valve released Half-Life 2 to further success, with a new setting and characters and physics-based gameplay. It was followed by the extra level Lost Coast (2005) and the episodic sequels Episode One (2006) and Episode Two (2007). The first game in the Portal series, set in the same universe as Half-Life, was released in 2007. Over the following decade, numerous Half-Life games were canceled, including Episode Three, a version of Half-Life 3, and games developed by Junction Point Studios and Arkane Studios. In 2020, after years of speculation, Valve released its flagship virtual reality game, Half-Life: Alyx. Set before Half-Life 2, players control Freeman`s ally Alyx Vance in her quest to defeat the alien Combine.";

        //select title in combobox
        storedInfoView.cleanPageIntroText();
        storedInfoView.setSelectedTitleIndex(0);

        //waiting for save
        waitForControllerTaskInStoredInfoPresenter();

        //simulate JTextPane
        JTextPane JTP = new JTextPane();
        JTP.setContentType("text/html");
        JTP.setText(Utilidades.formatData(title,extract));

        //check results
        assertEquals(JTP.getText(),storedInfoView.getActualSearch());
    }

    @Test
    public void testIntegracionDeleteFromLocalStorage() throws Exception {
        initSystem();

        //setting last search
        String title = "Z";
        String extract = "Z is a game";
        //simulate new search saved succesfully
        videoGameInfoModel.setLastPageSearchedWithSuccessInWiki(true);
        videoGameInfoModel.setLastPageTitleSearchedInWiki(title);
        videoGameInfoModel.setLastIntroPageSearchedInWiki(extract);

        wikipediaSearchPresenter.onEventSaveSearchLocally();
        waitForControllerTaskInSearchPresenter();
        //select from local storage
        storedInfoView.setSelectedTitleIndex(5);
        waitForControllerTaskInStoredInfoPresenter();

        storedInfoPresenter.onEventDeleteLocalEntryInfo();
        waitForControllerTaskInStoredInfoPresenter();

        assertEquals(false,DatabaseImplementation.getInstance().getTitles().contains(title));
    }

    //methods for testing
    private void waitForControllerTaskInSearchPresenter() throws InterruptedException {
        while (wikipediaSearchPresenter.isActivellyWorking()) Thread.sleep(1);
    }

    private void waitForControllerTaskInStoredInfoPresenter() throws InterruptedException {
        while (storedInfoPresenter.isActivellyWorking()) Thread.sleep(1);
    }

    public void initSystem(){
        videoGameInfoModel = ModelModule.getInstance().setUpModel(DatabaseImplementation.getInstance(), new WikipediaSearcherImpl());

        wikipediaSearchPresenter = PresenterModule.getInstance().setUpWikipediaSearchPresenter(videoGameInfoModel);
        storedInfoPresenter = PresenterModule.getInstance().setUpStoredInfoPresenter(videoGameInfoModel);

        wikipediaSearchView = ViewModule.getInstance().setUpWikipediaSearchView(wikipediaSearchPresenter);
        storedInfoView = ViewModule.getInstance().setUpStoredInfoView(storedInfoPresenter);

        wikipediaSearchPresenter.setView(wikipediaSearchView);
        storedInfoPresenter.setView(storedInfoView);
    }

}

