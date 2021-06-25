package dyds.catalog.alpha.fulllogic.vista;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;

import dyds.catalog.alpha.fulllogic.modelo.repositorio.DataBase;
import dyds.catalog.alpha.fulllogic.modelo.repositorio.DataBaseImplementation;
import dyds.catalog.alpha.fulllogic.presentador.StoredInfoPresenter;

public class StoredInfoViewImpl implements StoredInfoView{

    private JPanel storagePanel;
    private JComboBox storedTitlesComboBox;
    private JTextPane storedPageIntroTextPane;
    private JButton deleteButton;

    private StoredInfoPresenter storedInformationPresenter;

    public StoredInfoViewImpl(StoredInfoPresenter storedInformationPresenter){
        this.storedInformationPresenter = storedInformationPresenter;
        formatView();
        initListeners();
    }

    private void formatView(){
        storedPageIntroTextPane.setContentType("text/html");
    }

    private void initListeners(){

        deleteButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent){
                setWorkingStatus();
                storedInformationPresenter.onEventDeleteLocalEntryInfo();
            }
        });

        storedTitlesComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                setWorkingStatus();
                storedInformationPresenter.onEventSearchLocalEntriesInfo();
            }
        });
    }

    public Container getContent(){
        return this.storagePanel;
    }

    private void setWorkingStatus() {
        for(Component c: this.storagePanel.getComponents()){
            c.setEnabled(false);
        }
        storedPageIntroTextPane.setText("");
    }
    
    private void setWatingStatus() {
        for(Component c: this.storagePanel.getComponents()){
            c.setEnabled(true);
        }
    }

    public int getSelectedTitleIndex() {
        return storedTitlesComboBox.getSelectedIndex();
    }

    public String getSelectedTitle() {
        return storedTitlesComboBox.getSelectedItem().toString();
    }

    public void cleanPageIntroText(){
        storedTitlesComboBox.setSelectedIndex(-1);
        storedPageIntroTextPane.setText("");
    }

    public void setStoredSearchedTitles(Object[] storedTitles){
        storedTitlesComboBox.setModel(new DefaultComboBoxModel(storedTitles));
        this.setWatingStatus();
    }

    public void setLocalStoredPageIntro(String storedPageIntro){
        storedPageIntroTextPane.setText(storedPageIntro);
        this.setWatingStatus();
    }

    public void operationSucceded(String title, String message){
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
        this.setWatingStatus();
    }

    public void operationFailed(String title, String message){
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
        this.setWatingStatus();;
    }

    public void setSelectedStoredTitle(String title) {
        storedTitlesComboBox.setSelectedItem(title);
    }

}
