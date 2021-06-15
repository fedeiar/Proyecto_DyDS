package dyds.catalog.alpha.fulllogic.presentador;

import dyds.catalog.alpha.fulllogic.modelo.*;
import dyds.catalog.alpha.fulllogic.vista.MainWindow;

import javax.swing.*;

public class ImplementacionPresentadorBusquedasEnWikipedia implements WikipediaSearchPresenter {
    MainWindow view;
    VideoGameInfoModel model;

    public ImplementacionPresentadorBusquedasEnWikipedia() {
        inicializarModelo();
    }

    private void inicializarModelo(){
        model = new VideoGameInfoModelImpl();
        model.setOyenteGestionDeInformacion(new WikipediaInfoListener() {

            public void notificarInformacionBuscada() {
                view.setUltimaBusquedaEfectuada(model.getInformacionUltimaBusqueda());
                view.setTituloUltimaBusquedaEfectuada(model.getTituloUltimaBusqueda());
                view.setWatingStatus();
            }

            public void notificarNuevaInformacionRegistrada() {
                view.setComboBox(new DefaultComboBoxModel(model.getTotalTitulosRegistrados()));
            }
        });
    }


    public void setView(MainWindow view){
        this.view = view;
    }

    public void notificacionRealizarNuevaBusqueda() {
        view.setWorkingStatus();

        String datosIngresados = view.getDatosIngresados();
        model.realizarBusquedaEnWikipedia(datosIngresados);

        view.setWatingStatus();
    }

    public void notificacionGuardarBusquedaLocalmente() {
        model.guardarInformacionLocalmente(view.getInformacionBuscada(), view.getTituloInformacionBuscada());
    }
}
