import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import static java.awt.Toolkit.getDefaultToolkit;

public class UserExperience extends JComponent implements ActionListener
{
    private Main main;

    Timer paintTicker = new Timer(100, this);
   // private TestSequences ts;
    private int screenWidth = getDefaultToolkit().getScreenSize().width;
    private int screenHeight = getDefaultToolkit().getScreenSize().height;
    private int leftMargin = screenWidth/20;
    private int middleMargin = screenWidth/2;
    private int rightMargin = screenWidth - screenWidth/10;
    private int sensorBubblePitch = screenWidth/16;
    private int emitterBubblePitch = screenWidth/8;
    private int emitterRowYpos = screenHeight/10;
    private int sensorRowYpos = screenHeight/120;

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
    private JFrame display;

    private Font buttonFont = new Font("Bank Gothic", Font.BOLD, 15);
    private Font passFailFont = new Font("Bank Gothic", Font.BOLD, 45);
    private Ellipse2D.Double[] emitterBubbleArray = new Ellipse2D.Double[8];
    private Ellipse2D.Double[] sensorBubbleArray = new Ellipse2D.Double[16];
    private int errBit; // Error bit position
    private int errEmitter = 2;      // byte used for emitter errors
    private boolean isPass;
    private boolean isCommTestRunning;
    private BasicStroke bubbleStroke = new BasicStroke(4.0f);
    private int bubbleDiameter = 30;

    public UserExperience(String version)
    {
        display = new JFrame(version);
        paintTicker.start();

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
        display.add(commButton);

        resetButton.setBounds(leftMargin, 345, 120, 58); // RESET button
        resetButton.setHorizontalAlignment(SwingConstants.CENTER);
        resetButton.addActionListener(main);
        display.add(resetButton);

        printButton.setBounds(middleMargin, 345, 120, 58); // PRINT button
        printButton.setHorizontalAlignment(SwingConstants.CENTER);
        printButton.addActionListener(main);
        display.add(printButton);

        display.setSize(getDefaultToolkit().getScreenSize().width, getDefaultToolkit().getScreenSize().height);
        display.add(this);
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.getContentPane().setBackground(Color.LIGHT_GRAY);
        display.setVisible(true);

        for (int i = 0; i < sensorBubbleArray.length; i++)// Load 16 sensor indicators
        {
            sensorBubbleArray[i] = new Ellipse2D.Double((sensorBubblePitch * i), sensorRowYpos, bubbleDiameter, bubbleDiameter);
        }
        for (int i = 0; i < emitterBubbleArray.length; i++)//Load 8 emitter indicators
        {
            emitterBubbleArray[i] = new Ellipse2D.Double(emitterBubblePitch * i, emitterRowYpos, bubbleDiameter, bubbleDiameter);
        }
    }
   public void paint(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        String s = "";
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setFont(buttonFont);
        g2.setStroke(bubbleStroke);
        FontRenderContext frc = g2.getFontRenderContext();

        if (isCommTestRunning)
        {
            commButton.setBackground(Color.BLUE);
        }
        for (int i = 0; i < sensorBubbleArray.length; i++)
        {
            g2.draw(sensorBubbleArray[i]);
            s = "" + i;
            Rectangle2D bounds = g2.getFont().getStringBounds(s, frc);
            float width = (float) bounds.getWidth();
            if (width > 10)
            {
                width = width/4;
            }
            g2.drawString(s, (int) ((sensorBubblePitch * i) + width), sensorRowYpos);
        }
        for (int i = 0; i < emitterBubbleArray.length; i++)
        {
            errBit = 1;
            errBit = errBit << i;
            errBit = errEmitter & errBit; // current error masked
            if (errBit == 0)//Pass
            {
                g2.setColor(Color.GREEN); // Pass green
                g2.fill(emitterBubbleArray[i]);
            }
            if (errBit == 1)//Fail
            {
                g2.setColor(Color.RED); // Failed red
                g2.fill(emitterBubbleArray[i]);
            }
            g2.setStroke(bubbleStroke);
            g2.setColor(Color.BLACK);
            g2.draw(emitterBubbleArray[i]);
            s = i + "";
            Rectangle2D bounds = g2.getFont().getStringBounds(s, frc);
            float width = (float) bounds.getWidth();
            g2.drawString(s, (int) ((emitterBubblePitch * i) + width), emitterRowYpos);
        }
    }
    public void actionPerformed(ActionEvent e)
    {
        repaint();
    }
//    public void setTs(TestSequences ts)
//    {
//        this.ts = ts;
//    }
    public void setMain(Main main)
    {
        this.main = main;
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
    public void setCommTestRunning(boolean commTestRunning)
    {
        isCommTestRunning = commTestRunning;
    }
}


