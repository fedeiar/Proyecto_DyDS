package dyds.catalog.alpha.fulllogic.modelo;

import dyds.catalog.alpha.fulllogic.modelo.repositorio.*;

import static dyds.catalog.alpha.fulllogic.utils.Utilidades.textToHtml;

public class VideoGameInfoModelImpl implements VideoGameInfoModel{

    private WikipediaSearcher wikipediaSearcher;
    private DataBase dataBase;

    private WikipediaSearchInfoListener wikipediaSearchInfoListener;
    private StoredInfoListener storedInfoListener;

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

    public void setVideoGameInfoRepository(DataBase dataBase){
        this.dataBase = dataBase;
    }

    public void setWikipediaSearchInfoListener(WikipediaSearchInfoListener wikipediaSearchInfoListener){
        this.wikipediaSearchInfoListener = wikipediaSearchInfoListener;
    }

    public void setStoredInformationListener(StoredInfoListener oyenteGestionDeInformacionLocal){
        this.storedInfoListener = oyenteGestionDeInformacionLocal;
    }
    

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

    @Override public void storeLastSearchedPage() {
        if(lastPageSearchedWithSuccess){
            dataBase.saveInfo(lastPageTitleSearched, lastIntroPageSearched);
        }
        //TODO: avisarle al presentador de la vista de la busqueda que la busqueda fue guardada exitosamente, para ello debemos tener algún método en algún oyente.
        //es probable que tengamos que hacer otros oyentes que no dependan de los presentadores, sino de los datos del modelo.

        storedInfoListener.didUpdateStoredTitles();
    }

    @Override public void searchInLocalStorage(String videoGameTitle) {
        lastIntroPageSearchedLocally = dataBase.getExtract(videoGameTitle);
        lastPageTitleSearchedLocally = videoGameTitle;
        storedInfoListener.didSearchPageStoredLocally();
    }

    @Override public void deleteFromLocalStorage(String videoGameTitle){
        dataBase.deleteEntry(videoGameTitle);
        storedInfoListener.didDeletePageStoredLocally();
    }

    
}
