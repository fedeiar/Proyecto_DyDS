package dyds.catalog.alpha.fulllogic.modelo;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;



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

    @Override public boolean searchPage(String searchedTerm) throws Exception {
        JsonObject searchResult = searchPageInWikipediaSearchAPI(searchedTerm);

        if(pageFound(searchResult)){
            String pageId = getPageId(searchResult);
            searchResultTitle = getTitle(searchResult);
            searchResultPageIntro = searchFirstPageIntroInWikipediaPageAPI(pageId);
        }

        return pageFound(searchResult);
    }

    private JsonObject searchPageInWikipediaSearchAPI(String terminoDeBusqueda) throws Exception{
        JsonObject searchResult = null;
        
        Response<String> callResponse = searchAPI.searchForTerm(terminoDeBusqueda + " articletopic:\"video-games\"").execute();

        Gson gson = new Gson();
        JsonObject searchResponseJsonObject = gson.fromJson(callResponse.body(), JsonObject.class);
        JsonObject query = searchResponseJsonObject.get("query").getAsJsonObject();
        Iterator<JsonElement> resultIterator = query.get("search").getAsJsonArray().iterator();

        if (resultIterator.hasNext()) {
            searchResult = resultIterator.next().getAsJsonObject();
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

    private String searchFirstPageIntroInWikipediaPageAPI(String searchResultPageId) throws Exception{
        Response<String> callResponse = pageAPI.getExtractByPageID(searchResultPageId).execute();

        Gson gson = new Gson();
        JsonObject pageIDresponseJsonObject = gson.fromJson(callResponse.body(), JsonObject.class);
        JsonObject query = pageIDresponseJsonObject.get("query").getAsJsonObject();
        JsonObject pages = query.get("pages").getAsJsonObject();
        
        JsonElement searchResultExtract = getFirstPageExtract(pages);

        String firstPageIntro = searchResultExtract.getAsString();
        
        return firstPageIntro;
    }

    private JsonElement getFirstPageExtract(JsonObject pages){
        Set<Map.Entry<String, JsonElement>> pagesSet = pages.entrySet();
        Map.Entry<String, JsonElement> first = pagesSet.iterator().next();
        JsonObject page = first.getValue().getAsJsonObject();
        return page.get("extract");
    }

    @Override public String getLastSearchedTitle() {
        return searchResultTitle;
    }

    @Override public String getLastSearchedPageIntro() {
        return searchResultPageIntro;
    }

}
