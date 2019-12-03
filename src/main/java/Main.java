import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main implements ActionListener
{
    /****************************************************************************************
     *      Full Swing Golf Strip Test                                                      *
     *      copyright 2019 Vic Wintriss                                                     */
     /****************************************************************************************/
    //public TestSequences ts = new TestSequences();
    private boolean isAllTestRunning;
    private boolean isComTestRunning;
    private boolean isTeeTestRunning;
    private boolean isSensorTestRunning;
    private boolean isScreenTestRunning;
    private boolean errFail = false;
    private int testByte;
    private String codeCat;
    private int errEmitter;
    private Timer ticker = new Timer(100, this);
    private Main main;
    private UserExperience ux;
    private TestSequences ts;

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
    public Main()
    {
       ux = new UserExperience("ver 502.23",this);
       ts = new TestSequences();
    }

    public void actionPerformed(ActionEvent e)
    {
            if (e.getActionCommand().equals("ALL"))//mode 1
            {
                isAllTestRunning = true;
                ux.setAllTestRunning(true);
            }
            if (e.getActionCommand().equals("TEE"))//mode 2
            {
                isTeeTestRunning = true;
                ux.setCommTestRunning(true);
            }
            if (e.getActionCommand().equals("SCREEN"))//mode 3
            {
                isScreenTestRunning = true;
                ux.setScreenTestRunning(true);
            }
            if (e.getActionCommand().equals("SENSORS"))//mode 4
            {
                isSensorTestRunning= true;
                ux.setSensorsTestRunning(true);
            }
            if (e.getActionCommand().equals("COMM"))//mode 5
            {
                isComTestRunning = true;
                ux.setCommTestRunning(true);
            }
            if (e.getActionCommand().equals("RESET"))//mode 0
            {
                System.out.println("RESET button pressed");//action 1
            }
            if (e.getActionCommand().equals("PRINT")) {
                System.out.println("PRINT button pressed");// action 2
            }
        }
//        if (e.getActionCommand().equals("RUN"))//action 3
//        {
//            if (modeAllTest)//mode 1...ALL
//            {
//                ts.resetErrors();
//                ts.testScreen(); // run first because to resetErrors() in test.
//                testTee();
//                testSensors();
//                ts.setErrTestByteLow(1);  // byte used for testing sensors errors, bottom 8 bits   ### REMOVE ###
//                ts.setErrTestByteHigh(4); // byte used for testing sensors errors, top 8 bits   ### REMOVE ###
//                ts.setErrEmitter(2);    // byte used for testing emitter errors  ### REMOVE ###
//                ts.setErrFail(true);     // bit indicating FAIL   ### REMOVE ###
//                buildDisplayErrorList(ts.getDisplayErrorList(), "All Test Errors => ");
//            }
//            if (modeTeeTest)//mode 2...TEE
//            {
//                ts.resetErrors();
//                testTee();
//                ts.setErrFail(false);
//                errFail = ts.getErrLpClkOut() | ts.getErrRipple() | ts.getErrRclk() | ts.getErrShiftLoad();
//                buildDisplayErrorList(ts.getDisplayErrorList(), "Tee Test Errors => ");
//            }
//            if (modeScreenTest)//mode 3...SCREEN
//            {
//                ts.resetErrors();
//                ts.testScreen();
//                ts.setErrFail(false);
//                errFail = ts.getErrLpClkOut() | ts.getErrRipple() | ts.getErrRclk() | ts.getErrShiftLoad();
//                buildDisplayErrorList(ts.getDisplayErrorList(), "Screen Test Errors =>  ");
//            }
//            if (modeSensorTest)//mode 4...SENSORS
//            {
//                ts.resetErrors();
//                testSensors();
//                errFail = false;
//                //errFail = errDataOut | errSin;
//                buildDisplayErrorList(ts.getDisplayErrorList(), "Sensor Test Errors =>  ");
//            }
//            if (modeBasicTest)//mode 5...COMM?
//            {
//                testByte = (byte) 0b10101110; // byte used for testing sensors. Active low, LSB is D1
//                for (int i = 0; i < 200; i++)
//                {
//                    ts.resetErrors();
//                    testBasic();
//                    try { Thread.sleep(100); }   // 1000 milliseconds is one second.
//                    catch(InterruptedException ex) { Thread.currentThread().interrupt(); }
//                }
//                buildDisplayErrorList(ts.getDisplayErrorList(), "Basic Test Errors => ");
//            }
//            ux.setErrEmitter(ts.getErrEmitter());
//            ux.setTestFinished(isTestFinished);
//            ux.getErrorCodeDisplayField().setText(codeCat);
//            ux.setErrEmitter(errEmitter);
//        }
//    }
//    private void testTee()// Set CPLD state machine to the tee frame and test all the emitters...mode 2
//    {
//        ts.resetErrors();
//        for (int i = 1; i < 5; i++)
//        {
//            ts.resetSequence();        // t1-t2
//            ts.teeSequence();          // t3-t8
//            ts.emitterSelSequence();   // t9-t14
//            ts.emitterFireSequence(i); // t15-t18
//            ts.resetSequence();        // t1-t2
//        }
//    }
//    private void testSensors()// Test each individual IR photodiode for correct operation...mode 4
//    {
//        ts.resetErrors();
//        for (int i = 0; i < 8; i++) // walking 1 test pattern
//        {
//            ts.resetSequence();      // t1-t2
//            ts.teeSequence();        // t3-t8
//            ts.emitterSelSequence(); // t9-t14
//            testByte = 128;
//            testByte = testByte >> i;
//            ts.loadTestWord(testByte);
//            ts.emitterFireSequence(0);          // t15-t18
//            ts.teeShiftOutSequence(false); // t19-t54
//            ts.resetSequence();                // t55-t56
//        }
//        for (int i = 0; i < 8; i++) // walking 0 test pattern
//        {
//            ts.resetSequence();      // t1-t2
//            ts.teeSequence();        // t3-t8
//            ts.emitterSelSequence(); // t9-t14
//            testByte = 128;
//            testByte = testByte >> i;
//            testByte = ~testByte;
//            ts.loadTestWord(testByte);
//            ts.emitterFireSequence(0);          // t15-t18
//            ts.teeShiftOutSequence(false); // t19-t54
//            ts.resetSequence();                // t55-t56
//        }
//    }
//    private void testBasic() // Test the majority of the Comm Board functionality. Uses testByteHigh, testByteLow, emitter, Sin...mode 5
//    {
//        ts.resetErrors();
//        ts.loadTestWord(testByte);
//        // Test in tee frame mode with on-board emitter
//        ts.resetSequence();
//        ts.teeSequence();
//        ts.emitterSelSequence();
//        ts.emitterFireSequence(0);
//        ts.teeShiftOutSequence(false);
//        // Test in tee frame mode with next board emitter
//        ts.resetSequence();
//        ts.teeSequence();
//        ts.emitterDeselSequence();
//        ts.emitterFireSequence(1);
//        ts.teeShiftOutSequence(true);
//        // Test the screen frame connections
//        ts.resetSequence();
//        ts.screenSequence();
//        ts.emitterSelSequence();
//        ts.emitterFireSequence(2);
//        ts.screenShiftOutSequence();
//        // End of testing
//        ts.resetSequence();
//     }
}


