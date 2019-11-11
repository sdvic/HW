import com.pi4j.io.gpio.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main
{
    /***************************************************************************************
     *      Full Swing Golf Strip Test
     *      copyright 2019 Vic Wintriss                                                    */
    private String version = "500.40Z";
    /**************************************************************************************/
    private Main()
    {
        JPanel layoutPanel = new JPanel(new FlowLayout());
        JCheckBox commBoard = new JCheckBox(" COMM Board Test?");
        JCheckBox longBoard = new JCheckBox(" LONG Board Test?");
        layoutPanel.add(commBoard);
        layoutPanel.add(longBoard);
        JScrollPane scroller = new JScrollPane(layoutPanel);
        scroller.setPreferredSize(new Dimension(400, 50));
        JOptionPane.showMessageDialog(null, scroller);
        UserExperience ux;ux = new UserExperience(version);
        new Timer(100, ux).start();
        if (commBoard.isSelected() && !longBoard.isSelected())
        {
            ux.setCommFlag(true);
            ux.createGUI(version);
        }
        if (longBoard.isSelected() && !commBoard.isSelected())
        {
            ux.setLongFlag(true);
            ux.createGUI(version);
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
        System.out.println("end constructor");
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> { //Prevents graphics problems
            new Main();
            Thread.currentThread().setPriority(10);
        });
    }
}

