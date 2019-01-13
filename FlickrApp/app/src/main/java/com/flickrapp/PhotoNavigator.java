package com.flickrapp;

interface PhotoNavigator {
    void handleError(String message);
    void indicateUserApiFired(String message);
}
