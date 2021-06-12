package dyds.catalog.alpha.fulllogic.presentador;

import dyds.catalog.alpha.fulllogic.modelo.*;
import dyds.catalog.alpha.fulllogic.vista.MainWindow;

import javax.swing.*;

public class ImplementacionPresentadorGestionDeInformacionLocal implements PresentadorGestionDeInformacionLocal{
    Modelo modelo;
    private MainWindow vista;

    public ImplementacionPresentadorGestionDeInformacionLocal(){
        inicializarRecursosPresentador();
    }

    private void inicializarRecursosPresentador(){
        modelo = new ImplementacionModelo();
        modelo.setOyenteGestionDeInformacionLocal(new OyenteGestionDeInformacionLocal() {
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

    public void notificacionBuscarInformacionLocalmente() {
        int indice = vista.getSelectedIndex();
        if(indice > -1)
            modelo.realizarBusquedaLocal(vista.getSelectedItem());
    }

    public void notificacionEliminarInformacionLocal() {
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
