package dyds.catalog.alpha.fulllogic.presentador;

import dyds.catalog.alpha.fulllogic.modelo.*;
import dyds.catalog.alpha.fulllogic.vista.MainWindow;

import javax.swing.*;

public class ImplementacionPresentadorBusquedasEnWikipedia implements PresentadorBusquedasEnWikipedia {
    MainWindow vista;
    Modelo modelo;

    public ImplementacionPresentadorBusquedasEnWikipedia() {
        inicializarModelo();
    }

    private void inicializarModelo(){
        modelo = new ImplementacionModelo();
        modelo.setOyenteGestionDeInformacion(new OyenteBusquedasEnWikipedia() {

            public void notificarInformacionBuscada() {
                vista.setUltimaBusquedaEfectuada(modelo.getInformacionUltimaBusqueda());
                vista.setTituloUltimaBusquedaEfectuada(modelo.getTituloUltimaBusqueda());
                vista.setWatingStatus();
            }

            public void notificarNuevaInformacionRegistrada() {
                vista.setComboBox(new DefaultComboBoxModel(modelo.getTotalTitulosRegistrados()));
            }
        });
    }


    public void setVista(MainWindow vista){
        this.vista = vista;
    }

    public void notificacionRealizarNuevaBusqueda() {
        vista.setWorkingStatus();

        String datosIngresados = vista.getDatosIngresados();
        modelo.realizarBusquedaEnWikipedia(datosIngresados);

        vista.setWatingStatus();
    }

    public void notificacionGuardarBusquedaLocalmente() {
        modelo.guardarInformacionLocalmente(vista.getInformacionBuscada(),vista.getTituloInformacionBuscada());
    }
}
