package dyds.catalog.alpha.fulllogic.presentador;

import dyds.catalog.alpha.fulllogic.modelo.*;
import dyds.catalog.alpha.fulllogic.utils.Utilidades;
import dyds.catalog.alpha.fulllogic.vista.*;

import javax.swing.*;

public class StoredInfoPresenterImpl implements StoredInfoPresenter{

    private VideoGameInfoModel videoGameInfoModel;
    private StoredInfoView view;

    public StoredInfoPresenterImpl(VideoGameInfoModel videoGameInfoModel){
        this.videoGameInfoModel = videoGameInfoModel;
        initListeners();
    }

    private void initListeners(){
        
        videoGameInfoModel.setStoredSearchedInformationListener(new StoredSearchedInfoListener() {

            public void didSearchPageStoredLocally() {
                WikipediaPage wikiPage = videoGameInfoModel.getLastLocallyStoredWikiPageSearched();

                String formattedPageIntroText = Utilidades.formatData(wikiPage.getTitle(), wikiPage.getPageIntro());
                
                view.setLocalStoredPageIntro(formattedPageIntroText);
            }

        });

        videoGameInfoModel.setSuccesfullySavedLocalInfoListener(new SuccesfullySavedLocalInfoListener(){

            @Override public void didSuccessSavePageLocally() {
                updateViewStoredTitles();
            }
            
        });

        videoGameInfoModel.setDeletedInfoListener(new DeletedInfoListener(){
            
            public void didDeletePageStoredLocally(){
                updateViewStoredTitles();
                // TODO: agregar un metodo a la vista en el que popeé un cartel de que un título fue borrado exitosamente. Luego ese método es invocado acá.
            }

        });
    }

    private void updateViewStoredTitles(){
        view.setStoredSearchedTitles(videoGameInfoModel.getTotalTitulosRegistrados());
        view.cleanPageIntroText();
    }


    public void onEventSearchLocalEntriesInfo() {
        //hacer un thread?

        //TODO: agregar el working y waiting status
        int indice = view.getSelectedTitleIndex();
        if(indice > -1)
            videoGameInfoModel.searchInLocalStorage(view.getSelectedTitle());
    }

    public void onEventDeleteLocalEntryInfo() {
        //hacer un thread?

        //TODO: agregar el working y waiting status
        int indice = view.getSelectedTitleIndex();
        String tituloInformacion = view.getSelectedTitle();
        if(indice > -1){
            videoGameInfoModel.deleteFromLocalStorage(tituloInformacion);
        }
    }

    //usar tal vez otro nombre, como initView
    public void setView(StoredInfoView vista) {
        this.view = vista;
        updateViewStoredTitles();
    }
}
