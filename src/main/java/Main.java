import com.pi4j.io.gpio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.awt.Toolkit.getDefaultToolkit;

/***********************************************************************
 * Full Swing Golf Strip Test version 99.25, 6/16/2019
 * copyright 2019 Vic Wintriss
 ***********************************************************************/
public class Main extends JComponent implements ActionListener
{
    private static long start;
    private static String jopInput;
    private static Timer paintTicker;
    private final static long INTERVAL = 10;
    private final static GpioController gpio = GpioFactory.getInstance();
    private final static GpioPinDigitalOutput pinA = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, PinState.LOW);
    private static Image screen;
    private  static GpioPinDigitalOutput pin3 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02);
    private  static GpioPinInput pin5 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_03);
    private  static GpioPinDigitalOutput pin7 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, PinState.LOW);
    private  static  GpioPinInput pin29 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_05);
    private static final GpioPinDigitalOutput pin16 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, "MyLED", PinState.LOW);
    private static JFrame gameWindow;

    private void main(String[] args) throws InterruptedException
    {
       new Main().getGoing();
    }

    public void getGoing()
    {
        System.out.println("<--Pi4J--> GPIO Control Example 99.26... started.");
        paintTicker = new Timer(20, this);
        screen = getDefaultToolkit().getImage("screen.jpg");
        jopInput = JOptionPane.showInputDialog("What do you want me to do for rev 99.26?");
        JFrame gameWindow = new JFrame("FSG StripTest");
        gameWindow.setSize(getDefaultToolkit().getScreenSize().width, getDefaultToolkit().getScreenSize().height);
        gameWindow.add(this);
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameWindow.getContentPane().setBackground(new Color(200, 235, 255));
        gameWindow.setVisible(true);
        paintTicker.start();
//        while (true)
//        {
//            sleepNano();
//            pin16.high();
//           sleepNano();
//            pin16.low();
//        }
    }

    private static void sleepNano()
    {
        start = System.nanoTime();
        long end=0;
        do{
            end = System.nanoTime();
        }while(start + INTERVAL >= end);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        repaint();
    }
    public void paint(Graphics g)
    {
       Graphics2D g2 =(Graphics2D)g;
       g2.drawOval(20, 20, 20, 20);
    }
}
