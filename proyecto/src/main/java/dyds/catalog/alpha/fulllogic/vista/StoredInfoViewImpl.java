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
        //como hacer esta instruccion mas clean?
        storedTitlesComboBox.setModel(new DefaultComboBoxModel(DataBaseImplementation.getInstance().getTitles().stream().sorted().toArray()));
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
    }

    public void setLocalStoredPageIntro(String storedPageIntro){
        storedPageIntroTextPane.setText(storedPageIntro);
    }

}
