import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main implements ActionListener
{
    /****************************************************************************************
     *      Full Swing Golf Strip Test                                                      *
     *      copyright 2019 Vic Wintriss                                                     *
    /****************************************************************************************/
    //public TestSequences ts = new TestSequences();
    private boolean errFail = false;
    private int testByte;
    private String codeCat;
    private int errEmitter;
    private Timer ticker = new Timer(100, this);
    private UserExperience ux;
    private TestSequences ts;

    public Main()
    {
        ux = new UserExperience("ver 504.01", this);
        ts = new TestSequences(this);
    }

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                new Main();
            }
        });
    }

    public void actionPerformed(ActionEvent e)
    {
        if (e.getActionCommand().equals("ALL"))//mode 1
        {
            ux.setAllTestRunning(true);
            resetErrors();
            testScreen(); // run first because to resetErrors() in test.
            testTee();
            testSensors();
            buildDisplayErrorList(ts.getDisplayErrorList(), "     All Test Errors =>  ");
            ux.setAllTestRunning(false);
        }
        if (e.getActionCommand().equals("TEE"))//mode 2
        {
            ux.setTeeTestRunning(true);
            resetErrors();
            testTee();
            ts.setErrFail(false);
            errFail = ts.getErrLpClkOut() | ts.getErrRipple() | ts.getErrRclk() | ts.getErrShiftLoad();
            buildDisplayErrorList(ts.getDisplayErrorList(), "     Tee Test Errors =>  ");
            ux.setTeeTestRunning(false);
        }
        if (e.getActionCommand().equals("SCREEN"))//mode 3
        {
            ux.setScreenTestRunning(true);
            resetErrors();
            testScreen();
            ts.setErrFail(false);
            errFail = ts.getErrLpClkOut() | ts.getErrRipple() | ts.getErrRclk() | ts.getErrShiftLoad();
            buildDisplayErrorList(ts.getDisplayErrorList(), "     Screen Test Errors =>  ");
            ux.setScreenTestRunning(false);
        }
        if (e.getActionCommand().equals("SENSORS"))//mode 4
        {
            ux.setSensorsTestRunning(true);
            resetErrors();
            testSensors();
            errFail = false;
            //errFail = errDataOut | errSin;
            buildDisplayErrorList(ts.getDisplayErrorList(), "     Sensor Test Errors =>  ");
            ux.setSensorsTestRunning(false);
        }
        if (e.getActionCommand().equals("COMM"))//mode 5
        {
            ux.setCommTestRunning(true);
            testByte = (byte) 0b10101110; // byte used for testing sensors. Active low, LSB is D1
            for (int i = 0; i < 200; i++)
            {
                resetErrors();
                testBasic();
                try
                {
                    Thread.sleep(100);
                }   // 1000 milliseconds is one second.
                catch (InterruptedException ex)
                {
                    Thread.currentThread().interrupt();
                }
            }
            buildDisplayErrorList(ts.getDisplayErrorList(), "     COMM Test Errors => ");
            ux.setCommTestRunning(false);

        }
        if (e.getActionCommand().equals("RESET"))//mode 0
        {
            System.out.println("RESET button pressed");//action 1
        }
        if (e.getActionCommand().equals("PRINT"))
        {
            System.out.println("PRINT button pressed");// action 2
        }
        if (e.getActionCommand().equals("RUN"))
        {
            System.out.println("RUN button pressed");// action 2
        }
        ux.setCodeCat(codeCat);
        ux.setErrEmitter(errEmitter);
    }

    private void testTee()// Set CPLD state machine to the tee frame and test all the emitters...mode 2
    {
        for (int i = 1; i < 5; i++)
        {
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

    private void testBasic() // Test the majority of the Comm Board functionality. Uses testByteHigh, testByteLow, emitter, Sin...mode 5
    {
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

    // Reset all errors and set all indicators to default state before running tests
    public void resetErrors()
    {
        for (int i = 0; i < ts.getDisplayErrorList().length; i++)
        {
            ts.setDisplayErrorList(i, false);
        }
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
    }

    public String buildDisplayErrorList(boolean[] errorList, String testSource)
    {
        codeCat = testSource;
        for (int i = 0; i < errorList.length; i++)
        {
            if (errorList[i])
            {
                codeCat += (i + ", ");
            }
        }
        return codeCat;
    }
}


