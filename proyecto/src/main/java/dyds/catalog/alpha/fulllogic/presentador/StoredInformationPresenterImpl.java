package dyds.catalog.alpha.fulllogic.presentador;

import dyds.catalog.alpha.fulllogic.modelo.*;
import dyds.catalog.alpha.fulllogic.vista.MainWindow;

import javax.swing.*;

public class StoredInformationPresenterImpl implements StoredInformationPresenter{

    private VideoGameInfoModel videoGameInfoModel;
    private MainWindow view;

    public StoredInformationPresenterImpl(){
        inicializarRecursosPresentador();
    }

    private void inicializarRecursosPresentador(){
        videoGameInfoModel = new VideoGameInfoModelImpl();
        videoGameInfoModel.setStoredInformationListener(new StoredInfoListener() {

            public void didSearchPageStoredLocally() {
                view.setInformacionBuscadaLocalmente(videoGameInfoModel.getUltimaBusquedaLocal());
            }

            public void didDeletePageStoredLocally() {
                
                updateViewStoredTitles();
            }

        });
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
