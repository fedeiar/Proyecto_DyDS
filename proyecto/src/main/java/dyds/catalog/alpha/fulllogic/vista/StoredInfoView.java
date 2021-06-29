package dyds.catalog.alpha.fulllogic.vista;

import java.awt.*;


public interface StoredInfoView {
    
    public Container getContent();

    public int getSelectedTitleIndex();

    public String getSelectedTitle();

    public void cleanPageIntroText();

    public void setStoredSearchedTitles(Object[] storedTitles);

    public void setLocalStoredPageIntro(String storedPageIntro);

    public void operationFailed(String pageSave, String s);

    public void operationSucceded(String pageDelete, String pageDeletedSuccesfully);


    //methods for testing
    
    public void setSelectedTitleIndex(int index);

    public String getLocalStoredPageIntro();

    public boolean doesComboBoxContainsTitle(String title);

    public int getTitleIndexInComboBox(String title);
}
