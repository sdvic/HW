import com.pi4j.io.gpio.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static java.awt.Toolkit.getDefaultToolkit;

/***********************************************************************
 * Full Swing Golf Strip Test version 99.45, 6/19/2019
 * copyright 2019 Vic Wintriss
 ***********************************************************************/
public class Main extends JComponent implements ActionListener, Runnable
{
    private final GpioController gpio = GpioFactory.getInstance();
    private final GpioPinDigitalOutput pinA = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, PinState.LOW);
    private String version = "99.45";
    JFrame display = new JFrame("FSG StripTest ver " + version);
    private long start;
    private String jopInputText;
    private Timer paintTicker;
    private long INTERVAL = 1000;
    private GpioPinDigitalOutput pin3 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02);
    private GpioPinInput pin5 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_03);
    private GpioPinDigitalOutput pin7 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, PinState.LOW);
    private JButton runButton = new JButton("RUN");
    private int screenWidth = getDefaultToolkit().getScreenSize().width;
    private int screenHeight = getDefaultToolkit().getScreenSize().height;

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Main());
    }

    @Override
    public void run()
    {


        //System.out.println("Please enter operator badge number " + version);
        paintTicker = new Timer(20, this);
        //jopInputText = JOptionPane.showInputDialog("What do you want me to do for rev " + version);
        display.setSize(getDefaultToolkit().getScreenSize().width, getDefaultToolkit().getScreenSize().height);
        display.add(this);
        //display.add(runButton);
        //runButton.addActionListener(this);
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.getContentPane().setBackground(new Color(193, 202, 202));
        display.setVisible(true);
        //display.pack();
        paintTicker.start();
        //while (true)
        {
            sleepNano();
            // pin16.high();
            //sleepNano();
            //pin16.low();
        }
    }

    private void sleepNano()
    {
        start = System.nanoTime();
        long end = 0;
        do
        {
            end = System.nanoTime();
        } while (start + INTERVAL >= end);
        System.out.println("Delay in nanoseconds is: " + (end - start));
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
            System.out.println("you pushed run");
        }
    }

    public void paint(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        //g2.drawString("Operator badge number " + jopInputText, 150, 150);
        for (int i = 0; i < 16; i++)
        {
            g2.setFont(new Font("Bank Gothic", Font.BOLD, 16));
            g2.drawString((i + 1) + "", (40 * i + 47), 37);
            g2.setStroke(new BasicStroke(2));
            g2.drawOval((40 * i + 40), 15, 30, 30);
        }
        g2.setStroke(new BasicStroke(.1f));
        g2.drawLine(40, 47, (screenWidth - 60), 47);
        g2.setColor(new Color(150, 157, 157));
        g2.fillRect(38, 48, (screenWidth - 80), 48);
        for (int i = 0; i < 4; i++)
        {
            g2.setColor(new Color(0,0,0));
            g2.drawOval((120 * i + 180), 53, 30, 30);
            g2.drawString((i + 1) + "", (120 * i + 190), 72);
        }
        g2.drawString("EMITTERS", 40, 72);
    }
}
