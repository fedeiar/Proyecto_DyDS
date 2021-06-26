package dyds.catalog.alpha.fulllogic.modelo;

import dyds.catalog.alpha.fulllogic.modelo.repositorio.*;
import dyds.catalog.alpha.fulllogic.utils.Utilidades;

import java.util.LinkedList;

public class VideoGameInfoModelImpl implements VideoGameInfoModel{

    private WikipediaSearcher wikipediaSearcher;
    private Database dataBase;

    private LinkedList<Listener> pageFoundInWikipediaListenerListenerList = new LinkedList<Listener>();
    private LinkedList<Listener> PageNotFoundInWikipediaListenerList = new LinkedList<Listener>();
    private LinkedList<Listener> searchedStoredInfoListenerList = new LinkedList<Listener>();
    private LinkedList<Listener> deletedInfoListenerList = new LinkedList<Listener>();
    private LinkedList<Listener> succesfullySavedInfoListenerList = new LinkedList<Listener>();
    private LinkedList<Listener> noResultsToSaveListenerList = new LinkedList<Listener>();

    private String lastIntroPageSearchedInWiki;
    private String lastPageTitleSearchedInWiki;
    private boolean lastPageSearchedWithSuccessInWiki;

    private String lastIntroPageSearchedLocally;
    private String lastPageTitleSearchedLocally;

    public VideoGameInfoModelImpl(Database database, WikipediaSearcher wikipediaSearcher){
        this.wikipediaSearcher = wikipediaSearcher;
        lastPageSearchedWithSuccessInWiki = false;
        dataBase = database;
        dataBase.loadDatabase();
    }

    @Override public void setVideoGameInfoRepository(Database dataBase){
        this.dataBase = dataBase;
        dataBase.loadDatabase();
    }

    @Override public void setPageFoundInWikipediaListener(Listener listener){
        this.pageFoundInWikipediaListenerListenerList.addLast(listener);
    }

    @Override public void setPageNotFoundInWikipediaListener(Listener listener){
        this.PageNotFoundInWikipediaListenerList.addLast(listener);
    }

    @Override public void setSearchedStoredInfoListener(Listener listener){
        this.searchedStoredInfoListenerList.addLast(listener);    
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
    

    @Override public void searchTermInWikipedia(String searchedTerm) throws Exception {
        boolean pageFound = wikipediaSearcher.searchPage(searchedTerm);
        lastPageSearchedWithSuccessInWiki = pageFound;

        if(pageFound){
            String pageTitle = wikipediaSearcher.getLastSearchedTitle();
            String pageIntroText = wikipediaSearcher.getLastSearchedPageIntro();
            
            lastPageTitleSearchedInWiki = Utilidades.giveFormatForStorage(pageTitle);
            lastIntroPageSearchedInWiki = Utilidades.giveFormatForStorage(pageIntroText);

            notifyAllListeners(pageFoundInWikipediaListenerListenerList);
        }
        else{
            notifyAllListeners(PageNotFoundInWikipediaListenerList);
        }
    }
    
    private void notifyAllListeners(LinkedList<Listener> list){
        for (Listener listener : list) {
            listener.notifyListener();
        }
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

        notifyAllListeners(searchedStoredInfoListenerList);
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
