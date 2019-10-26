import com.pi4j.component.motor.MotorState;
import com.pi4j.component.motor.StepperMotorBase;
import com.pi4j.component.motor.impl.GpioStepperMotorComponent;
import com.pi4j.io.gpio.*;
import javax.swing.*;
import static com.pi4j.io.gpio.RaspiPin.*;

public class Main extends StepperMotorBase
{
    /**************************************************************************************
     *      Full Swing Golf Strip Test
     *      copyright 2019 Vic Wintriss
     */
    public String version = "200.3";

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

    private GpioController gpio = GpioFactory.getInstance();
    private GpioPinDigitalOutput pin11 = gpio.provisionDigitalOutputPin(GPIO_00, "RasPi pin 11", PinState.LOW);
    private GpioPinDigitalOutput pin12 = gpio.provisionDigitalOutputPin(GPIO_01, "RasPi pin 12", PinState.LOW);
    private GpioPinDigitalOutput pin13 = gpio.provisionDigitalOutputPin(GPIO_02, "RasPi pin 13", PinState.LOW);
    private GpioPinDigitalOutput pin15 = gpio.provisionDigitalOutputPin(GPIO_03, "RasPi pin 15", PinState.LOW);
    private GpioPinDigitalOutput pin16 = gpio.provisionDigitalOutputPin(GPIO_04, "RasPi pin 16", PinState.LOW);
    private GpioPinDigitalOutput pin18 = gpio.provisionDigitalOutputPin(GPIO_05, "RasPi pin 18", PinState.LOW);
    private GpioPinDigitalOutput pin23 = gpio.provisionDigitalOutputPin(GPIO_23, "RasPi pin 23", PinState.LOW);
    private GpioPinDigitalOutput pin07 = gpio.provisionDigitalOutputPin(GPIO_07, "RasPi pin 07", PinState.LOW);
    private GpioPinDigitalOutput[] pins = {
            pin11,
            pin12,
            pin13,
            pin15,
            pin16,
            pin18,
            pin23,
            pin07
    };
    private GpioStepperMotorComponent motor = new GpioStepperMotorComponent(pins);

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
    private Main()
    {
        UserExperience ux = new UserExperience(version, motor, gpio);
        ux.createGUI(version);
        new Timer(100, ux).start();
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

