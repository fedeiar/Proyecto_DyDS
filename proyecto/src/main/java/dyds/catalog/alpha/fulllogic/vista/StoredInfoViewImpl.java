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
                storedInformationPresenter.onEventDeleteLocalEntryInfo();
            }
        });

        storedTitlesComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                storedInformationPresenter.onEventSearchLocalEntriesInfo();
            }
        });
    }

    public Container getContent(){
        return this.storagePanel;
    }

    public void setWorkingStatus() {
        for(Component c: this.storagePanel.getComponents()){
            c.setEnabled(false);
        }
        storedPageIntroTextPane.setText("");
    }
    
    public void setWatingStatus() {
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
        //TODO: habría vque poner el setWatingStatus() también aca?
    }

    public void setStoredSearchedTitles(Object[] storedTitles){
        storedTitlesComboBox.setModel(new DefaultComboBoxModel(storedTitles));
        this.setWatingStatus();
    }

    public void setLocalStoredPageIntro(String storedPageIntro){
        storedPageIntroTextPane.setText(storedPageIntro);
        this.setWatingStatus();
    }

    public void pagedDeletedSuccesfully(){
        JOptionPane.showMessageDialog(null, "Page deleted succesfully", "Page delete", JOptionPane.INFORMATION_MESSAGE);
        this.setWatingStatus();
    }

    public void failedPageDeletion(){
        JOptionPane.showMessageDialog(null, "Failed page deletion", "Page delete", JOptionPane.ERROR_MESSAGE);
        this.setWatingStatus();
    }

}
