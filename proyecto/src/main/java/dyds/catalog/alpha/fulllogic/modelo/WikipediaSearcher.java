package dyds.catalog.alpha.fulllogic.modelo;

import dyds.catalog.alpha.fulllogic.vista.MainWindow;

public interface WikipediaSearcher {
    public void realizarNuevaBusqueda(String terminoDeBusqueda);
    String getTituloUltimaBusqueda();
    String getInformacionUltimaBusqueda();
}
