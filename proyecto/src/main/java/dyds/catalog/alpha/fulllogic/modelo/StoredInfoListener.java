package dyds.catalog.alpha.fulllogic.modelo;

public interface StoredInfoListener {

    public void didSearchPageStoredLocally();

    public void didDeletePageStoredLocally();

    public void didUpdateStoredTitles();
}
