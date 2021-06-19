package dyds.catalog.alpha.fulllogic.presentador;

import dyds.catalog.alpha.fulllogic.modelo.*;
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

                String formattedPageIntroText = formatData(wikiPage.getTitle(), wikiPage.getPageIntro());
                
                view.setLocalStoredPageIntro(formattedPageIntroText);
            }

        });

        videoGameInfoModel.setStoredTitlesListener(new StoredTitlesListener(){

            public void didUpdateStoredTitles(){
                updateViewStoredTitles();
            }

        });

        videoGameInfoModel.setDeletedInfoListener(new DeletedInfoListener(){
            
            public void didDeletePageStoredLocally(){
                // TODO: agregar un metodo a la vista en el que popeé un cartel de que un título fue borrado exitosamente. Luego ese método es invocado acá.
            }

        });
    }

    private String formatData(String pageTitle, String pageIntroText){
        String formattedText;
        
        formattedText = "<h1>" + pageTitle + "</h1>";
        formattedText += pageIntroText.replace("\\n", "\n");
        
        return formattedText;
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
