package MineSweepPack;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

public class MainFrame extends JFrame
{
    public static JPanel contentPane;
    public static JTextField rowTxtFld;
    public static JTextField colTxtFld;
    public static JTextField mineTxtFld;
    public static JPanel controlPanel;
    public static JPanel gamePanel;
    public static Clock timePanel;
    public static JButton startBtn;
    public static Vector<Vector<Grid>> Grids;
    public static int rowAmount;
    public static int colAmount;
    public static boolean isFirst = true;
    public static boolean isEnd = false;
    

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    MainFrame frame = new MainFrame();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public MainFrame() {
        setFont(new Font("Dialog", Font.PLAIN, 28));
        setTitle("扫雷大作战");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1220, 715);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(new BorderLayout(0, 0));
        setContentPane(contentPane);
        setLocationRelativeTo(null);
        
        timePanel = new Clock();
        contentPane.add(timePanel, BorderLayout.WEST);
        timePanel.setVisible(true);
      //  new Thread(timePanel.cp).run(); 
        
        controlPanel = new JPanel();
        contentPane.add(controlPanel, BorderLayout.NORTH);
        controlPanel.setLayout(new GridLayout(0, 7, 0, 0));
        
        startBtn = new JButton("重新开始");
        
        startBtn.setFont(new Font("宋体", Font.PLAIN, 28));
        controlPanel.add(startBtn);
        
        startBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                try {
                    int rowNum = Integer.parseInt(rowTxtFld.getText());
                    int colNum = Integer.parseInt(colTxtFld.getText());
                    int mineNum = Integer.parseInt(mineTxtFld.getText());
                    if (!checkNum(rowNum, colNum, mineNum)) {
                        JOptionPane.showMessageDialog
                        (null,"请确认行列数在5-50之间,雷的数目不超过格子总数",  "提示", JOptionPane.ERROR_MESSAGE); 
                        return;
                    }
                    isEnd = false;
                    colAmount = colNum;
                    rowAmount = rowNum;
                    addGameLables(rowNum, colNum, mineNum);
                    gamePanel.setVisible(false);
                    gamePanel.setVisible(true);
                    return;
                }
                catch (Exception e){
                    JOptionPane.showMessageDialog
                    (null,"请检查您的行列数和雷数格式是否正确",  "提示", JOptionPane.ERROR_MESSAGE); 
                }
                
            }
        });
        
        JLabel label = new JLabel("行数  ");
        label.setHorizontalAlignment(SwingConstants.RIGHT);
        label.setFont(new Font("宋体", Font.PLAIN, 28));
        controlPanel.add(label);
        
        rowTxtFld = new JTextField();
        rowTxtFld.setText("15");
        rowTxtFld.setFont(new Font("宋体", Font.PLAIN, 28));
        controlPanel.add(rowTxtFld);
        rowTxtFld.setColumns(10);
        
        JLabel label_1 = new JLabel("列数 ");
        label_1.setHorizontalAlignment(SwingConstants.RIGHT);
        label_1.setFont(new Font("宋体", Font.PLAIN, 28));
        controlPanel.add(label_1);
        
        colTxtFld = new JTextField();
        colTxtFld.setText("25");
        colTxtFld.setFont(new Font("宋体", Font.PLAIN, 28));
        colTxtFld.setColumns(10);
        controlPanel.add(colTxtFld);
        
        JLabel label_2 = new JLabel("雷数  ");
        label_2.setHorizontalAlignment(SwingConstants.RIGHT);
        label_2.setFont(new Font("宋体", Font.PLAIN, 28));
        controlPanel.add(label_2);
        
        mineTxtFld = new JTextField();
        mineTxtFld.setText("30");
        mineTxtFld.setFont(new Font("宋体", Font.PLAIN, 28));
        mineTxtFld.setColumns(10);
        controlPanel.add(mineTxtFld);
        
        gamePanel = new JPanel();
        contentPane.add(gamePanel, BorderLayout.CENTER);
        gamePanel.setVisible(true);
        gamePanel.setBackground(Color.LIGHT_GRAY);
        gamePanel.setLayout(new GridLayout(1, 0, 0, 0));
        
       
    }
    
    boolean checkNum(int rowNum,int colNum,int mineNum) {
        if (rowNum<5||rowNum>50)
            return false;
        if (colNum<5||colNum>50)
            return false;
        int allNum = colNum*rowNum;
        if (mineNum>=allNum)
            return false;
        return true;
    }
    
    void addGameLables(int rowNum,int colNum,int mineNum) {
        gamePanel.removeAll();
        gamePanel.setLayout(new GridLayout(rowNum,  colNum, 1, 1));
        Grids = new Vector<Vector<Grid>>();
        for (int i=0;i<rowNum;++i) {
            Grids.add(new Vector<Grid>());
            for (int j=0;j<colNum;++j) {
                Grid newGrid = new Grid(i, j);
                Grids.get(i).addElement(newGrid);
                newGrid.init();
                gamePanel.add(newGrid);
                newGrid.setVisible(true);
            }
        }
        int mineCount = 0;
        while (mineCount<mineNum)
        {
            int rdmRow = (int)(Math.random()*rowNum);
            int rdmCol = (int)(Math.random()*colNum);
            Grid nowGrid = Grids.get(rdmRow).get(rdmCol);
            if (!nowGrid.isMine) {
                nowGrid.isMine=true;
                mineCount+=1;
            }
        }
        for (int i=0;i<rowNum;++i) {
            for (int j=0;j<colNum;++j) {
                Grid newGrid=Grids.elementAt(i).elementAt(j);
                newGrid.setStyle();
                newGrid.addMouseListener(new GridClick());
                newGrid.close();
            }
        }
    }
    
    static void gameLoseOver()
    {
        timePanel.clockEnd();
        isFirst=true;
        isEnd=true;
        for (int i=0;i<rowAmount;++i) {
            for (int j=0;j<colAmount;++j) {
                Grid newGrid=Grids.elementAt(i).elementAt(j);
                if (!newGrid.isOpen) {
                    newGrid.open();
                }
            }
        }
        JOptionPane.showMessageDialog
        (null,"对不起您输了",  "结局", JOptionPane.ERROR_MESSAGE); 
    }
    
    static void gameWinOver()
    {
        timePanel.clockEnd();
        isFirst=true;
        isEnd=true;
        for (int i=0;i<rowAmount;++i) {
            for (int j=0;j<colAmount;++j) {
                Grid newGrid=Grids.elementAt(i).elementAt(j);
                if (!newGrid.isOpen) {
                    newGrid.open();
                }
            }
        }
        JOptionPane.showMessageDialog
        (null,"恭喜您赢得胜利",  "结局", JOptionPane.ERROR_MESSAGE); 
    }
    
    static boolean checkWin()
    {
        boolean isWin = true;
        outer: 
            for (int i=0;i<rowAmount;++i) {
                for (int j=0;j<colAmount;++j) {
                Grid newGrid=Grids.elementAt(i).elementAt(j);
                if (newGrid.isMine&&!newGrid.isOpen)
                    continue;
                if (!newGrid.isMine&&newGrid.isOpen)
                    continue;
                isWin=false;
                break outer;
            }
        }
        return isWin;
    }
    
    
    static void boundOpen()
    {
        int [][] flag= new int[rowAmount][colAmount];
        //System.out.println("23423423");
        for (int i=0;i<rowAmount;++i) {
            for (int j=0;j<colAmount;++j) {
                flag[i][j]=0;
            }
        }
      int[] dirX = { 0, 0, 1, -1 };
      int[] dirY = { 1, -1, 0, 0 };
      for (int i=0;i<rowAmount;++i) {
          for (int j=0;j<colAmount;++j) {
              Grid newGrid=Grids.elementAt(i).elementAt(j);
              if (newGrid.isMine)
                  continue;
              if (newGrid.isOpen)
                  continue;
              for (int k=0;k<4;++k) {
                  int tryRow = newGrid.row + dirX[k];
                  int tryCol = newGrid.col + dirY[k];
                  //System.out.println(tryRow+"  "+tryCol);
                  if (tryRow < 0 || tryRow >= MainFrame.rowAmount)
                      continue;
                  if (tryCol < 0 || tryCol >= MainFrame.colAmount)
                      continue;
                  Grid tryGrid = MainFrame.Grids.elementAt(tryRow).elementAt(tryCol);
                  if (!tryGrid.isMine&&tryGrid.isOpen&&tryGrid.content.equals("")) {
                      flag[i][j]=1;
                      break;
                  }  
              }
          }
      }
      for (int i=0;i<rowAmount;++i) {
          for (int j=0;j<colAmount;++j) {
              if (flag[i][j]==1) {
                  MainFrame.Grids.elementAt(i).elementAt(j).open();
              }
          }
      }
    }
}
