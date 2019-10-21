import com.pi4j.component.motor.MotorState;
import com.pi4j.component.motor.StepperMotorBase;
import com.pi4j.component.motor.impl.GpioStepperMotorComponent;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;

import javax.swing.*;

import static com.pi4j.io.gpio.RaspiBcmPin.*;

public class Main extends StepperMotorBase implements Runnable
{
    /**************************************************************************************
     *      Full Swing Golf Strip Test
     *      copyright 2019 Vic Wintriss
     */
    public String version = "102.51";

    /*
    * Used in this program
    * WiringPi GPIO         RaspberryPi Physical Pin    BroadCom GPIO (* on test board)
    *   0                       11                              17*
    *   1                       12                              18
    *   2                       13                              27
    *   3                       15                              22*
    *   4                       16                              23*
    *   5                       18                              24
    *   6                       22                              25
    *   7                        7                               4
    *   8                        3                               2
    *   9                        5                               3
    *   10                      24                               8
    *   11                      26                               7
    *   12                      19                              10
    *   13                      21                               9
    *   14                      23                              11
    *   15                       8                              14*
    *   16                      10      Below are on P5         15*
    *   17                                  51
    *   18                                  52
    *   19                                  53
    *   20                                  54
    *   21                      29                               5*
    *   22                      31                               6*
    *   23                      33                              13*
    *   24                      35                              19*
    *   25                      37                              26
    *   26                      32                              12*
    *   27                      36                              16*
    *   28                      38                              20*
    *   29                      40                              21*
    *   30                      27                               0
    *   31                      28                               1
     **************************************************************************************/
    private byte[] blinkSequence = //byte order (x)(x)(x)(x)(5)(36)(31)(33)
            {
                    (byte) 0b00000000,
                    (byte) 0b11111111
            };
    private GpioController gpio = GpioFactory.getInstance();
    private GpioPinDigitalOutput pin33 = gpio.provisionDigitalOutputPin(GPIO_23, "RasPi physical pin 33", PinState.LOW);
    private GpioPinDigitalOutput pin31 = gpio.provisionDigitalOutputPin(GPIO_22, "RasPi physical pin 31", PinState.HIGH);
    private GpioPinDigitalOutput pin36 = gpio.provisionDigitalOutputPin(GPIO_27, "RasPi physical pin 36", PinState.LOW);
    private GpioPinDigitalOutput pin5 = gpio.provisionDigitalOutputPin(GPIO_09, "RasPi physical pin 5", PinState.LOW);
    //    private GpioPinDigitalOutput pin31 = gpio.provisionDigitalOutputPin(GPIO_06, "RasPi pin 31", PinState.LOW);
//    private GpioPinDigitalOutput pin31 = gpio.provisionDigitalOutputPin(GPIO_13, "RasPi pin 31", PinState.LOW);
//    private GpioPinDigitalOutput pin31 = gpio.provisionDigitalOutputPin(GPIO_15, "RasPi pin 31", PinState.LOW);
//    private GpioPinDigitalOutput pin31 = gpio.provisionDigitalOutputPin(GPIO_17, "RasPi pin 31", PinState.LOW);
//    private GpioPinDigitalOutput pin36 = gpio.provisionDigitalOutputPin(GPIO_27, "RasPi pin 36", PinState.LOW);
    private GpioPinDigitalOutput[] pins = {
            pin33,
            pin31,
            pin36,
            pin5
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

