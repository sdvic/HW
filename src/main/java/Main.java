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

public class Main extends StepperMotorBase
{
    /**************************************************************************************
     *      Full Swing Golf Strip Test
     *      copyright 2019 Vic Wintriss
     */     public String version = "102.26";
    /*      October 17, 2019 Alternate Flashing lights OK, refactor
     **************************************************************************************/
    private GpioController gpio = GpioFactory.getInstance();
    private GpioPinDigitalOutput pin33 = gpio.provisionDigitalOutputPin(GPIO_23, "RasPi pin 33", PinState.LOW);
    private GpioPinDigitalOutput pin31 = gpio.provisionDigitalOutputPin(GPIO_22, "RasPi pin 31", PinState.LOW);
    private GpioPinDigitalOutput[] pins = {
            pin31,
            pin33
    };

    byte[] blinkSequence = {/* byte order (x)(x)(x)(x)(x)(x)(33)(31) */
            (byte) 0b00000001,
            (byte) 0b00000010
    };
    private GpioStepperMotorComponent motor = new GpioStepperMotorComponent(pins);
    private UserExperience UX = new UserExperience();
    private Timer paintTicker = new Timer(100, UX);

    public static void main(String[] args) throws Exception
    {
        new Main().getGoing();// Get out of static context
    }

    public void getGoing() throws Exception
    {
        UX.createGUI(version);
        paintTicker.start();
        System.out.println("Starting Stepper");
        motor.setStepInterval(1000);
        motor.setStepSequence(blinkSequence);
        motor.step(100);
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

