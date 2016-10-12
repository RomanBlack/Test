package ru.romanblack.test.network.response;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.List;

import ru.romanblack.test.data.entities.Quote;

@Root(name = "result", strict = false)
public class QuotesResponse implements Serializable {

    @Element(name = "totalPages")
    private int totalPages;

    @Element(name = "quotes")
    private QuotesList quotesList;

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public QuotesList getQuotesList() {
        return quotesList;
    }

    public void setQuotesList(QuotesList quotesList) {
        this.quotesList = quotesList;
    }

    public static class QuotesList implements Serializable {

        @ElementList(inline = true)
        public List<Quote> quotes;

        public List<Quote> getQuotes() {
            return quotes;
        }

        public void setQuotes(List<Quote> quotes) {
            this.quotes = quotes;
        }
    }
}
