package com.alabamaor.moviesapp.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import com.alabamaor.moviesapp.R;
import com.alabamaor.moviesapp.model.Movie;
import com.alabamaor.moviesapp.viewModel.ScannerViewModel;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.zxing.Result;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;
import static androidx.core.content.PermissionChecker.checkSelfPermission;

public class ScannerFragment extends Fragment {

    private static final int PERMISSIONS_REQUEST_CODE = 10;

    @BindView(R.id.scanner_view)
    CodeScannerView scannerView;


    private CodeScanner mCodeScanner;
    private ScannerViewModel mViewModel;

    public static ScannerFragment newInstance() {
        return new ScannerFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scanner_fragment, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(ScannerViewModel.class);
        mCodeScanner = new CodeScanner(getContext(), scannerView);
        mCodeScanner.setDecodeCallback(result -> getActivity().runOnUiThread(() -> {
            Gson gson = new Gson();
            mViewModel.uploadMovie(gson.fromJson(result.toString(), Movie.class));

            Log.i("ALABAMA",result.getText());
        }));
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");

        mViewModel.getIsAdded().observe(getViewLifecycleOwner(), isAdded -> {
            if (isAdded != null){
                NavDirections action = ScannerFragmentDirections.actionScannerFragmentToMovieListFragment(isAdded);
                Navigation.findNavController(getView()).navigate(action);
            }
        });

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        checkPermissions();
    }

    private void checkPermissions() {
        boolean permissionGranted = checkSelfPermission(requireContext(), Manifest.permission_group.CAMERA) == PERMISSION_GRANTED;

        if (permissionGranted) {
            openCamera();
        } else {
            if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
                showError();
            } else {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && requestCode == PERMISSIONS_REQUEST_CODE) {
            openCamera();
        } else {
            showError();
        }
    }

    private void showError() {
        Snackbar.make(getView(), R.string.no_permission, Snackbar.LENGTH_LONG).show();
    }

    private void openCamera() {
        mCodeScanner.startPreview();
    }


    @Override
    public void onStop() {
        super.onStop();
        mCodeScanner.stopPreview();
    }
}