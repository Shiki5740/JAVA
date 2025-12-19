import javax.swing.*;
import java.awt.*;
import java.util.List;

public class RankingWindow extends JFrame {
    private RecordData recordData;
    
    public RankingWindow() {
        setTitle("扫雷游戏 - 排行榜");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
        
        // 初始化记录数据
        recordData = new RecordData();
        
        // 创建主面板
        JPanel mainPanel = new JPanel(new GridLayout(3, 1, 0, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // 添加各难度的排行榜
        mainPanel.add(createDifficultyRanking(RecordData.BEGINNER, "初级 (9x9, 10颗地雷)"));
        mainPanel.add(createDifficultyRanking(RecordData.INTERMEDIATE, "中级 (16x16, 40颗地雷)"));
        mainPanel.add(createDifficultyRanking(RecordData.EXPERT, "高级 (16x30, 99颗地雷)"));
        
        add(mainPanel, BorderLayout.CENTER);
        
        pack();
        setLocationRelativeTo(null);
    }
    
    /**
     * 创建单个难度级别的排行榜面板
     * @param difficulty 难度级别
     * @param difficultyName 难度名称
     * @return 排行榜面板
     */
    private JPanel createDifficultyRanking(String difficulty, String difficultyName) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder(difficultyName));
        
        // 获取该难度的记录列表
        List<RecordData.Record> records = recordData.getRecords(difficulty);
        
        // 创建表格模型和表格
        String[] columnNames = {"排名", "玩家", "时间 (秒)"};
        Object[][] data = new Object[records.size()][3];
        
        for (int i = 0; i < records.size(); i++) {
            RecordData.Record record = records.get(i);
            data[i][0] = i + 1; // 排名从1开始
            data[i][1] = record.getUsername();
            data[i][2] = record.getTime();
        }
        
        JTable table = new JTable(data, columnNames);
        table.setEnabled(false); // 表格内容不可编辑
        table.setRowHeight(25);
        table.setFont(new Font("宋体", Font.PLAIN, 14));
        
        // 添加表格到滚动面板
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(500, 200));
        
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // 如果没有记录，显示提示信息
        if (records.isEmpty()) {
            JLabel noRecordLabel = new JLabel("暂无记录");
            noRecordLabel.setFont(new Font("宋体", Font.PLAIN, 14));
            noRecordLabel.setHorizontalAlignment(SwingConstants.CENTER);
            noRecordLabel.setVerticalAlignment(SwingConstants.CENTER);
            panel.add(noRecordLabel, BorderLayout.CENTER);
        }
        
        return panel;
    }
}
