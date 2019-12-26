import com.pi4j.io.gpio.*;

public class TestSequences
{
    // Gpio pins used for the tester
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
    // Flags used to set test mode
    private boolean modeAllTest = true; // default test mode
    private boolean modeTeeTest = false;
    private boolean modeScreenTest = false;
    private boolean modeSensorTest = false;
    private boolean modeBasicTest = false;
    private boolean modeReset = false;
    // Flags used for error reporting
    private boolean errDataOut = false; // flags for individual errors
    private boolean errLpClkOut = false;
    private boolean errModeOut = false;
    private boolean errClkOut = false;
    private boolean errEripple = false;
    private boolean errRclk = false;
    private boolean errShiftLoad = false;
    private boolean errSin = false;
    private boolean errFail = false; // one or more tests failed
    private int errTestByteHigh = 0; // byte used for reporting sensors errors, top 8 bits
    private int errTestByteLow = 0;  // byte used for reporting sensors errors, bottom 8 bits
    private int errEmitter = 0;      // byte used for reporting emitter errors
    // Variables used for testing
    private int testByte = 0;        // byte used for testing sensors, top and bottom 8 bits
    private Main main;
    private boolean[] ErrorList = new boolean[8];
    private boolean[] emitterErrorList = new boolean[5];
    private boolean[] sensorErrorList = new boolean[16];

