package dyds.catalog.alpha.fulllogic.modelo;

import dyds.catalog.alpha.fulllogic.modelo.repositorio.Database;

public class ModelModule {
    private static ModelModule instance;
    private VideoGameInfoModel videoGameInfoModel;

    private ModelModule() {
        
    }

    public static ModelModule getInstance() {
        if(instance == null) {
            instance = new ModelModule();
        }
        return instance;
    }

    public VideoGameInfoModel setUpModel(Database database, WikipediaSearcher wikipediaSearcher){
        videoGameInfoModel = new VideoGameInfoModelImpl(database, wikipediaSearcher);
        return videoGameInfoModel;
    }
}
