package dyds.catalog.alpha.fulllogic.presentador;

import dyds.catalog.alpha.fulllogic.vista.MainWindow;

public class ImplementacionPresentadorRealizarBusquedas implements PresentadorRealizarBusquedas {
    MainWindow vista;
    BuscadorEnWikipedia buscadorEnWikipedia;

    public ImplementacionPresentadorRealizarBusquedas() {
        inicializarRecursosPresentador();
    }

    private void inicializarRecursosPresentador(){
        buscadorEnWikipedia = new ImplementadorBuscadorEnWikipedia();
    }

    public void setVista(MainWindow vista){
        this.vista = vista;
    }

    @Override
    public void notificacionNuevaBusqueda() {
        /*  recuperar la informacion necesaria
           para buscar por el buscadorEnWikipedia.
        */
        vista.setWorkingStatus();

        String datosIngresados = vista.getDatosIngresados();
        buscadorEnWikipedia.realizarNuevaBusqueda(datosIngresados);

        vista.setUltimaBusquedaEfectuada(buscadorEnWikipedia.getInformacionUltimaBusqueda());
        vista.setTituloUltimaBusquedaEfectuada(buscadorEnWikipedia.getTituloUltimaBusqueda());

        vista.setWatingStatus();
    }


}
