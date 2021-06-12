package dyds.catalog.alpha.fulllogic.modelo;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static dyds.catalog.alpha.fulllogic.utils.Utilidades.textToHtml;


public class ImplementadorBuscadorEnWikipedia implements BuscadorEnWikipedia{
    String searchResultTitle = null; //For storage purposes, se below that it may not coincide with the searched term
    String text = ""; //Last searched text! this variable is central for everything

    private Retrofit retrofit;
    private WikipediaSearchAPI searchAPI ;
    private WikipediaPageAPI pageAPI ;

    public ImplementadorBuscadorEnWikipedia(){
        inicializarBuscador();
    }

    private void inicializarBuscador(){
        retrofit = new Retrofit.Builder()
                .baseUrl("https://en.wikipedia.org/w/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        searchAPI = retrofit.create(WikipediaSearchAPI.class);
        pageAPI = retrofit.create(WikipediaPageAPI.class);
    }

    public void realizarNuevaBusqueda(String terminoDeBusqueda) {
        Response<String> callResponse;
            try {
                    //First, lets search for the term in Wikipedia
                    callResponse = searchAPI.searchForTerm(terminoDeBusqueda + " articletopic:\"video-games\"").execute();

                    System.out.println("JSON " + callResponse.body());

                    Gson gson = new Gson();
                    JsonObject jobj = gson.fromJson(callResponse.body(), JsonObject.class);
                    JsonObject query = jobj.get("query").getAsJsonObject();
                    Iterator<JsonElement> resultIterator = query.get("search").getAsJsonArray().iterator();

                    JsonObject searchResult = null;
                    JsonElement searchResultExtract = null;

                    searchResultTitle = null; //The searched term may not be the same as the real page title
                    String searchResultPageId = null;

                    if (resultIterator.hasNext()) {
                        searchResult = resultIterator.next().getAsJsonObject();
                        searchResultTitle = searchResult.get("title").getAsString();
                        searchResultPageId = searchResult.get("pageid").getAsString();
                    }

                    //If we found a page related to the term, get the text searchResultExtract from there
                    if(searchResultPageId != null){
                        callResponse = pageAPI.getExtractByPageID(searchResultPageId).execute();

                        System.out.println("JSON " + callResponse.body());
                        jobj = gson.fromJson(callResponse.body(), JsonObject.class);
                        query = jobj.get("query").getAsJsonObject();
                        JsonObject pages = query.get("pages").getAsJsonObject();
                        Set<Map.Entry<String, JsonElement>> pagesSet = pages.entrySet();
                        Map.Entry<String, JsonElement> first = pagesSet.iterator().next();
                        JsonObject page = first.getValue().getAsJsonObject();
                        searchResultExtract = page.get("extract");
                    }

                    if (searchResultExtract == null) {
                        text = "No Results";
                    } else {
                        text = "<h1>" + searchResultTitle + "</h1>";
                        text += searchResultExtract.getAsString().replace("\\n", "\n");
                        text = textToHtml(text, terminoDeBusqueda);

                    }

                } catch (IOException e1) {
                    e1.printStackTrace();
                }

    }

    @Override
    public String getTituloUltimaBusqueda() {
        return searchResultTitle;
    }
    @Override
    public String getInformacionUltimaBusqueda() {
        return text;
    }
}
