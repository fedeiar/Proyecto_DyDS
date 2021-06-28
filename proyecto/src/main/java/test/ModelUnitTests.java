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

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import javax.xml.crypto.dsig.spec.ExcC14NParameterSpec;

import java.util.List;

public class ModelUnitTests {
    VideoGameInfoModel videoGameInfoModel;
    StubDataBase stubDataBase;

    WikipediaSearchPresenter wikipediaSearchPresenter;
    StoredInfoPresenter storedInfoPresenter;

    WikipediaSearchView wikipediaSearchView;
    StoredInfoView storedInfoView;

    

    @Before
    public void setUpEnviroment(){
        stubDataBase = new StubDataBase();
        videoGameInfoModel = ModelModule.getInstance().setUpModel(stubDataBase, mock(WikipediaSearcher.class));
    }


    @Test(timeout = 4000)
    public void testNewSearchInWikipediaAndFound() throws Exception {
        //enviorment setup
        String title = "League of Legends";
        String extract = "League of Legends is a game of ...";
        
        //Mock wikipediaSearcher
        WikipediaSearcher mockWikipediaSearcher = mock(WikipediaSearcher.class);

        //Mocking methods in wikipediaSearcher
        when(mockWikipediaSearcher.searchPage(title)).thenReturn(true);
        when(mockWikipediaSearcher.getLastSearchedTitle()).thenReturn(title);
        when(mockWikipediaSearcher.getLastSearchedPageIntro()).thenReturn(extract);

        //setup model
        videoGameInfoModel.setWikipediaSearcher(mockWikipediaSearcher);

        //set at least one listener
        Listener listener = mock(Listener.class);
        videoGameInfoModel.setPageFoundInWikipediaListener(listener);

        //init test
        videoGameInfoModel.searchTermInWikipedia(title);

        //check results
        verify(videoGameInfoModel.getListPageFoundInWikipediaListenerListenerList().getFirst()).notifyListener();
        assertEquals(title, videoGameInfoModel.getLastWikiPageSearched().getTitle());
    }

    @Test(timeout = 4000)
    public void testNewSearchInWikipediaButNotFound() throws Exception {
        //enviorment setup
        String title = "League of Legends";
        String extract = "";

        //Mock wikipediaSearcher
        WikipediaSearcher mockWikipediaSearcher = mock(WikipediaSearcher.class);

        //Mocking methods in wikipediaSearcher
        when(mockWikipediaSearcher.searchPage(title)).thenReturn(false);
        when(mockWikipediaSearcher.getLastSearchedTitle()).thenReturn(extract);
        when(mockWikipediaSearcher.getLastSearchedPageIntro()).thenReturn(extract);

        videoGameInfoModel.setWikipediaSearcher(mockWikipediaSearcher);

        //set at least one listener
        Listener listener = mock(Listener.class);
        videoGameInfoModel.setPageNotFoundInWikipediaListener(listener);

        //init test
        videoGameInfoModel.searchTermInWikipedia(title);

        //check results
        verify(videoGameInfoModel.getListPageNotFoundInWikipediaListenerList().getFirst()).notifyListener();
        assertNull(videoGameInfoModel.getLastWikiPageSearched().getPageIntro());
    }

    @Test
    public void testSuccesfullySaveLocally() throws Exception {
        //enviroment setup
        String title = "League of Legends";
        String extract = "League of Legends is a game of ...";

        //Supongo que la ultima busqueda fue realizada exitosamente
        //seteo una ultima busqueda
        videoGameInfoModel.setLastPageTitleSearchedInWiki(title);
        videoGameInfoModel.setLastIntroPageSearchedInWiki(extract);
        videoGameInfoModel.setLastPageSearchedWithSuccessInWiki(true);

        //set at least one listener
        Listener listener = mock(Listener.class);
        videoGameInfoModel.setSuccesfullySavedLocalInfoListener(listener);

        //run unit
        videoGameInfoModel.storeLastSearchedPage();

        //revisar si el resultado es el esperado
        verify(videoGameInfoModel.getListSuccesfullySavedInfoListenerList().getFirst()).notifyListener();
        assertEquals(extract, stubDataBase.getExtract(null));
    }

    @Test
    public void testNoResultsToSaveLocally() throws Exception {
        //enviroment setup
        String title = "League of Legends";
        String extract = "League of Legends is a game of ...";

        //Supongo que la ultima busqueda no produjo resultados
        //seteo una ultima busqueda
        videoGameInfoModel.setLastPageTitleSearchedInWiki(title);
        videoGameInfoModel.setLastIntroPageSearchedInWiki(extract);
        videoGameInfoModel.setLastPageSearchedWithSuccessInWiki(false);

        //set at least one listener
        Listener listener = mock(Listener.class);
        videoGameInfoModel.setNoResultsToSaveListener(listener);

        //run unit
        videoGameInfoModel.storeLastSearchedPage();

        //revisar si el resultado es el esperado
        verify(videoGameInfoModel.getListNoResultsToSaveListener().getFirst()).notifyListener();
        assertNull(stubDataBase.getExtract(title));
    }

    @Test
    public void testFailedToSaveInDatabase(){
        try{
            //enviroment setup
            String title = "League of Legends";
            String extract = "League of Legends is a game of ...";

            Database database = mock(Database.class);
            doThrow(new Exception()).when(database).saveInfo(title, extract);
            videoGameInfoModel.setVideoGameInfoRepository(database);

            videoGameInfoModel.setLastPageTitleSearchedInWiki(title);
            videoGameInfoModel.setLastIntroPageSearchedInWiki(extract);
            videoGameInfoModel.setLastPageSearchedWithSuccessInWiki(true);

            //run unit
            videoGameInfoModel.storeLastSearchedPage();

            //verify output
            fail("Expected exception did not happen");
        }
        catch(Exception e){

        }
    
    }

    @Test
    public void testDeleteFromLocalStorage() throws Exception {
        //enviroment setup
        String title = "League of Legends";
        String extract = "League of Legends is a game of ...";
        //set something in database
        stubDataBase.saveInfo(title, extract);

        //set up stubDatabase
        videoGameInfoModel = new VideoGameInfoModelImpl(stubDataBase,mock(WikipediaSearcher.class));

        //set at least one listener
        Listener listener = mock(Listener.class);
        videoGameInfoModel.setDeletedInfoListener(listener);

        //metodo eliminar informacion localmente
        videoGameInfoModel.deleteFromLocalStorage(title);

        //check results
        verify(videoGameInfoModel.getListDeletedInfoListenerList().getFirst()).notifyListener();
        assertNull(stubDataBase.getExtract(null));
    }

}

