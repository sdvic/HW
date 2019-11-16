import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main implements ActionListener
{
    /****************************************************************************************
     *      Full Swing Golf Strip Test                                                      *
     *      copyright 2019 Vic Wintriss                                                     */
     private String version = "501.7";
     /****************************************************************************************/
    public TestSequences ts = new TestSequences();
    public UserExperience ux = new UserExperience(version);
    private boolean modeAllTest = false;
    private boolean modeTeeTest = false;
    private boolean modeScreenTest = false;
    private boolean modeSensorTest = false;
    private boolean modeBasicTest = false;
    private JButton allButton;
    private JButton screenButton;
    private JButton sensorsButton;
    private JButton teeButton;
    private JButton commButton;
    private JButton runButton;
    private int testByte;
    Main main;
    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(Main::new);
    }
    public Main()
    {
        main = this;
        ts.setUx(ux);
        ux.setTs(ts);
        new Timer(100, ux).start();
        ux.createGUI(version);
    }
    private void testTee()// Set CPLD state machine to the tee frame and test all the emitters
    {
        ts.resetErrors();
        for (int i = 1; i < 5; i++)
        {
            ts.resetSequence();        // t1-t2
            ts.teeSequence();          // t3-t8
            ts.emitterSelSequence();   // t9-t14
            ts.emitterFireSequence(i); // t15-t18
            ts.resetSequence();        // t1-t2
        }
    }
    private void testSensors()// Test each individual IR photodiode for correct operation
    {
        ts.resetErrors();
        for (int i = 0; i < 8; i++) // walking 1 test pattern
        {
            ts.resetSequence();      // t1-t2
            ts.teeSequence();        // t3-t8
            ts.emitterSelSequence(); // t9-t14
            testByte = 128;
            testByte = testByte >> i;
            ts.loadTestWord(testByte);
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
            ts.loadTestWord(testByte);
            ts.emitterFireSequence(0);          // t15-t18
            ts.teeShiftOutSequence(false); // t19-t54
            ts.resetSequence();                // t55-t56
        }
    }
    private void testBasic() // Test the majority of the Comm Board functionality. Uses testByteHigh, testByteLow, emitter, Sin
    {
        ts.resetErrors();
        ts.loadTestWord(testByte);
        // Test in tee frame mode with on-board emitter
        ts.resetSequence();
        ts.teeSequence();
        ts.emitterSelSequence();
        ts.emitterFireSequence(0);
        ts.teeShiftOutSequence(false);
        // Test in tee frame mode with next board emitter
        ts.resetSequence();
        ts.teeSequence();
        ts.emitterDeselSequence();
        ts.emitterFireSequence(1);
        ts.teeShiftOutSequence(true);
        // Test the screen frame connections
        ts.resetSequence();
        ts.screenSequence();
        ts.emitterSelSequence();
        ts.emitterFireSequence(2);
        ts.screenShiftOutSequence();
        // End of testing
        ts.resetSequence();
    }
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == allButton)
        {
            modeAllTest = true;
            modeTeeTest = false;
            modeScreenTest = false;
            modeSensorTest = false;
            modeBasicTest = false;
        }
        if (e.getSource() == teeButton)
        {
            modeAllTest = false;
            modeTeeTest = true;
            modeScreenTest = false;
            modeSensorTest = false;
            modeBasicTest = false;
        }
        if (e.getSource() == screenButton)
        {
            modeAllTest = false;
            modeTeeTest = false;
            modeScreenTest = true;
            modeSensorTest = false;
            modeBasicTest = false;
        }
        if (e.getSource() == sensorsButton)
        {
            modeAllTest = false;
            modeTeeTest = false;
            modeScreenTest = false;
            modeSensorTest = true;
            modeBasicTest = false;
        }
        if (e.getSource() == commButton)
        {
            modeAllTest = false;
            modeTeeTest = false;
            modeScreenTest = false;
            modeSensorTest = false;
            modeBasicTest = true;
        }
        if (e.getSource() == runButton)
        {
            if (modeAllTest)
            {
                testTee();
                ts.testScreen();
                testSensors();
                ux.setCodeCat("All Tests Error Codes => ");
            }
            if (modeTeeTest)
            {
                testTee();
                ux.setCodeCat("Tee Test Error Codes => ");
            }
            if (modeScreenTest)
            {
                ts.testScreen();
                ux.setCodeCat("Screen Error Codes => ");
            }
            if (modeSensorTest)
            {
                testSensors();
                ux.setCodeCat("Sensors Error Codes => ");
            }
            if (modeBasicTest)
            {
                testByte = (byte) 0b10101110; // byte used for testing sensors
                for (int i = 0; i < 200; i++)
                {
                    testBasic();
                    try
                    {
                        Thread.sleep(100); // 1000 milliseconds is one second.
                    }
                    catch (InterruptedException ex)
                    {
                        Thread.currentThread().interrupt();
                    }
                }
            }
            ux.buildErrorListDisplay();
        }
    }

    public void setAllButton(JButton allButton)
    {
        this.allButton = allButton;
    }

    public void setScreenButton(JButton screenButton)
    {
        this.screenButton = screenButton;
    }

    public void setSensorsButton(JButton sensorsButton)
    {
        this.sensorsButton = sensorsButton;
    }

    public void setTeeButton(JButton teeButton)
    {
        this.teeButton = teeButton;
    }

    public void setRunButton(JButton runButton)
    {
        this.runButton = runButton;
    }
}

