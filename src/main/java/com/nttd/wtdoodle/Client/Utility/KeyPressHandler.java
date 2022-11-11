package com.nttd.wtdoodle.Client.Utility;

public class KeyPressHandler {
    public static KeyPressHandler instance = new KeyPressHandler();
    boolean searchButtonClicked;
    boolean friendRequestButtonClicked;
    boolean hostButtonClicked;

    public KeyPressHandler(){
        searchButtonClicked = false;
        friendRequestButtonClicked = false;
        hostButtonClicked = false;
    }
    public static KeyPressHandler getInstance(){
        return instance;
    }
    public boolean isHostButtonClicked() {

        return hostButtonClicked;
    }

    public void setHostButtonClicked(boolean hostButtonClicked) {
        this.hostButtonClicked = hostButtonClicked;
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