    public TestSequences(Main main)
    {
        this.main = main;
    }
//    // Set CPLD state machine to the RESET state
    public void resetSequence() {
        pin35.low();  // ModeIn t1
        pin36.low();  // ClkIn t2
        pin31.low();  // DataIn
        pin33.high(); // LpClkIn
        pin10.low();  // Sin
        pin11.low();  // LedOn
    }
  // Set CPLD state machine to the tee frame state. Test signals
    public void teeSequence() {
        pin36.high(); // ClkIn t3
        if (pin40.isLow())
        {
            ErrorList[1] = true;// LpClkOut
        }
        if (pin15.isLow())
        {
            ErrorList[4] = true;// Eripple
        }
        if (pin16.isLow())
        {
            ErrorList[5] = true; // Rclk
        }
        if (pin08.isLow())
        {
            ErrorList[6] = true;// ShiftLoad
        }
        pin35.high(); // ModeIn t4
        pin36.low();  // ClkIn t5
        pin36.high(); // ClkIn t6
        pin36.high(); // ClkIn t7
        pin36.high(); // ClkIn t8
    }
    // Set CPLD state machine to the screen frame state. Test signals
    void screenSequence()
    {
        pin36.high(); // ClkIn
        if (pin40.isLow())
        {
            ErrorList[1] = true;// LpClkOut
        }
        if (pin15.isLow())
        {
           ErrorList[4] = true; // Eripple
        }
        if (pin16.isLow())
        {
           ErrorList[5] = true;// Rclk
        }
        if (pin08.isLow())
        {
            ErrorList[6] = true;// ShiftLoad
        }
        pin35.high(); // ModeIn
        pin36.low();  // ClkIn
        pin36.high(); // ClkIn
        pin36.low();  // ClkIn
        pin36.high(); // ClkIn
    }
    void emitterSelSequence() // Set CPLD state machine to select on-board emitter. Test signals
    {
        pin35.low();  // ModeIn t9
        if (pin40.isLow())
        {
            ErrorList[1] = true;// LpClkOut Error
        }
        if (pin15.isLow())
        {
            ErrorList[4] = true;// Eripple Error
        }
        if (pin16.isLow())
        {
            ErrorList[5] = true; // Rclk Error
        }
        if (pin08.isLow())
        {
            ErrorList[6] = true;// ShiftLoad Error
        }
        pin35.high(); // ModeIn t10
        if (pin40.isLow())
        {
            ErrorList[1] = true;// LpClkOut Error
        }
        if (pin15.isLow())
        {
            ErrorList[4] = true;// Eripple Error
        }
        if (pin16.isLow())
        {
            ErrorList[5] = true;// Rclk Error
        }
        if (pin08.isHigh())
        {
            ErrorList[6] = true;// ShiftLoad Error
        }
        pin36.low();   //ClkIn t11
        if (pin15.isLow())
        {
            ErrorList[4] = true; // Eripple Error
        }
        pin36.high(); // ClkIn t12
        pin36.high(); // ClkIn t13
        if (pin15.isLow())
        {
            ErrorList[4] = true;// Eripple Error
        }
        pin36.high(); // ClkIn t14
    }
    void emitterDeselSequence()// Set CPLD state machine to select next board emitter. Test signals
    {
       pin35.low();  // ModeIn t9
        if (pin40.isLow())
        {
            ErrorList[1] = true;// LpClkOut Error
        }
        if (pin15.isLow())
        {
            ErrorList[4] = true;// Eripple Error
        }
        if (pin16.isLow())
        {
            ErrorList[5] = true;// Rclk Error
        }
        if (pin08.isLow())
        {
            ErrorList[6] = true;// ShiftLoad Error
        }
        pin35.high();  //ModeIn t10
        if (pin40.isLow())
        {
            ErrorList[1] = true;// LpClkOut Error
        }
        if (pin15.isLow())
        {
            ErrorList[4] = true;// Eripple Error
        }
        if (pin16.isLow())
        {
            ErrorList[5] = true;// Rclk Error
        }
        if (pin08.isHigh())
        {
            ErrorList[6] = true;// ShiftLoad Error
        }
        pin36.low();   //ClkIn t11
        if (pin15.isLow())
        {
            ErrorList[4] = true;// Eripple Error
        }
        pin36.high(); // ClkIn t12
        pin36.low();  // ClkIn t13
        if (pin15.isHigh())
        {
            ErrorList[4] = true;// Eripple Error
        }
        pin36.high(); // ClkIn t14
    }
    void emitterFireSequence(int emitter)// Set CPLD state machine to set emitter position, fire emitter. Test signals
    {
        selectEmitter(emitter);
        pin35.low();  // ModeIn t15 ", eelIndex => " + eelIndex
        if (pin40.isLow())
        {
            ErrorList[1] = true;// LpClkOut Error
            emitterErrorList[emitter] = true;
        }
        if (pin15.isLow())
        {
            ErrorList[4] = true;// Eripple Error
            emitterErrorList[emitter]  = true;
        }
        if (pin16.isLow())
        {
            ErrorList[5] = true; // Rclk Error
            emitterErrorList[emitter] = true;
        }
        if (pin08.isHigh())
        {
            ErrorList[6] = true;// ShiftLoad Error
            emitterErrorList[emitter] = true;
        }
        pin35.high(); // ModeIn t16
        if (pin07.isHigh())
        {
            errEmitter = errEmitter | emitter;   // Emitter Error
            emitterErrorList[emitter] = true;
        }
        pin11.high(); // LedOn t17
        pin36.low();  // ClkIn
        if (pin16.isHigh())
        {
            errRclk = true;       // Rclk Error
            ErrorList[5] = true;
            emitterErrorList[emitter] = true;
        }
        if (pin07.isLow())
        {
            errEmitter = errEmitter | emitter;   // Emitter Error
            emitterErrorList[emitter] = true;
        }
        pin36.high(); // ClkIn t18
        pin11.low();  // LedOn
        if (pin16.isLow())
        {
            errRclk = true;        // Rclk Error
            ErrorList[5] = true;
            emitterErrorList[emitter] = true;
        }
        if (pin07.isHigh())
        {
            errEmitter = errEmitter | emitter;   // Emitter Error
            emitterErrorList[emitter] = true;
        }
    }
    void teeShiftOutSequence(boolean sIn) // Set CPLD state machine to shift out data from the tee frame, including Sin. Test signals
    {
        int data; // Photo diode test pattern data masked for each LED position
        boolean state; // Pin state
        if (sIn == false)
        {
            pin10.low();   // Sin
        }
        else
        {
            pin10.high(); // Sin
        }
        pin35.low();   // ModeIn t19
        if (pin40.isLow())
        {
            errLpClkOut = true;   // LpClkOut Error
            ErrorList[1] = true;
        }
        if (pin15.isLow())
        {
            errEripple = true;    // Eripple Error
            ErrorList[4] = true;
        }
        if (pin16.isLow())
        {
            errRclk = true;        // Rclk Error
            ErrorList[5] = true;
        }
        if (pin08.isHigh())
        {
            errShiftLoad = true; // ShiftLoad Error
            ErrorList[6] = true;
        }
        pin35.high(); // ModeIn t20
        if (pin07.isHigh())
        {
            errEmitter = 0;   // Emitter Error
        }
        for (int i = 0; i < 8; i++) // t21-t36// shift out photo diode data from the sensor board CPLD shift register, low byte
        {
            pin36.low();  // ClkIn
            if (pin40.isHigh())
            {
                errLpClkOut = true;  // LpClkOut Error
                ErrorList[1] = true;
                getSensorErrorList()[i] = true;
            }
            pin36.high(); // ClkIn
            if (pin40.isLow())
            {
                errLpClkOut = true;   // LpClkOut Error
                ErrorList[1] = true;
                getSensorErrorList()[i] = true;
            }
            data = testByte & i ^ 2; // current test pattern masked // test for correct IR detection by photo diodes
            state = pin38.getState().isHigh();
            if (state != (data != 0))
            {
                errDataOut = true;   // DataOut Error
                ErrorList[0] = true;
                getSensorErrorList()[i] = true;
            }
        }
        for (int i = 0; i < 8; i++) // t37-t54 // shift out photo diode data from the sensor board CPLD shift register, high byte
        {
            pin36.low();  // ClkIn
            if (pin40.isHigh())
            {
                errLpClkOut = true;  // LpClkOut Error
                ErrorList[1] = true;
                getSensorErrorList()[i + 8] = true;
            }
            pin36.high(); // ClkIn
            if (pin40.isLow())
            {
                errLpClkOut = true;   // LpClkOut Error
                ErrorList[1] = true;
                getSensorErrorList()[i + 8] = true;
            }
            data = testByte & i ^ 2; // current test pattern masked // test for correct IR detection by photo diodes
            state = pin38.getState().isHigh();
            if (state != (data != 0))
            {
                errDataOut = true;   // DataOut Error
                ErrorList[0] = true;
                getSensorErrorList()[i + 8] = true;
            }
        }
        pin36.low();  // ClkIn t55  // shift out Sin data
        if (pin40.isHigh())
        {
            errLpClkOut = true;  // LpClkOut Error
            ErrorList[1] = true;
        }
        pin36.high(); // ClkIn t56
        if (pin40.isLow())
        {
            errLpClkOut = true;   // LpClkOut Error
            ErrorList[1] = true;
        }
        state = pin38.getState().isHigh();
        if (state != sIn)
        {
            errSin = true;   // Sin Error
            ErrorList[7] = true;
        }
    }
    void screenShiftOutSequence()// Set CPLD state machine to shift out data from the screen frame. Test signals
    {
        pin35.low();  // ModeIn t19 // test the screen frame connector
        if (pin32.isHigh())
        {
            errModeOut = true;   // ModeOut Error
            ErrorList[2] = true;
        }
        pin35.high(); // ModeIn t20
        if (pin32.isLow())
        {
            errModeOut = true;    // ModeOut Error
            ErrorList[2] = true;
        }
        pin31.low();  // DataIn t21
        pin33.low();  // LpClkIn
        if (pin38.isHigh())
        {
            errLpClkOut = true;  // DataOut Error
            ErrorList[1] = true;
        }
        if (pin40.isHigh())
        {
            errLpClkOut = true;  // LpClkOut Error
            ErrorList[1] = true;
        }
        pin33.high(); // ClkIn t22
        if (pin38.isHigh())
        {
            errLpClkOut = true;  // DataOut Error
            ErrorList[1] = true;
        }
        if (pin40.isLow())
        {
            errLpClkOut = true;   // LpClkOut Error
            ErrorList[1] = true;
        }
        pin31.high(); // DataIn t23
        pin33.low();  // LpClkIn
        if (pin38.isLow())
        {
            errLpClkOut = true;   // DataOut Error
            ErrorList[1] = true;
        }
        if (pin40.isHigh())
        {
            errLpClkOut = true;  // LpClkOut Error
            ErrorList[1] = true;
        }
        pin33.high(); // ClkIn t25
        if (pin38.isLow())
        {
            errLpClkOut = true;   // DataOut Error
            ErrorList[1] = true;
        }
        if (pin40.isLow())
        {
            errLpClkOut = true;   // LpClkOut Error
            ErrorList[1] = true;
        }
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
    // Load the LED shift register with the sensor test pattern
    public void loadTestWord(int testByte) {
        boolean state;
        int led; // LED position to be loaded into shift register
        for (int i = 0; i < 8; i++) // Load test pattern, MSB D8 ... LSB D1
        {
            led = 128;
            led = led >> i;
            led = testByte & led;  // current byte test pattern masked
            state = !(led == 0);
            pin37.low();           // LedClk
            pin13.setState(state); // LedData
            pin37.high();          // LedClk
        }
        pin13.low();  // LedData Leave low after done
        pin37.low();  // LedClk Leave low after done
    }
    public void screenTestSequence()
    {
        resetSequence();      // t1-t2
        screenSequence();     // t3-t8
        emitterSelSequence(); // t9-t14
         // reset errors at end of t14, bit bang the emitter fire sequence
        if (pin40.isLow())
        {
            errLpClkOut = true;   // LpClkOut Error
            ErrorList[1] = true;
        }
        if (pin32.isLow())
        {
            errModeOut = true;    // ModeOut Error
            ErrorList[2] = true;
        }
        if (pin29.isLow())
        {
            errClkOut = true;     // ClkOut Error
            ErrorList[3] = true;
        }
        pin35.low();  // ModeIn t15
        if (pin32.isHigh())
        {
            errModeOut = true;   // ModeOut Error
            ErrorList[2] = true;
        }
        pin35.high(); // ModeIn t16
        if (pin32.isLow())
        {
            errModeOut = true;    // ModeOut Error
            ErrorList[2] = true;
        }
        pin36.low();  // ClkIn t17
        if (pin29.isHigh())
        {
            errClkOut = true;    // ClkOut Error
            ErrorList[3] = true;
        }
        pin36.high(); // ClkIn t18
        if (pin29.isLow())
        {
            errClkOut = true;     // ClkOut Error
            ErrorList[3] = true;
        }
    }
    public void setErrTestByteHigh(int errTestByteHigh)
    {
        this.errTestByteHigh = errTestByteHigh;
    }
    public void setErrTestByteLow(int errTestByteLow)
    {
        this.errTestByteLow = errTestByteLow;
    }
    public void setErrFail(boolean errFail)
    {
        this.errFail = errFail;
    }
    public void setErrEmitter(int errEmitter)
    {
        this.errEmitter = errEmitter;
    }
    public void setErrDataOut(boolean errDataOut)
    {
        this.errDataOut = errDataOut;
    }
    public void setErrLpClkOut(boolean errLpClkOut)
    {
        this.errLpClkOut = errLpClkOut;
    }
    public void setErrModeOut(boolean errModeOut)
    {
        this.errModeOut = errModeOut;
    }
    public void setErrClkOut(boolean errClkOut)
    {
        this.errClkOut = errClkOut;
    }
    public void setErrEripple(boolean errEripple)
    {
        this.errEripple = errEripple;
    }
    public void setErrRclk(boolean errRclk)
    {
        this.errRclk = errRclk;
    }
    public void setErrShiftLoad(boolean errShiftLoad)
    {
        this.errShiftLoad = errShiftLoad;
    }
    public void setErrSin(boolean errSin)
    {
        this.errSin = errSin;
    }
    public boolean getErrLpClkOut()
    {
       return errLpClkOut;
    }
    public boolean getErrRipple()
    {
        return errEripple;
    }
    public boolean getErrRclk()
    {
        return errRclk;
    }
    public boolean getErrShiftLoad()
    {
        return errShiftLoad;
    }
    public int getErrEmitter() { return errEmitter; }
    public boolean[] getErrorList()
    {
        return ErrorList;
    }
    public void setDisplayErrorList(int x, boolean trueFalse)
    {
        ErrorList[x] = trueFalse;
    }
    public boolean[] getEmitterErrorList() {return emitterErrorList;}
    public void setEmitterErrorList(int y, boolean truefalse)
    {
        emitterErrorList[y] = truefalse;
    }

    public boolean[] getSensorErrorList()
    {
        return sensorErrorList;
    }

    public void setSensorErrorList(boolean[] sensorErrorList)
    {
        this.sensorErrorList = sensorErrorList;
    }

    public void setSensorErrorList(int l, boolean b)
    {
    }
}