import com.pi4j.io.gpio.*;
import java.awt.*;
import static java.awt.Color.RED;

public class TestSequences
{
    // Raspberry Pi Gpio pins used for the tester
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
    private boolean errDataOut = false;
    private boolean errLpClkOut = false;// flags for individual errors
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
    private int testByte = 0;        // byte used for testing sensors, top and bottom 8 bits
    private Main main;
    Bubble bubba;
    private boolean[] independentErrorList = new boolean[8];

    public TestSequences(Main main)
    {
        this.main = main;
    }
    public void resetSequence()// Set CPLD state machine to the RESET state
    {
        pin35.low();  // ModeIn t1
        pin36.low();  // ClkIn t2
        pin31.low();  // DataIn
        pin33.high(); // LpClkIn
        pin10.low();  // Sin
        pin11.low();  // LedOn
    }
    public void teeSequence()    // Set CPLD state machine to the tee frame state. Test signals
    {
        pin36.high(); // ClkIn t3
        if (pin40.isLow()) {independentErrorList[1] = true;} // LpClkOut
        if (pin15.isLow()) { independentErrorList[4] = true;}// Eripple
        if (pin16.isLow()) { independentErrorList[5] = true;} // Rclk
        if (pin08.isLow()) { independentErrorList[6] = true;} // ShiftLoad
        pin35.high(); // ModeIn t4
        pin36.low();  // ClkIn t5
        pin36.high(); // ClkIn t6
        pin36.high(); // ClkIn t7
        pin36.high(); // ClkIn t8
    }
    void screenSequence() // Set CPLD state machine to the screen frame state. Test signals
    {
        pin36.high(); // ClkIn
        if (pin40.isLow()) {independentErrorList[1] = true;}// LpClkOut
        if (pin15.isLow()) {independentErrorList[4] = true;} // Eripple
        if (pin16.isLow()) {independentErrorList[5] = true;}// Rclk
        if (pin08.isLow()) {independentErrorList[6] = true;}// ShiftLoad
        pin35.high(); // ModeIn
        pin36.low();  // ClkIn
        pin36.high(); // ClkIn
        pin36.low();  // ClkIn
        pin36.high(); // ClkIn
    }

    void emitterSelSequence() // Set CPLD state machine to select on-board emitter. Test signals
    {
        pin35.low();  // ModeIn t9
        if (pin40.isLow()) {independentErrorList[1] = true;}// LpClkOut Error
        if (pin15.isLow()) {independentErrorList[4] = true;}// Eripple Error
        if (pin16.isLow()) {independentErrorList[5] = true;} // Rclk Error
        if (pin08.isLow()) {independentErrorList[6] = true;}// ShiftLoad Error
        pin35.high(); // ModeIn t10
        if (pin40.isLow()) {independentErrorList[1] = true;}// LpClkOut Error
        if (pin15.isLow()) {independentErrorList[4] = true;}// Eripple Error
        if (pin16.isLow()) {independentErrorList[5] = true;}// Rclk Error
        if (pin08.isHigh()) {independentErrorList[6] = true;}// ShiftLoad Error
        pin36.low();   //ClkIn t11
        if (pin15.isLow()) {independentErrorList[4] = true;}// Eripple Error
        pin36.high(); // ClkIn t12
        pin36.high(); // ClkIn t13
        if (pin15.isLow()) {independentErrorList[4] = true;}// Eripple Error
        pin36.high(); // ClkIn t14
    }
    void emitterDeselSequence()// Set CPLD state machine to select next board emitter. Test signals
    {
        pin35.low();  // ModeIn t9
        if (pin40.isLow()) {independentErrorList[1] = true;}// LpClkOut Error
        if (pin15.isLow()) {independentErrorList[4] = true;}// Eripple Error
        if (pin16.isLow()) {independentErrorList[5] = true;}// Rclk Error
        if (pin08.isLow()) {independentErrorList[6] = true;}// ShiftLoad Error
        pin35.high();  //ModeIn t10
        if (pin40.isLow()) {independentErrorList[1] = true;}// LpClkOut Error
        if (pin15.isLow()) {independentErrorList[4] = true;}// Eripple Error
        if (pin16.isLow()) {independentErrorList[5] = true;}// Rclk Error
        if (pin08.isHigh()) {independentErrorList[6] = true;}// ShiftLoad Error
        pin36.low();   //ClkIn t11
        if (pin15.isLow()) {independentErrorList[4] = true;}// Eripple Error
        pin36.high(); // ClkIn t12
        pin36.low();  // ClkIn t13
        if (pin15.isHigh()) {independentErrorList[4] = true;}// Eripple Error
        pin36.high(); // ClkIn t14
    }

