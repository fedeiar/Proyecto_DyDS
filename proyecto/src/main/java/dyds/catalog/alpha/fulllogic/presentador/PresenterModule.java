package dyds.catalog.alpha.fulllogic.presentador;

import dyds.catalog.alpha.fulllogic.modelo.VideoGameInfoModel;
import dyds.catalog.alpha.fulllogic.presentador.StoredInfoPresenter;
import dyds.catalog.alpha.fulllogic.presentador.WikipediaSearchPresenter;

public class PresenterModule {
    

    private static PresenterModule instance;
    private StoredInfoPresenter storedInfoPresenter;
    private WikipediaSearchPresenter wikipediaSearchPresenter;

    private PresenterModule(){

    }

    public static PresenterModule getInstance(){
        if (instance == null){
            instance = new PresenterModule();
        }
        return instance;
    }

    public WikipediaSearchPresenter setUpWikipediaSearchPresenter(VideoGameInfoModel videoGameInfoModel){
        wikipediaSearchPresenter = new WikipediaSearchPresenterImpl(videoGameInfoModel);
        return wikipediaSearchPresenter;
    }

    public StoredInfoPresenter setUpStoredInfoView(VideoGameInfoModel videoGameInfoModel){
        StoredInfoPresenter storedInformationPresenter = new StoredInfoPresenterImpl(videoGameInfoModel);
        return storedInformationPresenter;
    }

}
