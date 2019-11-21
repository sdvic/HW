import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;

import static java.awt.Toolkit.getDefaultToolkit;

public class UserExperience extends JComponent implements ActionListener
{
    private Main main;

    Timer paintTicker = new Timer(100, this);
    private TestSequences ts;
    private int screenWidth = getDefaultToolkit().getScreenSize().width;
    private JButton allButton = new JButton("ALL");
    private JButton teeButton = new JButton("TEE");
    private JButton screenButton = new JButton("SCREEN");
    private JButton sensorsButton = new JButton("SENSORS");
    private JButton commButton = new JButton("COMM");
    private JButton runButton = new JButton("RUN");
    private JButton resetButton = new JButton("RESET");
    private JButton printButton = new JButton("PRINT");
    private JTextField passFailTextField = new JTextField();
    private JTextField errorCodeDisplayField = new JTextField();
    private JFrame display = new JFrame();
    private int leftMargin = 40;
    private int middleMargin = 250;
    private int rightMargin = 500;
    private Font buttonFont = new Font("Bank Gothic", Font.BOLD, 21);
    private Font passFailFont = new Font("Bank Gothic", Font.BOLD, 45);
    private String version;
    private Ellipse2D.Double[] emitterBubbleArray = new Ellipse2D.Double[4];
    private Ellipse2D.Double[] sensorBubbleArray = new Ellipse2D.Double[16];
    private int errBit; // Error bit position
    private int errEmitter = 2;      // byte used for emitter errors
    private boolean isPass;
    private boolean isTestFinished;
    private BasicStroke bubbleStroke = new BasicStroke(4.0f);
    public UserExperience(String version)
    {
        this.version = version;
        paintTicker.start();
    }

    public void createGUI(String version)
    {
        display.setSize(getDefaultToolkit().getScreenSize().width, getDefaultToolkit().getScreenSize().height);
        display.add(this);//Adds Graphics
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.setVisible(true);

        allButton.setBounds(leftMargin, 108, 150, 34); // ALL Button
        allButton.setHorizontalAlignment(SwingConstants.CENTER);
        allButton.addActionListener(main);
        display.add(allButton);

        teeButton.setBounds(leftMargin, 153, 150, 34); // TEE button
        teeButton.setHorizontalAlignment(SwingConstants.CENTER);
        teeButton.addActionListener(main);
        display.add(teeButton);
        screenButton.setBounds(leftMargin, 198, 150, 34); // SCREEN button
        screenButton.setHorizontalAlignment(SwingConstants.CENTER);
        screenButton.addActionListener(main);
        display.add(screenButton);

        sensorsButton.setBounds(leftMargin, 243, 150, 34); // SENSORS button
        sensorsButton.setHorizontalAlignment(SwingConstants.CENTER);
        sensorsButton.addActionListener(main);
        display.add(sensorsButton);

        runButton.setBounds(rightMargin, 345, 100, 58); // RUN button
        runButton.setHorizontalAlignment(SwingConstants.CENTER);
        runButton.addActionListener(main);
        display.add(runButton);

        passFailTextField.setBounds(rightMargin, 108, 120, 169);
        passFailTextField.setHorizontalAlignment((SwingConstants.CENTER));
        passFailTextField.setBackground(Color.WHITE);
        passFailTextField.setText("");
        display.add(passFailTextField);

        errorCodeDisplayField.setBounds(0, 289, screenWidth, 44);
        display.add(errorCodeDisplayField);

        commButton.setBounds(middleMargin, 108, 150, 34); // COMM button
        commButton.setHorizontalAlignment(SwingConstants.CENTER);
        commButton.addActionListener(main);

        resetButton.setBounds(leftMargin, 345, 120, 58); // RESET button
        resetButton.setHorizontalAlignment(SwingConstants.CENTER);
        resetButton.addActionListener(main);
        display.add(resetButton);

        printButton.setBounds(middleMargin, 345, 120, 58); // PRINT button
        printButton.setHorizontalAlignment(SwingConstants.CENTER);
        printButton.addActionListener(main);
        display.add(printButton);

        display.add(commButton);
        display.setSize(getDefaultToolkit().getScreenSize().width, getDefaultToolkit().getScreenSize().height);
        display.add(this);
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.getContentPane().setBackground(new Color(160, 160, 160));
        display.setTitle("FSG StripTest ver " + version);
        display.setVisible(true);

        for (int i = 0; i < sensorBubbleArray.length; i++)// Draw 16 sensor indicators
        {
            sensorBubbleArray[i] = new Ellipse2D.Double((42 * i + 26), 10, 30, 30);
        }
        for (int i = 0; i < emitterBubbleArray.length; i++)
        {
            emitterBubbleArray[i] = new Ellipse2D.Double((120 * i + 180), 59, 30, 30);
        }
    }
   public void paint(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        for (int sensorNumber = 0; sensorNumber < sensorBubbleArray.length; sensorNumber++)
        {
            g2.setStroke(bubbleStroke);
            g2.draw(sensorBubbleArray[sensorNumber]);
            g2.drawString("" + sensorNumber++, (int) (sensorBubbleArray[sensorNumber].getCenterX() - buttonFont.getSize()/4), (int) (sensorBubbleArray[sensorNumber].getCenterY() + buttonFont.getSize()/4));
        }
        for (int emitterNumber = 0; emitterNumber < emitterBubbleArray.length; emitterNumber++)
        {
            g2.setStroke(bubbleStroke);
            g2.draw(emitterBubbleArray[emitterNumber]);
            errBit = 1;
            errBit = errBit << emitterNumber;
            errBit = errEmitter & errBit; // current error masked
            if (errBit == 0)//Pass
            {
                g2.setColor(Color.GREEN); // Pass green
                g2.fill(emitterBubbleArray[emitterNumber]);
            }
            if (errBit == 1)//Fail
            {
                g2.setColor(Color.RED); // Failed red
                g2.fill(emitterBubbleArray[emitterNumber]);
            }
            g2.drawString("" + emitterNumber++, (int)emitterBubbleArray[emitterNumber].getCenterX() - buttonFont.getSize()/4, (int)emitterBubbleArray[emitterNumber].getCenterY() + buttonFont.getSize()/4);
        }
    }
    public void actionPerformed(ActionEvent e)
    {
        repaint();
    }
    public void setTs(TestSequences ts)
    {
        this.ts = ts;
    }
    public void setMain(Main main)
    {
        this.main = main;
    }
    public void setTestFinished(boolean isTestFinished)
    {
        this.isTestFinished = isTestFinished;
    }
    public JTextField getErrorCodeDisplayField()
    {
        return errorCodeDisplayField;
    }
    public void setPass(boolean pass)
    {
        isPass = pass;
    }

    public void setErrEmitter(int errEmitter)
    {
        this.errEmitter = errEmitter;
    }
}


