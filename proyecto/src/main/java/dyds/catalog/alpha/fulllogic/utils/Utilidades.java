package dyds.catalog.alpha.fulllogic.utils;

public class Utilidades {



    //TODO: lo de formatData() de los presentadores ponerlo acá.

    public static String formatData(String title, String text, String term){
        String formattedText;
        
        formattedText = formatData(title, text);
        formattedText = Utilidades.textToHtml(formattedText, term);
        
        return formattedText;
    }

    public static String formatData(String title, String text){
        String formattedText;
        
        formattedText = "<h1>" + title + "</h1>";
        formattedText += text.replace("\\n", "\n");
        
        return formattedText;
    }


    public static String textToHtml(String text, String term) {

        StringBuilder builder = new StringBuilder();

        builder.append("<font face=\"arial\">");

        String textWithBold = text
                //.replace("'", "`") //Replace to avoid SQL errors, we will have to find a workaround..
                .replaceAll("(?i)" + term, "<b>" + term +"</b>"); //Highlight the search term in the extract

        builder.append(textWithBold);

        builder.append("</font>");

        return builder.toString();
    }
}
