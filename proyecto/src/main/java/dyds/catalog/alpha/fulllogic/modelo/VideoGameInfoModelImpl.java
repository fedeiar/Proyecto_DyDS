package dyds.catalog.alpha.fulllogic.modelo;

import dyds.catalog.alpha.fulllogic.modelo.repositorio.*;

import static dyds.catalog.alpha.fulllogic.utils.Utilidades.textToHtml;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.LinkedList;

public class VideoGameInfoModelImpl implements VideoGameInfoModel{

    private WikipediaSearcher wikipediaSearcher;
    private DataBase dataBase;

    private WikipediaSearchedInfoListener wikipediaSearchInfoListener;
    private StoredSearchedInfoListener storedSearchedInfoListener;
    private DeletedInfoListener deletedInfoListener;
    private LinkedList<SuccesfullySavedInfoListener> succesfullySavedInfoListenerList = new LinkedList<SuccesfullySavedInfoListener>();
    private LinkedList<NoResultsToSaveListener> noResultsToSaveListenerList = new LinkedList<NoResultsToSaveListener>();

    private String lastIntroPageSearchedInWiki;
    private String lastPageTitleSearchedInWiki;
    private boolean lastPageSearchedWithSuccessInWiki;

    private String lastIntroPageSearchedLocally;
    private String lastPageTitleSearchedLocally;

    public VideoGameInfoModelImpl(WikipediaSearcher wikipediaSearcher){
        this.wikipediaSearcher = wikipediaSearcher;
        lastPageSearchedWithSuccessInWiki = false;
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

    @Override public void setSuccesfullySavedLocalInfoListener(SuccesfullySavedInfoListener succesfullySavedLocalInfoListener){
        succesfullySavedInfoListenerList.addLast(succesfullySavedLocalInfoListener);
    }

    @Override public void setUnsuccesfullySavedLocalInfoListener(NoResultsToSaveListener unsuccesfullySavedLocalInfoListener){
        noResultsToSaveListenerList.addLast(unsuccesfullySavedLocalInfoListener);
    }

    @Override public WikipediaPage getLastWikiPageSearched(){
        WikipediaPage wikiPage = new WikipediaPage(lastPageTitleSearchedInWiki, lastIntroPageSearchedInWiki);
        return wikiPage;
    }

    @Override public WikipediaPage getLastLocallyStoredWikiPageSearched(){
        WikipediaPage wikiPage = new WikipediaPage(lastPageTitleSearchedLocally, lastIntroPageSearchedLocally);
        return wikiPage;
    }

    @Override public Object[] getTotalTitulosRegistrados() throws SQLException{
        return dataBase.getTitles().stream().sorted().toArray();
    }
    

    //TODO: en los métodos que siguen, preguntar si el listener no es null antes de enviar un mensaje al que haya implementado al listener.

    @Override public void searchTermInWikipedia(String searchedTerm) {
        boolean pageFound = wikipediaSearcher.searchPage(searchedTerm);
        lastPageSearchedWithSuccessInWiki = pageFound;

        if(pageFound){
            //TODO: encapsular el titulo y el extracto(page title) en un objeto, debemos también encapsularlo en wikipediaSearchImpl?
            String pageTitle = wikipediaSearcher.getLastSearchedTitle();
            String pageIntroText = wikipediaSearcher.getLastSearchedPageIntro();
            
            lastPageTitleSearchedInWiki = giveFormatForStorage(pageTitle);
            lastIntroPageSearchedInWiki = giveFormatForStorage(pageIntroText);

            wikipediaSearchInfoListener.didFoundPageInWikipedia();
        }
        else{
            wikipediaSearchInfoListener.didNotFoundPageInWikipedia();
        }
    }

    private String giveFormatForStorage(String text){
        return text.replace("'", "`"); //Replace to avoid SQL errors, we will have to find a workaround..
    }

    @Override public void storeLastSearchedPage() throws SQLException{
        if(lastPageSearchedWithSuccessInWiki){
            dataBase.saveInfo(lastPageTitleSearchedInWiki, lastIntroPageSearchedInWiki);

            notifyAllSuccessfullySavedInfoListeners(succesfullySavedInfoListenerList);
        }
        else{
            notifyAllNoResultsToSaveListeners(noResultsToSaveListenerList);
        }
    }

    private void notifyAllSuccessfullySavedInfoListeners(LinkedList<SuccesfullySavedInfoListener> List){
        for (SuccesfullySavedInfoListener succesfullySavedInfoListener : List) {
            succesfullySavedInfoListener.didSuccessSavePageLocally();
        }
    }

    private void notifyAllNoResultsToSaveListeners(LinkedList<NoResultsToSaveListener> List){
        for (NoResultsToSaveListener noResultsToSave : List) {
            noResultsToSave.noResultsToSaveLocally();
        }
    }

   
    @Override public void searchInLocalStorage(String videoGameTitle) throws SQLException {
        lastIntroPageSearchedLocally = dataBase.getExtract(videoGameTitle);
        lastPageTitleSearchedLocally = videoGameTitle;

        storedSearchedInfoListener.didSearchPageStoredLocally();
    }

    @Override public void deleteFromLocalStorage(String videoGameTitle) throws SQLException{
        dataBase.deleteEntry(videoGameTitle);

        deletedInfoListener.didDeletePageStoredLocally();
    }

    //for testing

    public void setWikipediaSearcher(WikipediaSearcher wikipediaSearcher){
        this.wikipediaSearcher = wikipediaSearcher;
    };

    public void setLastPageSearchedWithSuccessInWiki(boolean value) {
        lastPageSearchedWithSuccessInWiki = value;
    };

    public void setLastPageTitleSearchedInWiki(String lastPageTitleSearchedInWiki){
        this.lastPageTitleSearchedInWiki = lastPageTitleSearchedInWiki;
    };

    public void setLastIntroPageSearchedInWiki(String lastIntroPageSearchedInWiki) {
        this.lastIntroPageSearchedInWiki = lastIntroPageSearchedInWiki;
    }
}
