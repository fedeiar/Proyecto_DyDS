package dyds.catalog.alpha.fulllogic.presentador;

import dyds.catalog.alpha.fulllogic.vista.MainWindow;

public interface BuscadorEnWikipedia {
    public void realizarNuevaBusqueda(String terminoDeBusqueda);
    String getTituloUltimaBusqueda();
    String getInformacionUltimaBusqueda();
}
