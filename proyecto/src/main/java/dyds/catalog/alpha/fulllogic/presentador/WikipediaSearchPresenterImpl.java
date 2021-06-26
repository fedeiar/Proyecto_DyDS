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
        
        videoGameInfoModel.setSuccesfullySearchedWikipediaInfoListener(new Listener() {

            public void notifyListener() {
                WikipediaPage wikiPage = videoGameInfoModel.getLastWikiPageSearched();

                String formattedPageIntroText = Utilidades.formatData(wikiPage.getTitle(), wikiPage.getPageIntro(), view.getSearchedTerm());
                
                view.setPageIntroText(formattedPageIntroText);
            }

        });

        videoGameInfoModel.setFailedSearchWikipediaInfoListener(new Listener(){

            @Override public void notifyListener() {
                String pageIntroText = "No Results";

                view.setPageIntroText(pageIntroText);
            }
            
        });

        videoGameInfoModel.setSuccesfullySavedLocalInfoListener(new Listener(){
            
            @Override public void notifyListener() {
                view.operationSucceded("Page Save", "Page saved succesfully");
            }

        });

        videoGameInfoModel.setNoResultsToSaveListener(new Listener(){
            
            @Override public void notifyListener() {
                view.operationFailed("Page Save", "Cant store a page if there are no results from the search");
            }
            
        });

    }

    public void setView(WikipediaSearchView view){
        this.view = view;
    }

    public void onEventSearchInWikipedia() {
        String lastTermSearched = view.getSearchedTerm();
        taskThread = new Thread(new Runnable(){

            @Override public void run(){
                try {
                    videoGameInfoModel.searchTermInWikipedia(lastTermSearched);
                } 
                catch (Exception e){
                    view.operationFailed("Search", "could not search for the term, probably an internet connection issue");
                }
            }

        });
        taskThread.start();
    }

    public void onEventSaveSearchLocally() {
        taskThread = new Thread(new Runnable(){
            @Override public void run() {
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
