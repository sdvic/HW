import com.pi4j.io.gpio.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.awt.Toolkit.getDefaultToolkit;

public class UserExperience extends JComponent implements ActionListener
{
    private GpioController gpio = GpioFactory.getInstance();
    private Main main;
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
    private int screenWidth = getDefaultToolkit().getScreenSize().width;
    private JButton allButton = new JButton("ALL");
    private JButton teeButton = new JButton("TEE");
    private JButton screenButton = new JButton("SCREEN");
    private JButton sensorsButton = new JButton("SENSORS");
    private JButton commButton = new JButton("COMM");
    private JButton runButton = new JButton("RUN");
    private JButton longFull = new JButton("LONG FULL");
    private JButton long34 = new JButton("LONG 3/4");
    private JButton long12 = new JButton("LONG 1/2");
    private JButton long14 = new JButton("LONG 1/4");
    private JTextField passTextField = new JTextField("PASS");
    private JTextField failTextField = new JTextField("FAIL");
    private JTextField errorCodeDisplayField = new JTextField();
    private JFrame display = new JFrame();
    private int leftMargin = 40;
    private int middleMargin = 250;
    private Font buttonFont = new Font("SansSerif", Font.PLAIN, 21);
    private Font resultFont = new Font("SansSerif", Font.BOLD, 28);
    private Font indicatorFont = new Font("Arial", Font.PLAIN, 17);
    private Graphics g;
    private String version;
    private boolean isCommBoardFlag = false;
    private boolean isLongBoardFlag = false;
    private boolean errDataOut = false;  // flags for errors
    private boolean errLpClkOut = false;
    private boolean errModeOut = false;
    private boolean errClkOut = false;
    private boolean errEripple = false;
    private boolean errRck = false;
    private boolean errShiftLoad = false;
    private boolean errSin = false;
    private int errEmitter = 0;      // byte used for emitter errors
    private int errTestByteHigh = 0; // byte used for sensors errors, top 8 bits
    private int errTestByteLow = 0;  // byte used for sensors errors, bottom 8 bits
    private int testByte = 0;        // byte used for testing sensors, top and bottom 8 bits
    private boolean sIn = false;     // bit used for testing Sin from Long Board

    public UserExperience(String version)
    {
        this.version = version;
    }

    public void createGUI(String version)
    {
        display.setSize(getDefaultToolkit().getScreenSize().width, getDefaultToolkit().getScreenSize().height);
        display.add(this);//Adds Graphics
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.setVisible(true);

        allButton.setBounds(leftMargin, 108, 150, 34); // ALL Button
        allButton.setHorizontalAlignment(SwingConstants.CENTER);
        allButton.setFont(buttonFont);
        allButton.addActionListener(this);
        display.add(allButton);

        teeButton.setBounds(leftMargin, 153, 150, 34); // TEE button
        teeButton.setHorizontalAlignment(SwingConstants.CENTER);
        teeButton.setFont(buttonFont);
        teeButton.addActionListener(this);
        display.add(teeButton);

        screenButton.setBounds(leftMargin, 198, 150, 34); // SCREEN button
        screenButton.setHorizontalAlignment(SwingConstants.CENTER);
        screenButton.setFont(buttonFont);
        screenButton.addActionListener(this);
        display.add(screenButton);

        sensorsButton.setBounds(leftMargin, 243, 150, 34); // SENSORS button
        sensorsButton.setHorizontalAlignment(SwingConstants.CENTER);
        sensorsButton.setFont(buttonFont);
        sensorsButton.addActionListener(this);
        display.add(sensorsButton);

        runButton.setBounds(500, 345, 100, 58); // RUN button
        runButton.setHorizontalAlignment(SwingConstants.CENTER);
        runButton.setFont(buttonFont);
        runButton.addActionListener(this);
        display.add(runButton);

        passTextField.setBounds(500, 125, 120, 50); // PASS indicator
        passTextField.setHorizontalAlignment(SwingConstants.CENTER);
        passTextField.setFont(resultFont);
        display.add(passTextField);

        failTextField.setBounds(500, 210, 120, 50); // FAIL indicator
        failTextField.setHorizontalAlignment(SwingConstants.CENTER);
        failTextField.setFont(resultFont);
        display.add(failTextField);

        errorCodeDisplayField.setBounds(0, 289, screenWidth, 44);
        display.add(errorCodeDisplayField);

        if (isCommBoardFlag)
        {
            System.out.println("comm board setup");
            commButton.setBounds(middleMargin, 108, 150, 34); // COMM button
            commButton.setHorizontalAlignment(SwingConstants.CENTER);
            commButton.setFont(buttonFont);
            commButton.addActionListener(this);
            display.add(commButton);
        }
        else if (isLongBoardFlag)
        {
            System.out.println("long board setup");
            longFull.setBounds(middleMargin, 108, 150, 34); // Long Full button
            longFull.setHorizontalAlignment(SwingConstants.CENTER);
            longFull.setFont(buttonFont);
            longFull.addActionListener(this);
            display.add(longFull);

            long34.setBounds(middleMargin, 153, 150, 34); // Long 3/4 button
            long34.setHorizontalAlignment(SwingConstants.CENTER);
            long34.setFont(buttonFont);
            long34.addActionListener(this);
            display.add(long34);

            long12.setBounds(middleMargin, 198, 150, 34); // Long 3/4 button
            long12.setHorizontalAlignment(SwingConstants.CENTER);
            long12.setFont(buttonFont);
            long12.addActionListener(this);
            display.add(long12);

            long14.setBounds(middleMargin, 243, 150, 34); // Long 3/4 button
            long14.setHorizontalAlignment(SwingConstants.CENTER);
            long14.setFont(buttonFont);
            long14.addActionListener(this);
            display.add(long14);
        }
        display.setSize(getDefaultToolkit().getScreenSize().width, getDefaultToolkit().getScreenSize().height);
        display.add(this);
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.getContentPane().setBackground(new Color(160, 160, 160));
        display.setTitle("FSG StripTest ver " + version);
        display.setVisible(true);
    }

