package ru.romanblack.test.network;

import retrofit2.http.GET;
import ru.romanblack.test.network.response.QuotesResponse;
import ru.romanblack.test.util.Consts;
import rx.Observable;

public interface Service {

    @GET(Consts.PATH_QUOTES)
    Observable<QuotesResponse> getQuotes();
}
