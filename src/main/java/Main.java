import com.pi4j.component.motor.MotorState;
import com.pi4j.component.motor.StepperMotorBase;
import com.pi4j.component.motor.impl.GpioStepperMotorComponent;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;

import javax.swing.*;

import static com.pi4j.io.gpio.RaspiBcmPin.GPIO_22;
import static com.pi4j.io.gpio.RaspiBcmPin.GPIO_23;

public class Main
{
    /**************************************************************************************
     *       Full Swing Golf Strip Test
     *       copyright 2019 Vic Wintriss
     */
    public String version = "102.6";
    /*      October 16, 2019 Flashing lights OK
     **************************************************************************************/
    private GpioController gpio = GpioFactory.getInstance();
    private GpioPinDigitalOutput pin33 = gpio.provisionDigitalOutputPin(GPIO_23, "RasPi pin 33", PinState.LOW);
    private GpioPinDigitalOutput pin31 = gpio.provisionDigitalOutputPin(GPIO_22, "RasPi pin 31", PinState.LOW);
    private GpioPinDigitalOutput[] pins = {
            pin31,
            pin33
    };
    private GpioStepperMotorComponent motor = new GpioStepperMotorComponent(pins);
    private Stepper step = new Stepper();
    private PinState[] streamA = {
            PinState.LOW,
            PinState.LOW,
            PinState.LOW,
            PinState.LOW,
            PinState.LOW,
            PinState.LOW,
            PinState.HIGH,
            PinState.HIGH,
            PinState.HIGH,
            PinState.HIGH,
            PinState.LOW
    };
    private UserExperience UX = new UserExperience();
    private Timer paintTicker = new Timer(50, UX);
    private Stepper stepper = new Stepper();

    public static void main(String[] args) throws Exception
    {
        new Main().getGoing();// Get out of static context
    }

    public void getGoing() throws Exception
    {
        paintTicker.start();
        UX.createGUI(version);
        stepper.getStepperGoing();
    }

    class Stepper extends StepperMotorBase
    {
        byte[] blinkSequence = new byte[4];

        public void getStepperGoing() throws Exception
        {
            System.out.println("Starting Stepper");
            while (true)
            {
                motor.setStepInterval(100);
                motor.setStepSequence(blinkSequence);
                motor.step(2);
                blinkSequence[0] = (byte) 0b0000;
                blinkSequence[1] = (byte) 0b0000;
                Thread.sleep(2000);
                blinkSequence[2] = (byte) 0b1111;
                blinkSequence[3] = (byte) 0b1111;
                Thread.sleep(2000);
                System.out.println(".");
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
}

