import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginWindow extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerButton;
    private JButton exitButton;
    private UserData userData;
    
    public LoginWindow() {
        setTitle("扫雷游戏 - 登录");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        setResizable(false);
        
        // 初始化用户数据存储
        userData = new UserData();
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // 标题标签
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel titleLabel = new JLabel("扫雷游戏登录");
        titleLabel.setFont(new Font("宋体", Font.BOLD, 18));
        add(titleLabel, gbc);
        
        // 用户名标签和输入框
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("用户名:"), gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        usernameField = new JTextField(20);
        add(usernameField, gbc);
        
        // 密码标签和输入框
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("密码:"), gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        passwordField = new JPasswordField(20);
        add(passwordField, gbc);
        
        // 按钮面板
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        
        loginButton = new JButton("登录");
        registerButton = new JButton("注册");
        exitButton = new JButton("退出");
        
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                register();
            }
        });
        
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        buttonPanel.add(exitButton);
        
        add(buttonPanel, gbc);
        
        pack();
        setLocationRelativeTo(null);
    }
    
    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入用户名和密码！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 验证登录信息
        if (!userData.loginUser(username, password)) {
            JOptionPane.showMessageDialog(this, "用户名或密码错误！", "错误", JOptionPane.ERROR_MESSAGE);
            passwordField.setText(""); // 清空密码框
            return;
        }
        
        // 隐藏登录窗口
        this.dispose();
        
        // 创建并显示难度选择窗口
        SwingUtilities.invokeLater(() -> {
            DifficultySelectWindow difficultyWindow = new DifficultySelectWindow(username);
            difficultyWindow.setVisible(true);
        });
    }
    
    private void register() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "请输入用户名和密码！", "错误", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // 注册新用户
        if (userData.registerUser(username, password)) {
            JOptionPane.showMessageDialog(this, "注册成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "用户名已存在！", "错误", JOptionPane.ERROR_MESSAGE);
        }
        
        // 清空密码框
        passwordField.setText("");
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginWindow loginWindow = new LoginWindow();
            loginWindow.setVisible(true);
        });
    }
}