import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;

import static java.awt.Toolkit.getDefaultToolkit;

public class UserExperience extends JComponent implements ActionListener
{
    public JTextField failTextField = new JTextField("FAIL");
    Timer paintTicker = new Timer(100, this);
    private TestSequences ts;
    private int screenWidth = getDefaultToolkit().getScreenSize().width;
    private JButton allButton = new JButton("ALL");
    private JButton teeButton = new JButton("TEE");
    private JButton screenButton = new JButton("SCREEN");
    private JButton sensorsButton = new JButton("SENSORS");
    private JButton commButton = new JButton("COMM");
    private JButton runButton = new JButton("RUN");
    private JButton longFull = new JButton("LONG FULL");
    private JButton long34 = new JButton("LONG 3/4");
    private JButton long12 = new JButton("LONG 1/2");
    private JButton long14 = new JButton("LONG 1/4");
    private JTextField passTextField = new JTextField("PASS");
    private JTextField errorCodeDisplayField = new JTextField();
    private JFrame display = new JFrame();
    private int leftMargin = 40;
    private int middleMargin = 250;
    private Font buttonFont = new Font("SansSerif", Font.PLAIN, 21);
    private Font resultFont = new Font("SansSerif", Font.BOLD, 28);
    private Font indicatorFont = new Font("Arial", Font.PLAIN, 17);
    private Graphics g;
    private String version;
    private boolean isCommBoardFlag = false;
    private boolean isLongBoardFlag = false;
    private boolean[] errorList = new boolean[8];
    private String codeCat = "";
    private Ellipse2D.Double[] emitterBubbleArray = new Ellipse2D.Double[4];

    public UserExperience(String version)
    {
        this.version = version;
        paintTicker.start();
    }

