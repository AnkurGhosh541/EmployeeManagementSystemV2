package user;

import javax.swing.*;
import java.awt.*;

public class PasswordChangePanel extends JPanel {
    private JPasswordField passwordField, conformField;

    public PasswordChangePanel() {
        setLayout(new GridLayout(0, 1));
        init();
    }

    private void init() {
        Font f = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
        JLabel passwordLabel = new JLabel("Enter new password");
        JLabel conformLabel = new JLabel("Confirm password");

        passwordLabel.setFont(f);
        conformLabel.setFont(f);

        passwordField = new JPasswordField(20);
        conformField = new JPasswordField(20);

        add(passwordLabel);
        add(passwordField);

        add(conformLabel);
        add(conformField);
    }

    public boolean confirmPassword() {
        String p1 = new String(passwordField.getPassword());
        String p2 = new String(conformField.getPassword());

        return p1.equals(p2);
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public void clear() {
        passwordField.setText("");
        conformField.setText("");
    }
}
