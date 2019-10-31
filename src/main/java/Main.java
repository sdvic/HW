import com.pi4j.component.motor.MotorState;
import com.pi4j.component.motor.StepperMotorBase;
import com.pi4j.component.motor.impl.GpioStepperMotorComponent;
import com.pi4j.io.gpio.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends StepperMotorBase implements ActionListener
{
    /***************************************************************************************
     *      Full Swing Golf Strip Test
     *      copyright 2019 Vic Wintriss                                                    */
    private String version = "401.00" + "";
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
    private GpioPinDigitalOutput pin35 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_24, "RasPi pin 35", PinState.LOW); // ModeIn
    private GpioPinDigitalOutput pin36 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_27, "RasPi pin 36", PinState.LOW); // ClkIn
    private GpioPinDigitalOutput pin31 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_22, "RasPi pin 31", PinState.LOW); // DataIn
    private GpioPinDigitalOutput pin33 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23, "RasPi pin 33", PinState.LOW); // LpClkIn
    private GpioPinDigitalOutput pin10 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_16, "RasPi pin 10", PinState.LOW); // Sin
    private GpioPinDigitalOutput pin03 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_08, "RasPi pin 03", PinState.LOW); // Esel0
    private GpioPinDigitalOutput pin05 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_09, "RasPi pin 05", PinState.LOW); // Esel1
    private GpioPinDigitalOutput pin37 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_25, "RasPi pin 37", PinState.LOW); // LedClk
    private GpioPinDigitalOutput pin13 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "RasPi pin 13", PinState.LOW); // LedData
    private GpioPinDigitalOutput pin11 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "RasPi pin 11", PinState.LOW); // LedOn
    private GpioPinDigitalOutput[] pins = {
            pin05, // Esel1
            pin03, // Esel0
            pin11, // LedOn
            pin10, // Sin
            pin33, // LpClkIn
            pin31, // DataIn
            pin36, // ClkIn
            pin35  // ModeIn
    };
    private byte[] blinkSequence = //byte order (07)(23)(18)(16)(15)(13)(12)(11) physical pins
            {
                    (byte) 0b11111111,
                    (byte) 0b11111110,
                    (byte) 0b11111101,
                    (byte) 0b11111011,
                    (byte) 0b11110111,
                    (byte) 0b11101111,
                    (byte) 0b11011111,
                    (byte) 0b10111111,
                    (byte) 0b01111111,
                    (byte) 0b11111111
            };
    private byte[] blinkSequence2 =
            {
                    (byte) 0b00001000,
                    (byte) 0b01000000,
                    (byte) 0b11001000,
                    (byte) 0b10000000,
                    (byte) 0b11000000,
                    (byte) 0b11000000,
                    (byte) 0b11000000,
                    (byte) 0b01000000,
                    (byte) 0b11000000,
                    (byte) 0b10000000,
                    (byte) 0b11000000,
                    (byte) 0b11000000,
                    (byte) 0b11000000,
                    (byte) 0b01000000,
                    (byte) 0b11000000,
                    (byte) 0b10000100,
                    (byte) 0b11000000,
                    (byte) 0b01000000,
                    (byte) 0b11000000,
                    (byte) 0b10000000,
                    (byte) 0b11000000,
                    (byte) 0b10000000,
                    (byte) 0b11000000,
                    (byte) 0b10000000,
                    (byte) 0b11000000,
                    (byte) 0b10000000,
                    (byte) 0b11000000,
                    (byte) 0b10000000,
                    (byte) 0b11000000,
                    (byte) 0b10000000,
                    (byte) 0b11000000,
                    (byte) 0b10000000,
                    (byte) 0b11000000,
                    (byte) 0b10000000,
                    (byte) 0b11000000,
                    (byte) 0b10000000,
                    (byte) 0b11000000,
                    (byte) 0b10000000,
                    (byte) 0b11000000,
                    (byte) 0b10000000,
                    (byte) 0b11000000,
                    (byte) 0b10000000,
                    (byte) 0b11000000,
                    (byte) 0b10000000,
                    (byte) 0b11000000,
                    (byte) 0b10000000,
                    (byte) 0b11000000,
                    (byte) 0b10000000,
                    (byte) 0b11000000,
                    (byte) 0b10000000,
                    (byte) 0b11000000,
                    (byte) 0b10000000,
                    (byte) 0b11000000,
                    (byte) 0b10000000,
                    (byte) 0b00000000
            };
    private GpioStepperMotorComponent motor = new GpioStepperMotorComponent(pins);
    private UserExperience ux;
    private JButton runButton;
    private JButton setButton;

    private Main()
    {
        ux = new UserExperience(version, motor, gpio);
        runButton = ux.getRunButton();
        runButton.addActionListener(this);
        setButton = ux.getSetButton();
        setButton.addActionListener(this);
        ux.createGUI(version);
        new Timer(100, this).start();
    }

    synchronized public static void main(String[] args)
    {
        SwingUtilities.invokeLater(() -> { //Prevents graphics problems
            new Main();
            Thread.currentThread().setPriority(10);
        });
    }

    synchronized public void actionPerformed(ActionEvent e)
    {
        ux.repaint();
        if (e.getSource() == ux.getRunButton())
        {
            motor.setStepInterval(100); //Pulse width
            motor.setStepSequence(blinkSequence2);
            for (int i = 0; i < 10; i++)
            {
                motor.step(blinkSequence2.length);
            }
        }
        if (e.getSource() == setButton)
        {
            System.out.println("Reading pin states:");
            System.out.print("38" + pin38.getState() + " ");
            System.out.print("32" + pin32.getState() + " ");
            System.out.print("29" + pin29.getState() + " ");
            System.out.print("15" + pin15.getState() + " ");
            System.out.print("16" + pin16.getState() + " ");
            System.out.print("08" + pin08.getState() + " ");
            System.out.println("07" + pin07.getState());
        }
    }

    @Override
    synchronized public void step(long l)
    {
    }

    @Override
    synchronized public MotorState getState()
    {
        return null;
    }

    @Override
    synchronized public void setState(MotorState motorState)
    {
    }
}

