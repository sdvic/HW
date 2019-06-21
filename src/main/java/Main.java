import com.pi4j.io.gpio.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.awt.Toolkit.getDefaultToolkit;
import javax.swing.Timer;

/***********************************************************************
 * Full Swing Golf Strip Test version 99.47, 6/20/2019
 * copyright 2019 Vic Wintriss
 ***********************************************************************/
public class Main extends JComponent implements ActionListener, Runnable
{
    private final GpioController gpio = GpioFactory.getInstance();
    private final GpioPinDigitalOutput pinA = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, PinState.LOW);
    private String version = "99.47";
    private int screenWidth = getDefaultToolkit().getScreenSize().width;
    private int screenHeight = getDefaultToolkit().getScreenSize().height;
    private boolean isRunPressed = false;
    JFrame display = new JFrame("FSG StripTest ver " + version);
    private long start;
    private String jopInputText;
    private Timer paintTicker = new Timer(1000, this);
    private long INTERVAL = 1000;
    private int leftMargin = 40;
    private int middleMargin = 250;
    private GpioPinDigitalOutput pin3 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02);
    private GpioPinInput pin5 = gpio.provisionDigitalInputPin(RaspiPin.GPIO_03);
    private GpioPinDigitalOutput pin7 = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_04, PinState.LOW);
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

    public static void main(String[] args)
    {
        SwingUtilities.invokeLater(new Main());
    }

    @Override
    public void run()
    {
        runButton.setBounds(450, 350, 100, 50);
        runButton.addActionListener(this);
        display.add(runButton);
        setButton.setBounds(150,350, 100, 50);
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

    private void sleepNano(int i)
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
        repaint();
        if (e.getSource() == paintTicker)
        {
        }
        if (e.getSource() == runButton)
        {
            isRunPressed = true;
            System.out.println("you pushed run");
        }
        if (e.getSource() == setButton)
        {
            System.out.println("you pushed set");

        }
    }

    public void paint(Graphics g)
    {
        Graphics2D g2 = (Graphics2D)g;
        for (int i = 0; i < 16; i++)
        {
            g2.setStroke(new BasicStroke(4));
            g2.drawOval((40 * i + 40), 15, 30, 30);
            g2.setColor(new  Color(255, 243, 20));
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
            g2.fillRect(500, 250, 150, 30);//not working
            System.out.println("FAIL red");
        }
    }
}
