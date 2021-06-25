package dyds.catalog.alpha.fulllogic.vista;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import dyds.catalog.alpha.fulllogic.presentador.StoredInfoPresenter;


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
        formatView();
        initListeners();
    }

    private void formatView(){
        searchedPageIntroTextPane.setContentType("text/html");
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

    public Container getContent(){
        return this.searchPanel;
    }

    public void setWorkingStatus() {
        for(Component c: this.searchPanel.getComponents()){
            c.setEnabled(false);
        }
    }
    
    public void setWatingStatus() {
        for(Component c: this.searchPanel.getComponents()){
            c.setEnabled(true);
        }
    }

    public String getSearchedTerm() {
        return searchTextField.getText();
    }

    public void setPageIntroText(String pageIntroText){
        searchedPageIntroTextPane.setText(pageIntroText);
        this.setWatingStatus();
        //TODO: considerar poner el setWatingStatus() aca y en todos los métodos que impliquen el fin de un recorrido, 
        //pero al hacer esto la vista es más inteligente (es mas MVC que MVP). No está mal pero aclararlo en el documento.
    }

    public void operationSucceded(String title, String message){
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
        this.setWatingStatus();
    }

    public void operationFailed(String title, String message){
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
        this.setWatingStatus();
    }

    //setters & getters for testing
    @Override
    public String getActualSearch() {
        return searchedPageIntroTextPane.getText();
    }

    public void setTermOfSearch(String termOfSearch){
        searchTextField.setText(termOfSearch);
    }
}
