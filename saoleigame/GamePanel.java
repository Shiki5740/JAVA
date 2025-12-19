import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GamePanel extends JPanel {
    private int rows;
    private int cols;
    private int mines;
    private int minesLeft;
    private int revealedCells;
    private boolean gameOver;
    private boolean gameWon;
    private boolean firstClick;
    private int timeElapsed;
    private Timer timer;
    
    private Cell[][] cells;
    private Minesweeper mainFrame;
    
    public GamePanel(int rows, int cols, int mines, Minesweeper mainFrame) {
        this.rows = rows;
        this.cols = cols;
        this.mines = mines;
        this.minesLeft = mines;
        this.mainFrame = mainFrame;
        this.gameOver = false;
        this.gameWon = false;
        this.firstClick = true;
        this.timeElapsed = 0;
        this.revealedCells = 0;
        
        setLayout(new GridLayout(rows, cols));
        setPreferredSize(new Dimension(cols * 30, rows * 30));
        
        // 初始化格子
        cells = new Cell[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cells[i][j] = new Cell(i, j);
                add(cells[i][j]);
            }
        }
        
        // 为每个格子添加鼠标事件监听
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int finalI = i;
                int finalJ = j;
                cells[i][j].addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        if (gameOver || gameWon) return;
                        
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            if (firstClick) {
                                placeMines(finalI, finalJ);
                                calculateAdjacentMines();
                                startTimer();
                                firstClick = false;
                            }
                            revealCell(finalI, finalJ);
                        } else if (SwingUtilities.isRightMouseButton(e)) {
                            flagCell(finalI, finalJ);
                        }
                        
                        checkWinCondition();
                    }
                });
            }
        }
        
        mainFrame.updateMineCounter(minesLeft);
    }
    
    private void placeMines(int safeRow, int safeCol) {
        Random random = new Random();
        int minesPlaced = 0;
        
        while (minesPlaced < mines) {
            int row = random.nextInt(rows);
            int col = random.nextInt(cols);
            
            // 确保不在第一次点击的位置及其周围放置地雷
            if (!cells[row][col].isMine() && 
                !(Math.abs(row - safeRow) <= 1 && Math.abs(col - safeCol) <= 1)) {
                cells[row][col].setMine(true);
                minesPlaced++;
            }
        }
    }
    
    private void calculateAdjacentMines() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (cells[i][j].isMine()) continue;
                
                int count = 0;
                for (int di = -1; di <= 1; di++) {
                    for (int dj = -1; dj <= 1; dj++) {
                        if (di == 0 && dj == 0) continue;
                        
                        int ni = i + di;
                        int nj = j + dj;
                        
                        if (ni >= 0 && ni < rows && nj >= 0 && nj < cols) {
                            if (cells[ni][nj].isMine()) {
                                count++;
                            }
                        }
                    }
                }
                cells[i][j].setAdjacentMines(count);
            }
        }
    }
    
    private void revealCell(int row, int col) {
        if (row < 0 || row >= rows || col < 0 || col >= cols) return;
        
        Cell cell = cells[row][col];
        if (cell.isRevealed() || cell.isFlagged() || gameOver) return;
        
        cell.reveal();
        revealedCells++;
        
        if (cell.isMine()) {
            gameOver = true;
            revealAllMines();
            JOptionPane.showMessageDialog(this, "游戏结束！你踩到了地雷！", "游戏结束", JOptionPane.ERROR_MESSAGE);
            stopTimer();
            return;
        }
        
        // 如果是空白格子（周围没有地雷），递归揭示周围的格子
        if (cell.getAdjacentMines() == 0) {
            for (int di = -1; di <= 1; di++) {
                for (int dj = -1; dj <= 1; dj++) {
                    if (di == 0 && dj == 0) continue;
                    revealCell(row + di, col + dj);
                }
            }
        }
    }
    
    private void flagCell(int row, int col) {
        Cell cell = cells[row][col];
        if (cell.isRevealed()) return;
        
        if (cell.isFlagged()) {
            cell.unflag();
            minesLeft++;
        } else {
            cell.flag();
            minesLeft--;
        }
        mainFrame.updateMineCounter(minesLeft);
    }
    
    private void revealAllMines() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (cells[i][j].isMine()) {
                    cells[i][j].reveal();
                }
            }
        }
    }
    
    private void checkWinCondition() {
        if (revealedCells == rows * cols - mines) {
            gameWon = true;
            stopTimer();
            
            // 获取当前游戏的难度
            String difficulty = mainFrame.getCurrentDifficulty();
            if (difficulty != null) {
                // 检查是否打破了记录
                if (mainFrame.getRecordData().isNewRecord(difficulty, timeElapsed)) {
                    // 更新记录
                    mainFrame.getRecordData().addRecord(difficulty, mainFrame.getUsername(), timeElapsed);
                    // 刷新记录面板
                    mainFrame.updateRecordPanel();
                    JOptionPane.showMessageDialog(this, "恭喜你！你创造了新的最快记录：" + timeElapsed + "秒！", "新记录！", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "恭喜你！成功扫完所有地雷！用时：" + timeElapsed + "秒", "游戏胜利", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "恭喜你！成功扫完所有地雷！用时：" + timeElapsed + "秒", "游戏胜利", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
    
    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timeElapsed++;
                // 确保在事件调度线程（EDT）中更新UI
                SwingUtilities.invokeLater(() -> {
                    mainFrame.updateTimer(timeElapsed);
                });
            }
        }, 1000, 1000);
    }
    
    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }
    
    public void resetGame() {
        stopTimer();
        gameOver = false;
        gameWon = false;
        firstClick = true;
        timeElapsed = 0;
        minesLeft = mines;
        revealedCells = 0;
        
        mainFrame.updateTimer(0);
        mainFrame.updateMineCounter(minesLeft);
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cells[i][j].reset();
            }
        }
    }
}