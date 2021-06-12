package dyds.catalog.alpha.fulllogic.presentador;

import dyds.catalog.alpha.fulllogic.vista.MainWindow;

public interface PresentadorBusquedasEnWikipedia {
    void notificacionRealizarNuevaBusqueda();
    void notificacionGuardarBusquedaLocalmente();
    void setVista(MainWindow vista);
}
