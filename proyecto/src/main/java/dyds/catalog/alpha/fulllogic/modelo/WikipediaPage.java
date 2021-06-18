package dyds.catalog.alpha.fulllogic.modelo;

public class WikipediaPage {
    
    private String title;
    private String pageIntro;

    public WikipediaPage(String title, String pageIntro){
        this.title = title;
        this.pageIntro = pageIntro;
    }

    public String getTitle(){
        return title;
    }

    public String getPageIntro(){
        return pageIntro;
    }
}
