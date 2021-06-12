package dyds.catalog.alpha.fulllogic.vista;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import dyds.catalog.alpha.fulllogic.modelo.DataBase;
import dyds.catalog.alpha.fulllogic.presentador.*;

public class MainWindow {
  private JTextField textField1;
  private JButton goButton;
  private JPanel contentPane;
  private JTextPane textPane1;
  private JButton saveLocallyButton;
  private JTabbedPane tabbedPane1;
  private JPanel searchPanel;
  private JPanel storagePanel;
  private JComboBox comboBox1;
  private JTextPane textPane2;
  private JButton deleteButton;
  DataBase database;

  PresentadorBusquedasEnWikipedia presentadorBusquedasEnWikipedia;
  PresentadorGestionDeInformacionLocal presentadorGestionDeInformacionLocal;

  String searchResultTitle = null; //For storage purposes, se below that it may not coincide with the searched term
  String text = ""; //Last searched text! this variable is central for everything

  public MainWindow() {
    database = new DataBase();
    inicializarPresentadores();

    comboBox1.setModel(new DefaultComboBoxModel(database.getTitles().stream().sorted().toArray()));

    textPane1.setContentType("text/html");
    textPane2.setContentType("text/html");

    inicializarBotones();
  }

  private void inicializarBotones(){
    goButton.addActionListener(new ActionListener() {
      @Override public void actionPerformed(ActionEvent e) {
        new Thread(new Runnable() {
          @Override
          public void run() {
            presentadorBusquedasEnWikipedia.notificacionRealizarNuevaBusqueda();
          }
        }).start();
      }
    });

    saveLocallyButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        presentadorBusquedasEnWikipedia.notificacionGuardarBusquedaLocalmente();
      }
    });

    comboBox1.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        presentadorGestionDeInformacionLocal.notificacionBuscarInformacionLocalmente();
      }
    });

    deleteButton.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent actionEvent) {
        presentadorGestionDeInformacionLocal.notificacionEliminarInformacionLocal();
      }
    });
  }

  private void inicializarPresentadores(){
    presentadorBusquedasEnWikipedia = new ImplementacionPresentadorBusquedasEnWikipedia();
    presentadorBusquedasEnWikipedia.setVista(this);

    presentadorGestionDeInformacionLocal = new ImplementacionPresentadorGestionDeInformacionLocal();
    presentadorGestionDeInformacionLocal.setVista(this);

    /*presentadorEliminarInformacionLocalmente = new ImplementacionPresentadorEliminarInformacionLocal();
    presentadorEliminarInformacionLocalmente.setVista(this);

    presentadorRecuperarInformacionLocalmente = new ImplementacionPresentadorRecuperarInformacionLocalmente();
    presentadorRecuperarInformacionLocalmente.setVista(this);*/
  }

  public int getSelectedIndex() {
    return comboBox1.getSelectedIndex();
  }

  public String getSelectedItem() {
    return comboBox1.getSelectedItem().toString();
  }

  public void setWorkingStatus() {
    for(Component c: this.searchPanel.getComponents()) c.setEnabled(false);
    textPane1.setText("");
  }

  public void setWatingStatus() {
    for(Component c: this.searchPanel.getComponents()) c.setEnabled(true);
  }

  public String getDatosIngresados() {
    return textField1.getText();
  }

  public void setUltimaBusquedaEfectuada(String ultimaBusquedaEfectuada){
    text = ultimaBusquedaEfectuada;
    textPane1.setText(ultimaBusquedaEfectuada);
  }

  public void setTituloUltimaBusquedaEfectuada(String tituloUltimaBusquedaEfectuada){
    searchResultTitle = tituloUltimaBusquedaEfectuada;
  }

  public void eliminarInformacionDeLaVista(){
    comboBox1.setSelectedIndex(-1);
    textPane2.setText("");
  }

  public void setInformacionBuscadaLocalmente(String informacionBuscadaLocalmente){
    textPane2.setText(informacionBuscadaLocalmente);
  }

  public void setComboBox(DefaultComboBoxModel defaultComboBoxModel){
    comboBox1.setModel(defaultComboBoxModel);
  }
  public String getInformacionBuscada() {
    return text;
  }
  public String getTituloInformacionBuscada() {
    return searchResultTitle;
  }

  public static void main(String[] args) {

    DataBase.loadDatabase();

    JFrame frame = new JFrame("Video Game Info Catalog");
    frame.setContentPane(new MainWindow().contentPane);
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    frame.pack();
    frame.setVisible(true);

    //Debuggin Stuff - Remove later
    //DataBase.saveInfo("test", "sarasa");
    //System.out.println(DataBase.getExtract("test"));
    //System.out.println(DataBase.getExtract("nada"));
  }
}
