package test;

import dyds.catalog.alpha.fulllogic.modelo.ModelModule;
import dyds.catalog.alpha.fulllogic.modelo.VideoGameInfoModel;
import dyds.catalog.alpha.fulllogic.modelo.WikipediaSearcher;
import dyds.catalog.alpha.fulllogic.modelo.WikipediaSearcherImpl;
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
import org.mockito.Mockito;

import javax.swing.*;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class IntegrationTests {
    VideoGameInfoModel videoGameInfoModel;
    WikipediaSearchPresenter wikipediaSearchPresenter;
    StoredInfoPresenter storedInfoPresenter;

    WikipediaSearchView wikipediaSearchView;
    StoredInfoView storedInfoView;

    @Before
    public void initSystem(){
        videoGameInfoModel = ModelModule.getInstance().setUpModel(DatabaseImplementation.getInstance(), new StubWikipediaSearcher());

        wikipediaSearchPresenter = PresenterModule.getInstance().setUpWikipediaSearchPresenter(videoGameInfoModel);
        storedInfoPresenter = PresenterModule.getInstance().setUpStoredInfoPresenter(videoGameInfoModel);

        wikipediaSearchView = ViewModule.getInstance().setUpWikipediaSearchView(wikipediaSearchPresenter);
        storedInfoView = ViewModule.getInstance().setUpStoredInfoView(storedInfoPresenter);

        wikipediaSearchPresenter.setView(wikipediaSearchView);
        storedInfoPresenter.setView(storedInfoView);
    }

    @Test
    public void testIntegracionNuevaBusquedaExitosa() throws Exception{
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
        waitForWikipediaSearchPresenter();

        //simulate JtextPane
        JTextPane JTP = new JTextPane();
        JTP.setContentType("text/html");
        JTP.setText(Utilidades.formatData(title,extract,term));

        //check results
        assertEquals(JTP.getText(),wikipediaSearchView.getActualSearch());
    }

    @Test
    public void testIntegracionNuevaBusquedaSinExito() throws Exception{
        //mock WikipediaSearcher
        WikipediaSearcher mockWikipediaSearcher = mock(WikipediaSearcher.class);
        when(mockWikipediaSearcher.searchPage("Something")).thenReturn(false);
        videoGameInfoModel.setWikipediaSearcher(mockWikipediaSearcher);

        //Simular el ingreso de datos a la vista
        wikipediaSearchView.setTermOfSearch("League of Legends");
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
        waitForWikipediaSearchPresenter();

        //check results
        assertEquals(extract, DatabaseImplementation.getInstance().getExtract(title));
        assertEquals(true,DatabaseImplementation.getInstance().getTitles().contains(title));
    }

    @Test
    public void testIntegracionNoResultsToSaveLocally() throws Exception {
        //setting last search
        String title = "League of Legends";
        String extract = "No results";
        videoGameInfoModel.setLastPageTitleSearchedInWiki(title);
        videoGameInfoModel.setLastIntroPageSearchedInWiki(extract);

        //simulate last failed search
        videoGameInfoModel.setLastPageSearchedWithSuccessInWiki(false);

        //calling event
        wikipediaSearchPresenter.onEventSaveSearchLocally();

        //waiting for save
        waitForWikipediaSearchPresenter();

        //check results
        assertEquals(false,DatabaseImplementation.getInstance().getTitles().contains(title));
    }

    @Test
    public void testFailedToSaveLastSuccesfulSearchInDatabase(){
        try{
            //enviroment setup
            Database database = mock(Database.class);
            doThrow().when(database).saveInfo(Mockito.anyString(), Mockito.anyString());
            videoGameInfoModel.setVideoGameInfoRepository(database);

            String title = "League of legends";
            String extract = "League of legends is a game of";

            videoGameInfoModel.setLastPageSearchedWithSuccessInWiki(true);
            videoGameInfoModel.setLastPageTitleSearchedInWiki(title);
            videoGameInfoModel.setLastIntroPageSearchedInWiki(extract);

            //functionality execution
            wikipediaSearchPresenter.onEventSaveSearchLocally();

            waitForWikipediaSearchPresenter();

            //compare result
            fail();
        }
        catch(Exception e){

        }

    }

    @Test
    public void testIntegracionDeleteFromLocalStorage() throws Exception {
        //setting last search
        String title = "Z";
        String extract = "Z is a game";

        //simulate new search saved succesfully
        videoGameInfoModel.setLastPageSearchedWithSuccessInWiki(true);
        videoGameInfoModel.setLastPageTitleSearchedInWiki(title);
        videoGameInfoModel.setLastIntroPageSearchedInWiki(extract);

        wikipediaSearchPresenter.onEventSaveSearchLocally();
        waitForWikipediaSearchPresenter();

        //get index of title, and selecting in combobox
        List<String> listOfTitles = DatabaseImplementation.getInstance().getTitles();
        java.util.Collections.sort(listOfTitles);
        int indexTitleInComboBox = listOfTitles.indexOf(title);
        storedInfoView.setSelectedTitleIndex(indexTitleInComboBox);
        waitForStoredInfoPresenter();

        storedInfoPresenter.onEventDeleteLocalEntryInfo();
        waitForStoredInfoPresenter();

        assertEquals(false,DatabaseImplementation.getInstance().getTitles().contains(title));
    }

    @Test
    public void testIntegracionGetPageIntroFromLocalStorage() throws Exception{
        //setting last search
        String title = "League of legends";
        String extract = "League of legends is a game of";

        //simulate sucessfully last search
        videoGameInfoModel.setLastPageSearchedWithSuccessInWiki(true);
        videoGameInfoModel.setLastPageTitleSearchedInWiki(title);
        videoGameInfoModel.setLastIntroPageSearchedInWiki(extract);

        //calling event
        wikipediaSearchPresenter.onEventSaveSearchLocally();
        waitForWikipediaSearchPresenter();

        //get index of title, and selecting in combobox
        List<String> listOfTitles = DatabaseImplementation.getInstance().getTitles();
        java.util.Collections.sort(listOfTitles);
        int indexTitleInComboBox = listOfTitles.indexOf(title);
        storedInfoView.setSelectedTitleIndex(indexTitleInComboBox);

        //waiting for save
        waitForStoredInfoPresenter();

        //simulate JTextPane
        JTextPane JTP = new JTextPane();
        JTP.setContentType("text/html");
        JTP.setText(Utilidades.formatData(title,extract));

        //check results
        assertEquals(JTP.getText(),storedInfoView.getActualSearch());
    }

    //methods for testing

    private void waitForWikipediaSearchPresenter() throws InterruptedException {
        while (wikipediaSearchPresenter.isActivellyWorking()) Thread.sleep(1);
    }

    private void waitForStoredInfoPresenter() throws InterruptedException {
        while (storedInfoPresenter.isActivellyWorking()) Thread.sleep(1);
    }

}
