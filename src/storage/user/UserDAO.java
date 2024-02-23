package storage.user;

import user.User;

import java.io.*;
import java.util.ArrayList;

public class UserDAO {
    private static UserDAO userDAO = null;

    private UserDAO() {

    }

    public static UserDAO getInstance() {
        if (userDAO == null) {
            userDAO = new UserDAO();
        }
        return userDAO;
    }

    public boolean storeFile(UserTO[] users) {
        try (FileOutputStream fos = new FileOutputStream("resources/user.db")) {
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            for (UserTO user : users) {
                oos.writeObject(user.getUser());
            }
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    public UserTO[] loadFile() {
        ArrayList<UserTO> userTOList = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream("resources/user.db")) {
            ObjectInputStream ois = new ObjectInputStream(fis);
            while (true) {
                try {
                    User user = (User) ois.readObject();
                    UserTO userTO = new UserTO(user.getUsername(), user);
                    userTOList.add(userTO);
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            userTOList = null;
        }

        if (userTOList != null)
            return userTOList.toArray(new UserTO[0]);
        else
            return null;
    }
}
