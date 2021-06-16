package dyds.catalog.alpha.fulllogic.vista;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import dyds.catalog.alpha.fulllogic.modelo.ModelModule;
import dyds.catalog.alpha.fulllogic.modelo.VideoGameInfoModel;
import dyds.catalog.alpha.fulllogic.modelo.repositorio.DataBaseImplementation;
import dyds.catalog.alpha.fulllogic.presentador.PresenterModule;
import dyds.catalog.alpha.fulllogic.presentador.StoredInfoPresenter;
import dyds.catalog.alpha.fulllogic.presentador.WikipediaSearchPresenter;

public class Main {
    public static void main(String[] args) {

        VideoGameInfoModel videoGameInfoModel = ModelModule.getInstance().setUpModel();
        //aca o el modelo obtiene por si mismo la BD?
        videoGameInfoModel.setVideoGameInfoRepository(DataBaseImplementation.getInstance());
        //esto debe hacerse aca o en el modelo?
        //DataBaseImplementation.getInstance().loadDatabase();

        WikipediaSearchPresenter wikipediaSearchPresenter = PresenterModule.getInstance().setUpWikipediaSearchPresenter(videoGameInfoModel);
        StoredInfoPresenter storedInfoPresenter = PresenterModule.getInstance().setUpStoredInfoView(videoGameInfoModel);

        WikipediaSearchView wikipediaSearchView = ViewModule.getInstance().setUpWikipediaSearchView(wikipediaSearchPresenter);
        StoredInfoView storedInfoView = ViewModule.getInstance().setUpStoredInfoView(storedInfoPresenter);

        wikipediaSearchPresenter.setView(wikipediaSearchView);
        storedInfoPresenter.setView(storedInfoView);

        

        
    
        ViewCoordinator viewCoordinator = new ViewCoordinator(wikipediaSearchView, storedInfoView);
        JFrame frame = new JFrame("Video Game Info Catalog");
        frame.setContentPane(viewCoordinator.getContent());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    
      
    }
}
