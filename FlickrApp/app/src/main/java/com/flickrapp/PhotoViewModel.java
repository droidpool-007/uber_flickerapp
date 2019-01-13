package com.flickrapp;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.flickrapp.pojo.Photo;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class PhotoViewModel extends AndroidViewModel {
    private final String TAG = PhotoViewModel.class.getName();

    private PhotoNavigator mPhotoNavigator;
    private ObservableBoolean isSearchEnabled = new ObservableBoolean(true);
    private ObservableBoolean isLoading = new ObservableBoolean(false);

    private ObservableField<String> mSearchText = new ObservableField<>();
    private MutableLiveData<List<Photo>> mPhotoList = new MutableLiveData<>();
    private Application application;
    private GetPhotoDataTask mTask = null;

    public PhotoViewModel(@NonNull Application application) {
        super(application);
        this.application = application;
    }

    public void setNavigator(PhotoNavigator navigator) {
        this.mPhotoNavigator = navigator;
    }

    public String getSearchText() {
        return mSearchText.get();
    }

    public void setSearchText(String s) {
        this.mSearchText.set(s);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if(mTask != null && !mTask.isCancelled()) {
            mTask.cancel(true);
        }
    }

    public void getPhotosFromApi() {
        mTask = new GetPhotoDataTask();
        mTask.execute();
    }

    public MutableLiveData<List<Photo>> getPhotos() {
        return mPhotoList;
    }

    public void setPhotos(List<Photo> photoList) {
        this.mPhotoList.setValue(photoList);
    }

    public ObservableBoolean getIsSearchEnabled() {
        return isSearchEnabled;
    }

    public void setIsSearchEnabled(boolean isSearchEnabled) {
        this.isSearchEnabled.set(isSearchEnabled);
    }

    public ObservableBoolean getIsLoading() {
        return isLoading;
    }

    public void setIsLoading(boolean isLoading) {
        this.isLoading.set(isLoading);
    }

    public class GetPhotoDataTask extends AsyncTask<String, Void, String> {
        public static final String REQUEST_METHOD = "GET";
        public static final int READ_TIMEOUT = 15000;
        public static final int CONNECTION_TIMEOUT = 15000;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setIsLoading(true);
            setIsSearchEnabled(false);
            mPhotoNavigator.indicateUserApiFired(application.getResources().getString(R.string.api_fired_message));
        }

        @Override
        protected String doInBackground(String... params){
            if(!Utils.isInternetAvailable(application.getApplicationContext())) {
                mPhotoNavigator.handleError(application.getResources().getString(R.string.network_error));
                return null;
            }
            String result;
            String inputLine;
            InputStreamReader streamReader = null;
            BufferedReader reader = null;
            try {
                @SuppressLint("StringFormatMatches")
                String url = application.getResources().getString(R.string.flickr_photo_json_url, getSearchText());
                URL myUrl = new URL(url);

                HttpURLConnection connection =(HttpURLConnection) myUrl.openConnection();
                connection.setRequestMethod(REQUEST_METHOD);
                connection.setReadTimeout(READ_TIMEOUT);
                connection.setConnectTimeout(CONNECTION_TIMEOUT);
                connection.connect();
                streamReader = new InputStreamReader(connection.getInputStream());
                reader = new BufferedReader(streamReader);
                StringBuilder stringBuilder = new StringBuilder();
                while((inputLine = reader.readLine()) != null){
                    stringBuilder.append(inputLine);
                }

                result = stringBuilder.toString();
            }
            catch(Exception e){
                Log.e(TAG, "Error occured during HTTP call", e);
                result = null;
            } finally {
                try {
                    if(reader != null) {
                        reader.close();
                    }
                    if(streamReader!= null) {
                        streamReader.close();
                    }
                } catch (IOException e) {
                    Log.e(TAG, "Error occured during releasing objects", e);
                }
            }
            return result;
        }
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            setIsLoading(false);
            setIsSearchEnabled(true);

            try {
                JSONObject jsonObject = new JSONObject(result);
                JSONObject jsonPhotoObject = jsonObject.getJSONObject("photos");
                JSONArray jsonArray = jsonPhotoObject.getJSONArray("photo");
                if (jsonArray != null && jsonArray.length() > 0) {
                    List<Photo> photoList = Arrays.asList(new Gson().fromJson(jsonArray.toString(), Photo[].class));
                    setPhotos(photoList);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
