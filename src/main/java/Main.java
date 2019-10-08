//import com.pi4j.component.motor.MotorState;
//import com.pi4j.component.motor.StepperMotorBase;
//import com.pi4j.component.motor.impl.GpioStepperMotorCompone

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.pi4j.io.gpio.RaspiBcmPin.GPIO_23;
import static java.awt.Toolkit.getDefaultToolkit;

public class Main extends JComponent implements ActionListener
{
  /**************************************************************************************
   *       Full Swing Golf Strip Test
   *       copyright 2019 Vic Wintriss
   */      private String version = "100.9";
   /*      October 8, 2019
   **************************************************************************************/
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
    final GpioController gpio = GpioFactory.getInstance();
    final GpioPinDigitalOutput pin33 = gpio.provisionDigitalOutputPin(GPIO_23, "RasPi pin 33", PinState.LOW);
    private PinState[] streamA =
            {
                    PinState.LOW,
                    PinState.LOW,
                    PinState.LOW,
                    PinState.LOW,
                    PinState.LOW,
                    PinState.LOW,
                    PinState.HIGH,
                    PinState.HIGH,
                    PinState.HIGH,
                    PinState.HIGH,
                    PinState.LOW
            };

    public static void main(String[] args) throws Exception
    {
        new Main().getGoing();
    }

    public void getGoing() throws Exception
    {
        //Stepper step = new Stepper();
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
    private void makePulseStream(PinState[] streamArray)
    {
        //for (int i = 0; i < streamArray.length; i++)
        while (true)
        {
            System.out.print(".");
            try {
                Thread.sleep(500);
                pin33.setState( PinState.HIGH);
                Thread.sleep(500);
                pin33.setState( PinState.LOW);
            }
            catch (InterruptedException e) {
                System.out.println("sleep exception in makePulseStream()");
            }
        }
    }

//    class Stepper extends StepperMotorBase
//    {
//        final GpioPinDigitalOutput pin33 = gpio.provisionDigitalOutputPin(GPIO_23, "RasPi pin 33", PinState.LOW);
//
//        public void getStepperGoing() throws InterruptedException
//        {
//            System.out.println("Starting Stepper");
//            final GpioPinDigitalOutput[] pins = {
//                    gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, PinState.LOW),
//                    gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, PinState.LOW),
//                    gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, PinState.LOW),
//                    gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, PinState.LOW)
//            };
//            gpio.setShutdownOptions(true, PinState.LOW, pins);
//            GpioStepperMotorComponent motor = new GpioStepperMotorComponent(pins);
//            byte[] single_step_sequence = new byte[4];
//            single_step_sequence[0] = (byte) 0b0001;
//            single_step_sequence[1] = (byte) 0b0010;
//            single_step_sequence[2] = (byte) 0b0100;
//            single_step_sequence[3] = (byte) 0b1000;
//            motor.setStepInterval(2);
//            motor.setStepSequence(single_step_sequence);
//            motor.setStepsPerRevolution(2038);
//            System.out.println("   Motor FORWARD for 2038 steps.");
//            motor.step(2038);
//            System.out.println("   Motor STOPPED for 2 seconds.");
//            Thread.sleep(2000);
//            motor.stop();
//            gpio.shutdown();
//        }
//
//        @Override
//        public void step(long l)
//        {
//
//        }
//
//        @Override
//        public MotorState getState()
//        {
//            return null;
//        }
//
//        @Override
//        public void setState(MotorState motorState)
//        {
//
//        }
//    }
}

