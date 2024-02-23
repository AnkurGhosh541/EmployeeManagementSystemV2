package storage.user;

import user.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FileStorage implements UserStorageRepository {

    @Override
    public boolean store(HashMap<String, User> userMap) {
        ArrayList<UserTO> userList = new ArrayList<>();

        for (Map.Entry<String, User> stringUserEntry : userMap.entrySet()) {
            String username = stringUserEntry.getKey();
            User user = stringUserEntry.getValue();
            UserTO userTO = new UserTO(username, user);
            userList.add(userTO);
        }

        boolean res = UserDAO.getInstance().storeFile(userList.toArray(new UserTO[0]));
        return res;
    }

    @Override
    public HashMap<String, User> load() {
        HashMap<String, User> userMap = new HashMap<>();

        UserTO[] userTOS = UserDAO.getInstance().loadFile();

        if (userTOS == null) {
            return null;
        }

        for (UserTO userTO : userTOS) {
            userMap.put(userTO.getUsername(), userTO.getUser());
        }

        return userMap;
    }
}
