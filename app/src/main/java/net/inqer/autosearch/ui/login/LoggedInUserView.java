package net.inqer.autosearch.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String displayName;
    private String token;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(String displayName, String token) {
        this.displayName = displayName;
        this.token = token;
    }

    String getDisplayName() {
        return displayName;
    }

    public String getToken() {
        return token;
    }
}
