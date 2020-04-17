package net.inqer.autosearch.data.source.api;

import net.inqer.autosearch.data.model.AccountProperties;
import net.inqer.autosearch.data.model.City;
import net.inqer.autosearch.data.model.Filter;
import net.inqer.autosearch.data.model.api.PageResponse;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Url;

public interface MainApi {

    @GET("account/")
    Call<AccountProperties> getAccountProperties();

    @GET("cities/{city_slug}/")
    Call<City> getCityBySlug(@Header("Authorization") String authToken, @Path("city_slug") String slug);


    // TODO: Move filter-related request to sub-component?
    @GET("filters/")
    Flowable<PageResponse<Filter>> getFilters();

    @POST("filters/")
    Completable createFilter(@Body Filter filter);

    @POST("filters/") // TODO: Implement this
    Completable createAllFilters(@Body List<Filter> filters);

    @DELETE("filters/") // TODO: Implement this
    Completable deleteFilter(@Body Filter filter);

    @DELETE // TODO: Implement this
    Completable deleteAllFilters(@Body List<Filter> filters);

    @POST("filters/") // TODO: Implement this
    Completable clearFilters();

    @GET
    Single<PageResponse<Object>> getPage(@Url String fullUrl);
}