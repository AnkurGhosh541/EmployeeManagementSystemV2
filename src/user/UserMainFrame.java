package user;

import employee.EmployeeMainFrame;
import storage.user.UserRepository;
import utilities.Email;
import utilities.Encryption;
import utilities.OTP;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

public class UserMainFrame extends JFrame implements ActionListener {

    private boolean isLogin;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    private JLabel switchLabel;
    private JLabel switchBtn;
    private JButton comboBtn;
    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    private PasswordChangePanel passwordChangePanel;
    private final HashMap<String, String> passwordMap;
    private HashMap<String, User> userMap;
    private final UserRepository userRepository;

    public UserMainFrame() {
        setTitle("Log In");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300, 400);
        setResizable(false);
        setLocationRelativeTo(null);
        isLogin = true;
        passwordMap = new HashMap<>();
        userRepository = new UserRepository();
        userRepository.getRepository("File");
        loadUsers();
        init();
    }

    private void loadUsers() {
        userMap = userRepository.read();
        if (userMap == null) {
            JOptionPane.showMessageDialog(this, "Could not load users !!", "ERROR", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        for (Map.Entry<String, User> userEntry : userMap.entrySet()) {
            String username = userEntry.getKey();
            User user = userEntry.getValue();
            passwordMap.put(username, user.getPassword());
        }
    }

    private void saveUsers() {
        boolean res;
        do {
            res = userRepository.save(userMap);
        } while (!res);
    }

    private void init() {
        cardLayout = new CardLayout(20, 20);
        mainPanel = new JPanel();
        mainPanel.setLayout(cardLayout);

        JPanel loginPanelContainer = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        loginPanel = new LoginPanel();
        registerPanel = new RegisterPanel();

        JLabel forgotPasswordBtn = new JLabel("forgot password");
        forgotPasswordBtn.setForeground(Color.BLUE);
        forgotPasswordBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                String username = JOptionPane.showInputDialog(mainPanel, "Enter your username");
                if (username == null) return;
                if (username.isBlank()) {
                    JOptionPane.showMessageDialog(mainPanel, "Please enter a valid username", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (!userMap.containsKey(username)) {
                    JOptionPane.showMessageDialog(mainPanel, "No user with this username found", "User not found", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                String email = userMap.get(username).getEmail();

                if (!validate(email, "password"))
                    return;

                passwordChangePanel = new PasswordChangePanel();
                do {
                    int res = JOptionPane.showConfirmDialog(mainPanel, passwordChangePanel, "Reset password", JOptionPane.OK_CANCEL_OPTION);
                    if (res == JOptionPane.CANCEL_OPTION || res == JOptionPane.CLOSED_OPTION) {
                        break;
                    } else if (res == JOptionPane.OK_OPTION) {
                        if (passwordChangePanel.confirmPassword()) {
                            String password = passwordChangePanel.getPassword();
                            passwordChangePanel.clear();
                            String hashedPassword = Encryption.getHashedString(password);
                            passwordMap.put(username, hashedPassword);
                            User updatedUser = new User(username, email, hashedPassword);
                            userMap.put(username, updatedUser);
                            userRepository.save(userMap);
                            JOptionPane.showMessageDialog(mainPanel, "Password Changed !");
                            return;
                        } else {
                            JOptionPane.showMessageDialog(mainPanel, "Both passwords should be same");
                        }
                    }
                } while (true);
            }
        });

        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 0.9;
        c.fill = GridBagConstraints.BOTH;
        loginPanelContainer.add(loginPanel, c);

        c.gridy = 1;
        c.weighty = 0.1;
        c.fill = GridBagConstraints.BOTH;
        loginPanelContainer.add(forgotPasswordBtn, c);

        mainPanel.add("Login", loginPanelContainer);
        mainPanel.add("Register", registerPanel);

        JPanel bottomPanel = new JPanel(new GridBagLayout());

        comboBtn = new JButton("Log In");
        comboBtn.setPreferredSize(new Dimension(160, 40));
        comboBtn.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
        comboBtn.setFocusable(false);
        comboBtn.addActionListener(this);

        JPanel switchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 30));
        switchLabel = new JLabel("No account yet ?");
        switchBtn = new JLabel("Register");
        switchBtn.setForeground(Color.BLUE);

        switchBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                cardLayout.next(mainPanel);
                if (isLogin) {
                    switchLabel.setText("Already have an account ?");
                    switchBtn.setText("Log In");
                    comboBtn.setText("Register");
                    setTitle("Register");
                    isLogin = false;
                    loginPanel.clear();
                } else {
                    switchLabel.setText("No account yet ?");
                    switchBtn.setText("Register");
                    comboBtn.setText("Log In");
                    setTitle("Log In");
                    isLogin = true;
                    registerPanel.clear();
                }
            }
        });

        switchPanel.add(switchLabel);
        switchPanel.add(switchBtn);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        bottomPanel.add(comboBtn, c);

        c.gridy = 1;
        bottomPanel.add(switchPanel, c);

        add(mainPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private boolean validate(String email, String type) {
        String otp;
        do {
            otp = OTP.getOTP();
        } while (otp.length() != 6);

        String message, subject, success;
        if (type.equalsIgnoreCase("verification")) {
            message = "Email validation code : " + otp;
            subject = "EMAIL VERIFICATION";
        } else {
            message = "Code for changing your password is " + otp;
            subject = "PASSWORD CHANGE OTP";
        }
        Email.sendMail(email, message, subject);
        String userOTP;
        do {
            userOTP = JOptionPane.showInputDialog(mainPanel, "Enter the code sent to " + email);
            if (userOTP == null) break;
            else if (userOTP.equals(otp)) {
                return true;
            } else {
                JOptionPane.showMessageDialog(this, "Please enter a valid code", "Wrong OTP", JOptionPane.ERROR_MESSAGE);
            }
        } while (true);

        return false;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equalsIgnoreCase("log in")) {
            String[] loginInfo = loginPanel.getLoginInfo();
            loginPanel.clear();

            String username = loginInfo[0];
            String password = loginInfo[1];

            if (username.isBlank()) {
                JOptionPane.showMessageDialog(this, "Username can not be empty or only whitespace", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!passwordMap.containsKey(username)) {
                JOptionPane.showMessageDialog(this, "Username not found", "No Such User", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String hashedPassword;
            do {
                hashedPassword = Encryption.getHashedString(password);
            } while (hashedPassword == null);

            String mainPassword = passwordMap.get(username);

            if (hashedPassword.equals(mainPassword)) {
                JOptionPane.showMessageDialog(this, "Logged In Successfully !");
                EmployeeMainFrame ef = new EmployeeMainFrame(userMap.get(username));
                ef.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Incorrect password", "Password mismatch", JOptionPane.ERROR_MESSAGE);
            }
        } else if (cmd.equalsIgnoreCase("register")) {
            String[] registerInfo = registerPanel.getRegInfo();
            registerPanel.clear();

            String username = registerInfo[0];
            String email = registerInfo[1];
            String password = registerInfo[2];

            if (username.isBlank()) {
                JOptionPane.showMessageDialog(this, "Username can not be empty or only whitespace", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (email.isBlank()) {
                JOptionPane.showMessageDialog(this, "E-mail can not be empty or only whitespace", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Password can not be empty", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            } else if (userMap.containsKey(username)) {
                JOptionPane.showMessageDialog(this, "A user with the same username already exists", "User already exists", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!validate(email, "verification"))
                return;

            JOptionPane.showMessageDialog(this, "Email verification successful !");

            String hashedPassword;
            do {
                hashedPassword = Encryption.getHashedString(password);
            } while (hashedPassword == null);

            User newUser = new User(username, email, hashedPassword);
            passwordMap.put(username, hashedPassword);
            userMap.put(username, newUser);
            saveUsers();
            JOptionPane.showMessageDialog(this, "User registration done !");
        }
    }
}
