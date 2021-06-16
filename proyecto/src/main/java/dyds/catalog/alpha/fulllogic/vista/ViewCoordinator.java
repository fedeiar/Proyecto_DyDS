package dyds.catalog.alpha.fulllogic.vista;

import javax.swing.*;
import java.awt.*;

public class ViewCoordinator {

    private JPanel contentPane;
    private JTabbedPane tabbedPane1;
    

    Container wikipediaSearchViewPanel;
    Container storedInfoViewPanel;


    public ViewCoordinator(WikipediaSearchView wikipediaSearchView, StoredInfoView storedInfoView){
        wikipediaSearchViewPanel = wikipediaSearchView.getContent();
        storedInfoViewPanel = storedInfoView.getContent();

        initTabbedPane();
    }

    private void initTabbedPane(){
        tabbedPane1.addTab("Search in Wikipedia", wikipediaSearchViewPanel);
        tabbedPane1.addTab("Interact with Stored Info", storedInfoViewPanel);
    }

    public Container getContent(){
        return this.contentPane;
    }

}
