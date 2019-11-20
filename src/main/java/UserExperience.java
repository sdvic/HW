import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;

import static java.awt.Toolkit.getDefaultToolkit;

public class UserExperience extends JComponent implements ActionListener
{
    private Main main;

    Timer paintTicker = new Timer(100, this);
    private TestSequences ts;
    private int screenWidth = getDefaultToolkit().getScreenSize().width;
    private JButton allButton = new JButton("ALL");
    private JButton teeButton = new JButton("TEE");
    private JButton screenButton = new JButton("SCREEN");
    private JButton sensorsButton = new JButton("SENSORS");
    private JButton commButton = new JButton("COMM");
    private JButton runButton = new JButton("RUN");
    private JButton resetButton = new JButton("RESET");
    private JButton printButton = new JButton("PRINT");
    private JTextField passFailTextField = new JTextField();
    private JTextField errorCodeDisplayField = new JTextField();
    private JFrame display = new JFrame();
    private int leftMargin = 40;
    private int middleMargin = 250;
    private Font buttonFont = new Font("SansSerif", Font.PLAIN, 21);
    private Font resultFont = new Font("SansSerif", Font.BOLD, 28);
    private Font indicatorFont = new Font("Arial", Font.PLAIN, 17);
    private Font passFailFont = new Font("Bank Gothic", Font.BOLD, 45);
    private String version;
    private Ellipse2D.Double[] emitterBubbleArray = new Ellipse2D.Double[4];
    private int errBit; // Error bit position
    private int errEmitter = 2;      // byte used for emitter errors
    private boolean isTestFinished;// = true;
    private boolean isTestStarting;

    public UserExperience(String version)
    {
        this.version = version;
        paintTicker.start();
    }

    public void createGUI(String version)
    {
        display.setSize(getDefaultToolkit().getScreenSize().width, getDefaultToolkit().getScreenSize().height);
        display.add(this);//Adds Graphics
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.setVisible(true);

        allButton.setBounds(leftMargin, 108, 150, 34); // ALL Button
        allButton.setHorizontalAlignment(SwingConstants.CENTER);
        allButton.setFont(buttonFont);
        allButton.addActionListener(main);
        display.add(allButton);

        teeButton.setBounds(leftMargin, 153, 150, 34); // TEE button
        teeButton.setHorizontalAlignment(SwingConstants.CENTER);
        teeButton.setFont(buttonFont);
        teeButton.addActionListener(main);
        display.add(teeButton);

        screenButton.setBounds(leftMargin, 198, 150, 34); // SCREEN button
        screenButton.setHorizontalAlignment(SwingConstants.CENTER);
        screenButton.setFont(buttonFont);
        screenButton.addActionListener(main);
        display.add(screenButton);

        sensorsButton.setBounds(leftMargin, 243, 150, 34); // SENSORS button
        sensorsButton.setHorizontalAlignment(SwingConstants.CENTER);
        sensorsButton.setFont(buttonFont);
        sensorsButton.addActionListener(main);
        display.add(sensorsButton);

        runButton.setBounds(500, 345, 100, 58); // RUN button
        runButton.setHorizontalAlignment(SwingConstants.CENTER);
        runButton.setFont(buttonFont);
        runButton.addActionListener(main);
        display.add(runButton);

        passFailTextField.setBounds(500, 108, 120, 169);
        passFailTextField.setHorizontalAlignment((SwingConstants.CENTER));
        passFailTextField.setFont(resultFont);
        passFailTextField.setBackground(Color.WHITE);
        passFailTextField.setText("");
        display.add(passFailTextField);

        errorCodeDisplayField.setBounds(0, 289, screenWidth, 44);
        display.add(errorCodeDisplayField);

        commButton.setBounds(middleMargin, 108, 150, 34); // COMM button
        commButton.setHorizontalAlignment(SwingConstants.CENTER);
        commButton.setFont(buttonFont);
        commButton.addActionListener(main);

        resetButton.setBounds(100, 345, 120, 58); // RESET button
        resetButton.setHorizontalAlignment(SwingConstants.CENTER);
        resetButton.setFont(buttonFont);
        resetButton.addActionListener(main);
        display.add(resetButton);

        printButton.setBounds(300, 345, 120, 58); // PRINT button
        printButton.setHorizontalAlignment(SwingConstants.CENTER);
        printButton.setFont(buttonFont);
        printButton.addActionListener(main);
        display.add(printButton);

        display.add(commButton);
        display.setSize(getDefaultToolkit().getScreenSize().width, getDefaultToolkit().getScreenSize().height);
        display.add(this);
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.getContentPane().setBackground(new Color(160, 160, 160));
        display.setTitle("FSG StripTest ver " + version);
        display.setVisible(true);
    }

