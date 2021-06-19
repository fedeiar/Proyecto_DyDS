package dyds.catalog.alpha.fulllogic.presentador;

import dyds.catalog.alpha.fulllogic.modelo.*;
import dyds.catalog.alpha.fulllogic.utils.Utilidades;

import dyds.catalog.alpha.fulllogic.vista.*;

import javax.swing.*;

public class WikipediaSearchPresenterImpl implements WikipediaSearchPresenter {

    private WikipediaSearchView view;
    private VideoGameInfoModel videoGameInfoModel;
    //TODO: en realidad es mejor q siemrpe lo recupere de la vista.
    private String lastTermSearched;

    public WikipediaSearchPresenterImpl(VideoGameInfoModel videoGameInfoModel) {
        this.videoGameInfoModel = videoGameInfoModel;
        initListeners();
    }

    private void initListeners(){
        
        videoGameInfoModel.setWikipediaSearchInfoListener(new WikipediaSearchedInfoListener() {

            public void didFoundPageInWikipedia() {
                WikipediaPage wikiPage = videoGameInfoModel.getLastWikiPageSearched();

                String formattedPageIntroText = Utilidades.formatData(wikiPage.getTitle(), wikiPage.getPageIntro(), lastTermSearched);
                

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
                // TODO: agregar un método a la vista que reporte un cartel cuando no se guardó una página exitosamente
                
            }
            
        });

    }

    public void setView(WikipediaSearchView view){
        this.view = view;
    }

    public void onEventSearchInWikipedia() {
        //TODO: agregar un thread que encapsule a estas 3 lineas.

        view.setWorkingStatus();

        lastTermSearched = view.getSearchedTerm();
        videoGameInfoModel.searchTermInWikipedia(lastTermSearched);

       
    }

    public void onEventSaveSearchLocally() {
        //agregar un thread?

        //agregar el working y waiting status
        //view.setWorkingStatus().
        videoGameInfoModel.storeLastSearchedPage();
    }
}
