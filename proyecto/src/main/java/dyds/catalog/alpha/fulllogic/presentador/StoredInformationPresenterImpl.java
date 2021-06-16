package dyds.catalog.alpha.fulllogic.presentador;

import dyds.catalog.alpha.fulllogic.modelo.*;
import dyds.catalog.alpha.fulllogic.vista.MainWindow;

import javax.swing.*;

public class StoredInformationPresenterImpl implements StoredInformationPresenter{

    private VideoGameInfoModel videoGameInfoModel;
    private MainWindow view;

    public StoredInformationPresenterImpl(VideoGameInfoModel videoGameInfoModel){
        this.videoGameInfoModel = videoGameInfoModel;
        initListeners();
    }

    private void initListeners(){
        
        videoGameInfoModel.setStoredInformationListener(new StoredInfoListener() {

            public void didSearchPageStoredLocally() {
                String pageIntroText = videoGameInfoModel.getLastLocalSearchedPage();
                String pageTitle = videoGameInfoModel.getLastLocalSearchedTitle();

                String formattedPageIntroText = formatData(pageIntroText, pageTitle);
                
                view.setInformacionBuscadaLocalmente(formattedPageIntroText);
            }

            public void didDeletePageStoredLocally() {
                
                updateViewStoredTitles();
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
        view.setComboBox(new DefaultComboBoxModel(videoGameInfoModel.getTotalTitulosRegistrados()));
        view.eliminarInformacionDeLaVista();
    }

    public void onEventSearchLocalEntriesInfo() {
        int indice = view.getSelectedIndex();
        if(indice > -1)
            videoGameInfoModel.searchInLocalStorage(view.getSelectedItem());
    }

    public void onEventDeleteLocalEntryInfo() {
        int indice = view.getSelectedIndex();
        String tituloInformacion = view.getSelectedItem();
        if(indice > -1){
            videoGameInfoModel.deleteFromLocalStorage(tituloInformacion);
        }
    }

    public void setVista(MainWindow vista) {
        this.view = vista;
    }
}
