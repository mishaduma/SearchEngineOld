package main.parser;

import main.lemmatizator.LemmasCounter;
import main.model.Field;
import main.model.FieldService;
import main.model.Page;
import main.model.RankedLemma;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.util.*;

public class Source {

    private String url;
    private Page page = new Page();
    private Connection connection;
    private ArrayList<RankedLemma> rankedLemmas;
    private FieldService fieldService;

    public Source(String url, ArrayList<RankedLemma> rankedLemmas, FieldService fieldService) {
        this.url = url;
        this.rankedLemmas = rankedLemmas;
        this.fieldService = fieldService;
    }

    public Collection<Source> getChildren() {
        Collection<Source> children = new HashSet<>();
        Set<String> childNames = new HashSet<>();

        try {
            connection = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1;en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .timeout(1000);

            Elements elements = connection.get().getElementsByTag("a");
            elements.stream()
                    .map(element -> element.absUrl("href"))
                    .filter(element -> element.contains(url))
                    .map(element -> element.substring(0, element.lastIndexOf(".html") + 5))
                    .filter(element -> element.length() > url.length())
                    .forEach(childNames::add);

            page.setPath(url);
            page.setContent(connection.get().html());
            page.setCode(connection.execute().statusCode());

            synchronized (rankedLemmas) {
                for (Field field : fieldService.getFields()) {
                    Map<String, Integer> newLemmas = new LemmasCounter().getLemmas(connection.get().getElementsByTag(field.getSelector()).toString()
                            .replaceAll("[^А-ЯЁа-яё\\s-]", "")
                            .replaceAll("\\s{2,}", " "));
                    for (int i = 0; i < rankedLemmas.size(); i++) {
                        if (newLemmas.keySet().contains(rankedLemmas.get(i).getLemma()) && rankedLemmas.get(i).getUrl().equals(url)) {
                            rankedLemmas.get(i).setRank(rankedLemmas.get(i).getRank() + (newLemmas.get(rankedLemmas.get(i).getLemma()).intValue() * field.getWeight()));
                            newLemmas.remove(rankedLemmas.get(i).getLemma());
                        }
                    }

                    for (String lemma : newLemmas.keySet()) {
                        RankedLemma rankedLemma = new RankedLemma();
                        rankedLemma.setLemma(lemma);
                        rankedLemma.setUrl(url);
                        rankedLemma.setRank(newLemmas.get(lemma).intValue() * field.getWeight());
                        rankedLemmas.add(rankedLemma);
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        for (String child : childNames) {
            children.add(new Source(child, rankedLemmas, fieldService));
        }
        return children;
    }

    public Page getPage() {
        return page;
    }
}