    private void resetErrors()
    {
        errDataOut = false;
        errLpClkOut = false;
        errModeOut = false;
        errClkOut = false;
        errEripple = false;
        errRck = false;
        errShiftLoad = false;
        errSin = false;
        errEmitter = 0;
        errTestByteHigh = 0;
        errTestByteLow = 0;
    }

    // Set CPLD state machine to the RESET state
    private void resetSequence()
    {
        pin35.low();  // ModeIn t1
        pin36.low();  // ClkIn t2
        pin31.low();  // DataIn
        pin33.high(); // LpClkIn
        pin10.low();  // Sin
        pin11.low();  // LedOn
    }

    // Set CPLD state machine to the tee frame state. Test signals
    private void teeSequence()
    {
        pin36.high(); // ClkIn t3
        if (pin40.isLow())
        {
            errLpClkOut = true;   // LpClkOut
        }
        if (pin15.isLow())
        {
            errEripple = true;    // Eripple
        }
        if (pin16.isLow())
        {
            errRck = true;        // Rclk
        }
        if (pin08.isLow())
        {
            errShiftLoad = true;  // ShiftLoad
        }
        pin35.high(); // ModeIn t4
        pin36.low();  // ClkIn t5
        pin36.high(); // ClkIn t6
        pin36.high(); // ClkIn t7
        pin36.high(); // ClkIn t8
    }

    // Set CPLD state machine to the screen frame state. Test signals
    private void screenSequence()
    {
        pin36.high(); // ClkIn
        if (pin40.isLow())
        {
            errLpClkOut = true;   // LpClkOut
        }
        if (pin15.isLow())
        {
            errEripple = true;    // Eripple
        }
        if (pin16.isLow())
        {
            errRck = true;        // Rclk
        }
        if (pin08.isLow())
        {
            errShiftLoad = true;  // ShiftLoad
        }
        pin35.high(); // ModeIn
        pin36.low();  // ClkIn
        pin36.high(); // ClkIn
        pin36.low();  // ClkIn
        pin36.high(); // ClkIn
    }

    // Set CPLD state machine to select on-board emitter. Test signals
    private void emitterSelSequence()
    {
        pin35.low();  // ModeIn t9
        if (pin40.isLow())
        {
            errLpClkOut = true;   // LpClkOut Error
        }
        if (pin15.isLow())
        {
            errEripple = true;    // Eripple Error
        }
        if (pin16.isLow())
        {
            errRck = true;        // Rclk Error
        }
        if (pin08.isLow())
        {
            errShiftLoad = true;  // ShiftLoad Error
        }
        pin35.high(); // ModeIn t10
        if (pin40.isLow())
        {
            errLpClkOut = true;   // LpClkOut Error
        }
        if (pin15.isLow())
        {
            errEripple = true;    // Eripple Error
        }
        if (pin16.isLow())
        {
            errRck = true;        // Rclk Error
        }
        if (pin08.isHigh())
        {
            errShiftLoad = true; // ShiftLoad Error
        }
        pin36.low();  // ClkIn t11
        if (pin15.isLow())
        {
            errEripple = true;    // Eripple Error
        }
        pin36.high(); // ClkIn t12
        pin36.high(); // ClkIn t13
        if (pin15.isLow())
        {
            errEripple = true;    // Eripple Error
        }
        pin36.high(); // ClkIn t14
    }

