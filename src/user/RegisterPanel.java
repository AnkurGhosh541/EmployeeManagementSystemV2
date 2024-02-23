package user;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class RegisterPanel extends JPanel {

    private JTextField usernameField, emailField;
    private JPasswordField passwordField;

    public RegisterPanel() {
        setLayout(new GridLayout(0, 1));
        init();
    }

    private void init() {
        JLabel usernameLabel = new JLabel("Username");
        JLabel emailLabel = new JLabel("E-Mail");
        JLabel passwordLabel = new JLabel("Password");

        Font f = new Font(Font.SANS_SERIF, Font.PLAIN, 16);

        usernameField = new JTextField(20);
        emailField = new JTextField(20);
        passwordField = new JPasswordField(20);

        emailLabel.setToolTipText("Please enter a valid email as it will need to be verified");
        emailField.setToolTipText("Please enter a valid email as it will need to be verified");

        usernameField.setFont(f);
        emailField.setFont(f);
        passwordField.setFont(f);

        add(usernameLabel);
        add(usernameField);

        add(emailLabel);
        add(emailField);

        add(passwordLabel);
        add(passwordField);
    }

    public String[] getRegInfo() {

        return new String[]{usernameField.getText(), emailField.getText(), Arrays.toString(passwordField.getPassword())};
    }

    public void clear() {
        usernameField.setText("");
        emailField.setText("");
        passwordField.setText("");
    }
}
