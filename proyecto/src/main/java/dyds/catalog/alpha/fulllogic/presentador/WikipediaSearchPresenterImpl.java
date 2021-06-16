package dyds.catalog.alpha.fulllogic.presentador;

import dyds.catalog.alpha.fulllogic.modelo.*;
import dyds.catalog.alpha.fulllogic.utils.Utilidades;
import dyds.catalog.alpha.fulllogic.vista.MainWindow;

import javax.swing.*;

public class WikipediaSearchPresenterImpl implements WikipediaSearchPresenter {
    MainWindow view;
    VideoGameInfoModel model;

    public WikipediaSearchPresenterImpl() {
        inicializarModelo();
    }

    private void inicializarModelo(){
        model = new VideoGameInfoModelImpl();
        model.setOyenteGestionDeInformacion(new WikipediaSearchInfoListener() {

            public void notificarInformacionBuscada() {
                String pageIntroText = model.getInformacionUltimaBusqueda();
                String termSearched = model.getLastSearchedTerm();
                String pageTitle = model.getTituloUltimaBusqueda();

                String formattedPageIntroText = formatearDatos(pageIntroText, termSearched, pageTitle);

                

                view.setUltimaBusquedaEfectuada(formattedPageIntroText);
                view.setTituloUltimaBusquedaEfectuada(pageTitle);
                view.setWatingStatus();
            }

            public void notificarNuevaInformacionRegistrada() {
                view.setComboBox(new DefaultComboBoxModel(model.getTotalTitulosRegistrados()));
            }
        });
    }

    private String formatearDatos(String pageIntroText, String termSearched, String pageTitle){
        String formattedText;
        if (pageIntroText.equals("No Results")) {
            formattedText = "No Results";
        }
        else{
            formattedText = "<h1>" + pageTitle + "</h1>";
            formattedText += pageIntroText.replace("\\n", "\n");
            formattedText = Utilidades.textToHtml(pageIntroText, termSearched);
        }
        return formattedText;
        
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

    public void onEventSaveSearchLocally() {
        model.guardarInformacionLocalmente(view.getInformacionBuscada(), view.getTituloInformacionBuscada());
    }
}
