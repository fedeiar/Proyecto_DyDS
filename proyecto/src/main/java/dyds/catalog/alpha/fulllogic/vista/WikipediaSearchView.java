package dyds.catalog.alpha.fulllogic.vista;
import java.awt.*;

public interface WikipediaSearchView {
    
    public Container getContent();

    public void setWorkingStatus();

    public void setWatingStatus();

    public String getSearchedTerm();

    public void setPageIntroText(String pageIntroText);

    public void pageSavedSuccesfully();

    public void failedPageSaving();
}
