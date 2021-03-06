package dyds.catalog.alpha.fulllogic.utils;

public class Utilidades {


    public static String giveFormatForStorageInSQL(String text){
        return text.replace("'", "`");
    }

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


    private static String textToHtml(String text, String term) {

        StringBuilder builder = new StringBuilder();

        builder.append("<font face=\"arial\">");

        String textWithBold = text
                .replaceAll("(?i)" + term, "<b>" + term +"</b>"); //Highlight the search term in the extract

        builder.append(textWithBold);

        builder.append("</font>");

        return builder.toString();
    }
}
