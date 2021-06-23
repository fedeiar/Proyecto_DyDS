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
    private WikipediaSearchAPI searchAPI;
    private WikipediaPageAPI pageAPI;

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
        JsonObject searchResult = searchPageInWikipediaSearchAPI(searchedTerm);

        if(pageFound(searchResult)){
            String pageId = getPageId(searchResult);
            searchResultTitle = getTitle(searchResult);
            searchResultPageIntro = searchFirstPageIntroInWikipediaPageAPI(pageId);
        }

        return pageFound(searchResult);
    }

    private JsonObject searchPageInWikipediaSearchAPI(String terminoDeBusqueda){
        Response<String> callResponse;
        JsonObject jobj;
        JsonObject query;
        JsonObject searchResult = null;
        try {
            callResponse = searchAPI.searchForTerm(terminoDeBusqueda + " articletopic:\"video-games\"").execute();

            Gson gson = new Gson();
            jobj = gson.fromJson(callResponse.body(), JsonObject.class);
            query = jobj.get("query").getAsJsonObject();
            Iterator<JsonElement> resultIterator = query.get("search").getAsJsonArray().iterator();

            if (resultIterator.hasNext()) {
                searchResult = resultIterator.next().getAsJsonObject();
            }
        }
        catch (IOException e1) {
            //TODO: deberíamos capturar aquí cuando queremos realizar una busqueda sin internet?
            e1.printStackTrace();
        }
        return searchResult;
    }

    private String getPageId(JsonObject searchResult){
        return searchResult.get("pageid").getAsString();
    }

    private String getTitle(JsonObject searchResult){
        return searchResult.get("title").getAsString();
    }

    private boolean pageFound(JsonObject searchResult){
        return searchResult != null;
    }

    private String searchFirstPageIntroInWikipediaPageAPI(String searchResultPageId){
        Response<String> callResponse;
        JsonObject jobj;
        JsonObject query;
        Gson gson = new Gson();
        JsonElement searchResultExtract;
        String firstPageIntro = null;
        try {
                callResponse = pageAPI.getExtractByPageID(searchResultPageId).execute();

                jobj = gson.fromJson(callResponse.body(), JsonObject.class);
                query = jobj.get("query").getAsJsonObject();
                JsonObject pages = query.get("pages").getAsJsonObject();
                
                searchResultExtract = getFirstPageExtract(pages);

                firstPageIntro = searchResultExtract.getAsString();
        }
        catch (IOException e1) {
            //TODO: deberíamos capturar aquí cuando queremos realizar una busqueda sin internet?
            e1.printStackTrace();
        }

        return firstPageIntro;
    }

    private JsonElement getFirstPageExtract(JsonObject pages){
        Set<Map.Entry<String, JsonElement>> pagesSet = pages.entrySet();
        Map.Entry<String, JsonElement> first = pagesSet.iterator().next();
        JsonObject page = first.getValue().getAsJsonObject();
        return page.get("extract");
    }

    public String getLastSearchedTitle() {
        return searchResultTitle;
    }

    public String getLastSearchedPageIntro() {
        return searchResultPageIntro;
    }

}
