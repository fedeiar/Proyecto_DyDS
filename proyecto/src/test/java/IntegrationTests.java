import dyds.catalog.alpha.fulllogic.modelo.ModelModule;
import dyds.catalog.alpha.fulllogic.modelo.VideoGameInfoModel;
import dyds.catalog.alpha.fulllogic.modelo.WikipediaSearcher;

import dyds.catalog.alpha.fulllogic.modelo.repositorio.Database;
import dyds.catalog.alpha.fulllogic.modelo.repositorio.DatabaseImplementation;
import dyds.catalog.alpha.fulllogic.presentador.PresenterModule;
import dyds.catalog.alpha.fulllogic.presentador.StoredInfoPresenter;
import dyds.catalog.alpha.fulllogic.presentador.WikipediaSearchPresenter;
import dyds.catalog.alpha.fulllogic.utils.Utilidades;
import dyds.catalog.alpha.fulllogic.vista.StoredInfoView;
import dyds.catalog.alpha.fulllogic.vista.ViewModule;
import dyds.catalog.alpha.fulllogic.vista.WikipediaSearchView;
import org.junit.Before;
import org.junit.Test;


import javax.swing.*;


import static org.junit.Assert.assertEquals;


import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import static org.mockito.Mockito.when;

public class IntegrationTests {
    VideoGameInfoModel videoGameInfoModel;
    Database database;
    StubWikipediaSearcher stubWikipediaSearcher;

    WikipediaSearchPresenter wikipediaSearchPresenter;
    StoredInfoPresenter storedInfoPresenter;

    WikipediaSearchView wikipediaSearchView;
    StoredInfoView storedInfoView;

    @Before
    public void initSystem(){
        database = DatabaseImplementation.getInstance();
        stubWikipediaSearcher = new StubWikipediaSearcher();
        videoGameInfoModel = ModelModule.getInstance().setUpModel(database, stubWikipediaSearcher);

        wikipediaSearchPresenter = PresenterModule.getInstance().setUpWikipediaSearchPresenter(videoGameInfoModel);
        storedInfoPresenter = PresenterModule.getInstance().setUpStoredInfoPresenter(videoGameInfoModel);

        wikipediaSearchView = ViewModule.getInstance().setUpWikipediaSearchView(wikipediaSearchPresenter);
        storedInfoView = ViewModule.getInstance().setUpStoredInfoView(storedInfoPresenter);

        wikipediaSearchPresenter.setView(wikipediaSearchView);
        storedInfoPresenter.setView(storedInfoView);
    }

    @Test
    public void testSuccesfullSearchInWikipedia() throws Exception{
        String title = "League of legends";
        String extract = "League of legends is a game of";
        String term = "league of legends";

        stubWikipediaSearcher.setValues(title,extract,true);

        wikipediaSearchView.setTermOfSearch(term);

        wikipediaSearchPresenter.onEventSearchInWikipedia();

        waitForWikipediaSearchPresenter();

        JTextPane jTextPane = new JTextPane();
        jTextPane.setContentType("text/html");
        jTextPane.setText(Utilidades.formatData(title,extract,term));

        assertEquals(jTextPane.getText(),wikipediaSearchView.getActualSearch());
    }

    @Test
    public void testNoResultsSearchInWikipedia() throws Exception{
        WikipediaSearcher mockWikipediaSearcher = mock(WikipediaSearcher.class);
        when(mockWikipediaSearcher.searchPage("Something")).thenReturn(false);
        videoGameInfoModel.setWikipediaSearcher(mockWikipediaSearcher);

        wikipediaSearchView.setTermOfSearch("League of Legends");
        wikipediaSearchPresenter.onEventSearchInWikipedia();

        waitForWikipediaSearchPresenter();

        JTextPane jTextPane = new JTextPane();
        jTextPane.setContentType("text/html");
        jTextPane.setText("No Results");

        assertEquals(jTextPane.getText(), wikipediaSearchView.getActualSearch());
    }

