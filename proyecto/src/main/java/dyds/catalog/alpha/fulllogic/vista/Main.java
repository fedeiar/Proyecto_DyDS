package dyds.catalog.alpha.fulllogic.vista;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import dyds.catalog.alpha.fulllogic.modelo.repositorio.DataBaseImplementation;

public class Main {
    public static void main(String[] args) {

        DataBaseImplementation.getInstance().loadDatabase();
    
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
