package dyds.catalog.alpha.fulllogic.vista;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import dyds.catalog.alpha.fulllogic.presentador.StoredInformationPresenter;

public class StoredInfoViewImpl implements StoredInfoView{
    private JPanel storagePanel;
    private JComboBox storedTitlesComboBox;
    private JTextPane storedPageIntroTextPane;
    private JButton deleteButton;

    private StoredInformationPresenter storedInformationPresenter;

    public StoredInfoViewImpl(StoredInformationPresenter storedInformationPresenter){
        this.storedInformationPresenter = storedInformationPresenter;
        initListeners();
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
