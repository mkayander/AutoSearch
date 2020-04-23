package net.inqer.autosearch.ui.fragment.search;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import net.inqer.autosearch.data.model.EditableFilter;
import net.inqer.autosearch.data.model.Region;
import net.inqer.autosearch.data.source.repository.RegionsRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.disposables.Disposable;

public class SearchViewModel extends ViewModel {
    private static final String TAG = "SearchViewModel";
    private final RegionsRepository regionsRepository;
    private MutableLiveData<EditableFilter> currentEditableFilter = new MutableLiveData<>(new EditableFilter());

    @Inject
    SearchViewModel(RegionsRepository regionsRepository) {
        this.regionsRepository = regionsRepository;
    }

    LiveData<EditableFilter> getCurrentFilter() {
        return currentEditableFilter;
    }

    public void reNew(EditableFilter instance) {
        currentEditableFilter.setValue(instance);
    }

    public Single<Region> getRegionById(String slug) {
        return regionsRepository.getById(slug);
    }

    public Flowable<List<Region>> observeRegions() {
        return regionsRepository.getAll();
    }

    public void setRegion(String rSlug) {
        Disposable d = regionsRepository.getById(rSlug)
                .subscribe(region -> {
                    EditableFilter filter = currentEditableFilter.getValue();
                    if (filter != null && region != null) {
                        filter.setRegion(region);
                        currentEditableFilter.postValue(filter);
                    } else Log.d(TAG, "setRegion: editable filter should never be null");
                }, throwable -> {
                    Log.e(TAG, "setRegion: Error: ", throwable);
                });
    }
}
