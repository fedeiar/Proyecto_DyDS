package dyds.catalog.alpha.fulllogic.presentador;

import dyds.catalog.alpha.fulllogic.modelo.DataBase;
import dyds.catalog.alpha.fulllogic.modelo.OyenteInformacionBuscadaLocalmente;
import dyds.catalog.alpha.fulllogic.vista.MainWindow;

public class ImplementacionPresentadorRecuperarInformacionLocalmente implements PresentadorRecuperarInformacionLocalmente {
    private MainWindow vista;
    private DataBase database;

    public ImplementacionPresentadorRecuperarInformacionLocalmente(){
        inicializarRecursosPresentador();
    }

    private void inicializarRecursosPresentador(){
        database = new DataBase();
        database.setOyenteInformacionBuscadaLocalmente(new OyenteInformacionBuscadaLocalmente() {
            @Override
            public void notificacionInformacionBuscadaLocalmente(String informacionBuscadaLocalmente) {
                vista.setInformacionBuscadaLocalmente(informacionBuscadaLocalmente);
            }
        });
    }
    public void notificacionBuscarInformacionLocalmente() {
        int indice = vista.getSelectedIndex();
        if(indice > -1)
            //textPane2.setText(DataBase.getExtract(comboBox1.getSelectedItem().toString()));
            database.getExtract(vista.getSelectedItem());
    }

    @Override
    public void setVista(MainWindow vista) {
        this.vista = vista;
    }
}
