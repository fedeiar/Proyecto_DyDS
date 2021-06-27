package dyds.catalog.alpha.fulllogic.presentador;

import dyds.catalog.alpha.fulllogic.vista.*;

public interface StoredInfoPresenter {

    public void onEventSearchLocalEntriesInfo();

    public void onEventDeleteLocalEntryInfo();

    public void setView(StoredInfoView view);

    //for testing
    public boolean isActivellyWorking();
}