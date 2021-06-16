package dyds.catalog.alpha.fulllogic.presentador;

import dyds.catalog.alpha.fulllogic.vista.MainWindow;

public interface StoredInformationPresenter {

    public void onEventSearchLocalEntryInfo();

    public void onEventDeleteLocalEntryInfo();

    public void setVista(MainWindow vista);
}
