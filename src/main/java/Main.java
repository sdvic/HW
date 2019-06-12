import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.awt.Toolkit.*;
import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPin;
//import com.pi4j.io.gpio.GpioPinDigitalInput;
//import com.pi4j.io.gpio.GpioPinDigitalOutput;
//import com.pi4j.io.gpio.PinDirection;
//import com.pi4j.io.gpio.PinMode;
//import com.pi4j.io.gpio.PinPullResistance;
//import com.pi4j.io.gpio.PinState;
//import com.pi4j.io.gpio.RaspiPin;
//import com.pi4j.io.gpio.trigger.GpioCallbackTrigger;
//import com.pi4j.io.gpio.trigger.GpioPulseStateTrigger;
//import com.pi4j.io.gpio.trigger.GpioSetStateTrigger;
//import com.pi4j.io.gpio.trigger.GpioSyncStateTrigger;
//import com.pi4j.io.gpio.event.GpioPinListener;
//import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
//import com.pi4j.io.gpio.event.GpioPinEvent;
//import com.pi4j.io.gpio.event.GpioPinListenerDigital;
//import com.pi4j.io.gpio.event.PinEventType;

/***********************************************************************
 * Full Swing Golf Strip Test version 0.0, 6/8/2019
 * copyright 2019 Vic Wintriss
 ***********************************************************************/
public class Main extends JComponent implements Runnable, ActionListener
{
    Timer paintTicker = new Timer(20, this);
    String jopInput = "";
//    final GpioController gpio = GpioFactory.getInstance();
//    private GpioPinDigitalOutput strobeLeft = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00);// Strobe...pin 11
//    private GpioPinInput echoLeft = gpio.provisionDigitalInputPin(RaspiPin.GPIO_03); // Echo...pin 15
//    private GpioPinDigitalOutput strobeRight = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01);// Strobe...pin 12
//    private GpioPinInput echoRight = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04); // Echo...pin 16
//    private GpioPinDigitalOutput strobeCenter = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02);// Strobe...pin 13
//    private GpioPinInput echoCenter = gpio.provisionDigitalInputPin(RaspiPin.GPIO_05); // Echo...pin 18

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Main());
    }

    @Override
    public void run()
    {
        jopInput =JOptionPane.showInputDialog("What do you want me to do?");
        JFrame gameWindow = new JFrame("FSG StripTest");
        gameWindow.setSize(getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
        gameWindow.add(this);
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameWindow.getContentPane().setBackground(new Color(200, 235, 255));
        gameWindow.setVisible(true);
        paintTicker.start();
        sonar();
    }

    public void sonar()
    {
        System.out.println("Starting Sonar");
//        echoLeft.addListener(new GpioPinListenerDigital()
//        {
//            @Override
//            public void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event)
//            {
//                if (event.getState() == PinState.HIGH)
//                {
//                }
//                if (event.getState() == PinState.LOW)
//                {
//                }
//
//            }
//        });
    }


    public void paint(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.fillOval(40, 40, 10, 10);
        g2.drawString(jopInput, 150, 150);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        repaint();
    }
}
