package dyds.catalog.alpha.fulllogic.presentador;

import dyds.catalog.alpha.fulllogic.modelo.*;
import dyds.catalog.alpha.fulllogic.utils.Utilidades;
import dyds.catalog.alpha.fulllogic.vista.*;

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

            @Override public void didSuccessSavePageLocally() {
                updateViewStoredTitles();
            }
            
        });

        videoGameInfoModel.setDeletedInfoListener(new DeletedInfoListener(){
            
            public void didSuccesfullyDeletePageStoredLocally(){
                updateViewStoredTitles();
                // TODO: agregar un metodo a la vista en el que popeé un cartel de que un título fue borrado exitosamente. Luego ese método es invocado acá.
            }

            public void didFailedDeletePageStoredLocally(){
                // TODO: agregar un metodo a la vista en el que popeé un cartel de que un titulo NO fue borrado exitosamente.
            }

        });
    }

    private void updateViewStoredTitles(){
        view.setStoredSearchedTitles(videoGameInfoModel.getTotalTitulosRegistrados());
        view.cleanPageIntroText();
    }


    public void onEventSearchLocalEntriesInfo() {
        //TODO: preg si está bien el thread asi.
        int index = view.getSelectedTitleIndex();
        if(aTitleWasSelected(index)){
            taskThread = new Thread(new Runnable(){

                @Override public void run() {
                    view.setWatingStatus();
                    videoGameInfoModel.searchInLocalStorage(view.getSelectedTitle());
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
                    view.setWatingStatus();
                    String tituloInformacion = view.getSelectedTitle();
                    videoGameInfoModel.deleteFromLocalStorage(tituloInformacion);
                }
            });
            taskThread.start();
        }
    
    }

    //usar tal vez otro nombre, como initView
    public void setView(StoredInfoView vista) {
        this.view = vista;
        updateViewStoredTitles();
    }
}
