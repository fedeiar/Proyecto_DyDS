package dyds.catalog.alpha.fulllogic.vista;

import java.awt.*;

public interface StoredInfoView {
    
    public Container getContent();

    public int getSelectedTitleIndex();

    public String getSelectedTitle();

    public void cleanPageIntroText();

    public void setStoredSearchedTitles(Object[] storedTitles);

    public void setLocalStoredPageIntro(String storedPageIntro);

    void operationFailed(String page_save, String s);

    void setWorkingStatus();

    void operationSucceded(String page_delete, String page_deleted_succesfully);
}
