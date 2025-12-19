import javax.swing.*;
import java.awt.*;

public class Minesweeper extends JFrame {
    private int rows;
    private int cols;
    private int mines;
    private String username;
    
    private GamePanel gamePanel;
    private JPanel topPanel;
    private JPanel recordPanel;
    private JLabel mineCounter;
    private JLabel timer;
    private JButton restartButton;
    private RecordData recordData;
    
    public Minesweeper(int rows, int cols, int mines, String username) {
        this.rows = rows;
        this.cols = cols;
        this.mines = mines;
        this.username = username;
        
        // 初始化记录数据
        recordData = new RecordData();
        
        setTitle("扫雷游戏 - " + username);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
        
        // 创建顶部面板（计时器、地雷计数器、重新开始按钮）
        topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 40, 10));
        
        // 地雷计数器（重新开始按钮左侧）
        mineCounter = new JLabel("地雷: " + mines);
        mineCounter.setFont(new Font("宋体", Font.BOLD, 16));
        mineCounter.setPreferredSize(new Dimension(100, 30));
        mineCounter.setHorizontalAlignment(SwingConstants.RIGHT);
        
        // 重新开始按钮
        Font buttonFont = new Font("宋体", Font.PLAIN, 14);
        restartButton = new JButton("重新开始");
        restartButton.setFont(buttonFont);
        restartButton.addActionListener(e -> restartGame());
        restartButton.setPreferredSize(new Dimension(120, 30));
        
        // 计时器（重新开始按钮右侧）
        timer = new JLabel("时间: 0");
        timer.setFont(new Font("宋体", Font.BOLD, 16));
        timer.setPreferredSize(new Dimension(100, 30));
        timer.setHorizontalAlignment(SwingConstants.LEFT);
        
        // 按顺序添加组件：地雷计数器 -> 重新开始按钮 -> 计时器
        topPanel.add(mineCounter);
        topPanel.add(restartButton);
        topPanel.add(timer);
        
        // 创建记录面板
        recordPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        recordPanel.setBorder(BorderFactory.createTitledBorder("最快记录"));
        
        // 添加各难度级别的记录显示
        updateRecordPanel();
        
        // 创建游戏面板
        gamePanel = new GamePanel(rows, cols, mines, this);
        
        add(topPanel, BorderLayout.NORTH);
        add(recordPanel, BorderLayout.CENTER);
        add(gamePanel, BorderLayout.SOUTH);
        
        pack();
        setLocationRelativeTo(null);
    }
    
    // 无参数构造函数，用于向后兼容
    public Minesweeper() {
        this(16, 30, 99, "玩家");
    }
    
    public void updateMineCounter(int minesLeft) {
        mineCounter.setText("地雷: " + minesLeft);
    }
    
    public void updateTimer(int seconds) {
        timer.setText("时间: " + seconds);
    }
    
    public void restartGame() {
        // 关闭当前游戏窗口
        this.dispose();
        
        // 重新显示难度选择窗口，允许用户重新选择难度
        SwingUtilities.invokeLater(() -> {
            DifficultySelectWindow difficultyWindow = new DifficultySelectWindow(username);
            difficultyWindow.setVisible(true);
        });
    }
    
    public String getUsername() {
        return username;
    }
    
    public RecordData getRecordData() {
        return recordData;
    }
    
    public String getCurrentDifficulty() {
        if (rows == 9 && cols == 9 && mines == 10) {
            return RecordData.BEGINNER;
        } else if (rows == 16 && cols == 16 && mines == 40) {
            return RecordData.INTERMEDIATE;
        } else if (rows == 16 && cols == 30 && mines == 99) {
            return RecordData.EXPERT;
        }
        return null;
    }
    
    public void updateRecordPanel() {
        // 清空记录面板
        recordPanel.removeAll();
        
        // 添加各难度级别的记录显示
        Font recordFont = new Font("宋体", Font.PLAIN, 14);
        JLabel beginnerRecord = new JLabel(recordData.getRecordString(RecordData.BEGINNER, "初级"));
        beginnerRecord.setFont(recordFont);
        JLabel intermediateRecord = new JLabel(recordData.getRecordString(RecordData.INTERMEDIATE, "中级"));
        intermediateRecord.setFont(recordFont);
        JLabel expertRecord = new JLabel(recordData.getRecordString(RecordData.EXPERT, "高级"));
        expertRecord.setFont(recordFont);
        
        recordPanel.add(beginnerRecord);
        recordPanel.add(intermediateRecord);
        recordPanel.add(expertRecord);
        
        // 重新绘制记录面板
        recordPanel.revalidate();
        recordPanel.repaint();
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            DifficultySelectWindow difficultyWindow = new DifficultySelectWindow("玩家");
            difficultyWindow.setVisible(true);
        });
    }
}