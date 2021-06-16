package dyds.catalog.alpha.fulllogic.modelo;

import dyds.catalog.alpha.fulllogic.modelo.repositorio.*;

import static dyds.catalog.alpha.fulllogic.utils.Utilidades.textToHtml;

public class VideoGameInfoModelImpl implements VideoGameInfoModel{

    private WikipediaSearcher buscadorEnWikipedia;
    private DataBase dataBase;

    private WikipediaSearchInfoListener wikipediaSearchInfoListener;
    private StoredInfoListener storedInfoListener;

    private String lastPageSearchedLocally;

    private String lastSearchedTerm;
    private String lastSearchedPageIntroText;
    private String lastSearchedPageTitle;

    public VideoGameInfoModelImpl(){
        buscadorEnWikipedia = new WikipediaSearcherImpl();
        dataBase = DataBaseImplementation.getInstance();
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
        String pageIntroText = buscadorEnWikipedia.realizarNuevaBusqueda(searchedTerm);
        String pageTitle = buscadorEnWikipedia.getTituloUltimaBusqueda();
 
        lastSearchedTerm = searchedTerm;

        lastSearchedPageIntroText = giveFormatForStorage(pageIntroText);
        lastSearchedPageTitle = giveFormatForStorage(pageTitle);


        wikipediaSearchInfoListener.didSearchInWikipedia();
    }

    private String giveFormatForStorage(String text){
        return text.replace("'", "`"); //Replace to avoid SQL errors, we will have to find a workaround..
    }

    
    @Override public void storeLastSearchedPage() {
        if(lastSearchedPageIntroText != "" && !lastSearchedPageIntroText.equals("No Results")){
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

    public void searchInLocalStorage(String searchedTerm) {
        lastPageSearchedLocally = dataBase.getExtract(searchedTerm);
        storedInfoListener.didSearchPageStoredLocally();
    }

    public String getUltimaBusquedaLocal(){
        return lastPageSearchedLocally;
    }

    public void deleteFromLocalStorage(String terminoDeBusqueda){
        dataBase.deleteEntry(terminoDeBusqueda);
        storedInfoListener.didDeletePageStoredLocally();
    }

    
}