    @Test
    public void testSuccessfullySavedLocalInfo() throws Exception {
        String title = "League of legends";
        String extract = "League of legends is a game of";

        videoGameInfoModel.setLastPageTitleSearchedInWiki(title);
        videoGameInfoModel.setLastIntroPageSearchedInWiki(extract);
        videoGameInfoModel.setLastPageSearchedWithSuccessInWiki(true);

        wikipediaSearchPresenter.onEventSaveSearchLocally();

        waitForWikipediaSearchPresenter();
    
        assertEquals(true, storedInfoPresenter.getView().doesComboBoxContainsTitle(title));
    }

    @Test
    public void testNoResultsToSaveLocally() throws Exception {
        String title = "League of Legends";
        String extract = "No results";

        videoGameInfoModel.setLastPageTitleSearchedInWiki(title);
        videoGameInfoModel.setLastIntroPageSearchedInWiki(extract);
        videoGameInfoModel.setLastPageSearchedWithSuccessInWiki(false);

        wikipediaSearchPresenter.onEventSaveSearchLocally();

        waitForWikipediaSearchPresenter();

        assertEquals(false,storedInfoPresenter.getView().doesComboBoxContainsTitle(title));
    }

    @Test
    public void testFailedToSaveInDatabaseLastSuccesfullSearchInWikipedia() throws Exception{
        String title = "League of legends";
        String extract = "League of legends is a game of";

        Database database = mock(Database.class);
        doThrow(new Exception()).when(database).saveInfo(title, extract);
        videoGameInfoModel.setVideoGameInfoRepository(database);

        videoGameInfoModel.setLastPageSearchedWithSuccessInWiki(true);
        videoGameInfoModel.setLastPageTitleSearchedInWiki(title);
        videoGameInfoModel.setLastIntroPageSearchedInWiki(extract);

        wikipediaSearchPresenter.onEventSaveSearchLocally();

        waitForWikipediaSearchPresenter();
        
        assertEquals("Failed page saving", wikipediaSearchPresenter.getView().getDialogTestMessage());
    }

    @Test
    public void testGetPageIntroFromLocalStorage() throws Exception{
        String title = "League of legends";
        String extract = "League of legends is a game of";

        videoGameInfoModel.setLastPageSearchedWithSuccessInWiki(true);
        videoGameInfoModel.setLastPageTitleSearchedInWiki(title);
        videoGameInfoModel.setLastIntroPageSearchedInWiki(extract);

        wikipediaSearchPresenter.onEventSaveSearchLocally();

        waitForWikipediaSearchPresenter();

        int indexTitleInComboBox = storedInfoPresenter.getView().getTitleIndexInComboBox(title);
        // por como funciona comboBox, el siguiente método hará que se disparé el listener del comboBox alertando así al Presentador, es por ello que después
        // de invocarlo no llamamos al Presentador como hacemos normalmente.
        storedInfoView.setSelectedTitleIndex(indexTitleInComboBox); 

        waitForStoredInfoPresenter();

        JTextPane jTextPane = new JTextPane();
        jTextPane.setContentType("text/html");
        jTextPane.setText(Utilidades.formatData(title,extract));

        assertEquals(jTextPane.getText(), storedInfoView.getLocalStoredPageIntro());
    }

    @Test
    public void testDeleteFromLocalStorage() throws Exception {
        String title = "Z";
        String extract = "Z is a game";

        videoGameInfoModel.setLastPageSearchedWithSuccessInWiki(true);
        videoGameInfoModel.setLastPageTitleSearchedInWiki(title);
        videoGameInfoModel.setLastIntroPageSearchedInWiki(extract);

        wikipediaSearchPresenter.onEventSaveSearchLocally();
        waitForWikipediaSearchPresenter();

        int indexTitleInComboBox = storedInfoPresenter.getView().getTitleIndexInComboBox(title);
        // mismo problema que el test anterior
        storedInfoView.setSelectedTitleIndex(indexTitleInComboBox);

        waitForStoredInfoPresenter();

        storedInfoPresenter.onEventDeleteLocalEntryInfo();
        waitForStoredInfoPresenter();

        assertEquals(false, storedInfoPresenter.getView().doesComboBoxContainsTitle(title));
    }

    

    private void waitForWikipediaSearchPresenter() throws InterruptedException {
        while (wikipediaSearchPresenter.isActivellyWorking()) Thread.sleep(1);
    }

    private void waitForStoredInfoPresenter() throws InterruptedException {
        while (storedInfoPresenter.isActivellyWorking()) Thread.sleep(1);
    }

}
