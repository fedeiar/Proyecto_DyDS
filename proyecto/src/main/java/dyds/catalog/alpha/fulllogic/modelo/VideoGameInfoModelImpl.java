package dyds.catalog.alpha.fulllogic.modelo;

import dyds.catalog.alpha.fulllogic.modelo.repositorio.*;

import static dyds.catalog.alpha.fulllogic.utils.Utilidades.textToHtml;

public class VideoGameInfoModelImpl implements VideoGameInfoModel{

    private WikipediaSearcher wikipediaSearcher;
    private DataBase dataBase;

    private WikipediaSearchedInfoListener wikipediaSearchInfoListener;
    private StoredSearchedInfoListener storedSearchedInfoListener;
    private StoredTitlesListener storedTitlesListener;
    private SavedLocallyInfoListener savedLocallyInfoListener;
    private DeletedInfoListener deletedInfoListener;



    private String lastIntroPageSearched;
    private String lastPageTitleSearched;

    private boolean lastPageSearchedWithSuccess;

    private String lastIntroPageSearchedLocally;
    private String lastPageTitleSearchedLocally;

    public VideoGameInfoModelImpl(){
        wikipediaSearcher = new WikipediaSearcherImpl();

        //TODO: preg aca o en el main?
        dataBase = DataBaseImplementation.getInstance();
        dataBase.loadDatabase();

        lastPageSearchedWithSuccess = false;
    }

    @Override public void setVideoGameInfoRepository(DataBase dataBase){
        this.dataBase = dataBase;
    }

    @Override public void setWikipediaSearchInfoListener(WikipediaSearchedInfoListener wikipediaSearchInfoListener){
        this.wikipediaSearchInfoListener = wikipediaSearchInfoListener;
    }

    @Override public void setStoredSearchedInformationListener(StoredSearchedInfoListener storedSearchedInfoListener){
        this.storedSearchedInfoListener = storedSearchedInfoListener;
    }

    @Override public void setStoredTitlesListener(StoredTitlesListener storedTitlesListener){
        this.storedTitlesListener = storedTitlesListener;
    }

    @Override public void setSavedLocallyInfoListener(SavedLocallyInfoListener savedLocallyInfoListener){
        this.savedLocallyInfoListener = savedLocallyInfoListener;
    }

    @Override public void setDeletedInfoListener(DeletedInfoListener deletedInfoListener){
        this.deletedInfoListener = deletedInfoListener;
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
        }
        //TODO: avisarle al presentador de la vista de la busqueda que la busqueda fue guardada exitosamente, para ello debemos tener algún método en algún oyente.
        //es probable que tengamos que hacer otros oyentes que no dependan de los presentadores, sino de los datos del modelo.
        savedLocallyInfoListener.didSavePageLocally();
        storedTitlesListener.didUpdateStoredTitles();
    }

    @Override public void searchInLocalStorage(String videoGameTitle) {
        lastIntroPageSearchedLocally = dataBase.getExtract(videoGameTitle);
        lastPageTitleSearchedLocally = videoGameTitle;

        storedSearchedInfoListener.didSearchPageStoredLocally();
    }

    @Override public void deleteFromLocalStorage(String videoGameTitle){
        dataBase.deleteEntry(videoGameTitle);

        deletedInfoListener.didDeletePageStoredLocally();
        storedTitlesListener.didUpdateStoredTitles();
    }

    
}
