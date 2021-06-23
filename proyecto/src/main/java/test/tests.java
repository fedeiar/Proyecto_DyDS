package test;

import dyds.catalog.alpha.fulllogic.modelo.*;
import dyds.catalog.alpha.fulllogic.modelo.repositorio.DataBase;
import dyds.catalog.alpha.fulllogic.modelo.repositorio.DataBaseImplementation;
import dyds.catalog.alpha.fulllogic.presentador.*;
import dyds.catalog.alpha.fulllogic.vista.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;



import org.junit.Test;
//import org.mockito.Mockito.*;

import org.junit.Before;
import java.sql.SQLException;

public class tests {
    VideoGameInfoModel videoGameInfoModel;

    PresenterModule presenterModule;
    StoredInfoPresenter storedInfoPresenter;
    WikipediaSearchPresenter wikipediaSearchPresenter;

    StoredInfoView storedInfoView;
    WikipediaSearchView wikipediaSearchView;

    @Before
    public void setUp() throws Exception {
        //TODO: preg: se lo paso al setup por parámetro, o primero creo y dsp seteo?
        videoGameInfoModel = ModelModule.getInstance().setUpModel(new WikipediaSearcherImpl());
        //TODO: preg: no es mejor pasarselo al constructor? ya que el modelo sin la BD no funciona, no le encuentro el sentido a setearselo.
        videoGameInfoModel.setVideoGameInfoRepository(DataBaseImplementation.getInstance());


        wikipediaSearchPresenter = PresenterModule.getInstance().setUpWikipediaSearchPresenter(videoGameInfoModel);
        storedInfoPresenter = PresenterModule.getInstance().setUpStoredInfoView(videoGameInfoModel);

        wikipediaSearchView = ViewModule.getInstance().setUpWikipediaSearchView(wikipediaSearchPresenter);
        storedInfoView = ViewModule.getInstance().setUpStoredInfoView(storedInfoPresenter);

        wikipediaSearchPresenter.setView(wikipediaSearchView);
        storedInfoPresenter.setView(storedInfoView);

    }

    @Test(timeout = 4000)
    public void testNewSearchInWikipedia() throws InterruptedException {
        //Stub database
        DataBase stubDataBase = new StubDataBase();

        //TODO: Mockear wikipediaSearcher
        //WikipediaSearcher wikipediaSearcher = mock(WikipediaSearcher.class);
        //wikipediaSearcher.when(searchPage(terminoDeBusqueda)).thenReturn("Quisiera definir valores fijos aca");

        //Stub wikipediaSearcher
        WikipediaSearcher StubWikipediaSearcher = new StubWikipediaSearcher();

        //set up stubDatabase + mockWikipediaSearcher
        videoGameInfoModel.setWikipediaSearcher(StubWikipediaSearcher);
        videoGameInfoModel.setVideoGameInfoRepository(stubDataBase);

        //init test
        videoGameInfoModel.searchTermInWikipedia("Fifa 21");
        //waitForControllerTaskInSearchWikipedia();

        //check results
        assertNotNull(wikipediaSearchView.getActualSearch());
        //p(wikipediaSearchView.getActualSearch().toString());
    }

    @Test
    public void testSaveLocally() throws InterruptedException, SQLException {
        //Stub database
        DataBase stubDataBase = new StubDataBase();

        //TODO: Mockear wikipediaSearcher
        //WikipediaSearcher wikipediaSearcher = mock(WikipediaSearcher.class);
        //wikipediaSearcher.when(getLastSearchedTitle()).thenReturn("Fifa 21");
        //wikipediaSearcher.when(getLastSearchedPageIntro()).thenReturn("Fifa 21 is a football game ...");

        //Stub wikipediaSearcher
        WikipediaSearcher StubWikipediaSearcher = new StubWikipediaSearcher();

        //set up stubDatabase + mockWikipediaSearcher
        videoGameInfoModel.setWikipediaSearcher(StubWikipediaSearcher);
        videoGameInfoModel.setVideoGameInfoRepository(stubDataBase);

        //Supongo que la ultima busqueda fue realizada exitosamente
        //seteo una ultima busqueda
        videoGameInfoModel.setLastPageSearchedWithSuccessInWiki(true);
        videoGameInfoModel.setLastPageTitleSearchedInWiki("Fifa 21");
        videoGameInfoModel.setLastIntroPageSearchedInWiki("Fifa 21 is a game of ...");

        //metodo guardar informacion localmente
        videoGameInfoModel.storeLastSearchedPage();
        //waitForControllerTaskInSearchWikipedia();


        //revisar si el resultado es el esperado
        assertNotNull(stubDataBase.getExtract(null));
        p(stubDataBase.getExtract(null));
    }

    @Test
    public void testDeleteFromLocalStorage() throws InterruptedException, SQLException{
        //Stub database
        DataBase stubDataBase = new StubDataBase();

        //TODO: Mockear wikipediaSearcher

        //Stub wikipediaSearcher
        WikipediaSearcher StubWikipediaSearcher = new StubWikipediaSearcher();

        //set up stubDatabase + mockWikipediaSearcher
        videoGameInfoModel.setWikipediaSearcher(StubWikipediaSearcher);
        videoGameInfoModel.setVideoGameInfoRepository(stubDataBase);


        //metodo eliminar informacion localmente
        videoGameInfoModel.deleteFromLocalStorage("Fifa 21");
        //waitForControllerTaskInSearchWikipedia();


        //revisar si el resultado es el esperado
    }


    //for testing
    public void p(String algo){
        System.out.println(algo);
    }

    private void waitForControllerTaskInSave() throws InterruptedException{
        while(wikipediaSearchPresenter.isActivellyWorking()) Thread.sleep(1);
    }
}

