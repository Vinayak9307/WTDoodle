package com.nttd.wtdoodle.Client.Utility;

public class KeyPressHandler {
    public static KeyPressHandler instance = new KeyPressHandler();
    boolean searchButtonClicked;
    boolean friendRequestButtonClicked;
    public KeyPressHandler(){
        searchButtonClicked = false;
        friendRequestButtonClicked = false;
    }
    public static KeyPressHandler getInstance(){
        return instance;
    }

    public boolean isSearchButtonClicked() {
        return searchButtonClicked;
    }

    public void setSearchButtonClicked(boolean searchButtonClicked) {
        this.searchButtonClicked = searchButtonClicked;
    }

    public boolean isFriendRequestButtonClicked() {
        return friendRequestButtonClicked;
    }

    public void setFriendRequestButtonClicked(boolean friendRequestButtonClicked) {
        this.friendRequestButtonClicked = friendRequestButtonClicked;
    }
}
