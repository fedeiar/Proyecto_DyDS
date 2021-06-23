package dyds.catalog.alpha.fulllogic.modelo;


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

    public VideoGameInfoModel setUpModel(WikipediaSearcher wikipediaSearcher){
        videoGameInfoModel = new VideoGameInfoModelImpl(wikipediaSearcher);
        return videoGameInfoModel;
    }
}
