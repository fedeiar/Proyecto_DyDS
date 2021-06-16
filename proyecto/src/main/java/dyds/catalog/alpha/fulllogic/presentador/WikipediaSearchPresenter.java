package dyds.catalog.alpha.fulllogic.presentador;

import dyds.catalog.alpha.fulllogic.vista.MainWindow;

public interface WikipediaSearchPresenter {

    public void notificacionRealizarNuevaBusqueda();
    
    public void onEventSaveSearchLocally();

    public void setView(MainWindow view);
}
