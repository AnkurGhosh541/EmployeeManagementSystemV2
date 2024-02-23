package storage.user;

import user.User;

import java.util.HashMap;

public interface UserStorageRepository {
    public boolean store(HashMap<String, User> userMap);
    public HashMap<String, User> load();
}
