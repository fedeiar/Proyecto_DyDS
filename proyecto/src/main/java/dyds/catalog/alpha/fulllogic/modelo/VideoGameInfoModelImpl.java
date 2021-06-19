package dyds.catalog.alpha.fulllogic.modelo;

import dyds.catalog.alpha.fulllogic.modelo.repositorio.*;

import static dyds.catalog.alpha.fulllogic.utils.Utilidades.textToHtml;

import java.sql.DatabaseMetaData;
import java.util.List;
import java.util.LinkedList;

public class VideoGameInfoModelImpl implements VideoGameInfoModel{

    private WikipediaSearcher wikipediaSearcher;
    private DataBase dataBase;

    private WikipediaSearchedInfoListener wikipediaSearchInfoListener;
    private StoredSearchedInfoListener storedSearchedInfoListener;
    private DeletedInfoListener deletedInfoListener;
    private LinkedList<SuccesfullySavedLocalInfoListener> succesfullySavedLocalInfoListenerList = new LinkedList<SuccesfullySavedLocalInfoListener>();
    private LinkedList<UnsuccesfullySavedLocalInfoListener> unsuccesfullySavedLocalInfoListenerList = new LinkedList<UnsuccesfullySavedLocalInfoListener>();


    private String lastIntroPageSearched;
    private String lastPageTitleSearched;

    private boolean lastPageSearchedWithSuccess;

    private String lastIntroPageSearchedLocally;
    private String lastPageTitleSearchedLocally;

    public VideoGameInfoModelImpl(){
        //TODO: en realidad lo tiene que recibir por parámetro, no crearlo
        wikipediaSearcher = new WikipediaSearcherImpl();

        lastPageSearchedWithSuccess = false;
    }

    @Override public void setVideoGameInfoRepository(DataBase dataBase){
        this.dataBase = dataBase;
        dataBase.loadDatabase();
    }

    @Override public void setWikipediaSearchInfoListener(WikipediaSearchedInfoListener wikipediaSearchInfoListener){
        this.wikipediaSearchInfoListener = wikipediaSearchInfoListener;
    }

    @Override public void setStoredSearchedInformationListener(StoredSearchedInfoListener storedSearchedInfoListener){
        this.storedSearchedInfoListener = storedSearchedInfoListener;
    }

    @Override public void setDeletedInfoListener(DeletedInfoListener deletedInfoListener){
        this.deletedInfoListener = deletedInfoListener;
    }

    @Override public void setSuccesfullySavedLocalInfoListener(SuccesfullySavedLocalInfoListener succesfullySavedLocalInfoListener){
        succesfullySavedLocalInfoListenerList.addLast(succesfullySavedLocalInfoListener);
    }

    @Override public void setUnsuccesfullySavedLocalInfoListener(UnsuccesfullySavedLocalInfoListener unsuccesfullySavedLocalInfoListener){
        unsuccesfullySavedLocalInfoListenerList.addLast(unsuccesfullySavedLocalInfoListener);
    }

    @Override public WikipediaPage getLastWikiPageSearched(){
        WikipediaPage wikiPage = new WikipediaPage(lastPageTitleSearched, lastIntroPageSearched);
        return wikiPage;
    }

    @Override public WikipediaPage getLastLocallyStoredWikiPageSearched(){
        WikipediaPage wikiPage = new WikipediaPage(lastPageTitleSearchedLocally, lastIntroPageSearchedLocally);
        return wikiPage;
    }

    @Override public Object[] getTotalTitulosRegistrados(){
        return dataBase.getTitles().stream().sorted().toArray();
    }
    

    //TODO: en los métodos que siguen, preguntar si el listener no es null antes de enviar un mensaje al que haya implementado al listener.

    @Override public void searchTermInWikipedia(String searchedTerm) {
        boolean pageFound = wikipediaSearcher.searchPage(searchedTerm);
        lastPageSearchedWithSuccess = pageFound;

        if(pageFound){
            //TODO: encapsular el titulo y el extracto(page title) en un objeto, debemos también encapsularlo en wikipediaSearchImpl?
            String pageTitle = wikipediaSearcher.getLastSearchedTitle();
            String pageIntroText = wikipediaSearcher.getLastSearchedPageIntro();
            
            lastPageTitleSearched = giveFormatForStorage(pageTitle);
            lastIntroPageSearched = giveFormatForStorage(pageIntroText);

            wikipediaSearchInfoListener.didFoundPageInWikipedia();
        }
        else{
            wikipediaSearchInfoListener.didNotFoundPageInWikipedia();
        }
    }

    private String giveFormatForStorage(String text){
        return text.replace("'", "`"); //Replace to avoid SQL errors, we will have to find a workaround..
    }

    @Override public void storeLastSearchedPage() {
        if(lastPageSearchedWithSuccess){
            dataBase.saveInfo(lastPageTitleSearched, lastIntroPageSearched);

            notifyAllSuccsessfullySavedLocallyInfoListeners(succesfullySavedLocalInfoListenerList);
        }else{
            notifyAllUnSuccsessfullySavedLocallyInfoListeners(unsuccesfullySavedLocalInfoListenerList);
        }
    }

    private void notifyAllSuccsessfullySavedLocallyInfoListeners(LinkedList<SuccesfullySavedLocalInfoListener> List){
        for (SuccesfullySavedLocalInfoListener succesfullySavedLocalInfoListener : List) {
            succesfullySavedLocalInfoListener.didSuccessSavePageLocally();
        }
    }

    private void notifyAllUnSuccsessfullySavedLocallyInfoListeners(LinkedList<UnsuccesfullySavedLocalInfoListener> List){
        for (UnsuccesfullySavedLocalInfoListener unsuccesfullySavedLocalInfoListener : List) {
            unsuccesfullySavedLocalInfoListener.didFailSavePageLocally();
        }
    }

   
    @Override public void searchInLocalStorage(String videoGameTitle) {
        lastIntroPageSearchedLocally = dataBase.getExtract(videoGameTitle);
        lastPageTitleSearchedLocally = videoGameTitle;

        storedSearchedInfoListener.didSearchPageStoredLocally();
    }

    @Override public void deleteFromLocalStorage(String videoGameTitle){
        dataBase.deleteEntry(videoGameTitle);

        deletedInfoListener.didDeletePageStoredLocally();
    }

    
}
