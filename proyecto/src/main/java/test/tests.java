package test;

import dyds.catalog.alpha.fulllogic.modelo.*;
import dyds.catalog.alpha.fulllogic.modelo.repositorio.DataBase;
import dyds.catalog.alpha.fulllogic.modelo.repositorio.DataBaseImplementation;
import dyds.catalog.alpha.fulllogic.presentador.*;
import dyds.catalog.alpha.fulllogic.vista.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.sql.DatabaseMetaData;

import org.junit.Test;
import org.junit.Before;

public class tests {
    VideoGameInfoModel videoGameInfoModel;

    PresenterModule presenterModule;
    StoredInfoPresenter storedInfoPresenter;
    WikipediaSearchPresenter wikipediaSearchPresenter;

    StoredInfoView storedInfoView;
    WikipediaSearchView wikipediaSearchView;

    @Before
    public void setUp() throws Exception {

        videoGameInfoModel = ModelModule.getInstance().setUpModel(DataBaseImplementation.getInstance(), new WikipediaSearcherImpl());
  
        wikipediaSearchPresenter = PresenterModule.getInstance().setUpWikipediaSearchPresenter(videoGameInfoModel);
        storedInfoPresenter = PresenterModule.getInstance().setUpStoredInfoView(videoGameInfoModel);

        wikipediaSearchView = ViewModule.getInstance().setUpWikipediaSearchView(wikipediaSearchPresenter);
        storedInfoView = ViewModule.getInstance().setUpStoredInfoView(storedInfoPresenter);

        wikipediaSearchPresenter.setView(wikipediaSearchView);
        storedInfoPresenter.setView(storedInfoView);

    }

    @Test(timeout = 4000)
    public void testNewSearchInWikipediaAndFound() throws Exception {
        //Mock wikipediaSearcher
        WikipediaSearcher mockWikipediaSearcher = mock(WikipediaSearcher.class);

        //Mocking methods in wikipediaSearcher
        //TODO tengo que poner un valor al parametro del searchPage?
        when(mockWikipediaSearcher.searchPage("League of Legends")).thenReturn(true);
        when(mockWikipediaSearcher.getLastSearchedTitle()).thenReturn("League of Legends");
        when(mockWikipediaSearcher.getLastSearchedPageIntro()).thenReturn("League of Legends is a game ...");

        //Mocking method from listener and setting in model
        WikipediaSearchedInfoListener mockWikipediaSearchedInfoListener = mock(WikipediaSearchedInfoListener.class);
        doNothing().when(mockWikipediaSearchedInfoListener).didFoundPageInWikipedia();
        videoGameInfoModel.setWikipediaSearchInfoListener(mockWikipediaSearchedInfoListener);

        //set up stubDatabase + mockWikipediaSearcher
        videoGameInfoModel.setWikipediaSearcher(mockWikipediaSearcher);

        //init test
        videoGameInfoModel.searchTermInWikipedia("League of Legends");

        //check results
        assertEquals("League of Legends",videoGameInfoModel.getLastWikiPageSearched().getTitle());
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

        //Mocking method from listener and setting in model
        WikipediaSearchedInfoListener mockWikipediaSearchedInfoListener = mock(WikipediaSearchedInfoListener.class);
        doNothing().when(mockWikipediaSearchedInfoListener).didNotFoundPageInWikipedia();
        videoGameInfoModel.setWikipediaSearchInfoListener(mockWikipediaSearchedInfoListener);

        //set up mockWikipediaSearcher
        videoGameInfoModel.setWikipediaSearcher(mockWikipediaSearcher);
        //videoGameInfoModel.setVideoGameInfoRepository(stubDataBase);

        //init test
        videoGameInfoModel.searchTermInWikipedia("League of Legends");

        //check results
        assertNull(videoGameInfoModel.getLastWikiPageSearched().getPageIntro());
    }

    @Test
    public void testSaveLocally() throws Exception {
        //Stub database
        DataBase stubDataBase = new StubDataBase();

        //set up stubDatabase
        videoGameInfoModel.setVideoGameInfoRepository(stubDataBase);

        //Supongo que la ultima busqueda fue realizada exitosamente
        //seteo una ultima busqueda
        videoGameInfoModel.setLastPageSearchedWithSuccessInWiki(true);
        videoGameInfoModel.setLastPageTitleSearchedInWiki("League of Legends");
        videoGameInfoModel.setLastIntroPageSearchedInWiki("League of Legends is a game of ...");

        //TODO do nothing methods of listeners
        //mockear cada listener en la lista, para que al realizar un save, no se notifique a nadie
        listeners_doNothing();

        //metodo guardar informacion localmente
        videoGameInfoModel.storeLastSearchedPage();

        //revisar si el resultado es el esperado
        assertEquals("League of Legends is a game of ...",stubDataBase.getExtract(null));
    }

    @Test
    public void testDeleteFromLocalStorage() throws Exception{
        //Stub database
        DataBase stubDataBase = new StubDataBase();

        //set something in database
        stubDataBase.saveInfo("Something","Something");

        //set up stubDatabase
        videoGameInfoModel.setVideoGameInfoRepository(stubDataBase);

        //metodo eliminar informacion localmente
        videoGameInfoModel.deleteFromLocalStorage("Fifa 21");

        //check results
        assertNull(stubDataBase.getExtract("Something"));
    }


    //for testing
    public void p(String algo){
        System.out.println(algo);
    }

    private void waitForControllerTaskInSave() throws InterruptedException{
        while(wikipediaSearchPresenter.isActivellyWorking()) Thread.sleep(1);
    }

    private void listeners_doNothing(){
        for(SuccesfullySavedInfoListener succesfullySavedInfoListener :
                videoGameInfoModel.getListOfSuccesfullySavedInfoListenerList()){
            succesfullySavedInfoListener = mock(SuccesfullySavedInfoListener.class);
            doNothing().when(succesfullySavedInfoListener).didSuccessSavePageLocally();
        }

        for(NoResultsToSaveListener noResultsToSaveListener :
                videoGameInfoModel.getListNoResultsToSaveListener()){
            noResultsToSaveListener = mock(NoResultsToSaveListener.class);
            doNothing().when(noResultsToSaveListener).noResultsToSaveLocally();
        }
    }

    @Test
    public void testUnitarioNuevaBusquedaExitosa() throws Exception{
        //Stub WikipediaSearcher and set values
        WikipediaSearcher stubWikipediaSearcher = new StubWikipediaSearcher();
        stubWikipediaSearcher.setValues("League of Legends is a game of ...","League of Legends",true);
        videoGameInfoModel.setWikipediaSearcher(stubWikipediaSearcher);

        //Simular el ingreso de datos a la vista
        wikipediaSearchView.setPageIntroText("League of Legends");

        //check results
        //TODO como chequear los resultados? porque en la vista la informacion esta mezclada con HTML
        //assertEquals("League of Legends is a game of ...",wikipediaSearchView.getActualSearch());
    }

   /* @Test
    public void testUnitarioNuevaBusquedaSinExito() throws Exception{
        //Stub WikipediaSearcher and set values
        WikipediaSearcher stubWikipediaSearcher = new StubWikipediaSearcher();
        stubWikipediaSearcher.setValues("No results","No results",false);
        videoGameInfoModel.setWikipediaSearcher(stubWikipediaSearcher);

        //Simular el ingreso de datos a la vista
        wikipediaSearchView.setPageIntroText("League of Legends");

        //check results
        assertEquals("No results",wikipediaSearchView.get);
    } */
}

