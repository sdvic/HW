import com.pi4j.io.gpio.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main implements ActionListener
{
    /***************************************************************************************
     *      Full Swing Golf Strip Test
     *      copyright 2019 Vic Wintriss                                                    */
    private String version = "500.49";
    /**************************************************************************************/
    private GpioController gpio = GpioFactory.getInstance();
    private GpioPinDigitalInput pin38 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_28, "Raspi pin 38", PinPullResistance.PULL_UP);  // DataOut
    private GpioPinDigitalInput pin40 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_29, "Raspi pin 40", PinPullResistance.PULL_UP);  // LpClkOut
    private GpioPinDigitalInput pin32 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_26, "Raspi pin 32", PinPullResistance.PULL_UP);  // ModeOut
    private GpioPinDigitalInput pin29 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_21, "Raspi pin 29", PinPullResistance.PULL_UP);  // ClkOut
    private GpioPinDigitalInput pin15 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_03, "Raspi pin 15", PinPullResistance.PULL_UP);  // Eripple
    private GpioPinDigitalInput pin16 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04, "Raspi pin 16", PinPullResistance.PULL_UP);  // Rclk
    private GpioPinDigitalInput pin08 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_15, "Raspi pin 08", PinPullResistance.PULL_UP);  // S/L
    private GpioPinDigitalInput pin07 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_07, "Raspi pin 07", PinPullResistance.PULL_UP);  // Emitter
    private GpioPinDigitalOutput pin35 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_24, "RasPi pin 35", PinState.LOW);  // ModeIn
    private GpioPinDigitalOutput pin36 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_27, "RasPi pin 36", PinState.LOW);  // ClkIn
    private GpioPinDigitalOutput pin31 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_22, "RasPi pin 31", PinState.LOW);  // DataIn
    private GpioPinDigitalOutput pin33 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23, "RasPi pin 33", PinState.HIGH); // LpClkIn
    private GpioPinDigitalOutput pin10 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_16, "RasPi pin 10", PinState.LOW);  // Sin
    private GpioPinDigitalOutput pin03 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_08, "RasPi pin 03", PinState.LOW);  // Esel0
    private GpioPinDigitalOutput pin05 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_09, "RasPi pin 05", PinState.LOW);  // Esel1
    private GpioPinDigitalOutput pin37 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25, "RasPi pin 37", PinState.LOW);  // LedClk
    private GpioPinDigitalOutput pin13 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "RasPi pin 13", PinState.LOW);  // LedData
    private GpioPinDigitalOutput pin11 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "RasPi pin 11", PinState.LOW);  // LedOn

    private GpioPinDigitalOutput[] pins = // Only here for Sequencer
            {
                    pin05, // Esel1
                    pin03, // Esel0
                    pin11, // LedOn
                    pin10, // Sin
                    pin33, // LpClkIn
                    pin31, // DataIn
                    pin36, // ClkIn
                    pin35  // ModeIn
            };

    private UserExperience ux;
    private JButton readPinStatesButton;//************************************
    private JButton resetSequenceButton;
    private JButton loadTestButton;
    private JButton emitterSelSequenceButton;
    private JButton testSensorBitsButton;
    private boolean errDataOut = false;  // flags for errors
    private boolean errLpClkOut = false;
    private boolean errModeOut = false;
    private boolean errClkOut = false;
    private boolean errEripple = false;
    private boolean errRck = false;
    private boolean errShiftLoad = false;
    private boolean errEmitter = false;
    private byte testByteHigh = 0;  // byte used for testing sensors, top 8 bits
    private byte testByteLow = 0;   // byte used for testing sensors, bottom 8 bits
    private boolean sIn = false;    // bit used for testing Sin from Long Board

    private Main()
    {
        ux = new UserExperience(version);
        readPinStatesButton = ux.getSetButton();//************************************
        readPinStatesButton.addActionListener(this);//************************************
        resetSequenceButton = ux.getResetSequenceButton();
        resetSequenceButton.addActionListener(this);
        emitterSelSequenceButton = ux.getEmitterSelSequenceButton();
        emitterSelSequenceButton.addActionListener(this);
        testSensorBitsButton = ux.getTestSensorBitsButton();
        testSensorBitsButton.addActionListener(this);
        ux.createGUI(version);
        new Timer(100, this).start();
    }

     public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> { //Prevents graphics problems
            new Main();
            Thread.currentThread().setPriority(10);
        });
    }

    // Reset all errors before running tests
    private void resetErrors() {
        errDataOut = false;
        errLpClkOut = false;
        errModeOut = false;
        errClkOut = false;
        errEripple = false;
        errRck = false;
        errShiftLoad = false;
        errEmitter = false;
    }

    // Set CPLD state machine to the RESET state
     private void resetSequence() {
        pin35.low();  // ModeIn t1
        pin36.low();  // ClkIn t2
        pin31.low();  // DataIn
        pin33.high(); // LpClkIn
        pin10.low();  // Sin
        pin11.low();  // LedOn
    }

    // Set CPLD state machine to the tee frame state. Test signals
     private void teeSequence() {
        pin36.high(); // ClkIn t3
        if ( pin40.isLow() ) errLpClkOut = true;   // LpClkOut
        if ( pin15.isLow() ) errEripple = true;    // Eripple
        if ( pin16.isLow() ) errRck = true;        // Rclk
        if ( pin08.isLow() ) errShiftLoad = true;  // ShiftLoad
        pin35.high(); // ModeIn t4
        pin36.low();  // ClkIn t5
        pin36.high(); // ClkIn t6
        pin36.high(); // ClkIn t7
        pin36.high(); // ClkIn t8
    }

    // Set CPLD state machine to the screen frame state. Test signals
     private void screenSequence() {
        pin36.high(); // ClkIn
        if ( pin40.isLow() ) errLpClkOut = true;   // LpClkOut
        if ( pin15.isLow() ) errEripple = true;    // Eripple
        if ( pin16.isLow() ) errRck = true;        // Rclk
        if ( pin08.isLow() ) errShiftLoad = true;  // ShiftLoad
        pin35.high(); // ModeIn
        pin36.low();  // ClkIn
        pin36.high(); // ClkIn
        pin36.low();  // ClkIn
        pin36.high(); // ClkIn
    }

    // Set CPLD state machine to select on-board emitter. Test signals
     private void emitterSelSequence() {
        pin35.low();  // ModeIn t9
        if ( pin40.isLow() ) errLpClkOut = true;   // LpClkOut Error
        if ( pin15.isLow() ) errEripple = true;    // Eripple Error
        if ( pin16.isLow() ) errRck = true;        // Rclk Error
        if ( pin08.isLow() ) errShiftLoad = true;  // ShiftLoad Error
        pin35.high(); // ModeIn t10
        if ( pin40.isLow() ) errLpClkOut = true;   // LpClkOut Error
        if ( pin15.isLow() ) errEripple = true;    // Eripple Error
        if ( pin16.isLow() ) errRck = true;        // Rclk Error
        if ( pin08.isHigh() ) errShiftLoad = true; // ShiftLoad Error
        pin36.low();  // ClkIn t11
        pin36.high(); // ClkIn t12
        pin36.high(); // ClkIn t13
        if ( pin15.isLow() ) errEripple = true;    // Eripple Error
        pin36.high(); // ClkIn t14
    }

    // Set CPLD state machine to select next board emitter. Test signals
     private void emitterDeselSequence() {
        pin35.low();  // ModeIn t9
        if ( pin40.isLow() ) errLpClkOut = true;   // LpClkOut Error
        if ( pin15.isLow() ) errEripple = true;    // Eripple Error
        if ( pin16.isLow() ) errRck = true;        // Rclk Error
        if ( pin08.isLow() ) errShiftLoad = true;  // ShiftLoad Error
        pin35.high(); // ModeIn t10
        if ( pin40.isLow() ) errLpClkOut = true;   // LpClkOut Error
        if ( pin15.isLow() ) errEripple = true;    // Eripple Error
        if ( pin16.isLow() ) errRck = true;        // Rclk Error
        if ( pin08.isHigh() ) errShiftLoad = true; // ShiftLoad Error
        pin36.low();  // ClkIn t11
        pin36.high(); // ClkIn t12
        pin36.low();  // ClkIn t13
        if ( pin15.isHigh() ) errEripple = true;   // Eripple Error
        pin36.high(); // ClkIn t14
    }

    // Set CPLD state machine to set Sin, fire emitter. Test signals
     private void emitterFireSequence() {
        if (sIn == false)
            pin10.low();   // Sin
        else pin10.high(); // Sin
        pin35.low();  // ModeIn t15
        if ( pin40.isLow() ) errLpClkOut = true;   // LpClkOut Error
        if ( pin15.isLow() ) errEripple = true;    // Eripple Error
        if ( pin16.isLow() ) errRck = true;        // Rclk Error
        if ( pin08.isHigh() ) errShiftLoad = true; // ShiftLoad Error
        pin35.high(); // ModeIn t16
        if ( pin07.isHigh() ) errEmitter = true;   // Emitter Error
        pin11.high(); // LedOn t17
        pin36.low();  // ClkIn
        if ( pin16.isHigh() ) errRck = true;       // Rclk Error
        if ( pin07.isHigh() ) errEmitter = true;   // Emitter Error
        pin36.high(); // ClkIn t18
        pin11.low();  // LedOn
    }

    // Set CPLD state machine to shift out data. Test signals
     private void shiftOutSequence() {
        int data; // Photo diode test pattern data masked for each LED position
        boolean state; // Pin state
        pin35.low();  // ModeIn t19
        if ( pin40.isLow() ) errLpClkOut = true;   // LpClkOut Error
        if ( pin15.isLow() ) errEripple = true;    // Eripple Error
        if ( pin16.isLow() ) errRck = true;        // Rclk Error
        if ( pin08.isHigh() ) errShiftLoad = true; // ShiftLoad Error
        pin35.high(); // ModeIn t20
        if ( pin07.isHigh() ) errEmitter = true;   // Emitter Error

        // shift out photo diode data from the sensor board CPLD shift register, low byte
        for (int i = 0; i < 8; i++) // t21-t36
        {
            pin36.low();  // ClkIn
            if ( pin40.isHigh() ) errLpClkOut = true;  // LpClkOut Error
            pin36.high(); // ClkIn
            if ( pin40.isLow() ) errLpClkOut = true;   // LpClkOut Error

            // test for correct IR detection by photo diodes
            data = testByteLow & i^2; // current test pattern masked
            state = pin38.getState().isHigh();
            if ( state != (data != 0) ) errDataOut = true;   // DataOut Error
        }
        // shift out photo diode data from the sensor board CPLD shift register, high byte
        for (int i = 0; i < 8; i++) // t37-t54
        {
            pin36.low();  // ClkIn
            if ( pin40.isHigh() ) errLpClkOut = true;  // LpClkOut Error
            pin36.high(); // ClkIn
            if ( pin40.isLow() ) errLpClkOut = true;   // LpClkOut Error

            // test for correct IR detection by photo diodes
            data = testByteHigh & i^2; // current test pattern masked
            state = pin38.getState().isHigh();
            if ( state != (data != 0) ) errDataOut = true;   // DataOut Error
        }
        // shift out Sin data
        pin36.low();  // ClkIn
        if ( pin40.isHigh() ) errLpClkOut = true;  // LpClkOut Error
        pin36.high(); // ClkIn
        if ( pin40.isLow() ) errLpClkOut = true;   // LpClkOut Error
        state = pin38.getState().isHigh();
        if ( state != sIn ) errDataOut = true;   // DataOut Error
    }

    //  Selects one of four emitter positions for testing
    private void selectEmitter(int emitter) {
        switch (emitter) {
            case 1:
                pin03.low();  // Esel0
                pin05.low();  // Esel1
                break;
            case 2:
                pin03.low();  // Esel0
                pin05.high(); // Esel1
                break;
            case 3:
                pin03.high(); // Esel0
                pin05.low();  // Esel1
                break;
            case 4:
                pin03.high(); // Esel0
                pin05.high(); // Esel1
                break;
        }
    }

    private void loadTestWord() {
        boolean state = false;
        int led = 1; // LED position to be loaded into shift register
        for (int i = 0; i < 8; i++) // Load high byte
        {
            led = 128;
            led = led >> i;
            led = testByteHigh & led; // current test pattern masked
            state = !(led == 0);
            pin37.low();  // LedClk
            pin13.setState(state);
            pin37.high(); // LedClk
        }
        for (int i = 0; i < 8; i++) // Load low byte
        {
            led = 128;
            led = led >> i;
            led = testByteLow & led; // current test pattern masked
            state = !(led == 0);
            pin37.low();  // LedClk
            pin13.setState(state);
            pin37.high(); // LedClk
        }
    }

     public void actionPerformed(ActionEvent e)
    {
        ux.repaint();
        if (e.getSource() == testSensorBitsButton)//====
        {
            System.out.println("testing sensor bits");
            selectEmitter(1);
            testByteHigh = 0b1111110;  // byte used for testing sensors, top 8 bits
            testByteLow = 0b01011111;   // byte used for testing sensors, bottom 8 bits
            sIn = false;
            for (int i = 0; i < 100; i++)
            {
                resetSequence();
                loadTestWord();
                teeSequence();
//            screenSequence();
                emitterSelSequence();
//            emitterDeselSequence();
                emitterFireSequence();
                shiftOutSequence();
                resetSequence();
                try {
                    Thread.sleep(100);                 // 1000 milliseconds is one second.
                } catch(InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        if (e.getSource() == resetSequenceButton)//=====
        {
            System.out.println("reset sequence");
            resetSequence();
        }
        if (e.getSource() == emitterSelSequenceButton)
        {
            System.out.println("emitter sel sequence");
            emitterSelSequence();
        }
        if (e.getSource() == loadTestButton)
        {
            System.out.println("load test");
            loadTestWord();
        }
        if (e.getSource() == readPinStatesButton)//========
        {
            System.out.println("Reading pin states:");
            System.out.print("38" + pin38.getState() + " ");
            System.out.print("40" + pin40.getState() + " ");
            System.out.print("32" + pin32.getState() + " ");
            System.out.print("29" + pin29.getState() + " ");
            System.out.print("15" + pin15.getState() + " ");
            System.out.print("16" + pin16.getState() + " ");
            System.out.print("08" + pin08.getState() + " ");
            System.out.println("07" + pin07.getState());
        }
    }
}