    // Set CPLD state machine to select next board emitter. Test signals
    private void emitterDeselSequence()
    {
        pin35.low();  // ModeIn t9
        if (pin40.isLow())
        {
            errLpClkOut = true;   // LpClkOut Error
        }
        if (pin15.isLow())
        {
            errEripple = true;    // Eripple Error
        }
        if (pin16.isLow())
        {
            errRck = true;        // Rclk Error
        }
        if (pin08.isLow())
        {
            errShiftLoad = true;  // ShiftLoad Error
        }
        pin35.high(); // ModeIn t10
        if (pin40.isLow())
        {
            errLpClkOut = true;   // LpClkOut Error
        }
        if (pin15.isLow())
        {
            errEripple = true;    // Eripple Error
        }
        if (pin16.isLow())
        {
            errRck = true;        // Rclk Error
        }
        if (pin08.isHigh())
        {
            errShiftLoad = true; // ShiftLoad Error
        }
        pin36.low();  // ClkIn t11
        if (pin15.isLow())
        {
            errEripple = true;    // Eripple Error
        }
        pin36.high(); // ClkIn t12
        pin36.low();  // ClkIn t13
        if (pin15.isHigh())
        {
            errEripple = true;   // Eripple Error
        }
        pin36.high(); // ClkIn t14
    }

    // Set CPLD state machine to set emitter position, fire emitter. Test signals
    private void emitterFireSequence(int emitter)
    {
        selectEmitter(emitter);
        pin35.low();  // ModeIn t15
        if (pin40.isLow())
        {
            errLpClkOut = true;   // LpClkOut Error
        }
        if (pin15.isLow())
        {
            errEripple = true;    // Eripple Error
        }
        if (pin16.isLow())
        {
            errRck = true;        // Rclk Error
        }
        if (pin08.isHigh())
        {
            errShiftLoad = true; // ShiftLoad Error
        }
        pin35.high(); // ModeIn t16
        if (pin07.isHigh())
        {
            errEmitter = errEmitter | emitter;   // Emitter Error
        }
        pin11.high(); // LedOn t17
        pin36.low();  // ClkIn
        if (pin16.isHigh())
        {
            errRck = true;       // Rclk Error
        }
        if (pin07.isLow())
        {
            errEmitter = errEmitter | emitter;   // Emitter Error
        }
        pin36.high(); // ClkIn t18
        pin11.low();  // LedOn
        if (pin16.isLow())
        {
            errRck = true;        // Rclk Error
        }
        if (pin07.isHigh())
        {
            errEmitter = errEmitter | emitter;   // Emitter Error
        }
    }

