package net.inqer.autosearch.data.source.api;

import net.inqer.autosearch.data.model.AccountProperties;
import net.inqer.autosearch.data.model.City;
import net.inqer.autosearch.data.model.QueryFilter;
import net.inqer.autosearch.data.model.Region;
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
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface MainApi {

    @GET("account/")
    Call<AccountProperties> getAccountProperties();

    @GET("cities/{city_slug}/")
    Call<City> getCityBySlug(@Header("Authorization") String authToken, @Path("city_slug") String slug);


    // TODO: Move filter-related request to sub-component?
    @GET("filters/")
    Flowable<PageResponse<QueryFilter>> getFilters();

    @POST("filters/")
    Completable createFilter(@Body QueryFilter filter);

    @POST("filters/") // TODO: Implement this
    Completable createAllFilters(@Body List<QueryFilter> filters);

    @DELETE("filters/{itemId}")
    Completable deleteFilter(@Path("itemId") int id);

    @DELETE("filters/") // TODO: Implement this
    Completable deleteAllFilters();

    @POST("filters/") // TODO: Implement this
    Completable clearFilters();

    @GET
    <T> Single<PageResponse<T>> getPage(@Url String fullUrl);

    @GET
    Flowable<PageResponse<Region>> getPageRegion(@Url String gullUrl);

    @GET("cities/")
    Flowable<PageResponse<City>> getCitiesByRegion(@Query("region_slug") String regionSlug);

    @GET("regions/")
    Flowable<List<Region>> getRegions();
}
