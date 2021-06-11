package dyds.catalog.alpha.fulllogic.utils;

public class Utilidades {

    public static String textToHtml(String text, String term) {

        StringBuilder builder = new StringBuilder();

        builder.append("<font face=\"arial\">");

        String textWithBold = text
                .replace("'", "`") //Replace to avoid SQL errors, we will have to find a workaround..
                .replaceAll("(?i)" + term, "<b>" + term +"</b>"); //Highlight the search term in the extract

        builder.append(textWithBold);

        builder.append("</font>");

        return builder.toString();
    }
}
