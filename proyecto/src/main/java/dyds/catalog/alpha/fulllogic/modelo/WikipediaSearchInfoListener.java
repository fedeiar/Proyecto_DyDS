package dyds.catalog.alpha.fulllogic.modelo;

public interface WikipediaSearchInfoListener {

    public void didFoundPageInWikipedia();

    public void didNotFoundPageInWikipedia();

    public void notificarNuevaInformacionRegistrada();
}