    void emitterFireSequence(int emitter)// Set CPLD state machine to set emitter position, fire emitter. Test signals
    {
        selectEmitterSequence(emitter);
        bubba = main.getEmitterBubbleList()[emitter];
        pin35.low();  // ModeIn t15 ", eelIndex => " + eelIndex
        if (pin40.isLow()) {// LpClkOut Error
            bubba.backgroundColor = RED;
        }else {bubba.backgroundColor = Color.GREEN;}
        if (pin15.isLow()) {// Eripple Error
            bubba.backgroundColor = RED;}
        else {bubba.backgroundColor = Color.GREEN;}
        if (pin16.isLow()) {// Rclk Error
            bubba.backgroundColor = RED;}
        else {bubba.backgroundColor = Color.GREEN;}
        if (pin08.isHigh()) {// ShiftLoad Error
            bubba.backgroundColor = RED;}
        else {bubba.backgroundColor = Color.GREEN;}
        pin35.high(); // ModeIn t16
        if (pin07.isHigh()) {// Emitter Error
            errEmitter = errEmitter | emitter;
            bubba.backgroundColor = RED;}
        else {bubba.backgroundColor = Color.GREEN;}
        pin11.high(); // LedOn t17
        pin36.low();  // ClkIn
        if (pin16.isHigh()) {// Rclk Error
            errRclk = true;
            bubba.backgroundColor = RED;}
        else {bubba.backgroundColor = Color.GREEN;}
        if (pin07.isLow()) {// Emitter Error
            errEmitter = errEmitter | emitter;
            bubba.backgroundColor = RED;}
        else {bubba.backgroundColor = Color.GREEN;}
        pin36.high(); // ClkIn t18
        pin11.low();  // LedOn
        if (pin16.isLow()) {// Rclk Error
            errRclk = true;
            bubba.backgroundColor = RED;}
        else {bubba.backgroundColor = Color.GREEN;}
        if (pin07.isHigh()) {errEmitter = errEmitter | emitter;}   // Emitter Error
        main.setBubble(main.getEmitterBubbleList(), emitter, bubba);
    }

    void teeShiftOutSequence(boolean sIn) // Set CPLD state machine to shift out data from the tee frame, including Sin. Test signals
    {
        bubba = null;
        int data; // Photo diode test pattern data masked for each LED position
        boolean state; // Pin state
        if (sIn == false) {pin10.low();}   // Sin
        else {pin10.high();} // Sin
        pin35.low();   // ModeIn t19
        if (pin40.isLow()) // LpClkOut Error
        {
            errLpClkOut = true;
            independentErrorList[1] = true;
        }
        if (pin15.isLow()) // Eripple Error
            {
            errEripple = true;
            independentErrorList[4] = true;
            }
        if (pin16.isLow()) // Rclk Error
        {
            errRclk = true;
            independentErrorList[5] = true;
        }
        if (pin08.isHigh()) // ShiftLoad Error
        {
            errShiftLoad = true;
            independentErrorList[6] = true;
        }
        pin35.high(); // ModeIn t20
        if (pin07.isHigh()) { errEmitter = 0;}  // Emitter Error
        for (int i = 0; i < 8; i++) // t21-t36// shift out photo diode data from the sensor board CPLD shift register, low byte
        {
            bubba = main.getSensorBubbleList()[i];
            pin36.low();  // ClkIn
            if (pin40.isHigh()) // LpClkOut Error
            {
                errLpClkOut = true;
                bubba.backgroundColor = RED;
            }
            else {bubba.backgroundColor = Color.GREEN;}
            pin36.high(); // ClkIn
            if (pin40.isLow()) // LpClkOut Error
            {
                errLpClkOut = true;
                bubba.backgroundColor = RED;
            }
            else {bubba.backgroundColor = Color.GREEN;}
            data = testByte & i ^ 2; // current test pattern masked // test for correct IR detection by photo diodes
            state = pin38.getState().isHigh();
            if (state != (data != 0)) {
                errDataOut = true;   // DataOut Error
                bubba.backgroundColor = RED;
            }
            else {bubba.backgroundColor = Color.GREEN;}
            main.setBubble(main.getSensorBubbleList(), i, bubba);
        }
        for (int i = 0; i < 8; i++) // t37-t54 // shift out photo diode data from the sensor board CPLD shift register, high byte
        {
            bubba = main.getSensorBubbleList()[i + 8];
            pin36.low();  // ClkIn
            if (pin40.isHigh()) // LpClkOut Error
            {
                errLpClkOut = true;
                bubba.backgroundColor = RED;
            }else {bubba.backgroundColor = Color.GREEN;}
            pin36.high(); // ClkIn
            if (pin40.isLow()) // LpClkOut Error
            {
                errLpClkOut = true;
                bubba.backgroundColor = RED;
            }
            else {bubba.backgroundColor = Color.GREEN;}
            data = testByte & i ^ 2; // current test pattern masked // test for correct IR detection by photo diodes
            state = pin38.getState().isHigh();
            if (state != (data != 0)) // DataOut Error
            {
                errDataOut = true;
                bubba.backgroundColor = RED;
            }
            else {bubba.backgroundColor = Color.GREEN;}
            pin36.low();  // ClkIn t55  // shift out Sin data
            if (pin40.isHigh()) // LpClkOut Error
            {
                errLpClkOut = true;
                bubba.backgroundColor = RED;
            }else { bubba.backgroundColor = Color.GREEN; }
            pin36.high(); // ClkIn t56
            if (pin40.isLow()) // LpClkOut Error
            {
                errLpClkOut = true;
                bubba.backgroundColor = RED;
            }
            else {bubba.backgroundColor = Color.GREEN;}
            state = pin38.getState().isHigh();
            if (state != sIn) // Sin Error
            {
                errSin = true;
                bubba.backgroundColor = RED;
            }else {bubba.backgroundColor = Color.GREEN;}
            main.setBubble(main.getSensorBubbleList(), i + 8, bubba);
        }
    }

