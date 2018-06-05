package MineSweepPack;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class Grid extends JLabel
{
    boolean isMine;
    boolean isOpen;
    int row;
    int col;
    String content;

    public Grid(int row, int col) {
        // TODO 自动生成的构造函数存根
        this.col = col;
        this.row = row;
        this.isMine = false;
    }

    void init() {
        // 绘制方格边框
        setBorder(BorderFactory.createLineBorder(Color.GRAY));
        // 设置方格为透明,便于我们填充颜色
        setOpaque(true);
        // 背景填充为黄色
        setBackground(Color.WHITE);
        // 将方格加入到容器中(即面板JPanel)
    }

    void setStyle() {
        setHorizontalAlignment(SwingConstants.CENTER);
        if (isMine) {
            setFont(new Font("宋体", 30, 30));
            setBackground(Color.DARK_GRAY);
            setForeground(Color.RED);
            content = "*";
            setText(content);
        } else {
            setFont(new Font("宋体", 30, 30));
            setBackground(Color.LIGHT_GRAY);
            setForeground(Color.black);
            int[] dirX = { 0, 0, 1, -1, 1, 1, -1, -1 };
            int[] dirY = { 1, -1, 0, 0, 1, -1, 1, -1 };
            int posRow = row;
            int posCol = col;
            int count = 0;
            for (int i = 0; i < 8; ++i) {
                int tryRow = posRow + dirX[i];
                int tryCol = posCol + dirY[i];
                if (tryRow < 0 || tryRow >= MainFrame.rowAmount)
                    continue;
                if (tryCol < 0 || tryCol >= MainFrame.colAmount)
                    continue;
                if (MainFrame.Grids.elementAt(tryRow).elementAt(tryCol).isMine)
                    count += 1;
            }
            content = new Integer(count).toString();
            if (content.equals("0"))
                content = "";
            setText(content);
        }
    }

    void close() {
        isOpen = false;
        setBackground(Color.white);
        setText("");
    }

    void open() {
        isOpen = true;
        if (isMine) {
            setBackground(Color.DARK_GRAY);
            setForeground(Color.RED);
            setText(content);
        } else {
            setBackground(Color.LIGHT_GRAY);
            setForeground(Color.black);
            setText(content);
        }
    }

    void expand(int posRow, int posCol) {
        open();
        // System.out.println(posRow + " " + posCol);
        int[] dirX = { 0, 0, 1, -1 };
        int[] dirY = { 1, -1, 0, 0 };
        for (int i = 0; i < 4; ++i) {
            int tryRow = posRow + dirX[i];
            int tryCol = posCol + dirY[i];
            if (tryRow < 0 || tryRow >= MainFrame.rowAmount)
                continue;
            if (tryCol < 0 || tryCol >= MainFrame.colAmount)
                continue;
            Grid tryGrid = MainFrame.Grids.elementAt(tryRow).elementAt(tryCol);
            if (!tryGrid.isMine && tryGrid.content.equals("") && !tryGrid.isOpen)
                tryGrid.expand(tryRow, tryCol);
        }
    }
}

class GridClick extends MouseAdapter
{
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == e.BUTTON3) {
            Grid newGrid = (Grid) e.getSource();
            if (newGrid.isOpen)
                return;
            if (newGrid.getText().equals("")){
                newGrid.setForeground(Color.BLACK);
                newGrid.setText("X");
            }
            else if (newGrid.getText().equals("X")) {
                newGrid.setForeground(Color.BLACK);
                newGrid.setText("");
            }
            return;
        } else if (e.getButton() == e.BUTTON1) {
            if (MainFrame.isEnd)
                return;
            if (MainFrame.isFirst) {
                MainFrame.isFirst = false;
                MainFrame.timePanel.clockStart();
            }
            Grid newGrid = (Grid) e.getSource();
            if (newGrid.isMine) {
                MainFrame.gameLoseOver();
                return;
            }
            if (newGrid.content.equals("")) {
                newGrid.expand(newGrid.row, newGrid.col);
                MainFrame.boundOpen();
            } else {
                newGrid.open();
            }
            if (MainFrame.checkWin()) {
                MainFrame.gameWinOver();
            }
        }
    };
}
