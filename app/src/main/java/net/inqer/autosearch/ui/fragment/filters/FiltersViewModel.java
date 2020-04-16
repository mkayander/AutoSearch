package net.inqer.autosearch.ui.fragment.filters;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import net.inqer.autosearch.data.model.Filter;
import net.inqer.autosearch.data.source.FiltersRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FiltersViewModel extends ViewModel {
    private static final String TAG = "FiltersViewModel";
    private final FiltersRepository repository;
    private MutableLiveData<List<Filter>> filtersEvent = new MutableLiveData<>();

    private CompositeDisposable disposeBag = new CompositeDisposable();

    @Inject
    FiltersViewModel(FiltersRepository filtersRepository) {
        this.repository = filtersRepository;
        subscribeObservers();
    }

    private void subscribeObservers() {
        disposeBag.clear();

        Disposable filtersSub =
                repository.getAll()
                        .subscribeOn(Schedulers.io())
                        .subscribe(filters -> {
                            filtersEvent.postValue(filters);
                        }, throwable -> {
                            Log.e(TAG, "filtersSub: Error: ", throwable);
                        }, () -> {
                            Log.w(TAG, "filtersSub: Flow Completed");
                        });

        disposeBag.add(filtersSub);
    }

    LiveData<List<Filter>> observeFilters() {
//        return repository.observeFilters();
//        return LiveDataReactiveStreams.fromPublisher(repository.getAll());
        return filtersEvent;
    }

//    LiveData<List<Filter>> observeFilterData() {
//        return filtersList;
//    }

    void resetFilterObserver() {
//        repository.resetFilterObserver();
    }

    void refreshData() {
        subscribeObservers();
//        Disposable refreshSub =
//                repository.refreshFilters()
//                        .subscribeOn(Schedulers.io())
//                        .subscribe(() -> {
//                            Log.d(TAG, "refreshData: Refreshed successfully");
//                        }, throwable -> {
//                            Log.e(TAG, "refreshData: Error: ", throwable);
//                        });
    }

    void deleteFilter(Filter filter) {
        Disposable d = repository.delete(filter)
        .subscribeOn(Schedulers.io())
        .subscribe(() -> {
            Log.d(TAG, "deleteFilter: Completed");
        }, throwable -> {
            Log.e(TAG, "deleteFilter: Error: ", throwable);
        });
    }

    Completable deleteFilterRx(Filter filter) { return repository.delete(filter); }

    void deleteFilters() {
        Disposable clearSub =
                repository.clear()
                        .subscribeOn(Schedulers.io())
                        .subscribe(() -> {
                            Log.d(TAG, "deleteFilters: Completed!");
                        }, throwable -> {
                            Log.e(TAG, "deleteFilters: Error:", throwable);
                        });
    }


    private void dispose(Disposable disposable) {
        disposable.dispose();
    }
}
