package com.flickrapp;

import android.databinding.DataBindingUtil;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.flickrapp.adapter.ItemOffsetDecoration;
import com.flickrapp.adapter.OnBottomReachedListener;
import com.flickrapp.adapter.PhotoAdapter;
import com.flickrapp.databinding.ActivityPhotoBinding;

public class PhotoActivity extends AppCompatActivity implements PhotoNavigator {

    private ActivityPhotoBinding mBinding;
    private PhotoViewModel viewModel;
    private PhotoAdapter mPhotoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_photo);

        setUpViewModel();
        initUi();

    }

    private void setUpViewModel() {
        viewModel = new PhotoViewModel(this.getApplication());
        viewModel.setNavigator(this);
        mBinding.setViewModel(viewModel);
    }

    private void initUi() {
        //Set up RecyclerView
        GridLayoutManager manager = new GridLayoutManager(this, getResources().getInteger(R.integer.no_of_grids));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.item_offset);
        mBinding.recyclerView.addItemDecoration(itemDecoration);
        mBinding.recyclerView.setLayoutManager(manager);
        mPhotoAdapter = new PhotoAdapter(this);
        mBinding.recyclerView.setAdapter(mPhotoAdapter);

        mPhotoAdapter.setOnBottomReachedListener(new OnBottomReachedListener() {
            @Override
            public void onBottomReached() {
               viewModel.getPhotosFromApi();
            }
        });

        //Subscribe to LiveData for api response
        subscribeUi();

        //setup Search
        mBinding.btSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetRecyclerView();

                if(mBinding.etSearchBox.getText() != null && mBinding.etSearchBox.getText().toString().trim().length() > 0) {
                    String searchText = mBinding.etSearchBox.getText().toString().trim();
                    viewModel.setSearchText(searchText);
                    viewModel.getPhotosFromApi();
                }
            }
        });
    }

    private void resetRecyclerView() {
        mPhotoAdapter.submitList(null);
        mPhotoAdapter.notifyDataChanged();
    }

    private void subscribeUi() {
        viewModel.getPhotos().observe(this,
                photoList -> {
                    if (photoList != null) {
                        mPhotoAdapter.submitList(photoList);
                        mPhotoAdapter.notifyDataChanged();
                    }
                });
    }

    @Override
    public void handleError(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void indicateUserApiFired(String message) {
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }
}
