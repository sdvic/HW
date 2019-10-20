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
import static com.pi4j.io.gpio.RaspiBcmPin.GPIO_06;
import static com.pi4j.io.gpio.RaspiBcmPin.GPIO_27;
//import static com.pi4j.io.gpio.RaspiBcmPin.GPIO_00;
//import static com.pi4j.io.gpio.RaspiBcmPin.GPIO_23;
//import static com.pi4j.io.gpio.RaspiBcmPin.GPIO_23;
//import static com.pi4j.io.gpio.RaspiBcmPin.GPIO_23;
// import static com.pi4j.io.gpio.RaspiBcmPin.GPIO_23;
//import static com.pi4j.io.gpio.RaspiBcmPin.GPIO_23;
//import static com.pi4j.io.gpio.RaspiBcmPin.GPIO_23;
//import static com.pi4j.io.gpio.RaspiBcmPin.GPIO_23;
public class Main extends StepperMotorBase implements Runnable
{
    /**************************************************************************************
     *      Full Swing Golf Strip Test
     *      copyright 2019 Vic Wintriss
     */
    public String version = "102.47";
    byte[] blinkSequence = {/* byte order (x)(x)(x)(x)(x)(36)(33)(31) */
                            (byte) 0b00000000,
                            (byte) 0b11111111
    };
    /*      October 19, 2019 Alternate Flashing lights OK, three GPIO pins working
     **************************************************************************************/
    private GpioController gpio = GpioFactory.getInstance();
    private GpioPinDigitalOutput pin33 = gpio.provisionDigitalOutputPin(GPIO_23, "RasPi pin 33", PinState.LOW);
    private GpioPinDigitalOutput pin31 = gpio.provisionDigitalOutputPin(GPIO_22, "RasPi pin 31", PinState.HIGH);
    private GpioPinDigitalOutput pin36 = gpio.provisionDigitalOutputPin(GPIO_27, "RasPi pin 36", PinState.LOW);
    //private GpioPinDigitalOutput pin22 = gpio.provisionDigitalOutputPin(GPIO_06, "RasPi pin 22", PinState.LOW);
//    private GpioPinDigitalOutput pin31 = gpio.provisionDigitalOutputPin(GPIO_06, "RasPi pin 31", PinState.LOW);
//    private GpioPinDigitalOutput pin31 = gpio.provisionDigitalOutputPin(GPIO_13, "RasPi pin 31", PinState.LOW);
//    private GpioPinDigitalOutput pin31 = gpio.provisionDigitalOutputPin(GPIO_15, "RasPi pin 31", PinState.LOW);
//    private GpioPinDigitalOutput pin31 = gpio.provisionDigitalOutputPin(GPIO_17, "RasPi pin 31", PinState.LOW);
//    private GpioPinDigitalOutput pin36 = gpio.provisionDigitalOutputPin(GPIO_27, "RasPi pin 36", PinState.LOW);
    private GpioPinDigitalOutput[] pins = {
            pin33,
            pin31,
            pin36
            //pin22
    };
    private GpioStepperMotorComponent motor = new GpioStepperMotorComponent(pins);
    private UserExperience UX = new UserExperience();
    private Timer paintTicker = new Timer(100, UX);

    public static void main(String[] args) throws Exception
    {
        SwingUtilities.invokeLater(new Main());//To prevent interference with Swing graphics
    }

    @Override
    public void run()
    {
        UX.createGUI(version);
        paintTicker.start();
        System.out.println("Starting Stepper");
        motor.setStepInterval(100);
        motor.setStepSequence(blinkSequence);
        motor.step(10000);
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

