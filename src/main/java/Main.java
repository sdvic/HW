import javax.swing.*;
import java.awt.*;

public class Main
{
    /**************************************************************************************/
    public TestSequences ts = new TestSequences();
    /***************************************************************************************
     *      Full Swing Golf Strip Test
     *      copyright 2019 Vic Wintriss                                                    */
    private String version = "500.40BR";
    public UserExperience ux = new UserExperience(version);

    private Main()
    {
        ts.setUx(ux);
        ux.setTs(ts);
        JPanel layoutPanel = new JPanel(new FlowLayout());
        JCheckBox commBoard = new JCheckBox(" COMM Board Test?");
        JCheckBox longBoard = new JCheckBox(" LONG Board Test?");
        layoutPanel.add(commBoard);
        layoutPanel.add(longBoard);
        JScrollPane scroller = new JScrollPane(layoutPanel);
        scroller.setPreferredSize(new Dimension(400, 50));
        JOptionPane.showMessageDialog(null, scroller);
        new Timer(100, ux).start();
        if (commBoard.isSelected() && !longBoard.isSelected())
        {
            ux.setCommFlag(true);
            ux.createGUI(version);
            ux.setCommFlag(true);
        }
        if (longBoard.isSelected() && !commBoard.isSelected())
        {
            ux.setLongFlag(true);
            ux.createGUI(version);
            ux.setLongFlag(true);
        }
        if (commBoard.isSelected() && longBoard.isSelected())
        {
            JOptionPane.showMessageDialog(null, "Please select only one test");
            System.exit(0);
        }
        if (!commBoard.isSelected() && !longBoard.isSelected())
        {
            JOptionPane.showMessageDialog(null, "PLease ease select at one test");
            System.exit(0);
        }
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                new Main();
            }
        });
    }
}

