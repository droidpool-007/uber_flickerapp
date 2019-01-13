# FlickrApp

#### App overview
- MVVM architecture is used
- Scrollable list is generated using RecyclerView with GridLayoutManager
- Network call is made using HttpURLConnection via AsyncTask in ViewModel
- Multiple api calls are restricted for search & bottom scroll

##### 3rd party if allowed, would had used
- Retrofit for network call
- Glide for fetching images



- No proper GIT history as this app was submitted around 10Nov as zipped file to HR.
- I remember Image getting populated but now there is some error like: when searched for kittens
java.io.IOException: Cleartext HTTP traffic to farm5.static.flickr.com not permitted
2019-01-13 23:18:13.405 29664-29787/com.flickrapp W/System.err:     at com.android.okhttp.HttpHandler$CleartextURLFilter.checkURLPermitted(HttpHandler.java:115)
- I have written very basic testcases as I haven't been exposed to work culture where test-cases were preferred but could always learn and adapt for any technical upgradations.
