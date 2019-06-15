import com.pi4j.io.gpio.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.awt.Toolkit.getDefaultToolkit;
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
 * Full Swing Golf Strip Test version 0.9, 6/14/2019
 * copyright 2019 Vic Wintriss
 ***********************************************************************/
public class Main extends JComponent implements ActionListener
{
    final GpioController gpio = GpioFactory.getInstance();
    final GpioPinDigitalOutput pinA = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, PinState.LOW);
    Timer paintTicker = new Timer(20, this);
    String jopInput = "";


//    private GpioPinInput echoLeft = gpio.provisionDigitalInputPin(RaspiPin.GPIO_03); // Echo...pin 15
//    private GpioPinDigitalOutput strobeRight = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01);// Strobe...pin 12
//    private GpioPinInput echoRight = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04); // Echo...pin 16
//    private GpioPinDigitalOutput strobeCenter = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02);// Strobe...pin 13
//    private GpioPinInput echoCenter = gpio.provisionDigitalInputPin(RaspiPin.GPIO_05); // Echo...pin 18


    public static void main(String[] args)
    {
        new Main().getGoing();
    }

    public void getGoing()
    {
        JFrame gameWindow = new JFrame("FSG StripTest");
        gameWindow.setSize(getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
        gameWindow.add(this);
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //ImageIcon imageIcon = new ImageIcon(getClass().getResource("screen.jpg"));
        gameWindow.setVisible(true);
        jopInput = JOptionPane.showInputDialog("Version 0.12?");
        //gameWindow.setPreferredSize(new Dimension(200, 300));
        //gameWindow.getContentPane().setBackground(new Color(200, 235, 255));
        gameWindow.setSize(getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
        gameWindow.add(this);
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameWindow.setVisible(true);
        paintTicker.start();
        System.out.println("pin 1 going high");
        pinA.high();
        System.out.println("pin 1 going low");
        pinA.low();

//        ImageIcon imageIcon = new ImageIcon(getClass().getResource("screen.jpg"));
//        JLabel label = new JLabel(imageIcon);
//        gameWindow.add(label);
    }

    public void paint(Graphics g)
    {
        Graphics2D g2 = (Graphics2D)g;
        g2.fillOval(getWidth()/2, getHeight()/2, 50, 50);
    }
    @Override
    public void actionPerformed(ActionEvent e)
    {
        repaint();
    }
}
