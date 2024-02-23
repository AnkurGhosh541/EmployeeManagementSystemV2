package storage.user;

import user.User;

import java.util.HashMap;

public class UserRepository extends BaseRepository{
    @Override
    public UserStorageRepository getRepository(String type) {
        if (type.equalsIgnoreCase("file")) {
            repository = new FileStorage();
        }
        return repository;
    }

    public boolean save(HashMap<String, User> userMap) {
        return repository.store(userMap);
    }

    public HashMap<String, User> read() {
        return repository.load();
    }
}
