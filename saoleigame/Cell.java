import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Cell extends JButton {
    private int row;
    private int col;
    private boolean isMine;
    private boolean isRevealed;
    private boolean isFlagged;
    private int adjacentMines;
    private Icon flagIcon; // 标记图标
    
    // 构造函数 - 带Minesweeper参数
    public Cell(int row, int col, Minesweeper minesweeper) {
        this.row = row;
        this.col = col;
        this.isMine = false;
        this.isRevealed = false;
        this.isFlagged = false;
        this.adjacentMines = 0;
        
        // 创建自定义绘制的美化标记图标
        createBeautifulFlagIcon();
        
        setFont(new Font("宋体", Font.BOLD, 16));
        setFocusPainted(false);
        setMargin(new Insets(0, 0, 0, 0));
        setPreferredSize(new Dimension(30, 30));
        updateDisplay();
    }
    
    // 构造函数 - 无参数
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.isMine = false;
        this.isRevealed = false;
        this.isFlagged = false;
        this.adjacentMines = 0;
        
        // 创建自定义绘制的美化标记图标
        createBeautifulFlagIcon();
        
        setFont(new Font("宋体", Font.BOLD, 16));
        setFocusPainted(false);
        setMargin(new Insets(0, 0, 0, 0));
        setPreferredSize(new Dimension(30, 30));
        updateDisplay();
    }
    
    // 创建三角旗标记图标
    private void createBeautifulFlagIcon() {
        flagIcon = new Icon() {
            @Override
            public void paintIcon(Component c, Graphics g, int x, int y) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // 绘制旗杆
                g2d.setColor(new Color(100, 80, 60));
                g2d.fillRect(x + 4, y + 2, 3, 22);
                
                // 绘制三角旗（直角三角形）
                Polygon flag = new Polygon();
                flag.addPoint(x + 7, y + 3);      // 旗杆顶端连接点
                flag.addPoint(x + 22, y + 11);     // 三角形右端点
                flag.addPoint(x + 7, y + 19);       // 旗杆底端连接点
                
                // 渐变填充三角旗
                GradientPaint gradient = new GradientPaint(x + 7, y + 3, new Color(255, 0, 0), x + 22, y + 11, new Color(180, 0, 0));
                g2d.setPaint(gradient);
                g2d.fill(flag);
                
                // 绘制三角旗边缘
                g2d.setColor(new Color(200, 20, 20));
                g2d.setStroke(new BasicStroke(0.8f));
                g2d.draw(flag);
                
                // 绘制高光
                g2d.setColor(new Color(255, 255, 255, 120));
                Polygon highlight = new Polygon();
                highlight.addPoint(x + 7, y + 3);
                highlight.addPoint(x + 17, y + 9);
                highlight.addPoint(x + 7, y + 13);
                g2d.fill(highlight);
            }
            
            @Override
            public int getIconWidth() {
                return 25; // 图标宽度
            }
            
            @Override
            public int getIconHeight() {
                return 25; // 图标高度
            }
        };
        System.out.println("三角旗标记图标创建成功");
        System.out.println("图标大小: " + flagIcon.getIconWidth() + "x" + flagIcon.getIconHeight());
    }
    
    private Icon loadFlagIcon() {
        // 尝试多种方式加载图片
        String[] paths = { "a.png", "./a.png", "d:/Updata by edge/saoleigame/a.png" };
        
        for (String path : paths) {
            try {
                File imageFile = new File(path);
                System.out.println("尝试加载图片: " + path);
                System.out.println("文件存在: " + imageFile.exists());
                System.out.println("文件绝对路径: " + imageFile.getAbsolutePath());
                System.out.println("文件可读: " + imageFile.canRead());
                System.out.println("文件大小: " + imageFile.length() + " bytes");
                
                if (imageFile.exists() && imageFile.canRead()) {
                    // 使用ImageIO加载图片以确保能正确读取
                    javax.imageio.ImageIO.read(imageFile);
                    System.out.println("ImageIO读取图片成功");
                    
                    Icon icon = new ImageIcon(javax.imageio.ImageIO.read(imageFile));
                    System.out.println("创建ImageIcon成功");
                    System.out.println("图标大小: " + icon.getIconWidth() + "x" + icon.getIconHeight());
                    
                    if (icon.getIconWidth() > 0 && icon.getIconHeight() > 0) {
                        System.out.println("图片加载成功: " + path);
                        return icon;
                    } else {
                        System.out.println("图片尺寸无效");
                    }
                }
            } catch (Exception e) {
                System.out.println("加载图片失败 (" + path + "): " + e.getMessage());
                e.printStackTrace();
            }
        }
        
        System.out.println("所有图片加载方式都失败了");
        return null;
    }
    
    public int getRow() {
        return row;
    }
    
    public int getCol() {
        return col;
    }
    
    public boolean isMine() {
        return isMine;
    }
    
    public void setMine(boolean isMine) {
        this.isMine = isMine;
    }
    
    public boolean isRevealed() {
        return isRevealed;
    }
    
    public boolean isFlagged() {
        return isFlagged;
    }
    
    public int getAdjacentMines() {
        return adjacentMines;
    }
    
    public void setAdjacentMines(int count) {
        this.adjacentMines = count;
    }
    
    public void reveal() {
        isRevealed = true;
        isFlagged = false;
        updateDisplay();
    }
    
    public void flag() {
        if (!isRevealed) {
            isFlagged = true;
            updateDisplay();
        }
    }
    
    public void unflag() {
        isFlagged = false;
        updateDisplay();
    }
    
    public void reset() {
        isMine = false;
        isRevealed = false;
        isFlagged = false;
        adjacentMines = 0;
        updateDisplay();
    }
    
    private void updateDisplay() {
        if (isRevealed) {
            setIcon(null); // 清除图标
            if (isMine) {
                setBackground(Color.RED);
                setText("");
            } else {
                setBackground(Color.WHITE); // 已揭示的普通格子使用白色背景
                if (adjacentMines > 0) {
                    setText(String.valueOf(adjacentMines));
                    setForeground(getNumberColor(adjacentMines));
                } else {
                    setText("");
                }
            }
        } else if (isFlagged) {
            if (flagIcon != null) {
                System.out.println("设置标记图标");
                System.out.println("按钮大小: " + getWidth() + "x" + getHeight());
                System.out.println("图标大小: " + flagIcon.getIconWidth() + "x" + flagIcon.getIconHeight());
                
                // 设置图标
                setIcon(flagIcon);
                
                // 清除文字和背景色
                setText("");
                setBackground(null);
                
                System.out.println("图标设置后 - 图标: " + getIcon() + ", 文字: " + getText() + ", 背景: " + getBackground());
            } else {
                System.out.println("标记图标为null，使用默认样式");
                // 如果图标加载失败，使用原来的样式
                setText("💣");
                setBackground(new Color(255, 200, 200));
            }
        } else {
            setIcon(null); // 清除图标
            setText("");
            setBackground(new Color(200, 200, 200)); // 未揭示的格子使用灰色背景
        }
    }
    
    private Color getNumberColor(int number) {
        switch (number) {
            case 1: return Color.BLUE;
            case 2: return Color.GREEN;
            case 3: return Color.RED;
            case 4: return Color.MAGENTA;
            case 5: return Color.ORANGE;
            case 6: return Color.CYAN;
            case 7: return Color.BLACK;
            case 8: return Color.GRAY;
            default: return Color.BLACK;
        }
    }
}