package dyds.catalog.alpha.fulllogic.presentador;

import dyds.catalog.alpha.fulllogic.modelo.*;
import dyds.catalog.alpha.fulllogic.utils.Utilidades;
import dyds.catalog.alpha.fulllogic.vista.*;

import java.sql.SQLException;

import javax.swing.*;

public class StoredInfoPresenterImpl implements StoredInfoPresenter{

    private VideoGameInfoModel videoGameInfoModel;
    private StoredInfoView view;
    private Thread taskThread;

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

            @Override public void didSuccessSavePageLocally(){
                //TODO: preguntar si la excepcion esta bien capturada aca
                try {
                    updateViewStoredTitles();
                }
                catch (SQLException e) {
                    view.operationFailed("Page save", "Error updating stored titles when saving");
                }
            }
            
        });

        videoGameInfoModel.setDeletedInfoListener(new DeletedInfoListener(){
            
            public void didSuccesfullyDeletePageStoredLocally(){
                //TODO: preguntar si la excepcion esta bien capturada aca
                try{
                    updateViewStoredTitles();
                    view.operationSucceded("Page delete", "Page deleted succesfully");
                }
                catch(SQLException e){
                    view.operationFailed("Page delete", "Error updating stored titles when deleting");
                }
            }

            public void didFailedDeletePageStoredLocally(){
                view.operationFailed("Page delete", "Failed page deletion");
            }

        });
    }

    private void updateViewStoredTitles() throws SQLException{
        view.setStoredSearchedTitles(videoGameInfoModel.getTotalTitulosRegistrados());
        view.cleanPageIntroText();
    }


    public void onEventSearchLocalEntriesInfo() {
        //TODO: preg si está bien el thread asi.
        //TODO: preg si está bien capturada la excepción
        
        int index = view.getSelectedTitleIndex();
        if(aTitleWasSelected(index)){
            taskThread = new Thread(new Runnable(){

                @Override public void run() {
                    try {
                        view.setWorkingStatus();
                        videoGameInfoModel.searchInLocalStorage(view.getSelectedTitle());
                    } 
                    catch (SQLException e) {
                        view.operationFailed("Select title", "Failed to search the selected entry");
                    }
                }
                
            });
            taskThread.start();
        }
    }

    private boolean aTitleWasSelected(int index){
        return index > -1;
    }

    public void onEventDeleteLocalEntryInfo() {
        //TODO: preg si está bien el thread asi.

        int index = view.getSelectedTitleIndex();
        if(aTitleWasSelected(index)){
            taskThread = new Thread(new Runnable(){
                @Override public void run(){
                    view.setWorkingStatus();
                    String tituloInformacion = view.getSelectedTitle();
                    videoGameInfoModel.deleteFromLocalStorage(tituloInformacion);
                }
            });
            taskThread.start();
        }
    
    }

    public void setView(StoredInfoView vista) {
        //TODO: preguntar si la excepcion esta bien capturada aca
        this.view = vista;
        try {
            updateViewStoredTitles();
        } catch (SQLException e) {
            view.operationFailed("Initialization", "Error loading stored titles when initializating");
        }
    }
}
