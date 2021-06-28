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
import java.util.List;

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
        when(mockWikipediaSearcher.searchPage("League of Legends")).thenReturn(true);
        when(mockWikipediaSearcher.getLastSearchedTitle()).thenReturn("League of Legends");
        when(mockWikipediaSearcher.getLastSearchedPageIntro()).thenReturn("League of Legends is a game ...");

        //setup model
        videoGameInfoModel = ModelModule.getInstance().setUpModel(new StubDataBase(), mockWikipediaSearcher);

        //set at least one listener
        Listener listener = mock(Listener.class);
        doNothing().when(listener).notifyListener(); //TODO: para que esta linea? si por default los void ya no hacen nada
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
        StubWikipediaSearcher stubWikipediaSearcher = new StubWikipediaSearcher();
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

        //get index of title, and selecting in combobox
        List<String> listOfTitles = DatabaseImplementation.getInstance().getTitles();
        java.util.Collections.sort(listOfTitles);
        int indexTitleInComboBox = listOfTitles.indexOf(title);
        storedInfoView.setSelectedTitleIndex(indexTitleInComboBox);
        waitForControllerTaskInStoredInfoPresenter();

        storedInfoPresenter.onEventDeleteLocalEntryInfo();
        waitForControllerTaskInStoredInfoPresenter();

        assertEquals(false,DatabaseImplementation.getInstance().getTitles().contains(title));
    }

    @Test
    public void testIntegracionGetPageIntroFromLocalStorage() throws Exception{
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
        waitForControllerTaskInSearchPresenter();

        //get index of title, and selecting in combobox
        List<String> listOfTitles = DatabaseImplementation.getInstance().getTitles();
        java.util.Collections.sort(listOfTitles);
        int indexTitleInComboBox = listOfTitles.indexOf(title);
        storedInfoView.setSelectedTitleIndex(indexTitleInComboBox);

        //waiting for save
        waitForControllerTaskInStoredInfoPresenter();

        //simulate JTextPane
        JTextPane JTP = new JTextPane();
        JTP.setContentType("text/html");
        JTP.setText(Utilidades.formatData(title,extract));

        //check results
        assertEquals(JTP.getText(),storedInfoView.getActualSearch());
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

