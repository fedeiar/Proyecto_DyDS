package test;

import dyds.catalog.alpha.fulllogic.modelo.ModelModule;
import dyds.catalog.alpha.fulllogic.modelo.VideoGameInfoModel;
import dyds.catalog.alpha.fulllogic.modelo.WikipediaSearcher;
import dyds.catalog.alpha.fulllogic.modelo.WikipediaSearcherImpl;
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
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class testsIntegracion {
    VideoGameInfoModel videoGameInfoModel;
    WikipediaSearchPresenter wikipediaSearchPresenter;
    StoredInfoPresenter storedInfoPresenter;

    WikipediaSearchView wikipediaSearchView;
    StoredInfoView storedInfoView;

    @Before
    public void initSystem(){
        videoGameInfoModel = ModelModule.getInstance().setUpModel(DatabaseImplementation.getInstance(), new WikipediaSearcherImpl());

        wikipediaSearchPresenter = PresenterModule.getInstance().setUpWikipediaSearchPresenter(videoGameInfoModel);
        storedInfoPresenter = PresenterModule.getInstance().setUpStoredInfoPresenter(videoGameInfoModel);

        wikipediaSearchView = ViewModule.getInstance().setUpWikipediaSearchView(wikipediaSearchPresenter);
        storedInfoView = ViewModule.getInstance().setUpStoredInfoView(storedInfoPresenter);

        wikipediaSearchPresenter.setView(wikipediaSearchView);
        storedInfoPresenter.setView(storedInfoView);
    }

    //tests integracion
    @Test
    public void testIntegracionNuevaBusquedaExitosa() throws Exception{
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
        assertEquals(extract, DatabaseImplementation.getInstance().getExtract(title));
        assertEquals(true,DatabaseImplementation.getInstance().getTitles().contains(title));
    }

    @Test
    public void testIntegracionNoResultsToSaveLocally() throws Exception {
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

}
