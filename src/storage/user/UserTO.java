package storage.user;

import user.User;

public class UserTO {
    private final String username;
    private final User user;

    public UserTO(String username, User user) {
        this.username = username;
        this.user = user;
    }

    public String getUsername() {
        return username;
    }

    public User getUser() {
        return user;
    }
}
