package dyds.catalog.alpha.fulllogic.modelo;

public interface Modelo {
    public String getInformacionUltimaBusqueda();
    public String getTituloUltimaBusqueda();
    public Object[] getTotalTitulosRegistrados();
    public String getUltimaBusquedaLocal();

    public void setOyenteGestionDeInformacion(OyenteBusquedasEnWikipedia oyenteBusquedasEnWikipedia);
    public void setOyenteGestionDeInformacionLocal(OyenteGestionDeInformacionLocal oyenteGestionDeInformacionLocal);

    public void realizarBusquedaEnWikipedia(String terminoDeBusqueda);
    public void guardarInformacionLocalmente(String informacion, String tituloInformacion);

    public void realizarBusquedaLocal(String terminoDeBusqueda);
    public void eliminarInformacionLocalmente(String terminoDeBusuqeda);

}
