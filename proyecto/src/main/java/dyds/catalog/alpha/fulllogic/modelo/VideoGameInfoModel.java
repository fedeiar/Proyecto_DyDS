package dyds.catalog.alpha.fulllogic.modelo;

public interface VideoGameInfoModel {
    
    public String getInformacionUltimaBusqueda();
    public String getTituloUltimaBusqueda();
    public String getLastSearchedTerm();

    public String getUltimaBusquedaLocal();

    public Object[] getTotalTitulosRegistrados();
    
    public void setOyenteGestionDeInformacion(WikipediaSearchInfoListener oyenteBusquedasEnWikipedia);
    public void setOyenteGestionDeInformacionLocal(StoredInformationListener oyenteGestionDeInformacionLocal);

    public void realizarBusquedaEnWikipedia(String terminoDeBusqueda);
    public void guardarInformacionLocalmente(String informacion, String tituloInformacion);

    public void realizarBusquedaLocal(String terminoDeBusqueda);
    public void eliminarInformacionLocalmente(String terminoDeBusuqeda);

}
