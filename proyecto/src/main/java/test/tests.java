package test;

import dyds.catalog.alpha.fulllogic.modelo.*;
import dyds.catalog.alpha.fulllogic.presentador.*;
import dyds.catalog.alpha.fulllogic.vista.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class tests {
    VideoGameInfoModel model;

    PresenterModule presenterModule;
    StoredInfoPresenter storedInfoPresenter;
    WikipediaSearchPresenter wikipediaSearchPresenter;

    StoredInfoView storedInfoView;
    WikipediaSearchView wikipediaSearchView;

    @Before
    public void setUp() throws Exception {
        //crear modelo
        model = ModelModule.getInstance().setUpModel();

        //crear presentadores
        storedInfoPresenter = presenterModule.getInstance().setUpStoredInfoView(model);
        wikipediaSearchPresenter = presenterModule.getInstance().setUpWikipediaSearchPresenter(model);

        //crear vista
        storedInfoView = ViewModule.getInstance().setUpStoredInfoView(storedInfoPresenter);
        wikipediaSearchView = ViewModule.getInstance().setUpWikipediaSearchView(wikipediaSearchPresenter);

        //set vista a cada presentador
        storedInfoPresenter.setView(storedInfoView);
        wikipediaSearchPresenter.setView(wikipediaSearchView);

    }

    @Test(timeout = 4000)
    public void testNewSearchInWikipedia() throws InterruptedException {
        setSearchTermInWikipediaSearchView("Fifa 21");
        wikipediaSearchPresenter.onEventSearchInWikipedia();
        waitForControllerTaskInSearchWikipedia();
        assertNotNull(wikipediaSearchView.getActualSearch());
    }

    private void setSearchTermInWikipediaSearchView(String searchTerm) {
        wikipediaSearchView.setSearchTerm(searchTerm);
    }

    private void setSelectedTitleInStoredInfoView(String selectTitle){
        storedInfoView.setSelectedStoredTitle(selectTitle);
    }

    private void waitForControllerTaskInSearchWikipedia() throws InterruptedException{
        while(wikipediaSearchPresenter.isActivellyWorking()) Thread.sleep(1);
    }
}

