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

    private LinkedList<Listener> succesfullySearchedWikipediaInfoListenerList = new LinkedList<Listener>();
    private LinkedList<Listener> failedSearchWikipediaInfoListenerList = new LinkedList<Listener>();
    private LinkedList<Listener> storedSearchedInfoListenerList = new LinkedList<Listener>();
    private LinkedList<Listener> deletedInfoListenerList = new LinkedList<Listener>();
    private LinkedList<Listener> succesfullySavedInfoListenerList = new LinkedList<Listener>();
    private LinkedList<Listener> noResultsToSaveListenerList = new LinkedList<Listener>();

    private String lastIntroPageSearchedInWiki;
    private String lastPageTitleSearchedInWiki;
    private boolean lastPageSearchedWithSuccessInWiki;

    private String lastIntroPageSearchedLocally;
    private String lastPageTitleSearchedLocally;

    public VideoGameInfoModelImpl(DataBase database, WikipediaSearcher wikipediaSearcher){
        this.wikipediaSearcher = wikipediaSearcher;
        lastPageSearchedWithSuccessInWiki = false;
        dataBase = database;
        dataBase.loadDatabase();
    }

    @Override public void setVideoGameInfoRepository(DataBase dataBase){
        this.dataBase = dataBase;
        dataBase.loadDatabase();
    }

    @Override public void setSuccesfullySearchedWikipediaInfoListener(Listener listener){
        this.succesfullySearchedWikipediaInfoListenerList.addLast(listener);
    }

    @Override public void setFailedSearchWikipediaInfoListener(Listener listener){
        this.failedSearchWikipediaInfoListenerList.addLast(listener);
    }

    @Override public void setStoredSearchedInformationListener(Listener listener){
        this.storedSearchedInfoListenerList.addLast(listener);    
    }

    @Override public void setDeletedInfoListener(Listener listener){
        this.deletedInfoListenerList.addLast(listener);
    }

    @Override public void setSuccesfullySavedLocalInfoListener(Listener listener){
        this.succesfullySavedInfoListenerList.addLast(listener);
    }

    @Override public void setNoResultsToSaveListener(Listener listener){
        this.noResultsToSaveListenerList.addLast(listener);
    }

    @Override public WikipediaPage getLastWikiPageSearched(){
        WikipediaPage wikiPage = new WikipediaPage(lastPageTitleSearchedInWiki, lastIntroPageSearchedInWiki);
        return wikiPage;
    }

    @Override public WikipediaPage getLastLocallyStoredWikiPageSearched(){
        WikipediaPage wikiPage = new WikipediaPage(lastPageTitleSearchedLocally, lastIntroPageSearchedLocally);
        return wikiPage;
    }

    @Override public Object[] getAllStoredTitles() throws Exception{
        return dataBase.getTitles().stream().sorted().toArray();
    }
    

    //TODO: en los métodos que siguen, preguntar si el listener no es null antes de enviar un mensaje al que haya implementado al listener.

    @Override public void searchTermInWikipedia(String searchedTerm) throws Exception {
        boolean pageFound = wikipediaSearcher.searchPage(searchedTerm);
        lastPageSearchedWithSuccessInWiki = pageFound;

        if(pageFound){
            //TODO: encapsular el titulo y el extracto(page title) en un objeto, debemos también encapsularlo en wikipediaSearchImpl?
            String pageTitle = wikipediaSearcher.getLastSearchedTitle();
            String pageIntroText = wikipediaSearcher.getLastSearchedPageIntro();
            
            lastPageTitleSearchedInWiki = giveFormatForStorage(pageTitle);
            lastIntroPageSearchedInWiki = giveFormatForStorage(pageIntroText);

            notifyAllListeners(succesfullySearchedWikipediaInfoListenerList);
        }
        else{
            notifyAllListeners(failedSearchWikipediaInfoListenerList);
        }
    }

    private void notifyAllListeners(LinkedList<Listener> list){
        for (Listener listener : list) {
            listener.notifyListener();
        }
    }



    private String giveFormatForStorage(String text){
        return text.replace("'", "`");
    }

    
    @Override public void storeLastSearchedPage() throws Exception{
        if(lastPageSearchedWithSuccessInWiki){
            dataBase.saveInfo(lastPageTitleSearchedInWiki, lastIntroPageSearchedInWiki);

            notifyAllListeners(succesfullySavedInfoListenerList);
        }
        else{
            notifyAllListeners(noResultsToSaveListenerList);
        }
    }

    @Override public void searchInLocalStorage(String videoGameTitle) throws Exception {
        lastIntroPageSearchedLocally = dataBase.getExtract(videoGameTitle);
        lastPageTitleSearchedLocally = videoGameTitle;

        notifyAllListeners(storedSearchedInfoListenerList);
    }

    @Override public void deleteFromLocalStorage(String videoGameTitle) throws Exception{
        dataBase.deleteEntry(videoGameTitle);

        notifyAllListeners(deletedInfoListenerList);
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

    //getters for testing

    @Override
    public LinkedList<Listener> getListOfSuccesfullySavedInfoListenerList() {
        return succesfullySavedInfoListenerList;
    }

    @Override
    public LinkedList<Listener> getListNoResultsToSaveListener() {
        return noResultsToSaveListenerList;
    }
}
