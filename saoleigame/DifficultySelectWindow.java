import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DifficultySelectWindow extends JFrame {
    // 定义不同难度级别的参数
    public static final int BEGINNER_ROWS = 9;
    public static final int BEGINNER_COLS = 9;
    public static final int BEGINNER_MINES = 10;
    
    public static final int INTERMEDIATE_ROWS = 16;
    public static final int INTERMEDIATE_COLS = 16;
    public static final int INTERMEDIATE_MINES = 40;
    
    public static final int EXPERT_ROWS = 16;
    public static final int EXPERT_COLS = 30;
    public static final int EXPERT_MINES = 99;
    
    private ButtonGroup difficultyGroup;
    private JRadioButton beginnerRadio;
    private JRadioButton intermediateRadio;
    private JRadioButton expertRadio;
    private JButton startButton;
    private JButton exitButton;
    private String username;
    
    public DifficultySelectWindow(String username) {
        this.username = username;
        
        setTitle("扫雷游戏 - 难度选择");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        setResizable(false);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // 欢迎标签
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JLabel welcomeLabel = new JLabel("欢迎，" + username + "！请选择难度：");
        welcomeLabel.setFont(new Font("宋体", Font.BOLD, 16));
        add(welcomeLabel, gbc);
        
        // 难度单选按钮组
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        JPanel difficultyPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        difficultyGroup = new ButtonGroup();
        
        beginnerRadio = new JRadioButton("初级 (9x9, 10颗地雷)");
        intermediateRadio = new JRadioButton("中级 (16x16, 40颗地雷)");
        expertRadio = new JRadioButton("高级 (16x30, 99颗地雷)");
        
        Font radioFont = new Font("宋体", Font.PLAIN, 12);
        beginnerRadio.setFont(radioFont);
        intermediateRadio.setFont(radioFont);
        expertRadio.setFont(radioFont);
        
        beginnerRadio.setSelected(true); // 默认选择初级难度
        
        difficultyGroup.add(beginnerRadio);
        difficultyGroup.add(intermediateRadio);
        difficultyGroup.add(expertRadio);
        
        difficultyPanel.add(beginnerRadio);
        difficultyPanel.add(intermediateRadio);
        difficultyPanel.add(expertRadio);
        
        add(difficultyPanel, gbc);
        
        // 按钮面板
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        
        Font buttonFont = new Font("宋体", Font.PLAIN, 14);
        startButton = new JButton("开始游戏");
        startButton.setFont(buttonFont);
        JButton rankingButton = new JButton("排行榜");
        rankingButton.setFont(buttonFont);
        exitButton = new JButton("退出游戏");
        exitButton.setFont(buttonFont);
        
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });
        
        rankingButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRanking();
            }
        });
        
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitGame();
            }
        });
        
        buttonPanel.add(startButton);
        buttonPanel.add(rankingButton);
        buttonPanel.add(exitButton);
        
        add(buttonPanel, gbc);
        
        pack();
        setLocationRelativeTo(null);
    }
    
    private void startGame() {
        // 根据选择的难度设置游戏参数
        final int rows;
        final int cols;
        final int mines;
        
        if (beginnerRadio.isSelected()) {
            rows = BEGINNER_ROWS;
            cols = BEGINNER_COLS;
            mines = BEGINNER_MINES;
        } else if (intermediateRadio.isSelected()) {
            rows = INTERMEDIATE_ROWS;
            cols = INTERMEDIATE_COLS;
            mines = INTERMEDIATE_MINES;
        } else if (expertRadio.isSelected()) {
            rows = EXPERT_ROWS;
            cols = EXPERT_COLS;
            mines = EXPERT_MINES;
        } else {
            // 默认选择初级难度
            rows = BEGINNER_ROWS;
            cols = BEGINNER_COLS;
            mines = BEGINNER_MINES;
        }
        
        // 隐藏难度选择窗口
        this.dispose();
        
        // 创建并显示主游戏窗口
        SwingUtilities.invokeLater(() -> {
            Minesweeper minesweeper = new Minesweeper(rows, cols, mines, username);
            minesweeper.setVisible(true);
        });
    }
    
    private void exitGame() {
        System.exit(0);
    }
    
    private void showRanking() {
        SwingUtilities.invokeLater(() -> {
            RankingWindow rankingWindow = new RankingWindow();
            rankingWindow.setVisible(true);
        });
    }
}