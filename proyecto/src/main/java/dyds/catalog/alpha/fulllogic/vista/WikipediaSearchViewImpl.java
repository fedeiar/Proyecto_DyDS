package dyds.catalog.alpha.fulllogic.vista;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import dyds.catalog.alpha.fulllogic.presentador.StoredInformationPresenter;


import dyds.catalog.alpha.fulllogic.presentador.WikipediaSearchPresenter;

public class WikipediaSearchViewImpl implements WikipediaSearchView{

    private JPanel searchPanel;
    private JTextField searchTextField;
    private JButton searchButton;
    private JTextPane searchedPageIntroTextPane;
    private JButton saveLocallyButton;

    private WikipediaSearchPresenter wikipediaSearchPresenter;

    public WikipediaSearchViewImpl(WikipediaSearchPresenter wikipediaSearchPresenter){
        this.wikipediaSearchPresenter = wikipediaSearchPresenter;
        initListeners();
    }

    private void initListeners(){

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                wikipediaSearchPresenter.onEventSearchInWikipedia();
            }
        });

        saveLocallyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                wikipediaSearchPresenter.onEventSaveSearchLocally();
            }
        });
    
    }

    public void setWorkingStatus() {
        for(Component c: this.searchPanel.getComponents()) c.setEnabled(false);
        searchedPageIntroTextPane.setText("");
    }
    
    public void setWatingStatus() {
        for(Component c: this.searchPanel.getComponents()) c.setEnabled(true);
    }

    public String getSearchedText() {
        return searchTextField.getText();
    }

    public void setSearchedPageIntroText(String pageIntroText){
        searchedPageIntroTextPane.setText(pageIntroText);
    }

   



}
