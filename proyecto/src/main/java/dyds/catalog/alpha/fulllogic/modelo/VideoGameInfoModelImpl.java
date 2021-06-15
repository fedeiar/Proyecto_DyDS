package dyds.catalog.alpha.fulllogic.modelo;

import dyds.catalog.alpha.fulllogic.modelo.repositorio.*;

import static dyds.catalog.alpha.fulllogic.utils.Utilidades.textToHtml;

public class VideoGameInfoModelImpl implements VideoGameInfoModel{

    private WikipediaSearcher buscadorEnWikipedia;
    private DataBase dataBase;

    private WikipediaInfoListener oyenteBusquedasEnWikipedia;
    private LocalInformationListener oyenteGestionDeInformacionLocal;

    private String ultimaBusquedaLocal;

    private String ultimaBusquedaEnWikipedia;
    private String tituloUltimaBusquedaEnWikipedia;

    public VideoGameInfoModelImpl(){
        buscadorEnWikipedia = new WikipediaSearcherImpl();
        dataBase = DataBaseImplementation.getInstance();
    }

    @Override
    public void realizarBusquedaEnWikipedia(String terminoDeBusqueda) {
        ultimaBusquedaEnWikipedia = buscadorEnWikipedia.realizarNuevaBusqueda(terminoDeBusqueda);
        tituloUltimaBusquedaEnWikipedia = buscadorEnWikipedia.getTituloUltimaBusqueda();
        formatearDatos(ultimaBusquedaEnWikipedia, terminoDeBusqueda);
        //dar formato a los resultados de la busqueda?
        oyenteBusquedasEnWikipedia.notificarInformacionBuscada();
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
        return ultimaBusquedaEnWikipedia;
    }
    public String getTituloUltimaBusqueda(){
        //return buscadorEnWikipedia.getTituloUltimaBusqueda();
        return tituloUltimaBusquedaEnWikipedia;
    }

    public void setOyenteGestionDeInformacion(WikipediaInfoListener oyenteBusquedasEnWikipedia){
        this.oyenteBusquedasEnWikipedia = oyenteBusquedasEnWikipedia;
    }

    public void setOyenteGestionDeInformacionLocal(LocalInformationListener oyenteGestionDeInformacionLocal){
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
            ultimaBusquedaEnWikipedia = "No Results";
        }
        else {
            ultimaBusquedaEnWikipedia = "<h1>" + tituloUltimaBusquedaEnWikipedia + "</h1>";
            ultimaBusquedaEnWikipedia += searchResult.replace("\\n", "\n");
            ultimaBusquedaEnWikipedia = textToHtml(ultimaBusquedaEnWikipedia, terminoDeBusqueda);
        }
    }
    
}
