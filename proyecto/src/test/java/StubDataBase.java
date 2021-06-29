import dyds.catalog.alpha.fulllogic.modelo.repositorio.Database;

import java.sql.SQLException;
import java.util.ArrayList;

public class StubDataBase implements Database {
    String title = null;
    String extract = null;

    @Override
    public void loadDatabase() {

    }

    @Override
    public ArrayList<String> getTitles() throws SQLException {
        return new ArrayList<String>();
    }

    @Override
    public String getExtract(String title) throws SQLException {
        return extract;
    }

    @Override
    public void saveInfo(String title, String extract) throws SQLException {
        this.title = title;
        this.extract = extract;
    }

    @Override
    public void deleteEntry(String title) throws SQLException {
        this.title = null;
        this.extract = null;
    }
}
