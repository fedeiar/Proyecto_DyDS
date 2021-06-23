package dyds.catalog.alpha.fulllogic.presentador;

import dyds.catalog.alpha.fulllogic.modelo.*;
import dyds.catalog.alpha.fulllogic.utils.Utilidades;

import dyds.catalog.alpha.fulllogic.vista.*;

import java.sql.SQLException;

import javax.swing.*;

public class WikipediaSearchPresenterImpl implements WikipediaSearchPresenter {

    private WikipediaSearchView view;
    private VideoGameInfoModel videoGameInfoModel;
    private Thread taskThread;

    public WikipediaSearchPresenterImpl(VideoGameInfoModel videoGameInfoModel) {
        this.videoGameInfoModel = videoGameInfoModel;
        initListeners();
    }

    private void initListeners(){
        
        videoGameInfoModel.setWikipediaSearchInfoListener(new WikipediaSearchedInfoListener() {

            public void didFoundPageInWikipedia() {
                WikipediaPage wikiPage = videoGameInfoModel.getLastWikiPageSearched();

                String formattedPageIntroText = Utilidades.formatData(wikiPage.getTitle(), wikiPage.getPageIntro(), view.getSearchedTerm());
                
                view.setPageIntroText(formattedPageIntroText);
                view.setWatingStatus();
            }

            public void didNotFoundPageInWikipedia(){
                String pageIntroText = "No Results";

                view.setPageIntroText(pageIntroText);
                view.setWatingStatus();
            }

        });

        videoGameInfoModel.setSuccesfullySavedLocalInfoListener(new SuccesfullySavedInfoListener(){
            
            @Override public void didSuccessSavePageLocally() {
                view.operationSucceded("Page Save", "Page saved succesfully");
            }

        });

        videoGameInfoModel.setNoResultsToSaveListener(new NoResultsToSaveListener(){
            
            @Override public void noResultsToSaveLocally() {
                view.operationFailed("Page Save", "Please search for something in order to save it");
            }
            
        });

    }

    public void setView(WikipediaSearchView view){
        this.view = view;
    }

    public void onEventSearchInWikipedia() {
        //TODO: preg si está bien el thread asi.

        String lastTermSearched = view.getSearchedTerm();

        taskThread = new Thread(new Runnable(){

            @Override public void run(){
                //TODO: el setWorking tambien ponerlo en la vista para que la idea sea mas consistente.
                view.setWorkingStatus();
                videoGameInfoModel.searchTermInWikipedia(lastTermSearched);
            }

        });
        taskThread.start();

       
    }

    public void onEventSaveSearchLocally() {
        //TODO: preg si está bien el thread asi.
        //TODO: preg si está bien capturada la excepción
        taskThread = new Thread(new Runnable(){
            @Override public void run() {
                view.setWorkingStatus();
                try {
                    videoGameInfoModel.storeLastSearchedPage();
                } 
                catch (Exception e) {
                    view.operationFailed("Page save", "Failed page saving");
                }
            }
        });
        taskThread.start();
    }

    //for testing
    public boolean isActivellyWorking() {
        return taskThread.isAlive();
    };
}
