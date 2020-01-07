import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.awt.Color.*;
import static java.awt.Toolkit.getDefaultToolkit;

public class Main implements ActionListener
{
    /****************************************************************************************
     *      Full Swing Golf Strip Test                                                      *
     *      copyright 2019 Vic Wintriss                                                     *
     /****************************************************************************************/
    private int testByte;
    private String codeCat;
    private Bubble bubble = new Bubble(0, 0, Color.BLACK);
    Bubble[] sensorBubbleList = new Bubble[16];
    private Bubble[] emitterBubbleList = new Bubble[5];
    private int screenHeight = getDefaultToolkit().getScreenSize().height;
    private int screenWidth = getDefaultToolkit().getScreenSize().width;
    private int emitterRowYpos = screenHeight / 10;
    int sensorRowYpos = screenHeight / 80;
    private int leftMargin = screenWidth / 10;
    int sensorBubblePitch = screenWidth / 20;
    int emitterBubblePitch = screenWidth / 10;
    private int commTestProgress;
    private Main main;
    private UserExperience ux;
    private TestSequences ts;
    private Timer commTestTicker;
    private JProgressBar commTestProgressBar = new JProgressBar();
    private JButton commButton;
    private Color defaultButtonBackgroundColor;

    /************************************************
     * displayErrorList[0] => errDataOut
     * displayErrorList[1] => errLpClkOut
     * displayErrorList[2] => errModeOut
     * displayErrorList[3] => errClkOut
     * displayErrorList[4] => errEripple
     * displayErrorList[5] => errRclk
     * displayErrorList[6] => errShiftLoad
     * displayErrorList[7] => errSin
     ************************************************/
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> new Main());
    }
    public Main()
    {
        main = this;
        ux = new UserExperience("ver 510.00", this);
        ts = new TestSequences(this);
        commTestTicker = new Timer(100, ts);
        commButton = ux.getCommButton();
        defaultButtonBackgroundColor = ux.getDefaultButtonBackgroundColor();
        for (int i = 0; i < getSensorBubbleList().length; i++)//Setup 16 sensor indicators
        {
            setBubble(sensorBubbleList, i, new Bubble(leftMargin + (sensorBubblePitch * i), sensorRowYpos, BLACK));
        }
        for (int i = 0; i < getEmitterBubbleList().length; i++)//Setup 4? emitter indicators
        {
            setBubble(emitterBubbleList, i, new Bubble(leftMargin + emitterBubblePitch + (emitterBubblePitch * i), emitterRowYpos, ux.getDefaultButtonBackgroundColor()));
        }
    }
    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand().equals("ALL"))//mode 1
        {
            clearErrorLists();
            ux.getAllButton().setBackground(ux.getPressedButtonColor());
            testScreen(); // run first because to resetErrors() in test.
            testTee();
            testSensors();
            buildErrorCodeDisplayFieldString(ts.getIndependentErrorList(), "     All Test Errors =>  ");
            ux.getAllButton().setBackground(ux.getDefaultButtonBackgroundColor());
        }
        if (e.getActionCommand().equals("BASIC"))
        {
            clearErrorLists();
            ux.getBasicButton().setBackground(ux.getPressedButtonColor());
            ts.loadTestWordSequence(testByte);
            ts.resetSequence();// Test in tee frame mode with on-board emitter
            ts.teeSequence();
            ts.emitterSelSequence();
            ts.emitterFireSequence(0);
            ts.teeShiftOutSequence(false);
            ts.resetSequence(); // Test in tee frame mode with next board emitter
            ts.teeSequence();
            ts.emitterDeselSequence();
            ts.emitterFireSequence(1);
            ts.teeShiftOutSequence(true);
            ts.resetSequence(); // Test the screen frame connections
            ts.screenSequence();
            ts.emitterSelSequence();
            ts.emitterFireSequence(2);
            ts.screenShiftOutSequence();
            ts.resetSequence();// End of testing
            buildErrorCodeDisplayFieldString(ts.getIndependentErrorList(), "     Basic Test Errors =>  ");
            ux.getBasicButton().setBackground(ux.getDefaultButtonBackgroundColor());
        }
        if (e.getActionCommand().equals("TEE"))//mode 2
        {
            clearErrorLists();
            ux.getTeeButton().setBackground(ux.getPressedButtonColor());
            testTee();
            buildErrorCodeDisplayFieldString(ts.getIndependentErrorList(), "     Tee Test Errors =>  ");
            ux.getTeeButton().setBackground(ux.getDefaultButtonBackgroundColor());
        }
        if (e.getActionCommand().equals("SCREEN"))//mode 3
        {
            clearErrorLists();
            ux.getScreenButton().setBackground(ux.getPressedButtonColor());
            testScreen();
            buildErrorCodeDisplayFieldString(ts.getIndependentErrorList(), "     Screen Test Errors =>  ");
            ux.getScreenButton().setBackground(ux.getDefaultButtonBackgroundColor());
        }
        if (e.getActionCommand().equals("SENSORS"))//mode 4
        {
            clearErrorLists();
            ux.getSensorsButton().setBackground(ux.getPressedButtonColor());
            testSensors();
            buildErrorCodeDisplayFieldString(ts.getIndependentErrorList(), "     Sensor Test Errors =>  ");
            ux.getSensorsButton().setBackground(ux.getDefaultButtonBackgroundColor());
        }
        if (e.getActionCommand().equals("COMM"))//mode 5
        {
            clearErrorLists();
            ux.getCommButton().setBackground(ux.getPressedButtonColor());
            testByte = (byte) 0b10101110; // byte used for testing sensors. Active low, LSB is D1

            commTestTicker.start();
        }
        if (e.getActionCommand().equals("RESET")) {
            clearErrorLists();
            codeCat = "";
        }
        if (e.getActionCommand().equals("PRINT")) {
            System.out.println("PRINT button pressed");// action 2
        }
        ux.setCodeCat(codeCat);
    }
    private void testTee()// Set CPLD state machine to the tee frame and test all the emitters...mode 2
    {
        for (int i = 1; i < 5; i++) {
            ts.resetSequence();        // t1-t2
            ts.teeSequence();          // t3-t8
            ts.emitterSelSequence();   // t9-t14
            ts.emitterFireSequence(i); // t15-t18
            ts.resetSequence();        // t1-t2
        }
    }
    void testScreen()// Set CPLD state machine to the screen frame and test the interconnection signals
    {
        ts.screenTestSequence();
        ts.screenShiftOutSequence();
        ts.resetSequence();
    }
    private void testSensors()// Test each individual IR photodiode for correct operation...mode 4
    {
        for (int i = 0; i < 8; i++) // walking 1 test pattern
        {
            ts.resetSequence();      // t1-t2
            ts.teeSequence();        // t3-t8
            ts.emitterSelSequence(); // t9-t14
            testByte = 128;
            testByte = testByte >> i;
            ts.loadTestWordSequence(testByte);
            ts.emitterFireSequence(0);          // t15-t18
            ts.teeShiftOutSequence(false); // t19-t54
            ts.resetSequence();                // t55-t56
        }
        for (int i = 0; i < 8; i++) // walking 0 test pattern
        {
            ts.resetSequence();      // t1-t2
            ts.teeSequence();        // t3-t8
            ts.emitterSelSequence(); // t9-t14
            testByte = 128;
            testByte = testByte >> i;
            testByte = ~testByte;
            ts.loadTestWordSequence(testByte);
            ts.emitterFireSequence(0);          // t15-t18
            ts.teeShiftOutSequence(false); // t19-t54
            ts.resetSequence();                // t55-t56
        }
    }
    void testBasic() // Test the majority of the Comm Board functionality. Uses testByteHigh, testByteLow, emitter, Sin...mode 5
    {
        ts.loadTestWordSequence(testByte);
        ts.resetSequence();// Test in tee frame mode with on-board emitter
        ts.teeSequence();
        ts.emitterSelSequence();
        ts.emitterFireSequence(0);
        ts.teeShiftOutSequence(false);
        ts.resetSequence();// Test in tee frame mode with next board emitter
        ts.teeSequence();
        ts.emitterDeselSequence();
        ts.emitterFireSequence(1);
        ts.teeShiftOutSequence(true);
        ts.resetSequence(); // Test the screen frame connections
        ts.screenSequence();
        ts.emitterSelSequence();
        ts.emitterFireSequence(2);
        ts.screenShiftOutSequence();
        ts.resetSequence(); // End of testing
    }
    public void clearErrorLists() // Reset all errors and set all indicators to default state before running tests
    {
        for (int i = 0; i < ts.getIndependentErrorList().length; i++) {
            ts.setDisplayErrorList(i, false);
        }
        for (int i = 0; i < getEmitterBubbleList().length; i++) {
            Bubble bubba = getEmitterBubbleList()[i];
            bubba.setBackgroundColor(BLACK);
            getEmitterBubbleList()[i] = bubba;
        }
        for (int i = 0; i < getSensorBubbleList().length; i++) {
            Bubble bubba = getSensorBubbleList()[i];
            bubba.setBackgroundColor(BLACK);
            setBubble(getSensorBubbleList(), i, bubba);
        }
        ux.setButtonColor(ux.getSensorsButton(), ux.getDefaultButtonBackgroundColor());
        ux.setButtonColor(ux.getTeeButton(), ux.getDefaultButtonBackgroundColor());
        ux.setButtonColor(ux.getScreenButton(), ux.getDefaultButtonBackgroundColor());
        ux.setButtonColor(ux.getCommButton(), ux.getDefaultButtonBackgroundColor());
        ux.setButtonColor(ux.getBasicButton(), ux.getDefaultButtonBackgroundColor());
        ux.setButtonColor(ux.getAllButton(), ux.getDefaultButtonBackgroundColor());
        commTestProgressBar.setValue(0);
    }
    public String buildErrorCodeDisplayFieldString(boolean[] errorList, String testSource)
    {
        codeCat = testSource;
        for (int i = 0; i < errorList.length; i++) {
            if (errorList[i]) {
                codeCat += (i + ", ");
            }
        }
        codeCat += "\n\tEmitter Error # ";
        for (int j = 0; j < getEmitterBubbleList().length; j++) {
            if (getEmitterBubbleList()[j].backgroundColor.equals(RED)) {
                codeCat += (j + ", ");
            }
        }
        return codeCat;
    }
    public Bubble[] getEmitterBubbleList() {return emitterBubbleList;}
    public void setBubble(Bubble[] bubbleList, int bubbleNumber, Bubble bubble) { bubbleList[bubbleNumber] = bubble; }
    public Bubble[] getSensorBubbleList(){return sensorBubbleList;}
    public int getCommTestProgress()
    {
        return commTestProgress;
    }
    public void setCommTestProgress(int commTestProgress)
    {
        this.commTestProgress = commTestProgress;
    }
    public JProgressBar getCommTestProgressBar()
    {
        return commTestProgressBar;
    }
    public void setCommTestProgressBar(JProgressBar commTestProgressBar) {this.commTestProgressBar = commTestProgressBar;}
    public Timer getCommTestTicker()
    {
        return commTestTicker;
    }
    public void setCommTestTicker(Timer commTestTicker)
    {
        this.commTestTicker = commTestTicker;
    }
    public JButton getCommButton()
    {
        return commButton;
    }
    public void setCommButton(JButton commButton)
    {
        this.commButton = commButton;
    }
    public Color getDefaultButtonBackgroundColor()
    {
        return defaultButtonBackgroundColor;
    }
    public void setDefaultButtonBackgroundColor(Color defaultButtonBackgroundColor){this.defaultButtonBackgroundColor = defaultButtonBackgroundColor; }
}

