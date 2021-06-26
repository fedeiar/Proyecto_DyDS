package test;

import dyds.catalog.alpha.fulllogic.modelo.*;

import dyds.catalog.alpha.fulllogic.modelo.repositorio.DatabaseImplementation;
import dyds.catalog.alpha.fulllogic.presentador.*;

import dyds.catalog.alpha.fulllogic.vista.*;


import static org.mockito.Mockito.*;

import org.junit.Before;

import java.util.LinkedList;

public class tests {
    VideoGameInfoModel videoGameInfoModel;

    PresenterModule presenterModule;
    StoredInfoPresenter storedInfoPresenter;
    WikipediaSearchPresenter wikipediaSearchPresenter;

    StoredInfoView storedInfoView;
    WikipediaSearchView wikipediaSearchView;

    @Before
    public void setUp() throws Exception {
        videoGameInfoModel = ModelModule.getInstance().setUpModel(DatabaseImplementation.getInstance(),new WikipediaSearcherImpl());

        wikipediaSearchPresenter = PresenterModule.getInstance().setUpWikipediaSearchPresenter(videoGameInfoModel);
        storedInfoPresenter = PresenterModule.getInstance().setUpStoredInfoPresenter(videoGameInfoModel);

        wikipediaSearchView = ViewModule.getInstance().setUpWikipediaSearchView(wikipediaSearchPresenter);
        storedInfoView = ViewModule.getInstance().setUpStoredInfoView(storedInfoPresenter);

        wikipediaSearchPresenter.setView(wikipediaSearchView);
        storedInfoPresenter.setView(storedInfoView);

    }

    /*@Test(timeout = 4000)
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

        //set up mockWikipediaSearcher
        videoGameInfoModel.setWikipediaSearcher(mockWikipediaSearcher);

        //init test
        videoGameInfoModel.searchTermInWikipedia("League of Legends");

        //check results
        verify(mockWikipediaSearchedInfoListener).didFoundPageInWikipedia();
        assertEquals("League of Legends",videoGameInfoModel.getLastWikiPageSearched().getTitle());
    }*/

    /*@Test(timeout = 4000)
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

        //init test
        videoGameInfoModel.searchTermInWikipedia("League of Legends");

        //check results
        verify(mockWikipediaSearchedInfoListener).didNotFoundPageInWikipedia();
        assertNull(videoGameInfoModel.getLastWikiPageSearched().getPageIntro());
    }*/

    /*@Test
    public void testSuccesfullySaveLocally() throws Exception {
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
        videoGameInfoModel.setNewSuccesfullySavedInfoListenerList(newListSuccesfullyListenersDoNothing());

        //metodo guardar informacion localmente
        videoGameInfoModel.storeLastSearchedPage();

        //revisar si el resultado es el esperado
        verifyCalledMethodSuccefullyListeners();
        assertEquals("League of Legends is a game of ...",stubDataBase.getExtract(null));
    }*/

    /*@Test
    public void testDeleteFromLocalStorage() throws Exception{
        //Stub database
        DataBase stubDataBase = new StubDataBase();

        //set something in database
        stubDataBase.saveInfo("Something","Something");

        //set up stubDatabase
        videoGameInfoModel.setVideoGameInfoRepository(stubDataBase);

        //listener deleted
        DeletedInfoListener deletedInfoListener = mock(DeletedInfoListener.class);
        doNothing().when(deletedInfoListener).didDeletePageStoredLocally();

        //set new listener deleted
        videoGameInfoModel.setDeletedInfoListener(deletedInfoListener);

        //metodo eliminar informacion localmente
        videoGameInfoModel.deleteFromLocalStorage("Fifa 21");

        //check results
        verify(deletedInfoListener).didDeletePageStoredLocally();
        assertNull(stubDataBase.getExtract("Something"));
    }*/

    /*@Test
    public void testIntegracionNuevaBusquedaExitosa() throws Exception{
        //Stub WikipediaSearcher and set values
        WikipediaSearcher stubWikipediaSearcher = new StubWikipediaSearcher();
        stubWikipediaSearcher.setValues("League of Legends",
                "League of Legends is a game of ...",true);

        //set StubWikipediaSearcher
        videoGameInfoModel.setWikipediaSearcher(stubWikipediaSearcher);

        //Simular el ingreso de datos a la vista
        wikipediaSearchView.setPageIntroText("League of Legends");

        //search button
        wikipediaSearchPresenter.onEventSearchInWikipedia();

        //wait for search
        waitForControllerTaskInSearch();

        //check results
        //TODO como chequear los resultados? porque en la vista la informacion esta mezclada con HTML
        assertEquals("League of Legends",videoGameInfoModel.getLastWikiPageSearched().getTitle());
    }*/

    /*@Test
    public void testSaveLocally() throws Exception{
        //Stub WikipediaSearcher and set values
        WikipediaSearcher stubWikipediaSearcher = new StubWikipediaSearcher();
        stubWikipediaSearcher.setValues("No results","No results",false);
        videoGameInfoModel.setWikipediaSearcher(stubWikipediaSearcher);

        //Simular el ingreso de datos a la vista
        wikipediaSearchView.setPageIntroText("League of Legends");

        //check results
        assertEquals("No results",wikipediaSearchView.get);
    }*/

    //for testing
    public void p(String algo){
        System.out.println(algo);
    }

    private void waitForControllerTaskInSearch() throws InterruptedException{
        while(wikipediaSearchPresenter.isActivellyWorking()) Thread.sleep(1);
    }

    private LinkedList<Listener> newListSuccesfullyListenersDoNothing() {
        LinkedList<Listener> newListListenersSuccesfullyDoNothing = new LinkedList<Listener>();

        for (Listener succesfullySavedInfoListener :
                videoGameInfoModel.getListOfSuccesfullySavedInfoListenerList()) {
            newListListenersSuccesfullyDoNothing.add(mock(Listener.class));
            doNothing().when(newListListenersSuccesfullyDoNothing.getLast()).notifyListener();
        }

        return newListListenersSuccesfullyDoNothing;
    }

    private LinkedList<Listener> newListNoResultsListenersDoNothing(){
        LinkedList<Listener> newListNoResultsListenersDoNothing
                = new LinkedList<Listener>();

        for(Listener noResultsToSaveListener :
                videoGameInfoModel.getListNoResultsToSaveListener()){
            newListNoResultsListenersDoNothing.add(mock(Listener.class));
            doNothing().when(newListNoResultsListenersDoNothing.getLast()).notifyListener();
        }

        return newListNoResultsListenersDoNothing;
    }

    private void verifyCalledMethodSuccefullyListeners(){
        for(Listener succesfullySavedInfoListener :
                videoGameInfoModel.getListOfSuccesfullySavedInfoListenerList()){
            verify(succesfullySavedInfoListener).notifyListener();
        }
    }

    private void verifyCalledMethodNoResultsListeners(){
        for(Listener noResultsToSaveListener :
                videoGameInfoModel.getListNoResultsToSaveListener()){
            verify(noResultsToSaveListener).notifyListener();
        }
    }
}

