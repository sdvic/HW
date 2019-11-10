import com.pi4j.component.motor.impl.GpioStepperMotorComponent;

import javax.swing.*;
import java.awt.*;

import static java.awt.Toolkit.getDefaultToolkit;

public class UserExperience extends JComponent
{
    private int screenWidth = getDefaultToolkit().getScreenSize().width;
    private JButton testSensorBitsButton = new JButton("TEST SENSOR BITS");
    private JButton readPinStatesButton = new JButton("READ PIN STATES");
    private JButton resetSequenceButton = new JButton("RESET SEQUENCE");
    private JButton emitterSelSequenceButton = new JButton("EMMITTER SEL SEQUENCE");
    private JButton emitterTestButton = new JButton("       EMITTERS");
    private JButton sensorTextButton = new JButton("       SENSORS");
    private JButton nextBoardTextButton = new JButton("       NEXT BOARD");
    private JButton nextFrameTextButton = new JButton("       NEXT FRAME");
    private JButton commButton = new JButton("       COMM");
    private JButton longFullTextButton = new JButton("       LONG FULL");
    private JButton long34TextButton = new JButton("       LONG 3/4");
    private JButton long12TextButton = new JButton("       LONG 1/2");
    private JButton long14TextButton = new JButton("       LONG 1/4");
    private JButton passTextButton = new JButton("       PASS");
    private JButton failTextButton = new JButton("       FAIL");
    private JTextField errorCodeTextField = new JTextField("            \t ERROR CODES");
    JProgressBar progressBar = new JProgressBar(SwingConstants.HORIZONTAL, 20,   200);
    private JFrame display = new JFrame();
    private int leftMargin = 40;
    private int middleMargin = 250;
    private int rightMargin = 650;
    private Graphics g;
    private String version;
    private GpioStepperMotorComponent stepper;


    public UserExperience(String version)
    {
        this.version = version;
    }

    public void createGUI(String version)
    {
        Font ourFont = new Font ("Bank Gothic", Font.BOLD, 25);
        display.setFont(ourFont);
        display.setSize(getDefaultToolkit().getScreenSize().width, getDefaultToolkit().getScreenSize().height);
        display.add(this);
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.getContentPane().setBackground(new Color(193, 202, 202));
        display.setTitle(version);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setSize(100, 20);
        //display.add(progressBar);
        display.setVisible(true);
        testSensorBitsButton.setBounds(500, 350, 100, 50);
        testSensorBitsButton.setMargin(new Insets(0, 0, 0, 0));
        display.add(testSensorBitsButton);
        readPinStatesButton.setBounds(150, 350, 100, 50);
        display.add(readPinStatesButton);
        resetSequenceButton.setBounds(leftMargin, 110, 150, 30);
        display.add(resetSequenceButton);
        errorCodeTextField.setBounds(leftMargin, 420, rightMargin, 30);//Error codes
        display.add(errorCodeTextField);
        resetSequenceButton.setBounds(leftMargin, 100, 100, 50);
        display.add(resetSequenceButton);
        emitterTestButton.setBounds(leftMargin, 150, 150, 30);
        display.add(emitterSelSequenceButton);
        emitterSelSequenceButton.setBounds((leftMargin + 10), 200, 150, 30);
        display.add(sensorTextButton);
        nextBoardTextButton.setBounds(leftMargin, 250, 150, 30);
        display.add(nextBoardTextButton);
        nextFrameTextButton.setBounds(leftMargin, 300, 150, 30);
        display.add(nextFrameTextButton);
        commButton.setBounds(middleMargin, 110, 150, 30);
        display.add(commButton);
        longFullTextButton.setBounds(middleMargin, 150, 150, 30);
        display.add(longFullTextButton);
        long34TextButton.setBounds(middleMargin, 200, 150, 30);
        display.add(long34TextButton);
        long12TextButton.setBounds(middleMargin, 250, 150, 30);
        display.add(long12TextButton);
        long14TextButton.setBounds(middleMargin, 300, 150, 30);
        display.add(long14TextButton);
        passTextButton.setBounds(500, 150, 150, 30);
        display.add(passTextButton);
        failTextButton.setBounds(500, 250, 150, 30);
        display.add(failTextButton);
        display.setSize(getDefaultToolkit().getScreenSize().width, getDefaultToolkit().getScreenSize().height);
        display.add(this);
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.getContentPane().setBackground(new Color(193, 202, 202));
        display.setTitle("FSG StripTest version " + version);
        display.setVisible(true);
    }

    public void paint(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        for (int i = 0; i < 16; i++)
        {
            g2.setStroke(new BasicStroke(2.0f));
            g2.drawOval((40 * i + 40), 10, 30, 30);
            g2.setColor(new Color(255, 243, 20));
            g2.fillOval((40 * i + 40), 10, 30, 30);
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Bank Gothic", Font.BOLD, 18));
            g2.drawString((i + 1) + "", (40 * i + 47), 34);
        }
        g2.setStroke(new BasicStroke(4f));
        g2.drawLine(leftMargin, 47, (rightMargin), 47);//Emitters top line
        g2.setColor(new Color(0, 255, 0));
        g2.fillRect(leftMargin, 48, (rightMargin), 48);//Emmitters Box
        for (int i = 0; i < 4; i++)
        {
            g2.setColor(new Color(200, 200, 200));
            g2.fillOval((120 * i + 180), 53, 30, 30);
            g2.setColor(new Color(0, 0, 0));
            g2.drawOval((120 * i + 180), 53, 30, 30);
            g2.drawString((i + 1) + "", (120 * i + 190), 72);
        }
        g2.drawLine(leftMargin, 96, (rightMargin), 100);//Emitters bottom line
        g2.drawString("EMITTERS", leftMargin, 72);
    }

    public JButton getTestSensorBitsButton()
    {
        return testSensorBitsButton;
    }

    public JButton getSetButton()
    {
        return readPinStatesButton;
    }

    public JButton getResetSequenceButton() { return resetSequenceButton; }

    public JButton emitterJButton() {return emitterSelSequenceButton;}

    public JButton getReadPinStatesButton() { return readPinStatesButton; }

    public JButton getEmitterSelSequenceButton() { return emitterSelSequenceButton; }

    public void setErrorCodeTextField(String errorCodeTextField)
    {
        this.errorCodeTextField.setText(errorCodeTextField);
    }
}


