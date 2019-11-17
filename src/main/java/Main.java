import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main implements ActionListener
{
    /****************************************************************************************
     *      Full Swing Golf Strip Test                                                      *
     *      copyright 2019 Vic Wintriss                                                     */
     private String version = "501.37";
     /****************************************************************************************/
    public TestSequences ts = new TestSequences();
    public UserExperience ux = new UserExperience(version);
    private boolean modeAllTest = false;
    private boolean modeTeeTest = false;
    private boolean modeScreenTest = false;
    private boolean modeSensorTest = false;
    private boolean modeBasicTest = false;
    private int testByte;
    public static void main(String[] args)
    {
        new Main();
    }

    public Main()
    {
        ux.setMain(this);
        SwingUtilities.invokeLater(new Runnable()
        {
            @Override
            public void run()
            {
                ux.createGUI(version);
            }
        });

        ts.setUx(ux);
        ux.setTs(ts);
        new Timer(100, ux).start();
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
        if (e.getActionCommand().equals("ALL"))
        {
            modeAllTest = true;
            modeTeeTest = false;
            modeScreenTest = false;
            modeSensorTest = false;
            modeBasicTest = false;
        }
        if (e.getActionCommand().equals("TEE"))
        {
            modeAllTest = false;
            modeTeeTest = true;
            modeScreenTest = false;
            modeSensorTest = false;
            modeBasicTest = false;
        }
        if(e.getActionCommand().equals("SCREEN"))
        {
            modeAllTest = false;
            modeTeeTest = false;
            modeScreenTest = true;
            modeSensorTest = false;
            modeBasicTest = false;
        }
        if(e.getActionCommand().equals("SENSORS"))
        {
            modeAllTest = false;
            modeTeeTest = false;
            modeScreenTest = false;
            modeSensorTest = true;
            modeBasicTest = false;
        }
        if (e.getActionCommand().equals("COMM"))
        {
            modeAllTest = false;
            modeTeeTest = false;
            modeScreenTest = false;
            modeSensorTest = false;
            modeBasicTest = true;
        }
        if (e.getActionCommand().equals("RUN"))
        {
            if (modeAllTest)
            {
                testTee();
                ts.testScreen();
                testSensors();
            }
            if (modeTeeTest)
            {
                testTee();
            }
            if (modeScreenTest)
            {
                ts.testScreen();
            }
            if (modeSensorTest)
            {
                testSensors();
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
        }
    }
}

