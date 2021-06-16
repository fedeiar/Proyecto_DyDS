package dyds.catalog.alpha.fulllogic.modelo;

import dyds.catalog.alpha.fulllogic.modelo.repositorio.*;

import static dyds.catalog.alpha.fulllogic.utils.Utilidades.textToHtml;

public class VideoGameInfoModelImpl implements VideoGameInfoModel{

    private WikipediaSearcher wikipediaSearcher;
    private DataBase dataBase;

    private WikipediaSearchInfoListener wikipediaSearchInfoListener;
    private StoredInfoListener storedInfoListener;

    private String lastSearchedTerm;
    private String lastSearchedPageIntroText;
    private String lastSearchedPageTitle;

    private boolean lastPageSearchedWithSuccess;


    private String lastPageSearchedLocally;
    private String lastPageTitleSearchedLocally;

    public VideoGameInfoModelImpl(){
        wikipediaSearcher = new WikipediaSearcherImpl();
        dataBase = DataBaseImplementation.getInstance();

        lastPageSearchedWithSuccess = false;
    }


    @Override public String getLastSearchedPageIntroText() {
        return lastSearchedPageIntroText;
    }

    @Override public String getLastSearchedPageTitle(){
        return lastSearchedPageTitle;
    }

    @Override public String getLastSearchedTerm(){
        return lastSearchedTerm;
    }

    @Override public void searchTermInWikipedia(String searchedTerm) {
        boolean pageFound = wikipediaSearcher.searchPage(searchedTerm);
        lastPageSearchedWithSuccess = pageFound;

        if(pageFound){
            String pageIntroText = wikipediaSearcher.getLastSearchedPageIntro();
            String pageTitle = wikipediaSearcher.getLastSearchedTitle();
            lastSearchedTerm = searchedTerm;

            lastSearchedPageIntroText = giveFormatForStorage(pageIntroText);
            lastSearchedPageTitle = giveFormatForStorage(pageTitle);

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
            dataBase.saveInfo(lastSearchedPageTitle, lastSearchedPageIntroText);
        }
        wikipediaSearchInfoListener.notificarNuevaInformacionRegistrada();
    }

    

    public void setWikipediaSearchInfoListener(WikipediaSearchInfoListener wikipediaSearchInfoListener){
        this.wikipediaSearchInfoListener = wikipediaSearchInfoListener;
    }

    public void setStoredInformationListener(StoredInfoListener oyenteGestionDeInformacionLocal){
        this.storedInfoListener = oyenteGestionDeInformacionLocal;
    }

    public Object[] getTotalTitulosRegistrados(){
        return dataBase.getTitles().stream().sorted().toArray();
    }

    public String getLastLocalSearchedPage(){
        return lastPageSearchedLocally;
    }

    public String getLastLocalSearchedTitle(){
        return lastPageTitleSearchedLocally;
    }

    public void searchInLocalStorage(String videoGameTitle) {
        lastPageSearchedLocally = dataBase.getExtract(videoGameTitle);
        lastPageTitleSearchedLocally = videoGameTitle;
        storedInfoListener.didSearchPageStoredLocally();
    }

    public void deleteFromLocalStorage(String videoGameTitle){
        dataBase.deleteEntry(videoGameTitle);
        storedInfoListener.didDeletePageStoredLocally();
    }

    
}