    // Set CPLD state machine to shift out data from the tee frame, including Sin. Test signals
    private void teeShiftOutSequence(boolean sIn)
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
        }
        if (pin15.isLow())
        {
            errEripple = true;    // Eripple Error
        }
        if (pin16.isLow())
        {
            errRck = true;        // Rclk Error
        }
        if (pin08.isHigh())
        {
            errShiftLoad = true; // ShiftLoad Error
        }
        pin35.high(); // ModeIn t20
        if (pin07.isHigh())
        {
            errEmitter = 0;   // Emitter Error
        }

        // shift out photo diode data from the sensor board CPLD shift register, low byte
        for (int i = 0; i < 8; i++) // t21-t36
        {
            pin36.low();  // ClkIn
            if (pin40.isHigh())
            {
                errLpClkOut = true;  // LpClkOut Error
            }
            pin36.high(); // ClkIn
            if (pin40.isLow())
            {
                errLpClkOut = true;   // LpClkOut Error
            }

            // test for correct IR detection by photo diodes
            data = testByte & i ^ 2; // current test pattern masked
            state = pin38.getState().isHigh();
            if (state != (data != 0))
            {
                errDataOut = true;   // DataOut Error
            }
        }
        // shift out photo diode data from the sensor board CPLD shift register, high byte
        for (int i = 0; i < 8; i++) // t37-t54
        {
            pin36.low();  // ClkIn
            if (pin40.isHigh())
            {
                errLpClkOut = true;  // LpClkOut Error
            }
            pin36.high(); // ClkIn
            if (pin40.isLow())
            {
                errLpClkOut = true;   // LpClkOut Error
            }

            // test for correct IR detection by photo diodes
            data = testByte & i ^ 2; // current test pattern masked
            state = pin38.getState().isHigh();
            if (state != (data != 0))
            {
                errDataOut = true;   // DataOut Error
            }
        }
        // shift out Sin data
        pin36.low();  // ClkIn t55
        if (pin40.isHigh())
        {
            errLpClkOut = true;  // LpClkOut Error
        }
        pin36.high(); // ClkIn t56
        if (pin40.isLow())
        {
            errLpClkOut = true;   // LpClkOut Error
        }
        state = pin38.getState().isHigh();
        if (state != sIn)
        {
            errSin = true;   // Sin Error
        }
    }

    // Set CPLD state machine to shift out data from the screen frame. Test signals
    private void screenShiftOutSequence()
    {
        // test the screen frame connector
        pin35.low();  // ModeIn t19
        if (pin32.isHigh())
        {
            errModeOut = true;   // ModeOut Error
        }
        pin35.high(); // ModeIn t20
        if (pin32.isLow())
        {
            errModeOut = true;    // ModeOut Error
        }
        pin31.low();  // DataIn t21
        pin33.low();  // LpClkIn
        if (pin38.isHigh())
        {
            errLpClkOut = true;  // DataOut Error
        }
        if (pin40.isHigh())
        {
            errLpClkOut = true;  // LpClkOut Error
        }
        pin33.high(); // ClkIn t22
        if (pin38.isHigh())
        {
            errLpClkOut = true;  // DataOut Error
        }
        if (pin40.isLow())
        {
            errLpClkOut = true;   // LpClkOut Error
        }
        pin31.high(); // DataIn t23
        pin33.low();  // LpClkIn
        if (pin38.isLow())
        {
            errLpClkOut = true;   // DataOut Error
        }
        if (pin40.isHigh())
        {
            errLpClkOut = true;  // LpClkOut Error
        }
        pin33.high(); // ClkIn t25
        if (pin38.isLow())
        {
            errLpClkOut = true;   // DataOut Error
        }
        if (pin40.isLow())
        {
            errLpClkOut = true;   // LpClkOut Error
        }
    }

    //  Selects one of four emitter positions for testing
    private void selectEmitter(int emitter)
    {
        switch (emitter)
        {
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
    private void loadTestWord()
    {
        boolean state = false;
        int led; // LED position to be loaded into shift register
        for (int i = 0; i < 8; i++) // Load test pattern, msb D8 ... lsb D1
        {
            led = 128;
            led = led >> i;
            led = testByte & led; // current byte test pattern masked
            state = !(led == 0);
            pin37.low();           // LedClk
            pin13.setState(state); // LedData
            pin37.high();          // LedClk
        }
        pin13.low();  // LedData Leave low after done
        pin37.low();  // LedClk Leave low after done
    }

    // Set CPLD state machine to the tee frame and test all the emitters
    private void testTee()
    {
        resetErrors();
        for (int i = 1; i < 5; i++)
        {
            resetSequence();        // t1-t2
            teeSequence();          // t3-t8
            emitterSelSequence();   // t9-t14
            emitterFireSequence(i); // t15-t18
            resetSequence();        // t1-t2
        }
    }

    // Set CPLD state machine to the screen frame and test the interconnection signals
    private void testScreen()
    {
        resetSequence();      // t1-t2
        screenSequence();     // t3-t8
        emitterSelSequence(); // t9-t14
        resetErrors(); // reset errors at end of t14, bit bang the emitter fire sequence
        if (pin40.isLow())
        {
            errLpClkOut = true;   // LpClkOut Error
        }
        if (pin32.isLow())
        {
            errModeOut = true;    // ModeOut Error
        }
        if (pin29.isLow())
        {
            errClkOut = true;     // ClkOut Error
        }
        pin35.low();  // ModeIn t15
        if (pin32.isHigh())
        {
            errModeOut = true;   // ModeOut Error
        }
        pin35.high(); // ModeIn t16
        if (pin32.isLow())
        {
            errModeOut = true;    // ModeOut Error
        }
        pin36.low();  // ClkIn t17
        if (pin29.isHigh())
        {
            errClkOut = true;    // ClkOut Error
        }
        pin36.high(); // ClkIn t18
        if (pin29.isLow())
        {
            errClkOut = true;     // ClkOut Error
        }
        screenShiftOutSequence();
        resetSequence();
    }

    // Test each individual IR photodiode for correct operation
    private void testSensors()
    {
        resetErrors();
        for (int i = 0; i < 8; i++) // walking 1 test pattern
        {
            resetSequence();      // t1-t2
            teeSequence();        // t3-t8
            emitterSelSequence(); // t9-t14
            testByte = 128;
            testByte = testByte >> i;
            loadTestWord();
            emitterFireSequence(0);          // t15-t18
            teeShiftOutSequence(false); // t19-t54
            resetSequence();                // t55-t56
        }
        for (int i = 0; i < 8; i++) // walking 0 test pattern
        {
            resetSequence();      // t1-t2
            teeSequence();        // t3-t8
            emitterSelSequence(); // t9-t14
            testByte = 128;
            testByte = testByte >> i;
            testByte = ~testByte;
            loadTestWord();
            emitterFireSequence(0);          // t15-t18
            teeShiftOutSequence(false); // t19-t54
            resetSequence();                // t55-t56
        }
    }

    // Test the majority of the Comm Board functionality. Uses testByteHigh, testByteLow, emitter, Sin
    private void testBasic()
    {
        resetErrors();
        loadTestWord();
        // Test in tee frame mode with on-board emitter
        resetSequence();
        teeSequence();
        emitterSelSequence();
        emitterFireSequence(0);
        teeShiftOutSequence(false);
        // Test in tee frame mode with next board emitter
        resetSequence();
        teeSequence();
        emitterDeselSequence();
        emitterFireSequence(1);
        teeShiftOutSequence(true);
        // Test the screen frame connections
        resetSequence();
        screenSequence();
        emitterSelSequence();
        emitterFireSequence(2);
        screenShiftOutSequence();
        // End of testing
        resetSequence();
    }


    public void paint(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(indicatorFont);
        for (int i = 0; i < 16; i++)
        {
            g2.setColor(new Color(255, 255, 255));
            g2.fillOval((42 * i + 26), 10, 32, 32);
            g2.setColor(Color.BLACK);
            g2.drawString((i + 1) + "", (42 * i + 32), 32);
        }
        g2.setStroke(new BasicStroke(.4f));
        g2.setColor(Color.BLACK);
        g2.drawLine(0, 52, (screenWidth), 52);
        g2.setColor(new Color(153, 255, 255));
        g2.setColor(Color.BLACK);
        g2.drawLine(0, 97, (screenWidth), 97);
        for (int i = 0; i < 4; i++)
        {
            g2.setColor(new Color(255, 255, 255));
            g2.fillOval((120 * i + 180), 59, 30, 30);
            g2.setColor(new Color(0, 0, 0));
            g2.drawOval((120 * i + 180), 59, 30, 30);
            g2.drawString((i + 1) + "", (120 * i + 190), 80);
        }
        g2.setFont(buttonFont);
        g2.drawString("EMITTERS", leftMargin, 84);
        g2.drawLine(0, 288, (screenWidth), 288);
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 289, (screenWidth), 44);
        g2.setColor(Color.BLACK);
        g2.drawLine(0, 333, (screenWidth), 333);
    }

    public void actionPerformed(ActionEvent e)
    {
        repaint();
        if (errDataOut)
        {
            failTextField.setBackground(Color.red);
            errorCodeDisplayField.setText("errDataOut");
        }
        if (e.getSource() == runButton)
        {
            System.out.println("run button test");
            try
            {
                Thread.sleep(100);                 // 1000 milliseconds is one second.
            }
            catch (InterruptedException ex)
            {
                Thread.currentThread().interrupt();
            }
        }
        if (e.getSource() == allButton)
        {
            System.out.println("all button test");
            testBasic();
            errPrintOut();
        }
        if (e.getSource() == teeButton)
        {
            System.out.println("tee button test");
            testTee();
            errPrintOut();
        }
        if (e.getSource() == screenButton)
        {
            System.out.println("screen button test");
            testScreen();
            errPrintOut();
        }
        if (e.getSource() == sensorsButton)
        {
            System.out.println("sensors button test");
            testSensors();
            errPrintOut();
        }
    }

    private void errPrintOut()
    {
        System.out.println("errDataOut errLpClkOutut errModeOut errClkOut errEripple errRck errShiftLoad errSin");
        System.out.println(errDataOut + "     " + errLpClkOut + "     " + errModeOut + "     " + errClkOut + "     " + errEripple + "     " + errRck + "     " + errShiftLoad + "     " + errSin);
    }

    public void setCommFlag(boolean isCommBoardFlag)
    {
        this.isCommBoardFlag = isCommBoardFlag;
    }

    public void setLongFlag(boolean isLongBoardFlag)
    {
        this.isLongBoardFlag = isLongBoardFlag;
    }
}


