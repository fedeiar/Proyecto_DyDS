package dyds.catalog.alpha.fulllogic.presentador;

import dyds.catalog.alpha.fulllogic.modelo.*;
import dyds.catalog.alpha.fulllogic.vista.MainWindow;

import javax.swing.*;

public class StoredInformationPresenterImpl implements StoredInformationPresenter{
    VideoGameInfoModel modelo;
    private MainWindow vista;

    public StoredInformationPresenterImpl(){
        inicializarRecursosPresentador();
    }

    private void inicializarRecursosPresentador(){
        modelo = new VideoGameInfoModelImpl();
        modelo.setOyenteGestionDeInformacionLocal(new StoredInformationListener() {
            public void notificarInformacionBuscadaLocalmente() {
                vista.setInformacionBuscadaLocalmente(modelo.getUltimaBusquedaLocal());
            }
            public void notificarInformacionEliminada() {
                //aca
                actualizarVistaPorInformacionEliminada();
            }
        });
    }

    private void actualizarVistaPorInformacionEliminada(){
        vista.setComboBox(new DefaultComboBoxModel(modelo.getTotalTitulosRegistrados()));
        vista.eliminarInformacionDeLaVista();
    }

    public void onEventSearchLocalEntryInfo() {
        int indice = vista.getSelectedIndex();
        if(indice > -1)
            modelo.realizarBusquedaLocal(vista.getSelectedItem());
    }

    public void onEventDeleteLocalEntryInfo() {
        int indice = vista.getSelectedIndex();
        String tituloInformacion = vista.getSelectedItem();
        if(indice > -1){
            modelo.eliminarInformacionLocalmente(tituloInformacion);
        }
    }

    public void setVista(MainWindow vista) {
        this.vista = vista;
    }
}
