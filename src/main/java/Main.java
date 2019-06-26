import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.pi4j.io.gpio.RaspiPin.GPIO_23;
import static java.awt.Toolkit.getDefaultToolkit;

/***********************************************************************
 * Full Swing Golf Strip Test version 99.85, 6/25/2019
 * copyright 2019 Vic Wintriss
 ***********************************************************************/
public class Main extends JComponent implements ActionListener, Runnable
{
    private String version = "99.85";
    final GpioController gpio = GpioFactory.getInstance();
    final GpioPinDigitalOutput pin33 = gpio.provisionDigitalOutputPin(GPIO_23, "RasPi pin 33", PinState.LOW);
    //    private GpioPinDigitalOutput clkIn = gpio.provisionDigitalOutputPin(GPIO_08, "RasPi pin 36", PinState.LOW);
    //    private GpioPinDigitalOutput modeIn = gpio.provisionDigitalOutputPin(GPIO_24, "RasPi pin 35", PinState.LOW);
    //    private GpioPinDigitalOutput dataIn = gpio.provisionDigitalOutputPin(GPIO_22, "RasPi pin 31", PinState.LOW);
    //    private GpioPinDigitalOutput lpclkIn = gpio.provisionDigitalOutputPin(GPIO_23, "RasPi pin 33", PinState.LOW);
    //    private GpioPinDigitalOutput emitterFire = gpio.provisionDigitalOutputPin(GPIO_07, "RasPi pin 7", PinState.LOW);
    private int screenWidth = getDefaultToolkit().getScreenSize().width;
    private int screenHeight = getDefaultToolkit().getScreenSize().height;
    private JButton runButton = new JButton("RUN");
    private JButton setButton = new JButton("SET");
    private JTextField allTextField = new JTextField("       ALL");
    private JTextField emittersTextField = new JTextField("       EMITTERS");
    private JTextField sensorsTextField = new JTextField("       SENSORS");
    private JTextField nextBoardTextField = new JTextField("       NEXT BOARD");
    private JTextField nextFrameTextField = new JTextField("       NEXT FRAME");
    private JTextField commTextField = new JTextField("       COMM");
    private JTextField longFullTextField = new JTextField("       LONG FULL");
    private JTextField long34TextField = new JTextField("       LONG 3/4");
    private JTextField long12TextField = new JTextField("       LONG 1/2");
    private JTextField long14TextField = new JTextField("       LONG 1/4");
    private JTextField passTextField = new JTextField("       PASS");
    private JTextField failTextField = new JTextField("       FAIL");
    private boolean isRunPressed = false;
    private JFrame display = new JFrame("FSG StripTest ver " + version);
    private Timer paintTicker = new Timer(1000, this);
    private Timer pulseTicker = new Timer(1, this);
    private int leftMargin = 40;
    private int middleMargin = 250;
    private PinState[] streamA =
            {
            PinState.LOW,
            PinState.HIGH,
            PinState.LOW,
            PinState.HIGH,
            PinState.LOW,
            PinState.LOW,
            PinState.LOW,
            PinState.LOW,
            PinState.HIGH,
            PinState.LOW,
            PinState.HIGH,
            PinState.HIGH,
            PinState.HIGH,
            PinState.HIGH,
            PinState.LOW
            };

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Main());
    }

    @Override
    public void run()
    {
        createGUI();
        display.setSize(getDefaultToolkit().getScreenSize().width, getDefaultToolkit().getScreenSize().height);
        display.add(this);//Adds Graphics
        paintTicker.start();
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.getContentPane().setBackground(new Color(193, 202, 202));
        display.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == paintTicker)
        {
            repaint();
        }
        if (e.getSource() == runButton)
        {
            isRunPressed = true;
            makePulseStream(streamA);
            System.out.println("you pushed run");
        }
        if (e.getSource() == setButton)
        {
            isRunPressed = false;
            System.out.println("you pushed set");
        }
    }

    private void makePulseStream(PinState[] streamArray)
    {
        for (int i = 0; i < streamArray.length; i++)
        {
            pin33.setState(streamArray[i]);
            System.out.print(".");
        }
    }

    public void testWait()
    {
        final long INTERVAL = 100000;
        long start = System.nanoTime();
        long end = 0;
        do
        {
            end = System.nanoTime();
        } while (start + INTERVAL >= end);
        System.out.println(end - start);
    }

    public void paint(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        for (int i = 0; i < 16; i++)
        {
            g2.setStroke(new BasicStroke(4));
            g2.drawOval((40 * i + 40), 15, 30, 30);
            g2.setColor(new Color(255, 243, 20));
            g2.fillOval((40 * i + 40), 15, 30, 30);
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Bank Gothic", Font.BOLD, 12));
            g2.drawString((i + 1) + "", (40 * i + 47), 37);
        }
        g2.setStroke(new BasicStroke(.1f));
        g2.drawLine(40, 47, (screenWidth - 60), 47);
        g2.setColor(new Color(37, 243, 255));
        g2.fillRect(38, 48, (screenWidth - 80), 48);
        for (int i = 0; i < 4; i++)
        {
            g2.setColor(new Color(200, 123, 18));
            g2.fillOval((120 * i + 180), 53, 30, 30);
            g2.setColor(new Color(0, 0, 0));
            g2.drawOval((120 * i + 180), 53, 30, 30);
            g2.drawString((i + 1) + "", (120 * i + 190), 72);
        }
        g2.drawString("EMITTERS", leftMargin, 72);
        if (isRunPressed)
        {
            g2.setColor(Color.RED);
            g2.fillRect(500, 233, 150, 66);
        }
    }

    public void createGUI()
    {
        runButton.setBounds(500, 350, 100, 50);
        runButton.addActionListener(this);
        display.add(runButton);
        setButton.setBounds(150, 350, 100, 50);
        setButton.addActionListener(this);
        display.add(setButton);
        allTextField.setBounds(leftMargin, 100, 150, 30);
        display.add(allTextField);
        emittersTextField.setBounds(leftMargin, 150, 150, 30);
        display.add(emittersTextField);
        sensorsTextField.setBounds((leftMargin + 10), 200, 150, 30);
        display.add(sensorsTextField);
        nextBoardTextField.setBounds(leftMargin, 250, 150, 30);
        display.add(nextBoardTextField);
        nextFrameTextField.setBounds(leftMargin, 300, 150, 30);
        display.add(nextFrameTextField);
        commTextField.setBounds(middleMargin, 100, 150, 30);
        display.add(commTextField);
        longFullTextField.setBounds(middleMargin, 150, 150, 30);
        display.add(longFullTextField);
        long34TextField.setBounds(middleMargin, 200, 150, 30);
        display.add(long34TextField);
        long12TextField.setBounds(middleMargin, 250, 150, 30);
        display.add(long12TextField);
        long14TextField.setBounds(middleMargin, 300, 150, 30);
        display.add(long14TextField);
        passTextField.setBounds(500, 150, 150, 30);
        display.add(passTextField);
        failTextField.setBounds(500, 250, 150, 30);
        display.add(failTextField);
        display.setSize(getDefaultToolkit().getScreenSize().width, getDefaultToolkit().getScreenSize().height);
        display.add(this);
        paintTicker.start();
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.getContentPane().setBackground(new Color(193, 202, 202));
        display.setVisible(true);
    }
}

