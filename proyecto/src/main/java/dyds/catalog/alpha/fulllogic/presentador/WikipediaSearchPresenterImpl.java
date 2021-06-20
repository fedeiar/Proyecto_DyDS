package dyds.catalog.alpha.fulllogic.presentador;

import dyds.catalog.alpha.fulllogic.modelo.*;
import dyds.catalog.alpha.fulllogic.utils.Utilidades;

import dyds.catalog.alpha.fulllogic.vista.*;

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

        videoGameInfoModel.setSuccesfullySavedLocalInfoListener(new SuccesfullySavedLocalInfoListener(){
            
            @Override public void didSuccessSavePageLocally() {
                view.pageSavedSuccesfully();
            }

        });

        videoGameInfoModel.setUnsuccesfullySavedLocalInfoListener(new UnsuccesfullySavedLocalInfoListener(){

            @Override
            public void didFailSavePageLocally() {
                view.failedPageSaving();
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
                view.setWorkingStatus();
                videoGameInfoModel.searchTermInWikipedia(lastTermSearched);
            }

        });
        taskThread.start();

       
    }

    public void onEventSaveSearchLocally() {
        //TODO: preg si está bien el thread asi.

        taskThread = new Thread(new Runnable(){
            @Override public void run() {
                view.setWatingStatus();
                videoGameInfoModel.storeLastSearchedPage();
            }
        });
        taskThread.start();
    }
}
