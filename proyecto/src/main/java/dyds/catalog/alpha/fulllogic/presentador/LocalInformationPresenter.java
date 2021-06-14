package dyds.catalog.alpha.fulllogic.presentador;

import dyds.catalog.alpha.fulllogic.vista.MainWindow;

public interface LocalInformationPresenter {
    void notificacionBuscarInformacionLocalmente();
    void notificacionEliminarInformacionLocal();

    void setVista(MainWindow vista);
}
