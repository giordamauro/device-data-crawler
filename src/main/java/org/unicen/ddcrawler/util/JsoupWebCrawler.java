package org.unicen.ddcrawler.util;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class JsoupWebCrawler {

    private final Document doc;

    public JsoupWebCrawler(String url) {

        Objects.requireNonNull(url, "Url cannot be null");

        this.doc = getDocument(url);
    }

    public Elements select(String selector) {

        Objects.requireNonNull(selector, "Selector cannot be null");

        return doc.select(selector);
    }
    
    public List<Element> select(String selector, Predicate<Element> predicate) {

        Objects.requireNonNull(selector, "Selector cannot be null");
        Objects.requireNonNull(predicate, "Predicate cannot be null");

        Elements elements = doc.select(selector);
        return elements.stream()
        	.filter(predicate)
        	.collect(Collectors.toList());
    }

    public Optional<Element> selectOne(String selector) {

        Elements elements = select(selector);

        if (elements.size() > 1) {
            throw new IllegalStateException(String.format("Selected more than 1 element %s", elements));
        }

        return Optional.ofNullable(elements.first());
    }

    public Document getDocument() {
        return doc;
    }

    private Document getDocument(String url) {

        try {
            return Jsoup.connect(url).get();

        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