    void screenShiftOutSequence()// Set CPLD state machine to shift out data from the screen frame. Test signals
    {
        pin35.low();  // ModeIn t19 // test the screen frame connector
        if (pin32.isHigh()) // ModeOut Error
        {
            errModeOut = true;
            independentErrorList[2] = true;
        }
        pin35.high(); // ModeIn t20
        if (pin32.isLow())  // ModeOut Error
        {
            errModeOut = true;
            independentErrorList[2] = true;
        }
        pin31.low();  // DataIn t21
        pin33.low();  // LpClkIn
        if (pin38.isHigh())
        {// DataOut Error
            errLpClkOut = true;
            independentErrorList[1] = true;
        }
        if (pin40.isHigh()) // LpClkOut Error
        {
            errLpClkOut = true;
            independentErrorList[1] = true;
        }
        pin33.high(); // ClkIn t22
        if (pin38.isHigh()) // DataOut Error
        {
            errLpClkOut = true;
            independentErrorList[1] = true;
        }
        if (pin40.isLow()) // LpClkOut Error
        {
            errLpClkOut = true;
            independentErrorList[1] = true;
        }
        pin31.high(); // DataIn t23
        pin33.low();  // LpClkIn
        if (pin38.isLow()) // DataOut Error
        {
            errLpClkOut = true;
            independentErrorList[1] = true;
        }
        if (pin40.isHigh()) // LpClkOut Error
        {
            errLpClkOut = true;
            independentErrorList[1] = true;
        }
        pin33.high(); // ClkIn t25
        if (pin38.isLow()) // DataOut Error
        {
            errLpClkOut = true;
            independentErrorList[1] = true;
        }
        if (pin40.isLow()) // LpClkOut Error
        {
            errLpClkOut = true;
            independentErrorList[1] = true;
        }
    }

    //  Selects one of four emitter positions for testing
    private void selectEmitterSequence(int emitter)
    {
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
    public void loadTestWordSequence(int testByte)
    {
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
        if (pin40.isLow())  // LpClkOut Error
        {
            errLpClkOut = true;
            independentErrorList[1] = true;
        }
        if (pin32.isLow()) // ModeOut Error
        {
            errModeOut = true;
            independentErrorList[2] = true;
        }
        if (pin29.isLow())  // ClkOut Error
        {
            errClkOut = true;
            independentErrorList[3] = true;
        }
        pin35.low();  // ModeIn t15
        if (pin32.isHigh()) // ModeOut Error
        {
            errModeOut = true;
            independentErrorList[2] = true;
        }
        pin35.high(); // ModeIn t16
        if (pin32.isLow()) // ModeOut Error
        {
            errModeOut = true;
            independentErrorList[2] = true;
        }
        pin36.low();  // ClkIn t17
        if (pin29.isHigh())  // ClkOut Error
        {
            errClkOut = true;
            independentErrorList[3] = true;
        }
        pin36.high(); // ClkIn t18
        if (pin29.isLow()) // ClkOut Error
        {
            errClkOut = true;
            independentErrorList[3] = true;
        }
    }

    public void setErrTestByteHigh(int errTestByteHigh) { this.errTestByteHigh = errTestByteHigh; }
    public void setErrTestByteLow(int errTestByteLow){this.errTestByteLow = errTestByteLow;}
    public void setErrFail(boolean errFail){this.errFail = errFail;}
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

    public int getErrEmitter()
    {
        return errEmitter;
    }

    public boolean[] getIndependentErrorList()
    {
        return independentErrorList;
    }

    public void setDisplayErrorList(int x, boolean trueFalse)
    {
        independentErrorList[x] = trueFalse;
    }

    public void setSensorErrorList(int l, boolean b)
    {
    }
}