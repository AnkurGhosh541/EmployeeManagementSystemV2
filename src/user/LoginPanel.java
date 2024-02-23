package user;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class LoginPanel extends JPanel {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPanel() {
        setLayout(new GridLayout(0, 1));
        init();
    }

    private void init() {
        JLabel usernameLabel = new JLabel("Username");
        JLabel passwordLabel = new JLabel("Password");

        Font f = new Font(Font.SANS_SERIF, Font.PLAIN, 18);

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);

        usernameField.setFont(f);
        passwordField.setFont(f);

        add(usernameLabel);
        add(usernameField);

        add(passwordLabel);
        add(passwordField);

    }

    public String[] getLoginInfo() {

        return new String[]{usernameField.getText(), new String(passwordField.getPassword())};
    }

    public void clear() {
        usernameField.setText("");
        passwordField.setText("");
    }

}
