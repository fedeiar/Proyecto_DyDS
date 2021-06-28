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

public class ModelUnitTests {
    VideoGameInfoModel videoGameInfoModel;
    WikipediaSearchPresenter wikipediaSearchPresenter;
    StoredInfoPresenter storedInfoPresenter;

    WikipediaSearchView wikipediaSearchView;
    StoredInfoView storedInfoView;


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

}

