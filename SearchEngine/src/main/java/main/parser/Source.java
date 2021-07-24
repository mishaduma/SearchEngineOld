package main.parser;

import main.model.Page;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Source {

    private String url;
    private Page page = new Page();

    public Source(String url) {
        this.url = url;
    }

    public Collection<Source> getChildren() {
        Collection<Source> children = new HashSet<>();
        Set<String> childNames = new HashSet<>();

        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1;en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .timeout(1000)
                    .get();

            Elements elements = doc.getElementsByTag("a");
            elements.stream()
                    .map(element -> element.absUrl("href"))
                    .filter(element -> element.contains(url))
                    .map(element -> element.substring(0, element.lastIndexOf(".html") + 5))
                    .filter(element -> element.length() > url.length())
                    .forEach(childNames::add);

            if (!url.endsWith("/")) {
                url += "/";
            }
            page.setPath(url.substring(url.indexOf("//") + 2).substring(url.substring(url.indexOf("//") + 2).indexOf("/")));
            page.setContent(doc.html());
            page.setCode(Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1;en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .execute()
                    .statusCode());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        for (String child : childNames) {
            children.add(new Source(child));
        }
        return children;
    }

    public Page getPage() {
        return page;
    }

    public String getUrl() {
        return url;
    }
}
