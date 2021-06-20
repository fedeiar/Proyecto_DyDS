package dyds.catalog.alpha.fulllogic.vista;

import java.awt.*;

public interface StoredInfoView {
    
    public Container getContent();

    public void setWorkingStatus();
    
    public void setWatingStatus();

    public int getSelectedTitleIndex();

    public String getSelectedTitle();

    public void cleanPageIntroText();

    public void setStoredSearchedTitles(Object[] storedTitles);

    public void setLocalStoredPageIntro(String storedPageIntro);

    public void pagedDeletedSuccesfully();

    public void failedPageDeletion();
}
