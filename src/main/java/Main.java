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
    private String version = "400.25" + "";
    /**************************************************************************************/
    private GpioController gpio = GpioFactory.getInstance();
    private GpioPinDigitalInput pin38 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_20, "Raspi pin 38", PinPullResistance.PULL_UP);
    private GpioPinDigitalInput pin32 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_26, "Raspi pin 38", PinPullResistance.PULL_UP);
    private GpioPinDigitalInput pin29 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_21, "Raspi pin 38", PinPullResistance.PULL_UP);
    private GpioPinDigitalInput pin15 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_03, "Raspi pin 38", PinPullResistance.PULL_UP);
    private GpioPinDigitalInput pin16 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04, "Raspi pin 38", PinPullResistance.PULL_UP);
    private GpioPinDigitalInput pin08 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_15, "Raspi pin 38", PinPullResistance.PULL_UP);
    private GpioPinDigitalInput pin07 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_07, "Raspi pin 38", PinPullResistance.PULL_UP);
    private GpioPinDigitalOutput pin10 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_16, "RasPi pin 10", PinState.LOW);
    private GpioPinDigitalOutput pin11 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "RasPi pin 11", PinState.LOW);
    private GpioPinDigitalOutput pin31 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_22, "RasPi pin 31", PinState.LOW);
    private GpioPinDigitalOutput pin33 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_23, "RasPi pin 33", PinState.LOW);
    private GpioPinDigitalOutput pin35 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_24, "RasPi pin 35", PinState.LOW);
    private GpioPinDigitalOutput pin36 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_27, "RasPi pin 36", PinState.LOW);
    private GpioPinDigitalOutput[] pins = {
            pin10,
//            pin11,
//            pin31,
//            pin33,
//            pin35,
//            pin36,
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
    private GpioStepperMotorComponent motor = new GpioStepperMotorComponent(pins);
    private UserExperience ux;
    private JButton runButton;
    private JButton setButton;
    private byte[] blinkSequeneList = new byte[10];

    private Main()
    {
        for (int i = 0; i < blinkSequeneList.length; i++)
        {
            blinkSequeneList = new byte[]{blinkSequence[i]};
            String fromByteToString = String.format("%8s", Integer.toBinaryString(blinkSequeneList[i] & 0xFF)).replace(' ', '0');
            System.out.println(fromByteToString);
        }
        System.out.println(blinkSequeneList.length + " => blinkSequenceLength");
        ux = new UserExperience(version, motor, gpio);
        runButton = ux.getRunButton();
        runButton.addActionListener(this);
        setButton = ux.getSetButton();
        setButton.addActionListener(this);
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

    public void actionPerformed(ActionEvent e)
    {

        ux.repaint();
        if (e.getSource() == ux.getRunButton())
        {
            System.out.println("You pushed run, Starting test version " + version + ".");
            motor.setStepInterval(1);
            motor.setStepSequence(blinkSequence);
            for (int i = 0; i < 10000; i++)
            {
                motor.step(2);
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
    public void step(long l)
    {
    }

    @Override
    public MotorState getState()
    {
        return null;
    }

    @Override
    public void setState(MotorState motorState)
    {
    }
}

