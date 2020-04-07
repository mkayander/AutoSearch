package net.inqer.autosearch.ui.fragment.parameters;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.inqer.autosearch.dagger.ViewModelProviderFactory;
import net.inqer.autosearch.data.model.AccountProperties;
import net.inqer.autosearch.databinding.FragmentParametersBinding;

import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;

public class ParametersFragment extends DaggerFragment {
    private static final String TAG = "ParametersFragment";

    @Inject
    ViewModelProviderFactory providerFactory;

    private ParametersViewModel viewModel;

    private FragmentParametersBinding binding;

//    public static ParametersFragment newInstance() {
//        return new ParametersFragment();
//    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentParametersBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this, providerFactory).get(ParametersViewModel.class);

        viewModel.updateAccountData();
        viewModel.getAccountProperties().observe(getViewLifecycleOwner(), this::bindProfileData);

        binding.paramButtonSignOut.setOnClickListener(v -> {
            Toast.makeText(v.getContext(), "Sign Out button pressed", Toast.LENGTH_SHORT).show();
        });
        binding.paramButtonSignOut.setOnLongClickListener(v -> {
            Toast.makeText(v.getContext(), "Sign Out LONG click pressed", Toast.LENGTH_SHORT).show();
            return true;
        });
    }


    private void bindProfileData(@NotNull AccountProperties properties) {
        Log.d(TAG, "bindProfileData: Called.");
        Log.d(TAG, "bindProfileData: "+properties.toString());
        Log.d(TAG, "bindProfileData: "+binding.paramUsername.getText().toString());
        binding.paramUsername.setText(properties.getUsername());
        binding.paramEmail.setText(properties.getEmail());
        binding.paramLastLoginValue.setText(properties.getLast_login());
        binding.paramDateJoinedValue.setText(properties.getDate_joined());
    }
}