package dyds.catalog.alpha.fulllogic.modelo;

import dyds.catalog.alpha.fulllogic.modelo.repositorio.*;

import static dyds.catalog.alpha.fulllogic.utils.Utilidades.textToHtml;

public class VideoGameInfoModelImpl implements VideoGameInfoModel{

    private WikipediaSearcher buscadorEnWikipedia;
    private DataBase dataBase;

    private WikipediaSearchInfoListener oyenteBusquedasEnWikipedia;
    private StoredInformationListener oyenteGestionDeInformacionLocal;

    private String ultimaBusquedaLocal;

    private String lastSearchedTerm;
    private String lastSearchedPageIntroText;
    private String lastSearchedPageTitle;

    public VideoGameInfoModelImpl(){
        buscadorEnWikipedia = new WikipediaSearcherImpl();
        dataBase = DataBaseImplementation.getInstance();
    }

    @Override
    public void realizarBusquedaEnWikipedia(String terminoDeBusqueda) {
        String pageIntroText = buscadorEnWikipedia.realizarNuevaBusqueda(terminoDeBusqueda);;
        String pageTitle = buscadorEnWikipedia.getTituloUltimaBusqueda();;

        lastSearchedTerm = terminoDeBusqueda;
        lastSearchedPageIntroText = giveFormatForStorage(pageIntroText);
        lastSearchedPageTitle = giveFormatForStorage(pageTitle);

        //formatearDatos(pageIntroText, terminoDeBusqueda);

        oyenteBusquedasEnWikipedia.notificarInformacionBuscada();
    }

    private String giveFormatForStorage(String text){
        return text.replace("'", "`"); //Replace to avoid SQL errors, we will have to find a workaround..
    }

    @Override
    public String getLastSearchedTerm(){
        return lastSearchedTerm;
    }


    @Override
    public void guardarInformacionLocalmente(String informacion, String tituloInformacion) {
        if(informacion != "" && !informacion.equals("No Results")){
            // save to DB  <o/
            dataBase.saveInfo(tituloInformacion.replace("'", "`"), informacion);  //Dont forget the ' sql problem
            //comboBox1.setModel(new DefaultComboBoxModel(DataBase.getTitles().stream().sorted().toArray()));
        }
        oyenteBusquedasEnWikipedia.notificarNuevaInformacionRegistrada();
    }

    public String getInformacionUltimaBusqueda() {
        //return buscadorEnWikipedia.getInformacionUltimaBusqueda();
        return lastSearchedPageIntroText;
    }
    public String getTituloUltimaBusqueda(){
        //return buscadorEnWikipedia.getTituloUltimaBusqueda();
        return lastSearchedPageTitle;
    }

    public void setOyenteGestionDeInformacion(WikipediaSearchInfoListener oyenteBusquedasEnWikipedia){
        this.oyenteBusquedasEnWikipedia = oyenteBusquedasEnWikipedia;
    }

    public void setOyenteGestionDeInformacionLocal(StoredInformationListener oyenteGestionDeInformacionLocal){
        this.oyenteGestionDeInformacionLocal = oyenteGestionDeInformacionLocal;
    }

    public Object[] getTotalTitulosRegistrados(){
        return dataBase.getTitles().stream().sorted().toArray();
    }

    public void realizarBusquedaLocal(String terminoDeBusqueda) {
        ultimaBusquedaLocal = dataBase.getExtract(terminoDeBusqueda);
        oyenteGestionDeInformacionLocal.notificarInformacionBuscadaLocalmente();
    }

    public String getUltimaBusquedaLocal(){
        return ultimaBusquedaLocal;
    }

    public void eliminarInformacionLocalmente(String terminoDeBusqueda){
        dataBase.deleteEntry(terminoDeBusqueda);
        oyenteGestionDeInformacionLocal.notificarInformacionEliminada();
    }

    private void formatearDatos(String searchResult, String terminoDeBusqueda){
        if (searchResult == null) {
            lastSearchedPageIntroText = "No Results";
        }
        else {
            lastSearchedPageIntroText = "<h1>" + lastSearchedPageTitle + "</h1>";
            lastSearchedPageIntroText += searchResult.replace("\\n", "\n");
            lastSearchedPageIntroText = textToHtml(lastSearchedPageIntroText, terminoDeBusqueda);
        }
    }
    
}
