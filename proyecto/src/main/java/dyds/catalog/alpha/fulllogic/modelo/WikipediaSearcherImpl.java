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
   

    private Retrofit retrofit;
    private WikipediaSearchAPI searchAPI ;
    private WikipediaPageAPI pageAPI ;

    String searchResultTitle = null; 
    String searchResultPageIntro = ""; 

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

    public boolean searchPage(String searchedTerm) {
        String searchResultPageId = searchPageIDInWikipediaSearchAPI(searchedTerm);

        if(pageFound(searchResultPageId)){
            searchResultPageIntro = searchFirstPageIntroInWikipediaPageAPI(searchResultPageId);
        }

        return pageFound(searchResultPageId);
    }

    private String searchPageIDInWikipediaSearchAPI(String terminoDeBusqueda){
        Response<String> callResponse;
        JsonObject jobj,query;
        String searchResultPageId = null;
        try {
            callResponse = searchAPI.searchForTerm(terminoDeBusqueda + " articletopic:\"video-games\"").execute();


            Gson gson = new Gson();
            jobj = gson.fromJson(callResponse.body(), JsonObject.class);
            query = jobj.get("query").getAsJsonObject();
            Iterator<JsonElement> resultIterator = query.get("search").getAsJsonArray().iterator();

            JsonObject searchResult = null;

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

    private boolean pageFound(String searchResultPageId){
        return searchResultPageId != null;
    }

    private String searchFirstPageIntroInWikipediaPageAPI(String searchResultPageId){
        Response<String> callResponse;
        JsonObject jobj, query;
        Gson gson = new Gson();
        JsonElement searchResultExtract;
        String firstPageIntro = null;
        try {
                callResponse = pageAPI.getExtractByPageID(searchResultPageId).execute();

                jobj = gson.fromJson(callResponse.body(), JsonObject.class);
                query = jobj.get("query").getAsJsonObject();
                JsonObject pages = query.get("pages").getAsJsonObject();
                Set<Map.Entry<String, JsonElement>> pagesSet = pages.entrySet();
                Map.Entry<String, JsonElement> first = pagesSet.iterator().next();
                JsonObject page = first.getValue().getAsJsonObject();
                searchResultExtract = page.get("extract");

                firstPageIntro = searchResultExtract.getAsString();
        }
        catch (IOException e1) {
            e1.printStackTrace();
        }

        return firstPageIntro;
    }


    public String getLastSearchedTitle() {
        return searchResultTitle;
    }

    public String getLastSearchedPageIntro() {
        return searchResultPageIntro;
    }

}
