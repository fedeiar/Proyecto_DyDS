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


public class WikipediaSearcherImpl implements WikipediaSearcher{
    String searchResultTitle = null; //For storage purposes, se below that it may not coincide with the searched term
    String text = ""; //Last searched text! this variable is central for everything

    private Retrofit retrofit;
    private WikipediaSearchAPI searchAPI ;
    private WikipediaPageAPI pageAPI ;


    public WikipediaSearcherImpl(){
        initSearcher();
    }

    private void initSearcher(){
        retrofit = new Retrofit.Builder()
                .baseUrl("https://en.wikipedia.org/w/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        searchAPI = retrofit.create(WikipediaSearchAPI.class);
        pageAPI = retrofit.create(WikipediaPageAPI.class);
    }

    public String realizarNuevaBusqueda(String terminoDeBusqueda) {
        String searchResultPageId = searchInWikipediaSearchAPI(terminoDeBusqueda);
        String resultSearchInWikipedia = searchInWikipediaPageAPI(searchResultPageId);
        return resultSearchInWikipedia;
        //formatearDatos(resultSearchInWikipedia , terminoDeBusqueda);
        //dar formato acá o en el modelo ?
    }

    public String getTituloUltimaBusqueda() {
        return searchResultTitle;
    }
    public String getInformacionUltimaBusqueda() {
        return text;
    }

    private String searchInWikipediaSearchAPI(String terminoDeBusqueda){
        Response<String> callResponse;
        JsonObject jobj,query;
        String searchResultPageId = null;
        try {
            callResponse = searchAPI.searchForTerm
                    (terminoDeBusqueda + " articletopic:\"video-games\"").execute();

            System.out.println("JSON " + callResponse.body());

            Gson gson = new Gson();
            jobj = gson.fromJson(callResponse.body(), JsonObject.class);
            query = jobj.get("query").getAsJsonObject();
            Iterator<JsonElement> resultIterator = query.get("search").getAsJsonArray().iterator();

            JsonObject searchResult = null;
            JsonElement searchResultExtract = null;

            searchResultTitle = null; //The searched term may not be the same as the real page title

            if (resultIterator.hasNext()) {
                searchResult = resultIterator.next().getAsJsonObject();
                searchResultTitle = searchResult.get("title").getAsString();
                searchResultPageId = searchResult.get("pageid").getAsString();
            }
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
        return searchResultPageId;
    }

    private String searchInWikipediaPageAPI(String searchResultPageId){
        Response<String> callResponse;
        JsonObject jobj,query;
        Gson gson = new Gson();
        JsonElement searchResultExtract = null;
        String resultOfSearch;
        //If we found a page related to the term, get the text searchResultExtract from there
        try {
            if (searchResultPageId != null) {
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
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }
        if (searchResultExtract == null)
            resultOfSearch = "No Results";
        else
            resultOfSearch = searchResultExtract.getAsString();
        return resultOfSearch;
    }

    /*private void formatearDatos(String searchResult, String terminoDeBusqueda){
        if (searchResult == null) {
            text = "No Results";
        }
        else {
            text = "<h1>" + searchResultTitle + "</h1>";
            text += searchResult.replace("\\n", "\n");
            text = textToHtml(text, terminoDeBusqueda);
        }
    }*/
}