    public void createGUI(String version)
    {
        ts.setAllButton(allButton);
        ts.setCommButton(commButton);
        ts.setLong12(long12);
        ts.setLong14(long14);
        ts.setLong34(long34);
        ts.setLongFull(longFull);
        ts.setRunButton(runButton);
        ts.setScreenButton(screenButton);
        ts.setSensorsButton(sensorsButton);
        ts.setTeeButton(teeButton);
        display.setSize(getDefaultToolkit().getScreenSize().width, getDefaultToolkit().getScreenSize().height);
        display.add(this);//Adds Graphics
        display.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        display.setVisible(true);

        allButton.setBounds(leftMargin, 108, 150, 34); // ALL Button
        allButton.setHorizontalAlignment(SwingConstants.CENTER);
        allButton.setFont(buttonFont);
        allButton.addActionListener(ts);
        display.add(allButton);

        teeButton.setBounds(leftMargin, 153, 150, 34); // TEE button
        teeButton.setHorizontalAlignment(SwingConstants.CENTER);
        teeButton.setFont(buttonFont);
        teeButton.addActionListener(ts);
        display.add(teeButton);

        screenButton.setBounds(leftMargin, 198, 150, 34); // SCREEN button
        screenButton.setHorizontalAlignment(SwingConstants.CENTER);
        screenButton.setFont(buttonFont);
        screenButton.addActionListener(ts);
        display.add(screenButton);

        sensorsButton.setBounds(leftMargin, 243, 150, 34); // SENSORS button
        sensorsButton.setHorizontalAlignment(SwingConstants.CENTER);
        sensorsButton.setFont(buttonFont);
        sensorsButton.addActionListener(ts);
        display.add(sensorsButton);

        runButton.setBounds(500, 345, 100, 58); // RUN button
        runButton.setHorizontalAlignment(SwingConstants.CENTER);
        runButton.setFont(buttonFont);
        runButton.addActionListener(ts);
        display.add(runButton);

        passTextField.setBounds(500, 125, 120, 50); // PASS indicator
        passTextField.setHorizontalAlignment(SwingConstants.CENTER);
        passTextField.setFont(resultFont);
        display.add(passTextField);

        failTextField.setBounds(500, 210, 120, 50); // FAIL indicator
        failTextField.setHorizontalAlignment(SwingConstants.CENTER);
        failTextField.setFont(resultFont);
        display.add(failTextField);

        errorCodeDisplayField.setBounds(0, 289, screenWidth, 44);
        String codeCat = "";

        display.add(errorCodeDisplayField);

        if (isCommBoardFlag)
        {
            commButton.setBounds(middleMargin, 108, 150, 34); // COMM button
            commButton.setHorizontalAlignment(SwingConstants.CENTER);
            commButton.setFont(buttonFont);
            commButton.addActionListener(ts);
            display.add(commButton);
        }
        else if (isLongBoardFlag)
        {
            longFull.setBounds(middleMargin, 108, 150, 34); // Long Full button
            longFull.setHorizontalAlignment(SwingConstants.CENTER);
            longFull.setFont(buttonFont);
            longFull.addActionListener(ts);
            display.add(longFull);

            long34.setBounds(middleMargin, 153, 150, 34); // Long 3/4 button
            long34.setHorizontalAlignment(SwingConstants.CENTER);
            long34.setFont(buttonFont);
            long34.addActionListener(ts);
            display.add(long34);

            long12.setBounds(middleMargin, 198, 150, 34); // Long 3/4 button
            long12.setHorizontalAlignment(SwingConstants.CENTER);
            long12.setFont(buttonFont);
            long12.addActionListener(this);
            display.add(long12);

            long14.setBounds(middleMargin, 243, 150, 34); // Long 3/4 button
            long14.setHorizontalAlignment(SwingConstants.CENTER);
            long14.setFont(buttonFont);
            long14.addActionListener(ts);
            display.add(long14);
        }
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
        int errBit; // Error bit position
        int errEmitter = 2;      // byte used for emitter errors
        int errTestByteLow = 1;  // byte used for sensors errors, bottom 8 bits
        int errTestByteHigh = 4; // byte used for sensors errors, top 8 bits
        // Draw sensor indicators 1-8 with pass/fail colors
        for (int i = 0; i < 8; i++)
        {
            // test for correct IR detection by photo diodes
            g2.setColor(new Color(255, 255, 153)); // Pale Yellow
            errBit = 1;
            errBit = errBit << i;
            errBit = errTestByteLow & errBit; // current error masked
            if (errBit == 0)
            {
                g2.setColor(new Color(0, 255, 0));  // Passed green
            }
            else
            {
                g2.setColor(new Color(255, 0, 0)); // Failed red
            }
            g2.fillOval((42 * i + 26), 10, 32, 32);
            g2.setColor(Color.BLACK);
            g2.drawString((i + 1) + "", (42 * i + 36), 32);
        }
        // Draw sensor indicators 9-16 with pass/fail colors
        for (int i = 0; i < 8; i++)
        {
            // test for correct IR detection by photo diodes
            g2.setColor(new Color(255, 255, 153)); // Pale Yellow
            errBit = 1;
            errBit = errBit << i;
            errBit = errTestByteHigh & errBit; // current error masked
            if (errBit == 0)
            {
                g2.setColor(new Color(0, 255, 0));  // Passed green
            }
            else
            {
                g2.setColor(new Color(255, 0, 0)); // Failed red
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
                g2.setColor(new Color(0, 255, 0));  // Passed green
            }
            else
            {
                g2.setColor(new Color(255, 0, 0)); // Failed red
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
    }

    public void actionPerformed(ActionEvent e)
    {
        repaint();
    }

    public void setCommFlag(boolean isCommBoardFlag)
    {
        this.isCommBoardFlag = isCommBoardFlag;
    }

    public void buildErrorListDisplay()
    {
        for (int i = 0; i < errorList.length; i++)
        {
            if (errorList[i])
            {
                codeCat += (i + ", ");
            }
        }
        errorCodeDisplayField.setFont(buttonFont);
        errorCodeDisplayField.setText(codeCat);
        codeCat = " ";
    }

    public void setLongFlag(boolean isLongBoardFlag)
    {
        this.isLongBoardFlag = isLongBoardFlag;
    }

    public void setTs(TestSequences ts)
    {
        this.ts = ts;
    }

    public void setErrorList(boolean[] errorList)
    {
        this.errorList = errorList;
    }

    public void setCodeCat(String codeCat)
    {
        this.codeCat = codeCat;
    }
}


