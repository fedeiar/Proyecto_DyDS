package dyds.catalog.alpha.fulllogic.vista;
import java.awt.*;

public interface WikipediaSearchView {
    
    public Container getContent();

    public void setWorkingStatus();

    public void setWatingStatus();

    public String getSearchedTerm();

    public void setPageIntroText(String pageIntroText);

    public void operationSucceded(String title, String message);

    public void operationFailed(String title, String message);

    //setters & getters for testing
    public void setTermOfSearch(String termOfSearch);

    public String getActualSearch();
}
