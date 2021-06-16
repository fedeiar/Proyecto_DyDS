package dyds.catalog.alpha.fulllogic.presentador;

import dyds.catalog.alpha.fulllogic.modelo.*;
import dyds.catalog.alpha.fulllogic.utils.Utilidades;

import dyds.catalog.alpha.fulllogic.vista.*;

import javax.swing.*;

public class WikipediaSearchPresenterImpl implements WikipediaSearchPresenter {

    private WikipediaSearchView view;
    private VideoGameInfoModel videoGameInfoModel;
    private String lastTermSearched;

    public WikipediaSearchPresenterImpl(VideoGameInfoModel videoGameInfoModel) {
        this.videoGameInfoModel = videoGameInfoModel;
        initListeners();
    }

    private void initListeners(){
        
        videoGameInfoModel.setWikipediaSearchInfoListener(new WikipediaSearchInfoListener() {

            public void didFoundPageInWikipedia() {
                String pageIntroText = videoGameInfoModel.getLastSearchedPageIntroText();
                String pageTitle = videoGameInfoModel.getLastSearchedPageTitle();

                String formattedPageIntroText = formatData(pageIntroText, lastTermSearched, pageTitle);
                

                view.setPageIntroText(formattedPageIntroText);
                view.setWatingStatus();
            }

            public void didNotFoundPageInWikipedia(){
                String pageIntroText = "No Results";

                view.setPageIntroText(pageIntroText);
                view.setWatingStatus();
            }

            

        });

        // TODO: debería implementarse otro método de algún oyente para notificar al usuario que la busqueda fue guardada exitosamente.
    }

    public void setView(WikipediaSearchView view){
        this.view = view;
    }

    private String formatData(String pageIntroText, String termSearched, String pageTitle){
        String formattedText;
        
        formattedText = "<h1>" + pageTitle + "</h1>";
        formattedText += pageIntroText.replace("\\n", "\n");
        formattedText = Utilidades.textToHtml(formattedText, termSearched);
        
        return formattedText;
    }

    public void onEventSearchInWikipedia() {
        //agregar un thread

        view.setWorkingStatus();

        lastTermSearched = view.getSearchedTerm();
        videoGameInfoModel.searchTermInWikipedia(lastTermSearched);
    }

    public void onEventSaveSearchLocally() {
        //agregar un thread?

        //agregar el working y waiting status
        videoGameInfoModel.storeLastSearchedPage();
    }
}
