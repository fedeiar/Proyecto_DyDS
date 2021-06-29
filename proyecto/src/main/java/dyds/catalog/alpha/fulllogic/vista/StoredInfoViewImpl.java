package dyds.catalog.alpha.fulllogic.vista;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.*;

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

    @Override public Container getContent(){
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

    @Override public int getSelectedTitleIndex() {
        return storedTitlesComboBox.getSelectedIndex();
    }

    @Override public String getSelectedTitle() {
        return storedTitlesComboBox.getSelectedItem().toString();
    }

    @Override public void cleanPageIntroText(){
        storedTitlesComboBox.setSelectedIndex(-1);
        storedPageIntroTextPane.setText("");
    }

    @Override public void setStoredSearchedTitles(Object[] storedTitles){
        storedTitlesComboBox.setModel(new DefaultComboBoxModel(storedTitles));
        this.setWatingStatus();
    }

    @Override public void setLocalStoredPageIntro(String storedPageIntro){
        storedPageIntroTextPane.setText(storedPageIntro);
        this.setWatingStatus();
    }

    @Override public void operationSucceded(String title, String message){
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
        this.setWatingStatus();
    }

    @Override public void operationFailed(String title, String message){
        JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE);
        this.setWatingStatus();
    }

    
    //methods for testing

    @Override
    public void setSelectedTitleIndex(int index) {
        storedTitlesComboBox.setSelectedIndex(index);
    }

    @Override public String getLocalStoredPageIntro() {
        return storedPageIntroTextPane.getText();
    }

    @Override public boolean doesComboBoxContainsTitle(String title){
        boolean contains = false;;
        int size = storedTitlesComboBox.getItemCount();
        for(int i = 0; i < size && !contains; i++){
            String item = storedTitlesComboBox.getItemAt(i).toString();
            contains = item.equals(title);
        }
        return contains;
    }

    @Override public int getTitleIndexInComboBox(String title){
        int index = -1;
        int size = storedTitlesComboBox.getItemCount();
        for(int i = 0; i < size; i++){
            String item = storedTitlesComboBox.getItemAt(i).toString();
            if(item.equals(title)){
                index = i;
                break;
            }
        }
        return index;
    }


}
