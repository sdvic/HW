import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

import static java.awt.Frame.MAXIMIZED_BOTH;
import static java.awt.Toolkit.getDefaultToolkit;
public class UserExperience extends JComponent implements ActionListener
{
    private Main main;
    private Dimension screenSize;
    private int screenHeight = getDefaultToolkit().getScreenSize().height;
    private int screenWidth = getDefaultToolkit().getScreenSize().width;
    private int middleMargin = screenWidth/2;
    private int rightMargin = 2 * screenWidth/3;
    private int rowPitch = screenHeight/20;
    private int buttonRow1 = screenHeight/10;
    private int buttonRow2 = screenHeight/5;
    private int buttonWidth = 150;
    private int buttonHeight = 34;
    private int leftMargin = 100;
    private JButton allButton = new JButton("ALL");
    private JButton teeButton = new JButton("TEE");
    private JButton screenButton = new JButton("SCREEN");
    private JButton sensorsButton = new JButton("SENSORS");
    private JButton commButton = new JButton("COMM");
    private JButton runButton = new JButton("RUN");
    private JButton resetButton = new JButton("RESET");
    private JButton printButton = new JButton("PRINT");
    private JButton basicButton = new JButton("BASIC");
    private JTextArea errorCodeDisplayField = new JTextArea();
    private JFrame display;
    private Font buttonFont = new Font("Bank Gothic", Font.BOLD, 15);
    private Font passFailFont = new Font("Bank Gothic", Font.BOLD, 22);
    private boolean[] emitterErrorList;
    private boolean[] sensorErrorList;
    private Ellipse2D.Double[] sensorBubbleArray = new Ellipse2D.Double[16];
    private int errBit; // Error bit position
    private int errEmitter = 2;      // byte used for emitter errors
    private boolean isCommTestRunning;
    private boolean isAllTestRunning;
    private boolean isBasicTestRunning;
    private boolean isTeeTestRunning;
    private boolean isSensorsTestRunning;
    private BasicStroke bubbleStroke = new BasicStroke(4.0f);
    private int bubbleDiameter = 30;
    private float fontWidth;
    private float fontHeight;
    private Color pressedButtonColor = Color.YELLOW;
    private Color defaultButtonBackgroundColor = Color.LIGHT_GRAY;
    private Color defaultButtonForegroundColor = Color.DARK_GRAY;
    private boolean isScreenTestRunning;
    private String codeCat;
    private Rectangle2D.Double errorFieldBorder = new Rectangle2D.Double();
    private Timer paintTicker = new Timer(1000, this);

