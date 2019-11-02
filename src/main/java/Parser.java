import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    private static Pattern pattern = Pattern.compile("\\d{2}\\.\\d{2}");
    private static Pattern partOfDay = Pattern.compile("\\S{4,5}");

    private static String getInfoFromString(String stringDate, Pattern pattern) throws Exception {
        Matcher matcher = pattern.matcher(stringDate);
        if (matcher.find()) {
            return matcher.group();
        }
        throw new Exception("Can't find date in string");
    }
    private static int printValues(Elements values, int index) throws Exception {
        Element dayValue = values.get(index);
        String dayPart = getInfoFromString(dayValue.text(), partOfDay);
        int iterEnd = 0;
        if (dayPart.equals("Утро")) {
            iterEnd = 4;
        } else if (dayPart.equals("День")) {
            iterEnd = 3;
        } else if (dayPart.equals("Вечер")) {
            iterEnd = 2;
        } else iterEnd = 1;
        for (int i = 0; i < iterEnd; i++) {
            Element valueLine = values.get(index);
            for (Element td : valueLine.select("td")) {
                System.out.print(td.text() + "   ");
            }
            System.out.println();
            index++;
        }
        return index;
    }

    private static Document getPage() throws IOException {
        String url = "http://www.pogoda.msk.ru/";
        Document page = Jsoup.parse(new URL(url), 3000);
        return page;
    }

    public static void main(String[] args) throws Exception {
        Document page = getPage();
        Element tableWth = page.select("table[class=wt]").first();
        Elements names = tableWth.select("tr[class=wth]");
        Elements values = tableWth.select("tr[valign=top]");
        int index = 0;
        for (Element name : names) {
            String dateString = name.select("th[id=dt]").text();
            String date = getInfoFromString(dateString, pattern);
            System.out.println(date + "   Явление   Температура   Давление   Влажность   Ветер");
            index = printValues(values, index);
        }

    }
}
