import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.awt.Toolkit.*;

/***********************************************************************
 * Full Swing Golf Strip Test version 0.0, 6/8/2019
 * copyright 2019 Vic Wintriss
 ***********************************************************************/
public class Main extends JComponent implements Runnable, ActionListener
{
    Timer paintTicker = new Timer(20, this);
    String jopInput = "";

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Main());
    }

    @Override
    public void run()
    {
        jopInput =JOptionPane.showInputDialog("What do you want me to do?");
        JFrame gameWindow = new JFrame("FSG StripTest");
        gameWindow.setSize(getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
        gameWindow.add(this);
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameWindow.getContentPane().setBackground(new Color(200, 235, 255));
        gameWindow.setVisible(true);
        paintTicker.start();
    }

    public void paint(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.fillOval(40, 40, 10, 10);
        g2.drawString(jopInput, 150, 150);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        repaint();
    }
}
