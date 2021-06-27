package dyds.catalog.alpha.fulllogic.presentador;

import dyds.catalog.alpha.fulllogic.modelo.*;
import dyds.catalog.alpha.fulllogic.utils.Utilidades;
import dyds.catalog.alpha.fulllogic.vista.*;

public class StoredInfoPresenterImpl implements StoredInfoPresenter{

    private VideoGameInfoModel videoGameInfoModel;
    private StoredInfoView view;
    private Thread taskThread;

    public StoredInfoPresenterImpl(VideoGameInfoModel videoGameInfoModel){
        this.videoGameInfoModel = videoGameInfoModel;
        initListeners();
    }

    private void initListeners(){
        
        videoGameInfoModel.setSearchedStoredInfoListener(new Listener() {

            public void notifyListener() {
                WikipediaPage wikiPage = videoGameInfoModel.getLastLocallyStoredWikiPageSearched();

                String formattedPageIntroText = Utilidades.formatData(wikiPage.getTitle(), wikiPage.getPageIntro());
                
                view.setLocalStoredPageIntro(formattedPageIntroText);
            }

        });

        videoGameInfoModel.setSuccesfullySavedLocalInfoListener(new Listener(){

            @Override public void notifyListener(){
                try {
                    updateViewStoredTitles();
                }
                catch (Exception e) {
                    view.operationFailed("Page save", "Error updating stored titles after saving");
                }
            }
            
        });

        videoGameInfoModel.setDeletedInfoListener(new Listener(){
            
            @Override public void notifyListener(){
                try{
                    updateViewStoredTitles();
                    view.operationSucceded("Page delete", "Page deleted succesfully");
                }
                catch(Exception e){
                    view.operationFailed("Page delete", "Error updating stored titles when deleting");
                }
            }

        });
    }

    private void updateViewStoredTitles() throws Exception{
        view.setStoredSearchedTitles(videoGameInfoModel.getAllStoredTitles());
        view.cleanPageIntroText();
    }


    @Override public void onEventSearchLocalEntriesInfo() {
        int index = view.getSelectedTitleIndex();
        if(aTitleWasSelected(index)){
            taskThread = new Thread(new Runnable(){

                @Override public void run() {
                    try {
                        videoGameInfoModel.searchInLocalStorage(view.getSelectedTitle());
                    } 
                    catch (Exception e) {
                        view.operationFailed("Select title", "Failed to search the selected locally stored entry");
                    }
                }
                
            });
            taskThread.start();
        }
        else{
            view.setLocalStoredPageIntro("");
        }
    }

    private boolean aTitleWasSelected(int index){
        return index > -1;
    }

    @Override public void onEventDeleteLocalEntryInfo() {
        int index = view.getSelectedTitleIndex();
        if(aTitleWasSelected(index)){
            taskThread = new Thread(new Runnable(){

                @Override public void run(){
                    String tituloInformacion = view.getSelectedTitle();
                    try {
                        videoGameInfoModel.deleteFromLocalStorage(tituloInformacion);
                    } 
                    catch (Exception e) {
                        view.operationFailed("Page delete", "Failed page deletion");
                    }
                }

            });
            taskThread.start();
        }
        else{
            view.operationFailed("Page delete", "Please select a title to delete");
        }
    
    }

    @Override public void setView(StoredInfoView vista) {
        this.view = vista;
        try {
            updateViewStoredTitles();
        } catch (Exception e) {
            view.operationFailed("Initialization", "Error loading stored titles when initializating");
        }
    }

    //for testing
    @Override public boolean isActivellyWorking() {
        return taskThread.isAlive();
    };
}
