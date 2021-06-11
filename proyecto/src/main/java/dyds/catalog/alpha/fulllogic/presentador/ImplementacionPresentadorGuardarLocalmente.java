package dyds.catalog.alpha.fulllogic.presentador;

import dyds.catalog.alpha.fulllogic.modelo.DataBase;
import dyds.catalog.alpha.fulllogic.modelo.OyenteInformacionAlmacenada;
import dyds.catalog.alpha.fulllogic.vista.MainWindow;

import javax.swing.*;

public class ImplementacionPresentadorGuardarLocalmente implements PresentadorGuardarLocalmente{

    MainWindow vista;
    DataBase database;

    public ImplementacionPresentadorGuardarLocalmente(){
        inicializarRecursosPresentador();
    }

    private void inicializarRecursosPresentador(){
        database = new DataBase();

        database.setOyenteInformacionAlmacenada(new OyenteInformacionAlmacenada() {
            @Override
            public void notificacionNuevaInformacionRegistrada() {
                //hacer algo
                vista.setComboBox(new DefaultComboBoxModel(DataBase.getTitles().stream().sorted().toArray()));
                System.out.println("Nueva informacion registrada en la base de datos");
            }
        });
    }

    public void setVista(MainWindow vista) {
        this.vista = vista;
    }

    public void notificacionGuardarInformacionLocalmente() {
        guardarInformacionLocalmente(vista.getInformacionBuscada(),vista.getTituloInformacionBuscada());
    }

    private void guardarInformacionLocalmente(String informacionBuscada, String tituloInformacionBuscada){
        if(informacionBuscada != "" && !informacionBuscada.equals("No Results")){
            // save to DB  <o/
            database.saveInfo(tituloInformacionBuscada.replace("'", "`"), informacionBuscada);  //Dont forget the ' sql problem
            //comboBox1.setModel(new DefaultComboBoxModel(DataBase.getTitles().stream().sorted().toArray()));
        }
    }

}
