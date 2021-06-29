import dyds.catalog.alpha.fulllogic.modelo.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import dyds.catalog.alpha.fulllogic.modelo.repositorio.Database;

import dyds.catalog.alpha.fulllogic.presentador.StoredInfoPresenter;
import dyds.catalog.alpha.fulllogic.presentador.WikipediaSearchPresenter;
import dyds.catalog.alpha.fulllogic.vista.StoredInfoView;
import dyds.catalog.alpha.fulllogic.vista.WikipediaSearchView;

import org.junit.Before;
import org.junit.Test;


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
    public void testSuccessfulSearchInWikipedia() throws Exception {
        String title = "League of Legends";
        String extract = "League of Legends is a game of ...";
        
        WikipediaSearcher mockWikipediaSearcher = mock(WikipediaSearcher.class);
        when(mockWikipediaSearcher.searchPage(title)).thenReturn(true);
        when(mockWikipediaSearcher.getLastSearchedTitle()).thenReturn(title);
        when(mockWikipediaSearcher.getLastSearchedPageIntro()).thenReturn(extract);
        videoGameInfoModel.setWikipediaSearcher(mockWikipediaSearcher);

        Listener listener = mock(Listener.class);
        videoGameInfoModel.setPageFoundInWikipediaListener(listener);

        videoGameInfoModel.searchTermInWikipedia(title);

        verify(videoGameInfoModel.getListPageFoundInWikipediaListenerListenerList().getFirst()).notifyListener();
        assertEquals(title, videoGameInfoModel.getLastWikiPageSearched().getTitle());
    }

    @Test(timeout = 4000)
    public void testNoResultsFoundInWikipedia() throws Exception {
        String title = "League of Legends";
        String extract = "";

        WikipediaSearcher mockWikipediaSearcher = mock(WikipediaSearcher.class);
        when(mockWikipediaSearcher.searchPage(title)).thenReturn(false);
        when(mockWikipediaSearcher.getLastSearchedTitle()).thenReturn(extract);
        when(mockWikipediaSearcher.getLastSearchedPageIntro()).thenReturn(extract);
        videoGameInfoModel.setWikipediaSearcher(mockWikipediaSearcher);

        Listener listener = mock(Listener.class);
        videoGameInfoModel.setPageNotFoundInWikipediaListener(listener);

        videoGameInfoModel.searchTermInWikipedia(title);

        verify(videoGameInfoModel.getListPageNotFoundInWikipediaListenerList().getFirst()).notifyListener();
        assertNull(videoGameInfoModel.getLastWikiPageSearched().getPageIntro());
    }

    @Test
    public void testSuccessfullySavedLocalInfo() throws Exception {
        String title = "League of Legends";
        String extract = "League of Legends is a game of ...";

        videoGameInfoModel.setLastPageTitleSearchedInWiki(title);
        videoGameInfoModel.setLastIntroPageSearchedInWiki(extract);
        videoGameInfoModel.setLastPageSearchedWithSuccessInWiki(true);

        Listener listener = mock(Listener.class);
        videoGameInfoModel.setSuccesfullySavedLocalInfoListener(listener);

        videoGameInfoModel.storeLastSearchedPage();

        verify(videoGameInfoModel.getListSuccesfullySavedInfoListenerList().getFirst()).notifyListener();
        assertEquals(extract, stubDataBase.getExtract(null));
    }

    @Test
    public void testNoResultsToSaveLocally() throws Exception {
        String title = "League of Legends";
        String extract = "League of Legends is a game of ...";

        videoGameInfoModel.setLastPageTitleSearchedInWiki(title);
        videoGameInfoModel.setLastIntroPageSearchedInWiki(extract);
        videoGameInfoModel.setLastPageSearchedWithSuccessInWiki(false);

        Listener listener = mock(Listener.class);
        videoGameInfoModel.setNoResultsToSaveListener(listener);

        videoGameInfoModel.storeLastSearchedPage();

        verify(videoGameInfoModel.getListNoResultsToSaveListener().getFirst()).notifyListener();
        assertNull(stubDataBase.getExtract(title));
    }

    @Test
    public void testFailedToSaveInDatabase(){
        try{
            String title = "League of Legends";
            String extract = "League of Legends is a game of ...";

            Database database = mock(Database.class);
            doThrow(new Exception()).when(database).saveInfo(title, extract);
            videoGameInfoModel.setVideoGameInfoRepository(database);

            videoGameInfoModel.setLastPageTitleSearchedInWiki(title);
            videoGameInfoModel.setLastIntroPageSearchedInWiki(extract);
            videoGameInfoModel.setLastPageSearchedWithSuccessInWiki(true);

            videoGameInfoModel.storeLastSearchedPage();

            fail("Expected exception did not happen");
        }
        catch(Exception e){
            //if the exception was captured, then the test success.
        }
    
    }

    @Test
    public void testDeleteFromLocalStorage() throws Exception {
        String title = "League of Legends";
        String extract = "League of Legends is a game of ...";
        
        stubDataBase.saveInfo(title, extract);

        Listener listener = mock(Listener.class);
        videoGameInfoModel.setDeletedInfoListener(listener);

        videoGameInfoModel.deleteFromLocalStorage(title);

        verify(videoGameInfoModel.getListDeletedInfoListenerList().getFirst()).notifyListener();
        assertNull(stubDataBase.getExtract(null));
    }

}

