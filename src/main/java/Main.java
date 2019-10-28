import com.pi4j.component.motor.MotorState;
import com.pi4j.component.motor.StepperMotorBase;
import com.pi4j.component.motor.impl.GpioStepperMotorComponent;
import com.pi4j.io.gpio.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.pi4j.io.gpio.RaspiPin.*;

public class Main extends StepperMotorBase implements ActionListener
{
    /***************************************************************************************
     *      Full Swing Golf Strip Test
     *      copyright 2019 Vic Wintriss                                                    */
            String version = "400.0";
     /**************************************************************************************/
    private GpioController gpio = GpioFactory.getInstance();
    private GpioPinDigitalOutput pin10 = gpio.provisionDigitalOutputPin(GPIO_16, "RasPi pin 10", PinState.LOW);
    private GpioPinDigitalOutput pin11 = gpio.provisionDigitalOutputPin(GPIO_00, "RasPi pin 11", PinState.LOW);
    private GpioPinDigitalOutput pin31 = gpio.provisionDigitalOutputPin(GPIO_22, "RasPi pin 31", PinState.LOW);
    private GpioPinDigitalOutput pin33 = gpio.provisionDigitalOutputPin(GPIO_23, "RasPi pin 33", PinState.LOW);
    private GpioPinDigitalOutput pin35 = gpio.provisionDigitalOutputPin(GPIO_24, "RasPi pin 35", PinState.LOW);
    private GpioPinDigitalOutput pin36 = gpio.provisionDigitalOutputPin(GPIO_27, "RasPi pin 36", PinState.LOW);
    private GpioPinDigitalOutput[] pins = {
            pin10,
            pin11,
            pin31,
            pin33,
            pin35,
            pin36,
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

    public static void main(String[] args) throws Exception
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
        ux.repaint();
        if (e.getSource() == ux.getRunButton())
        {
            System.out.println("You pushed run, Starting test version " + version + ".");
            motor.setStepInterval(2000);
            motor.setStepSequence(blinkSequence);
            motor.step(10);
        }
        if (e.getSource() == setButton)
        {
            System.out.println("You pushed the set button!");
//            PinState pin38State = pin38.getState();
//            System.out.println("you pushed set and set pin38 " + pin38State);
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

