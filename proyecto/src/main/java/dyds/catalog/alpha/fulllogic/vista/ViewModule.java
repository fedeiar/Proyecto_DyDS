package dyds.catalog.alpha.fulllogic.vista;

import dyds.catalog.alpha.fulllogic.presentador.StoredInformationPresenter;
import dyds.catalog.alpha.fulllogic.presentador.WikipediaSearchPresenter;

public class ViewModule {
    

    private static ViewModule instance;
    private StoredInfoView storedInfoView;
    private WikipediaSearchView wikipediaSearchView;

    private ViewModule(){

    }

    public static ViewModule getInstance(){
        if (instance == null){
            instance = new ViewModule();
        }
        return instance;
    }

    public WikipediaSearchView setUpWikipediaSearchView(WikipediaSearchPresenter wikipediaSearchPresenter){
        wikipediaSearchView = new WikipediaSearchViewImpl(wikipediaSearchPresenter);
        return wikipediaSearchView;
    }

    public StoredInfoView setUpStoredInfoView(StoredInformationPresenter storedInformationPresenter){
        storedInfoView = new StoredInfoViewImpl(storedInformationPresenter);
        return storedInfoView;
    }

}
