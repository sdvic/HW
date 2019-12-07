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
   // private TestSequences ts;
    private int screenWidth = getDefaultToolkit().getScreenSize().width;
    private int screenHeight = getDefaultToolkit().getScreenSize().height;
    private int leftMargin = screenWidth/10;
    private int middleMargin = screenWidth/2;
    private int rightMargin = screenWidth - screenWidth/3;
    private int sensorBubblePitch = screenWidth/24;
    private int emitterBubblePitch = screenWidth/8;
    private int rowPitch = screenHeight/20;
    private int emitterRowYpos = screenHeight/10;
    private int sensorRowYpos = screenHeight/120;
    private int buttonRow1 = screenHeight/10;
    private int buttonRow2 = screenHeight/5;
    private int buttonWidth = 150;
    private int buttonHeight = 34;
    private JButton allButton = new JButton("ALL");
    private JButton teeButton = new JButton("TEE");
    private JButton screenButton = new JButton("SCREEN");
    private JButton sensorsButton = new JButton("SENSORS");
    private JButton commButton = new JButton("COMM");
    private JButton runButton = new JButton("RUN");
    private JButton resetButton = new JButton("RESET");
    private JButton printButton = new JButton("PRINT");
    private JTextField errorCodeDisplayField = new JTextField();
    private JFrame display;
    private Font buttonFont = new Font("Bank Gothic", Font.BOLD, 15);
    private Font passFailFont = new Font("Bank Gothic", Font.BOLD, 45);
    private Ellipse2D.Double[] emitterBubbleArray = new Ellipse2D.Double[4];
    private Ellipse2D.Double[] sensorBubbleArray = new Ellipse2D.Double[16];
    private int errBit; // Error bit position
    private int errEmitter = 2;      // byte used for emitter errors
    private boolean isCommTestRunning;
    private boolean isAllTestRunning;
    private boolean isTeeTestRunning;
    private boolean isSensorsTestRunning;
    private BasicStroke bubbleStroke = new BasicStroke(4.0f);
    private int bubbleDiameter = 30;
    private float fontWidth;
    private float fontHeight;
    private Timer paintTicker = new Timer(100, this);
    private Color pressedButtonColor = Color.BLUE;
    private Color defaultButtonColor = Color.YELLOW;
    private boolean isScreenTestRunning;
    private String codeCat;
    private Rectangle2D.Double errorFieldBorder = new Rectangle2D.Double();
    public UserExperience(String version, Main main)
    {
        this.main = main;//Set up dashboard
        display = new JFrame(version);
        display.setSize(getDefaultToolkit().getScreenSize().width, getDefaultToolkit().getScreenSize().height);
        display.add(this);//Adds Graphics
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.setVisible(true);

        allButton.setBounds(leftMargin, buttonRow2, buttonWidth, buttonHeight); // ALL Button
        allButton.setHorizontalAlignment(SwingConstants.CENTER);
        allButton.setBackground(defaultButtonColor);
        allButton.setBorderPainted(true);
        allButton.setOpaque(true);
        allButton.addActionListener(main);
        display.add(allButton);

        commButton.setBounds(middleMargin, buttonRow2 + rowPitch, buttonWidth, buttonHeight); // COMM button
        commButton.setHorizontalAlignment(SwingConstants.CENTER);
        commButton.setBackground(defaultButtonColor);
        commButton.setBorderPainted(true);
        commButton.setOpaque(true);
        commButton.addActionListener(main);
        display.add(commButton);

        errorCodeDisplayField.setBounds(leftMargin, 500, screenWidth - (screenWidth/5), 100);
        errorFieldBorder.setRect(leftMargin, 500, screenWidth - (screenWidth/5), 100);
        display.add(errorCodeDisplayField);

        printButton.setBounds(middleMargin, buttonRow1 + 5 * rowPitch, buttonWidth, buttonHeight); // PRINT button
        printButton.setHorizontalAlignment(SwingConstants.CENTER);
        printButton.setBackground(defaultButtonColor);
        printButton.setBorderPainted(true);
        printButton.setOpaque(true);
        printButton.addActionListener(main);
        display.add(printButton);

        resetButton.setBounds(leftMargin, buttonRow1 + 5 * rowPitch, buttonWidth, buttonHeight); // RESET button
        resetButton.setHorizontalAlignment(SwingConstants.CENTER);
        resetButton.setBackground(defaultButtonColor);
        resetButton.setBorderPainted(true);
        resetButton.setOpaque(true);
        resetButton.addActionListener(main);
        display.add(resetButton);

        runButton.setBounds(rightMargin, buttonRow1 + 5 * rowPitch, buttonWidth, buttonHeight); // RUN button
        runButton.setHorizontalAlignment(SwingConstants.CENTER);
        runButton.setBackground(defaultButtonColor);
        runButton.setBorderPainted(true);
        runButton.setOpaque(true);
        runButton.addActionListener(main);
        display.add(runButton);

        screenButton.setBounds(leftMargin, buttonRow1 + 2 * rowPitch, buttonWidth, buttonHeight); // SCREEN button
        screenButton.setHorizontalAlignment(SwingConstants.CENTER);
        screenButton.setBackground(defaultButtonColor);
        screenButton.setBorderPainted(true);
        screenButton.setOpaque(true);
        screenButton.addActionListener(main);
        display.add(screenButton);

        sensorsButton.setBounds(leftMargin, buttonRow1 + 3 * rowPitch, buttonWidth, buttonHeight); // SENSORS button
        sensorsButton.setHorizontalAlignment(SwingConstants.CENTER);
        sensorsButton.setBackground(defaultButtonColor);
        sensorsButton.setBorderPainted(true);
        sensorsButton.setOpaque(true);
        sensorsButton.addActionListener(main);
        display.add(sensorsButton);

        teeButton.setBounds(leftMargin, buttonRow1 + 4 * rowPitch, buttonWidth, buttonHeight); // TEE button
        teeButton.setHorizontalAlignment(SwingConstants.CENTER);
        teeButton.setBackground(defaultButtonColor);
        teeButton.setBorderPainted(true);
        teeButton.setOpaque(true);
        teeButton.addActionListener(main);
        display.add(teeButton);

        display.setSize(getDefaultToolkit().getScreenSize().width, getDefaultToolkit().getScreenSize().height);
        display.add(this);
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.getContentPane().setBackground(Color.LIGHT_GRAY);
        display.setVisible(true);

        paintTicker.start();
    }
   public void paint(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        String s = "";
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(bubbleStroke);
        g2.draw(errorFieldBorder);
        g2.setFont(buttonFont);
        FontRenderContext frc = g2.getFontRenderContext();
        drawSensorBubbles(g2, frc);
        drawEmitterBubbles(g2, frc);
        if (isCommTestRunning)
        {
            commButton.setOpaque(true);
            commButton.setBackground(pressedButtonColor);
        }else{commButton.setBackground(defaultButtonColor);}
        if (isAllTestRunning)
        {
            allButton.setOpaque(true);
            allButton.setBackground(pressedButtonColor);
        }else {allButton.setBackground(defaultButtonColor);}
        if (isTeeTestRunning)
        {
            teeButton.setOpaque(true);
            teeButton.setBackground(pressedButtonColor);
        }else{
            teeButton.setBackground(defaultButtonColor);
        }
        if (isSensorsTestRunning)
        {
            sensorsButton.setOpaque(true);
            sensorsButton.setBackground(pressedButtonColor);
        }else{sensorsButton.setBackground(defaultButtonColor);}
        if (isScreenTestRunning)
        {
            screenButton.setOpaque(true);
            screenButton.setBackground(pressedButtonColor);
        }else{screenButton.setBackground(defaultButtonColor);}
        errorCodeDisplayField.setText(codeCat);
    }
    private void drawEmitterBubbles(Graphics2D g2, FontRenderContext frc)
    {
        String s;
        for (int i = 0; i < emitterBubbleArray.length; i++)//Load 4 emitter indicators
        {
            emitterBubbleArray[i] = new Ellipse2D.Double(leftMargin + (emitterBubblePitch/2) + (emitterBubblePitch * i), emitterRowYpos, bubbleDiameter, bubbleDiameter);
            g2.setColor(Color.YELLOW);
            g2.fill(sensorBubbleArray[i]);
            g2.setColor(Color.BLACK);
            g2.draw(sensorBubbleArray[i]);
            s = "" + (i + 1);
            Rectangle2D bounds = g2.getFont().getStringBounds(s, frc);
            fontWidth = (float) bounds.getWidth();
            fontHeight = (float)(1.2 * bounds.getHeight());
            g2.drawString(s, (int) (leftMargin + (emitterBubblePitch/2) + (emitterBubblePitch * i) + fontWidth), emitterRowYpos + fontHeight);
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
        }
    }
    private void drawSensorBubbles(Graphics2D g2, FontRenderContext frc)
    {
        String s;
        for (int i = 0; i < sensorBubbleArray.length; i++)// Load 16 sensor indicators into bubble array
        {
            sensorBubbleArray[i] = new Ellipse2D.Double((leftMargin + sensorBubblePitch * i), sensorRowYpos + fontHeight, bubbleDiameter, bubbleDiameter);
            g2.setColor(Color.YELLOW);
            g2.fill(sensorBubbleArray[i]);
            g2.setColor(Color.BLACK);
            g2.draw(sensorBubbleArray[i]);
            s = "" + (i + 1);
            Rectangle2D bounds = g2.getFont().getStringBounds(s, frc);
            fontWidth = (float) bounds.getWidth();
            fontHeight = (float)(1.2 * bounds.getHeight());
            if (fontWidth > 10)//for two digit bubble
            {
                fontWidth = fontWidth /4;
            }
            g2.drawString(s, (int) (leftMargin + (sensorBubblePitch * i) + fontWidth), sensorRowYpos + 2 * fontHeight);
        }
    }
    @Override
    public void actionPerformed(ActionEvent actionEvent)
    {
        repaint();
    }
    public void setAllTestRunning(boolean allTestRunning)
    {
        isAllTestRunning = allTestRunning;
    }
    public void setTeeTestRunning(boolean teeTestRunning)
    {
        isTeeTestRunning = teeTestRunning;
    }
    public void setScreenTestRunning(boolean iScreenTestRunning)
    {
        this.isScreenTestRunning = iScreenTestRunning;
    }
    public void setSensorsTestRunning(boolean sensorsTestRunning)
    {
        isSensorsTestRunning = sensorsTestRunning;
    }
    public void setCodeCat(String codeCat)
    {
        this.codeCat = codeCat;
    }
    public JTextField getErrorCodeDisplayField()
    {
        return errorCodeDisplayField;
    }
    public void setErrEmitter(int errEmitter)
    {
        this.errEmitter = errEmitter;
    }
    public void setCommTestRunning(boolean isCommTestRunning)
    {
        this.isCommTestRunning = isCommTestRunning;
    }
}


