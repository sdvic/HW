import com.pi4j.component.motor.impl.GpioStepperMotorComponent;

import javax.swing.*;
import java.awt.*;

import static java.awt.Toolkit.getDefaultToolkit;

public class UserExperience extends JComponent
{
    private int screenWidth = getDefaultToolkit().getScreenSize().width;
    private JButton testSensorBitsButton = new JButton("TEST SENSOR BITS");//************************************
    private JButton readPinStatesButton = new JButton("READ PIN STATES");
    private JButton resetSequenceButton = new JButton("RESET SEQUENCE");
    private JButton emitterSelSequenceButton = new JButton("EMMITTER SEL SEQUENCE");
    private JTextField emittersTextField = new JTextField("       EMITTERS");
    private JTextField sensorsTextField = new JTextField("       SENSORS");
    private JTextField nextBoardTextField = new JTextField("       NEXT BOARD");
    private JTextField nextFrameTextField = new JTextField("       NEXT FRAME");
    private JTextField commTextField = new JTextField("       COMM");
    private JTextField longFullTextField = new JTextField("       LONG FULL");
    private JTextField long34TextField = new JTextField("       LONG 3/4");
    private JTextField long12TextField = new JTextField("       LONG 1/2");
    private JTextField long14TextField = new JTextField("       LONG 1/4");
    private JTextField passTextField = new JTextField("       PASS");
    private JTextField failTextField = new JTextField("       FAIL");
    private JFrame display = new JFrame();
    private int leftMargin = 40;
    private int middleMargin = 250;
    private Graphics g;
    private String version;
    private GpioStepperMotorComponent stepper;

    public UserExperience(String version)
    {
        this.version = version;
    }

    public void createGUI(String version)
    {
        display.setSize(getDefaultToolkit().getScreenSize().width, getDefaultToolkit().getScreenSize().height);
        display.add(this);//Adds Graphics
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.getContentPane().setBackground(new Color(193, 202, 202));
        display.setTitle(version);
        display.setVisible(true);
        testSensorBitsButton.setBounds(500, 350, 100, 50);
        display.add(testSensorBitsButton);
        readPinStatesButton.setBounds(150, 350, 100, 50);
        display.add(readPinStatesButton);
        resetSequenceButton.setBounds(leftMargin, 100, 150, 30);
        display.add(resetSequenceButton);
        emittersTextField.setBounds(leftMargin, 150, 150, 30);
        display.add(emitterSelSequenceButton);
        emitterSelSequenceButton.setBounds((leftMargin + 10), 200, 150, 30);
        display.add(sensorsTextField);
        nextBoardTextField.setBounds(leftMargin, 250, 150, 30);
        display.add(nextBoardTextField);
        nextFrameTextField.setBounds(leftMargin, 300, 150, 30);
        display.add(nextFrameTextField);
        commTextField.setBounds(middleMargin, 100, 150, 30);
        display.add(commTextField);
        longFullTextField.setBounds(middleMargin, 150, 150, 30);
        display.add(longFullTextField);
        long34TextField.setBounds(middleMargin, 200, 150, 30);
        display.add(long34TextField);
        long12TextField.setBounds(middleMargin, 250, 150, 30);
        display.add(long12TextField);
        long14TextField.setBounds(middleMargin, 300, 150, 30);
        display.add(long14TextField);
        passTextField.setBounds(500, 150, 150, 30);
        display.add(passTextField);
        failTextField.setBounds(500, 250, 150, 30);
        display.add(failTextField);
        display.setSize(getDefaultToolkit().getScreenSize().width, getDefaultToolkit().getScreenSize().height);
        display.add(this);
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.getContentPane().setBackground(new Color(193, 202, 202));
        display.setTitle("FSG StripTest ver " + version);
        display.setVisible(true);
    }

    public void paint(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        for (int i = 0; i < 16; i++)
        {
            g2.setStroke(new BasicStroke(4));
            g2.drawOval((40 * i + 40), 15, 30, 30);
            g2.setColor(new Color(255, 243, 20));
            g2.fillOval((40 * i + 40), 15, 30, 30);
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Bank Gothic", Font.BOLD, 12));
            g2.drawString((i + 1) + "", (40 * i + 47), 37);
        }
        g2.setStroke(new BasicStroke(.1f));
        g2.drawLine(40, 47, (screenWidth - 60), 47);
        g2.setColor(new Color(37, 243, 255));
        g2.fillRect(38, 48, (screenWidth - 80), 48);
        for (int i = 0; i < 4; i++)
        {
            g2.setColor(new Color(200, 123, 18));
            g2.fillOval((120 * i + 180), 53, 30, 30);
            g2.setColor(new Color(0, 0, 0));
            g2.drawOval((120 * i + 180), 53, 30, 30);
            g2.drawString((i + 1) + "", (120 * i + 190), 72);
        }
        g2.drawString("EMITTERS", leftMargin, 72);
    }

    public JButton getTestSensorBitsButton()
    {
        return testSensorBitsButton;
    }//************************************

    public JButton getSetButton()
    {
        return readPinStatesButton;
    }

    public JButton getResetSequenceButton() { return resetSequenceButton; }

    public JButton emitterJButton() {return emitterSelSequenceButton;}

    public JButton getReadPinStatesButton() { return readPinStatesButton; }

    public JButton getEmitterSelSequenceButton() { return emitterSelSequenceButton; }
}


