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
    private JButton basicButton = new JButton("BASIC");
    private JButton runButton = new JButton("RUN");
    private JButton resetButton = new JButton("RESET");
    private JButton printButton = new JButton("PRINT");
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
    private Color pressedButtonColor = Color.GREEN;
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

        getAllButton().setBounds(leftMargin, buttonRow2, buttonWidth, buttonHeight); // ALL Button
        getAllButton().setHorizontalAlignment(SwingConstants.CENTER);
        getAllButton().setBackground(getDefaultButtonBackgroundColor());
        getAllButton().setForeground(getDefaultButtonForegroundColor());
        getAllButton().setBorder(new BevelBorder(BevelBorder.RAISED));
        getAllButton().setBorder(BorderFactory.createLineBorder(Color.ORANGE, 4));
        getAllButton().setBorderPainted(true);
        getAllButton().setOpaque(true);
        getAllButton().setFocusPainted(false);
        getAllButton().addActionListener(main);
        display.add(getAllButton());

        getBasicButton().setBounds(leftMargin, buttonRow1 + 2 * 3 * rowPitch, buttonWidth, buttonHeight); // ALL Button
        getBasicButton().setHorizontalAlignment(SwingConstants.CENTER);
        getBasicButton().setBackground(getDefaultButtonBackgroundColor());
        getBasicButton().setForeground(getDefaultButtonForegroundColor());
        getBasicButton().setBorder(new BevelBorder(BevelBorder.RAISED));
        getBasicButton().setBorder(BorderFactory.createLineBorder(Color.ORANGE, 4));
        getBasicButton().setBorderPainted(true);
        getBasicButton().setOpaque(true);
        getBasicButton().setFocusPainted(false);
        getBasicButton().addActionListener(main);
        display.add(getBasicButton());

        getCommButton().setBounds(middleMargin, buttonRow2 + rowPitch, buttonWidth, buttonHeight); // COMM button
        getCommButton().setHorizontalAlignment(SwingConstants.CENTER);
        getCommButton().setBackground(getDefaultButtonBackgroundColor());
        getCommButton().setForeground(getDefaultButtonForegroundColor());
        getCommButton().setBorder(new BevelBorder(BevelBorder.RAISED));
        getCommButton().setBorder(BorderFactory.createLineBorder(Color.ORANGE, 4));
        getCommButton().setBorderPainted(true);
        getCommButton().setOpaque(true);
        getCommButton().setFocusPainted(false);
        getCommButton().addActionListener(main);
        display.add(getCommButton());

        errorCodeDisplayField.setBounds(leftMargin, 500, screenWidth - (screenWidth/5), 100);
        errorFieldBorder.setRect(leftMargin, 500, 4 * screenWidth/5, 100);
        errorCodeDisplayField.setFont(passFailFont);
        errorCodeDisplayField.setForeground(Color.WHITE);
        errorCodeDisplayField.setBackground(Color.BLACK);
        errorCodeDisplayField.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 4));
        display.add(errorCodeDisplayField);

        printButton.setBounds(middleMargin, buttonRow1 + 5 * rowPitch, buttonWidth, buttonHeight); // PRINT button
        printButton.setHorizontalAlignment(SwingConstants.CENTER);
        printButton.setBackground(getDefaultButtonBackgroundColor());
        printButton.setForeground(getDefaultButtonForegroundColor());
        printButton.setBorder(new BevelBorder(BevelBorder.RAISED));
        printButton.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 4));
        printButton.setBorderPainted(true);
        printButton.setOpaque(true);
        printButton.setFocusPainted(false);
        printButton.addActionListener(main);
        display.add(printButton);

        resetButton.setBounds(leftMargin, buttonRow1 + 5 * rowPitch, buttonWidth, buttonHeight); // RESET button
        resetButton.setHorizontalAlignment(SwingConstants.CENTER);
        resetButton.setBackground(getDefaultButtonBackgroundColor());
        resetButton.setForeground(getDefaultButtonForegroundColor());
        resetButton.setBorder(new BevelBorder(BevelBorder.RAISED));
        resetButton.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 4));
        resetButton.setBorderPainted(true);
        resetButton.setOpaque(true);
        resetButton.setFocusPainted(false);
        resetButton.addActionListener(main);
        display.add(resetButton);

        runButton.setBounds(rightMargin, buttonRow1 + 5 * rowPitch, buttonWidth, buttonHeight); // RUN button
        runButton.setHorizontalAlignment(SwingConstants.CENTER);
        runButton.setBackground(getDefaultButtonBackgroundColor());
        runButton.setForeground(getDefaultButtonForegroundColor());
        runButton.setBorder(new BevelBorder(BevelBorder.RAISED));
        runButton.setBorder(BorderFactory.createLineBorder(Color.ORANGE, 4));
        runButton.setBorderPainted(true);
        runButton.setOpaque(true);
        runButton.setFocusPainted(false);
        runButton.addActionListener(main);
        display.add(runButton);

        getScreenButton().setBounds(leftMargin, buttonRow1 + 2 * 5 * rowPitch, buttonWidth, buttonHeight); // SCREEN button
        getScreenButton().setHorizontalAlignment(SwingConstants.CENTER);
        getScreenButton().setBackground(getDefaultButtonBackgroundColor());
        getScreenButton().setForeground(getDefaultButtonForegroundColor());
        getScreenButton().setBorder(new BevelBorder(BevelBorder.RAISED));
        getScreenButton().setBorder(BorderFactory.createLineBorder(Color.ORANGE, 4));
        getScreenButton().setBorderPainted(true);
        getScreenButton().setOpaque(true);
        getScreenButton().setFocusPainted(false);
        getScreenButton().addActionListener(main);
        display.add(getScreenButton());

        getSensorsButton().setBounds(leftMargin, buttonRow1 + 3 * rowPitch, buttonWidth, buttonHeight); // SENSORS button
        getSensorsButton().setHorizontalAlignment(SwingConstants.CENTER);
        getSensorsButton().setBackground(getDefaultButtonBackgroundColor());
        getSensorsButton().setForeground(getDefaultButtonForegroundColor());
        getSensorsButton().setBorder(new BevelBorder(BevelBorder.RAISED));
        getSensorsButton().setBorder(BorderFactory.createLineBorder(Color.ORANGE, 4));
        getSensorsButton().setBorderPainted(true);
        getSensorsButton().setOpaque(true);
        getSensorsButton().setFocusPainted(false);
        getSensorsButton().addActionListener(main);
        display.add(getSensorsButton());

        getTeeButton().setBounds(leftMargin, buttonRow1 + 4 * rowPitch, buttonWidth, buttonHeight); // TEE button
        getTeeButton().setHorizontalAlignment(SwingConstants.CENTER);
        getTeeButton().setBackground(getDefaultButtonBackgroundColor());
        getTeeButton().setForeground(getDefaultButtonForegroundColor());
        getTeeButton().setBorder(new BevelBorder(BevelBorder.RAISED));
        getTeeButton().setBorder(BorderFactory.createLineBorder(Color.ORANGE, 4));
        getTeeButton().setBorderPainted(true);
        getTeeButton().setOpaque(true);
        getTeeButton().setFocusPainted(false);
        getTeeButton().addActionListener(main);
        display.add(getTeeButton());

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
            g2.drawString(s, (int) ((leftMargin + main.emitterBubblePitch) + (main.emitterBubblePitch * i) + fontWidth), (int)bubba.circle.y + fontHeight);
        }
    }
    private void drawSensorBubbles(Graphics2D g2, FontRenderContext frc)
    {
        String s = "";
        for (int i = 0; i < main.sensorBubbleList.length; i++)// Load 16 sensor indicators into bubble array
        {
            Bubble bubba = main.sensorBubbleList[i];
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
            g2.drawString(s, (int) ((leftMargin  + (main.sensorBubblePitch * i) + fontWidth)), (int)bubba.circle.y + fontHeight);

            //g2.drawString(s, (int) (leftMargin + (main.sensorBubblePitch * i) + fontWidth), main.sensorRowYpos * 4 * fontHeight);

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

    public JButton getSensorsButton()
    {
        return sensorsButton;
    }

    public void setSensorsButton(JButton sensorsButton)
    {
        this.sensorsButton = sensorsButton;
    }

    public Color getDefaultButtonForegroundColor()
    {
        return defaultButtonForegroundColor;
    }

    public void setDefaultButtonForegroundColor(Color defaultButtonForegroundColor)
    {
        this.defaultButtonForegroundColor = defaultButtonForegroundColor;
    }

    public Color getPressedButtonColor()
    {
        return pressedButtonColor;
    }

    public void setPressedButtonColor(Color pressedButtonColor)
    {
        this.pressedButtonColor = pressedButtonColor;
    }
    public void setButtonColor(JButton button, Color buttonColor)
    {
        button.setBackground(buttonColor);
    }

    public Color getDefaultButtonBackgroundColor()
    {
        return defaultButtonBackgroundColor;
    }

    public void setDefaultButtonBackgroundColor(Color defaultButtonBackgroundColor)
    {
        this.defaultButtonBackgroundColor = defaultButtonBackgroundColor;
    }

    public JButton getAllButton()
    {
        return allButton;
    }

    public void setAllButton(JButton allButton)
    {
        this.allButton = allButton;
    }

    public JButton getTeeButton()
    {
        return teeButton;
    }

    public void setTeeButton(JButton teeButton)
    {
        this.teeButton = teeButton;
    }

    public JButton getScreenButton()
    {
        return screenButton;
    }

    public void setScreenButton(JButton screenButton)
    {
        this.screenButton = screenButton;
    }

    public JButton getCommButton()
    {
        return commButton;
    }

    public void setCommButton(JButton commButton)
    {
        this.commButton = commButton;
    }

    public JButton getBasicButton()
    {
        return basicButton;
    }

    public void setBasicButton(JButton basicButton)
    {
        this.basicButton = basicButton;
    }
}