    public void paint(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.setFont(indicatorFont);
        int errTestByteLow = 1;  // byte used for sensors errors, bottom 8 bits
        int errTestByteHigh = 4; // byte used for sensors errors, top 8 bits
        for (int i = 0; i < 8; i++)// Draw sensor indicators 1-8 with pass/fail colors
        {
            g2.setColor(Color.YELLOW); // Pale Yellow // test for correct IR detection by photo diodes
            errBit = 1;
            errBit = errBit << i;
            errBit = errTestByteLow & errBit; // current error masked
            if (errBit == 0)
            {
                g2.setColor(Color.GREEN);  // Passed green
            }
            if (errBit == 1)
            {
                g2.setColor(Color.RED); // Failed red
            }
            if (isTestStarting)
            {
                g2.setColor(Color.WHITE);  // Starting white
            }
            g2.fillOval((42 * i + 26), 10, 32, 32);
            g2.setColor(Color.BLACK);
            g2.drawString((i + 1) + "", (42 * i + 36), 32);
        }
        for (int i = 0; i < 8; i++)// Draw sensor indicators 9-16 with pass/fail colors
        {
            // test for correct IR detection by photo diodes
            g2.setColor(Color.YELLOW); // Pale Yellow
            errBit = 1;
            errBit = errBit << i;
            errBit = errTestByteHigh & errBit; // current error masked
            if (errBit == 0)
            {
                g2.setColor(Color.GREEN);  // Passed green
            }
            if (errBit == 1)
            {
                g2.setColor(Color.RED); // Failed red
            }
            if (isTestStarting)
            {
                g2.setColor(Color.WHITE);  // Starting white
            }
            g2.fillOval((42 * i + 362), 10, 32, 32);
            g2.setColor(Color.BLACK);
            g2.drawString((i + 9) + "", (42 * i + 367), 32);
            g2.setStroke(new BasicStroke(.4f));
            g2.setColor(Color.BLACK);
            g2.drawLine(0, 52, (screenWidth), 52);
            g2.setColor(new Color(153, 255, 255));
            g2.setColor(Color.BLACK);
            g2.drawLine(0, 97, (screenWidth), 97);
        }
        for (int i = 0; i < emitterBubbleArray.length; i++)
        {
            emitterBubbleArray[i] = new Ellipse2D.Double((120 * i + 180), 59, 30, 30);
            g2.setColor(new Color(255, 255, 153)); // Pale Yellow
            errBit = 1;
            errBit = errBit << i;
            errBit = errEmitter & errBit; // current error masked
            if (errBit == 0)
            {
                g2.setColor(Color.GREEN);  // Passed green
            }
            if (errBit == 1)
            {
                g2.setColor(Color.RED); // Failed red
            }
            if (isTestStarting)
            {
                g2.setColor(Color.WHITE);  // Starting white
            }
            g2.fill(emitterBubbleArray[i]);
            g2.setColor(Color.BLACK);
            g2.draw(emitterBubbleArray[i]);
            g2.drawString((i + 1) + "", (120 * i + 190), 80);
        }
        g2.setFont(buttonFont);
        g2.drawString("EMITTERS", leftMargin, 84);
        g2.drawLine(0, 288, (screenWidth), 288);
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 289, (screenWidth), 44);
        g2.setColor(Color.BLACK);
        g2.drawLine(0, 333, (screenWidth), 333);
      if (!isTestStarting)
      {
          if (isTestFinished)
          {
              passFailTextField.setBackground(Color.GREEN);
              passFailTextField.setFont(passFailFont);
              passFailTextField.setText("PASS");
          }
          if (!isTestFinished)
          {
              passFailTextField.setBackground(Color.RED);
              passFailTextField.setFont(passFailFont);
              passFailTextField.setText("FAIL");
          }
      }
      if (isTestStarting)
      {
          passFailTextField.setBackground(Color.WHITE);
          passFailTextField.setText("");
      }
    }
    public void actionPerformed(ActionEvent e)
    {
        repaint();
    }
    public void setTs(TestSequences ts)
    {
        this.ts = ts;
    }
    public void setMain(Main main)
    {
        this.main = main;
    }

    public void setTestFinished(boolean testFinished)
    {
        isTestFinished = testFinished;
    }

    public JTextField getErrorCodeDisplayField()
    {
        return errorCodeDisplayField;
    }

    public JFrame getDisplay()
    {
        return display;
    }

    public void setDisplay(JFrame display)
    {
        this.display = display;
    }
    public void setTestStarting(boolean testStarting)
    {
        isTestStarting = testStarting;
    }
}