    public UserExperience(String version, Main main)
    {
        paintTicker.start();
        this.main = main;
        display = new JFrame(version);
        display.setType(Window.Type.UTILITY);
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        display.setSize(screenSize.width, screenSize.height);
        display.setExtendedState(MAXIMIZED_BOTH);
        display.setUndecorated(true); // Remove title bar
        display.add(this);//Adds Graphics
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.setVisible(true);

        allButton.setBounds(leftMargin, buttonRow2, buttonWidth, buttonHeight); // ALL Button
        allButton.setHorizontalAlignment(SwingConstants.CENTER);
        allButton.setBackground(defaultButtonBackgroundColor);
        allButton.setForeground(defaultButtonForegroundColor);
        allButton.setBorder(new BevelBorder(BevelBorder.RAISED));
        allButton.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 4));
        allButton.setBorderPainted(true);
        allButton.setOpaque(true);
        allButton.setFocusPainted(false);
        allButton.addActionListener(main);
        display.add(allButton);

        basicButton.setBounds(leftMargin, buttonRow1 + 2 * 3 * rowPitch, buttonWidth, buttonHeight); // ALL Button
        basicButton.setHorizontalAlignment(SwingConstants.CENTER);
        basicButton.setBackground(defaultButtonBackgroundColor);
        basicButton.setForeground(defaultButtonForegroundColor);
        basicButton.setBorder(new BevelBorder(BevelBorder.RAISED));
        basicButton.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 4));
        basicButton.setBorderPainted(true);
        basicButton.setOpaque(true);
        basicButton.setFocusPainted(false);
        basicButton.addActionListener(main);
        display.add(basicButton);

        commButton.setBounds(middleMargin, buttonRow2 + rowPitch, buttonWidth, buttonHeight); // COMM button
        commButton.setHorizontalAlignment(SwingConstants.CENTER);
        commButton.setBackground(defaultButtonBackgroundColor);
        commButton.setForeground(defaultButtonForegroundColor);
        commButton.setBorder(new BevelBorder(BevelBorder.RAISED));
        commButton.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 4));
        commButton.setBorderPainted(true);
        commButton.setOpaque(true);
        commButton.setFocusPainted(false);
        commButton.addActionListener(main);
        display.add(commButton);

        errorCodeDisplayField.setBounds(leftMargin, 500, screenWidth - (screenWidth/5), 100);
        errorFieldBorder.setRect(leftMargin, 500, 4 * screenWidth/5, 100);
        errorCodeDisplayField.setFont(passFailFont);
        errorCodeDisplayField.setForeground(Color.WHITE);
        errorCodeDisplayField.setBackground(Color.BLACK);
        errorCodeDisplayField.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 4));
        display.add(errorCodeDisplayField);

        printButton.setBounds(middleMargin, buttonRow1 + 5 * rowPitch, buttonWidth, buttonHeight); // PRINT button
        printButton.setHorizontalAlignment(SwingConstants.CENTER);
        printButton.setBackground(defaultButtonBackgroundColor);
        printButton.setForeground(defaultButtonForegroundColor);
        printButton.setBorder(new BevelBorder(BevelBorder.RAISED));
        printButton.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 4));
        printButton.setBorderPainted(true);
        printButton.setOpaque(true);
        printButton.setFocusPainted(false);
        printButton.addActionListener(main);
        display.add(printButton);

        resetButton.setBounds(leftMargin, buttonRow1 + 5 * rowPitch, buttonWidth, buttonHeight); // RESET button
        resetButton.setHorizontalAlignment(SwingConstants.CENTER);
        resetButton.setBackground(defaultButtonBackgroundColor);
        resetButton.setForeground(defaultButtonForegroundColor);
        resetButton.setBorder(new BevelBorder(BevelBorder.RAISED));
        resetButton.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 4));
        resetButton.setBorderPainted(true);
        resetButton.setOpaque(true);
        resetButton.setFocusPainted(false);
        resetButton.addActionListener(main);
        display.add(resetButton);

        runButton.setBounds(rightMargin, buttonRow1 + 5 * rowPitch, buttonWidth, buttonHeight); // RUN button
        runButton.setHorizontalAlignment(SwingConstants.CENTER);
        runButton.setBackground(defaultButtonBackgroundColor);
        runButton.setForeground(defaultButtonForegroundColor);
        runButton.setBorder(new BevelBorder(BevelBorder.RAISED));
        runButton.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 4));
        runButton.setBorderPainted(true);
        runButton.setOpaque(true);
        runButton.setFocusPainted(false);
        runButton.addActionListener(main);
        display.add(runButton);

        screenButton.setBounds(leftMargin, buttonRow1 + 2 * 5 * rowPitch, buttonWidth, buttonHeight); // SCREEN button
        screenButton.setHorizontalAlignment(SwingConstants.CENTER);
        screenButton.setBackground(defaultButtonBackgroundColor);
        screenButton.setForeground(defaultButtonForegroundColor);
        screenButton.setBorder(new BevelBorder(BevelBorder.RAISED));
        screenButton.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 4));
        screenButton.setBorderPainted(true);
        screenButton.setOpaque(true);
        screenButton.setFocusPainted(false);
        screenButton.addActionListener(main);
        display.add(screenButton);

        sensorsButton.setBounds(leftMargin, buttonRow1 + 3 * rowPitch, buttonWidth, buttonHeight); // SENSORS button
        sensorsButton.setHorizontalAlignment(SwingConstants.CENTER);
        sensorsButton.setBackground(defaultButtonBackgroundColor);
        sensorsButton.setForeground(defaultButtonForegroundColor);
        sensorsButton.setBorder(new BevelBorder(BevelBorder.RAISED));
        sensorsButton.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 4));
        sensorsButton.setBorderPainted(true);
        sensorsButton.setOpaque(true);
        sensorsButton.setFocusPainted(false);
        sensorsButton.addActionListener(main);
        display.add(sensorsButton);

        teeButton.setBounds(leftMargin, buttonRow1 + 4 * rowPitch, buttonWidth, buttonHeight); // TEE button
        teeButton.setHorizontalAlignment(SwingConstants.CENTER);
        teeButton.setBackground(defaultButtonBackgroundColor);
        teeButton.setForeground(defaultButtonForegroundColor);
        teeButton.setBorder(new BevelBorder(BevelBorder.RAISED));
        teeButton.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 4));
        teeButton.setBorderPainted(true);
        teeButton.setOpaque(true);
        teeButton.setFocusPainted(false);
        teeButton.addActionListener(main);
        display.add(teeButton);

        display.setSize(getDefaultToolkit().getScreenSize().width, getDefaultToolkit().getScreenSize().height);
        display.add(this);
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.getContentPane().setBackground(Color.BLACK);
        display.setVisible(true);
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

        if (isCommTestRunning)
        {
            commButton.setOpaque(true);
            commButton.setBackground(pressedButtonColor);
        }else{commButton.setBackground(defaultButtonBackgroundColor);}
        if (isAllTestRunning)
        {
            allButton.setOpaque(true);
            allButton.setBackground(pressedButtonColor);
        }else {allButton.setBackground(defaultButtonBackgroundColor);}
        if (isTeeTestRunning)
        {
            teeButton.setOpaque(true);
            teeButton.setBackground(pressedButtonColor);
        }else{
            teeButton.setBackground(defaultButtonBackgroundColor);
        }
        if (isSensorsTestRunning)
        {
            sensorsButton.setOpaque(true);
            sensorsButton.setBackground(pressedButtonColor);
        }else{sensorsButton.setBackground(defaultButtonBackgroundColor);}
        if (isScreenTestRunning)
        {
            screenButton.setOpaque(true);
            screenButton.setBackground(pressedButtonColor);
        }else{screenButton.setBackground(defaultButtonBackgroundColor);}
        if (isBasicTestRunning)
        {
            basicButton.setOpaque(true);
            basicButton.setBackground(pressedButtonColor);
        }else{basicButton.setBackground(defaultButtonBackgroundColor);}
        errorCodeDisplayField.setText(codeCat);
        drawSensorBubbles(g2, frc);
        drawEmitterBubbles(g2, frc);
    }
    private void drawEmitterBubbles(Graphics2D g2, FontRenderContext frc)
    {
        String s;
        for (int i = 0; i < main.getEmitterBubbleList().length; i++)//Load 4 emitter indicators
        {
            Bubble bubba = main.getEmitterBubbleList()[i];
            g2.setColor(bubba.getBackgroundColor());
            g2.fill(bubba.circle);
            g2.setColor(Color.ORANGE);
            g2.draw(bubba.circle);
            s = "" + (i + 1);
            Rectangle2D bounds = g2.getFont().getStringBounds(s, frc);
            fontWidth = (float) bounds.getWidth();
            fontHeight = (float)(1.2 * bounds.getHeight());
            g2.setColor(Color.WHITE);
            g2.drawString(s, (int) ((leftMargin + main.emitterBubblePitch) + (main.emitterBubblePitch * i) + fontWidth), screenHeight/30 + fontHeight);
        }
    }
    private void drawSensorBubbles(Graphics2D g2, FontRenderContext frc)
    {
        String s = "";
        for (int i = 0; i < main.sensorBubbleList.length; i++)// Load 16 sensor indicators into bubble array
        {
            Bubble bubba = main.sensorBubbleList[i];
            //System.out.println(".backgroundColor[" + i + "] => : " + bubba.backgroundColor);
            g2.setColor(bubba.backgroundColor);
            g2.fill(bubba.circle);
            g2.setColor(Color.ORANGE);
            g2.draw(bubba.circle);
            s = "" + (i + 1);
            Rectangle2D bounds = g2.getFont().getStringBounds(s, frc);
            fontWidth = (float) bounds.getWidth();
            fontHeight = (float)(1.2 * bounds.getHeight());
            if (fontWidth > 10)//for two digit bubble
            {
                fontWidth = fontWidth /4;
            }
            g2.setColor(Color.WHITE);
            g2.drawString(s, (int) (leftMargin + (main.sensorBubblePitch * i) + fontWidth), main.sensorRowYpos * 4 * fontHeight);

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
    public void setCodeCat(String codeCat) { this.codeCat = codeCat; }
    public JTextArea getErrorCodeDisplayField()
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
    public void setBasicTestRunning(boolean isBasicTestRunning) { this.isBasicTestRunning = isBasicTestRunning; }
    public void setEmitterErrorList(boolean[] emitterErrorList)
    {
        this.emitterErrorList = emitterErrorList;
    }
    public boolean[] getSensorErrorList()
    {
        return sensorErrorList;
    }
    public void setSensorErrorList(boolean[] sensorErrorList)
    {
        this.sensorErrorList = sensorErrorList;
    }
}


