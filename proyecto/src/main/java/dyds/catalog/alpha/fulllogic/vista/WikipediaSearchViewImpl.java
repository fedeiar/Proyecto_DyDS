package dyds.catalog.alpha.fulllogic.vista;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;


import dyds.catalog.alpha.fulllogic.presentador.WikipediaSearchPresenter;

public class WikipediaSearchViewImpl implements WikipediaSearchView{

    private JPanel searchPanel;
    private JTextField searchTextField;
    private JButton searchButton;
    private JTextPane searchedPageIntroTextPane;
    private JButton saveLocallyButton;

    private WikipediaSearchPresenter wikipediaSearchPresenter;

    //for testing
    private String dialogTestMessageBody = null;

    public WikipediaSearchViewImpl(WikipediaSearchPresenter wikipediaSearchPresenter){
        this.wikipediaSearchPresenter = wikipediaSearchPresenter;
        formatView();
        initListeners();
    }

    private void formatView(){
        searchedPageIntroTextPane.setContentType("text/html");
    }

    private void initListeners(){

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setWorkingStatus();
                wikipediaSearchPresenter.onEventSearchInWikipedia();
            }
        });

        saveLocallyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                setWorkingStatus();
                wikipediaSearchPresenter.onEventSaveSearchLocally();
            }
        });
    
    }

    @Override public Container getContent(){
        return this.searchPanel;
    }

    private void setWorkingStatus() {
        for(Component c: this.searchPanel.getComponents()){
            c.setEnabled(false);
        }
    }
    
    private void setWatingStatus() {
        for(Component c: this.searchPanel.getComponents()){
            c.setEnabled(true);
        }
    }

    @Override public String getSearchedTerm() {
        return searchTextField.getText();
    }

    @Override public void setPageIntroText(String pageIntroText){
        searchedPageIntroTextPane.setText(pageIntroText);
        this.setWatingStatus();
    }

    @Override public void operationSucceded(String title, String message){
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
        this.setWatingStatus();
    }

    @Override public void operationFailed(String title, String message){
        dialogTestMessageBody = message;
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
        this.setWatingStatus();
    }

    
    //methods for testing

    @Override public String getActualSearch() {
        return searchedPageIntroTextPane.getText();
    }

    @Override public void setTermOfSearch(String termOfSearch){
        searchTextField.setText(termOfSearch);
    }

    @Override public String getDialogTestMessage(){
        return dialogTestMessageBody;
    }
}
