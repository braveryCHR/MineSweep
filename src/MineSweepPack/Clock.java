package MineSweepPack;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.HeadlessException;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * 计时器
 */
public class Clock extends JPanel
{
    /**
     * 
     */
    private static final long serialVersionUID = -7323901807693104622L;
    private static final String INITIAL_LABEL_TEXT = "<html>&nbsp  计时器：<br>00:00:00<br>&nbsp &nbsp 000</html>";
    private CountingThread thread = new CountingThread();
    private long programStart = System.currentTimeMillis();
    public JLabel label = new JLabel(INITIAL_LABEL_TEXT);
    public boolean stopped = true;

    public void clockStart() {
        programStart = System.currentTimeMillis();
        stopped = false;
    }

    public void clockEnd() {
        stopped = true;
    }

    public void reSet() {
        label.setText(INITIAL_LABEL_TEXT);
    }

    public Clock() throws HeadlessException {
        setLayout(new BorderLayout());
        setupLabel();
        thread.start(); // 计数线程一直就运行着
    }

    // 配置标签
    private void setupLabel() {
        label.setFont(new Font(label.getFont().getName(), label.getFont().getStyle(), 40));
        this.add(label, BorderLayout.CENTER);
        label.setHorizontalTextPosition(SwingConstants.CENTER);
        label.setVerticalTextPosition(SwingConstants.CENTER);
    }

    private class CountingThread extends Thread
    {
        private CountingThread() {
            setDaemon(true);
        }

        @Override
        public void run() {
            while (true) {
                if (!stopped) {
                    long elapsed = System.currentTimeMillis() - programStart;
                    label.setText(format(elapsed));
                }
                try {
                    sleep(20); 
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        }

        // 将毫秒数格式化
        private String format(long elapsed) {
            int hour, minute, second, milli;
            milli = (int) (elapsed % 1000);
            elapsed = elapsed / 1000;
            second = (int) (elapsed % 60);
            elapsed = elapsed / 60;
            minute = (int) (elapsed % 60);
            elapsed = elapsed / 60;
            hour = (int) (elapsed % 60);
            return String.format("<html>&nbsp 计时器：<br>%02d:%02d:%02d<br>&nbsp &nbsp %03d</html>\"", hour, minute,
                    second, milli);
        }
    }
}
