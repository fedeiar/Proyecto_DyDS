package dyds.catalog.alpha.fulllogic.presentador;

import dyds.catalog.alpha.fulllogic.modelo.DataBase;
import dyds.catalog.alpha.fulllogic.modelo.OyenteInformacionEliminada;
import dyds.catalog.alpha.fulllogic.vista.MainWindow;

import javax.swing.*;

public class ImplementacionPresentadorEliminarInformacionLocal implements PresentadorEliminarInformacionLocalmente {
    private MainWindow vista;
    private DataBase database;


    public ImplementacionPresentadorEliminarInformacionLocal(){
        inicializarRecursosPresentador();
    }

    @Override
    public void notificacionEliminarBusquedaLocal() {
        int indice = vista.getSelectedIndex();
        String tituloInformacion = vista.getSelectedItem();
        if(indice > -1){
            database.deleteEntry(tituloInformacion);
            /*comboBox1.setModel(new DefaultComboBoxModel(DataBase.getTitles().stream().sorted().toArray()));
            comboBox1.setSelectedIndex(-1);
            textPane2.setText("");*/
        }
    }
    private void actualizarVista(){
        vista.setComboBox(new DefaultComboBoxModel(DataBase.getTitles().stream().sorted().toArray()));
        vista.eliminarInformacionDeLaVista();
    }

    private void inicializarRecursosPresentador(){
        database = new DataBase();

        database.setOyenteInformacionEliminada(new OyenteInformacionEliminada() {
            @Override
            public void notificacionInformacionEliminada() {
                actualizarVista();
            }
        });
    }

    @Override
    public void setVista(MainWindow vista) {
        this.vista = vista;
    }
}
