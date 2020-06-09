package com.alabamaor.moviesapp.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.alabamaor.moviesapp.model.Movie;
import com.alabamaor.moviesapp.viewModel.MovieListViewModel;
import com.alabamaor.moviesapp.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieListFragment extends Fragment implements ListAdapter.ListItem {

    
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    
    private MovieListViewModel mViewModel;
    private ListAdapter mAdapter;

    public static MovieListFragment newInstance() {
        return new MovieListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.movie_list_fragment, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);
        mAdapter = new ListAdapter(new ArrayList<>(), getContext());
        mAdapter.setListItemListener(this);
        mRecyclerView.setAdapter(mAdapter);
        mViewModel.init();
        observeData();

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("");
    }


    private void observeData() {
        mViewModel.getList().observe(getViewLifecycleOwner(), movies -> {
            if (movies != null){
                mAdapter.update(movies);
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add: {


                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMovieSelected(View v, Movie selected) {
        NavDirections action = MovieListFragmentDirections.actionToMovieDetailsFragment(selected);
        Navigation.findNavController(v).navigate(action);
    }
}