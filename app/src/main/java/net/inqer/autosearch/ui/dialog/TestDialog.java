package net.inqer.autosearch.ui.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import net.inqer.autosearch.data.model.Region;
import net.inqer.autosearch.data.source.api.MainApi;
import net.inqer.autosearch.databinding.DialogBinding;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.support.DaggerDialogFragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TestDialog extends DaggerDialogFragment {
    public static final String REG_NAME = "region_name";
    private static final String TAG = "TestDialog";
    @Inject
    MainApi api;
    private DialogBinding binding;
    private List<Region> arrayList = new ArrayList<>();
    private DialogListAdapter adapter;
    private CompositeDisposable disposableBag = new CompositeDisposable();
    private Intent data = new Intent();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DialogBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: ");

        setupRecyclerView();
        fetchData();
    }

    private void fetchData() {
        Disposable fetch = api.getListOfRegions()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(regions -> {
                    Log.d(TAG, "fetchData: received: " + regions);
                    arrayList.addAll(regions);
                    adapter.notifyDataSetChanged();
                }, throwable -> {
                    Log.e(TAG, "fetchData: Error: ", throwable);
                });
        disposableBag.add(fetch);

//        Disposable fetch = api.getRegions()
//                .concatMap(page -> {
////                    arrayList.addAll(regionPageResponse.getResults());
//                    if (page.getNext()!=null) {
//                        return Flowable.just(page)
//                                .concatWith(api.getPageRegion(page.getNext()));
//                    }
//                })

//        Disposable fetch2 = api.getRegions()
//                .flatMap(page -> getPageAndNext(page.getNext())
//                        .concatMap(page2 -> Flowable.fromArray(page2.getResults().toArray())))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(o -> {
//                    Log.d(TAG, "fetchData: "+o.toString());
//                    arrayList.add(((Region) o));
//                    adapter.notifyDataSetChanged();
//                }, throwable -> {
//                    Log.e(TAG, "fetchData: Error: ", throwable);
//                });
    }

//    private Flowable<PageResponse<Region>> getPageAndNext(String pageUrl) {
//        return api.getPageRegion(pageUrl)
//                .concatMap(page -> {
//                    if (page.getNext() == null) {
//                        return Flowable.just(page);
//                    }
//                    return Flowable.just(page)
//                            .concatWith(getPageAndNext(page.getNext()));
//                });
//    }

    private void setupRecyclerView() {
        binding.dialogLocRv.setHasFixedSize(true);
        binding.dialogLocRv.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new DialogListAdapter(arrayList, position -> {
            Region region = arrayList.get(position);
            Toast.makeText(getContext(), "Pressed: "+region.toString(), Toast.LENGTH_SHORT).show();

            data.putExtra(REG_NAME, region.getName());
            // TODO: this is a simple test-implementation. Need to get the actual object, nut just the name string

            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, data);
            dismiss();
        });
        binding.dialogLocRv.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach: Called");
//        disposableBag.clear();
    }

    public interface OnInputSelected {
        void sendInput(String input);
    }
}
