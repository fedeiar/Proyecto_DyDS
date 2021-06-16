package dyds.catalog.alpha.fulllogic.presentador;

import dyds.catalog.alpha.fulllogic.modelo.*;
import dyds.catalog.alpha.fulllogic.vista.*;

import javax.swing.*;

public class StoredInfoPresenterImpl implements StoredInfoPresenter{

    private VideoGameInfoModel videoGameInfoModel;
    private StoredInfoView view;

    public StoredInfoPresenterImpl(VideoGameInfoModel videoGameInfoModel){
        this.videoGameInfoModel = videoGameInfoModel;
        initListeners();
    }

    private void initListeners(){
        
        videoGameInfoModel.setStoredInformationListener(new StoredInfoListener() {

            public void didSearchPageStoredLocally() {
                String pageIntroText = videoGameInfoModel.getLastLocalSearchedPage();
                String pageTitle = videoGameInfoModel.getLastLocalSearchedTitle();

                String formattedPageIntroText = formatData(pageIntroText, pageTitle);
                
                view.setLocalStoredPageIntro(formattedPageIntroText);
            }

            public void didDeletePageStoredLocally() {
                
                updateViewStoredTitles();
            }

            public void didUpdateStoredTitles() {

                view.setStoredSearchedTitles(videoGameInfoModel.getTotalTitulosRegistrados());

                // hay que agregar un codigo que notifique al usuario que la página fue guardada exitosamente.
            }

        });
    }

    private String formatData(String pageIntroText, String pageTitle){
        String formattedText;
        
        formattedText = "<h1>" + pageTitle + "</h1>";
        formattedText += pageIntroText.replace("\\n", "\n");
        
        return formattedText;
    }


    private void updateViewStoredTitles(){
        view.setStoredSearchedTitles(videoGameInfoModel.getTotalTitulosRegistrados());
        view.cleanPageIntroText();
    }

    public void onEventSearchLocalEntriesInfo() {
        int indice = view.getSelectedTitleIndex();
        if(indice > -1)
            videoGameInfoModel.searchInLocalStorage(view.getSelectedTitle());
    }

    public void onEventDeleteLocalEntryInfo() {
        int indice = view.getSelectedTitleIndex();
        String tituloInformacion = view.getSelectedTitle();
        if(indice > -1){
            videoGameInfoModel.deleteFromLocalStorage(tituloInformacion);
        }
    }

    public void setView(StoredInfoView vista) {
        this.view = vista;
    }
}
