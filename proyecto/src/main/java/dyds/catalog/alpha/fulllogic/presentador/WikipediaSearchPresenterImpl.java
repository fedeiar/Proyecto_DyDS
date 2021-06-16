package dyds.catalog.alpha.fulllogic.presentador;

import dyds.catalog.alpha.fulllogic.modelo.*;
import dyds.catalog.alpha.fulllogic.utils.Utilidades;
import dyds.catalog.alpha.fulllogic.vista.MainWindow;

import javax.swing.*;

public class WikipediaSearchPresenterImpl implements WikipediaSearchPresenter {

    MainWindow view;
    VideoGameInfoModel videoGameInfoModel;

    public WikipediaSearchPresenterImpl(VideoGameInfoModel videoGameInfoModel) {
        this.videoGameInfoModel = videoGameInfoModel;
        initListeners();
    }

    private void initListeners(){
        
        videoGameInfoModel.setWikipediaSearchInfoListener(new WikipediaSearchInfoListener() {

            public void didFoundPageInWikipedia() {
                String pageIntroText = videoGameInfoModel.getLastSearchedPageIntroText();
                String termSearched = videoGameInfoModel.getLastSearchedTerm();
                String pageTitle = videoGameInfoModel.getLastSearchedPageTitle();

                String formattedPageIntroText = formatData(pageIntroText, termSearched, pageTitle);
                

                view.setUltimaBusquedaEfectuada(formattedPageIntroText);
                view.setWatingStatus();
            }

            public void didNotFoundPageInWikipedia(){
                String pageIntroText = "No Results";

                view.setUltimaBusquedaEfectuada(pageIntroText);
                view.setWatingStatus();
            }

            public void notificarNuevaInformacionRegistrada() {
                view.setComboBox(new DefaultComboBoxModel(videoGameInfoModel.getTotalTitulosRegistrados()));

                // hay que agregar un codigo que notifique al usuario que la página fue guardada exitosamente.
            }

        });
    }

    private String formatData(String pageIntroText, String termSearched, String pageTitle){
        String formattedText;
        
        formattedText = "<h1>" + pageTitle + "</h1>";
        formattedText += pageIntroText.replace("\\n", "\n");
        formattedText = Utilidades.textToHtml(formattedText, termSearched);
        
        return formattedText;
    }


    public void setView(MainWindow view){
        this.view = view;
    }

    public void onEventSearchInWikipedia() {
        view.setWorkingStatus();

        String datosIngresados = view.getDatosIngresados();
        videoGameInfoModel.searchTermInWikipedia(datosIngresados);

        view.setWatingStatus();
    }

    public void onEventSaveSearchLocally() {
        videoGameInfoModel.storeLastSearchedPage();
    }
}
