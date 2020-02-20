import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javax.swing.*;
import java.awt.*;
import static java.awt.Toolkit.getDefaultToolkit;

public class Main extends javafx.application.Application
{
    String version = "verson 600.03";
    private static final int R = 150;
    Circle[] circles = new Circle[16];
    Circle circle;
    Text text;
    Group group;
    StackPane stack = new StackPane();
    /****************************************************************************************
     *      Full Swing Golf Strip Test                                                      *
     *      copyright 2019 Vic Wintriss
     *       Private version...pro account                                                  *
     /****************************************************************************************/
    private int testByte;
    private String codeCat;
    //private Bubble bubble = new Bubble(0, 0, Color.BLACK);
    private Bubble[] sensorBubbleList = new Bubble[16];
    private Bubble[] emitterBubbleList = new Bubble[4];
    private int screenHeight = getDefaultToolkit().getScreenSize().height;
    int sensorRowYpos = screenHeight / 80;
    private int screenWidth = getDefaultToolkit().getScreenSize().width;
    int sensorBubblePitch = screenWidth / 20;
    int emitterBubblePitch = screenWidth / 10;
    private int emitterRowYpos = screenHeight / 10;
    private int leftMargin = screenWidth / 10;
    private int commTestProgress;
    private Main main;
    private UserExperience ux;
    private TestSequences ts;
    private Timer commTestTicker;
    private JProgressBar commTestProgressBar = new JProgressBar();
    private JButton commButton;
    private Color defaultButtonBackgroundColor;

    public Main()
    {
        main = this;
    }

    /************************************************
     * displayErrorList[0] => errDataOut
     * displayErrorList[1] => errLpClkOut
     * displayErrorList[2] => errModeOut
     * displayErrorList[3] => errClkOut
     * displayErrorList[4] => errEripple
     * displayErrorList[5] => errRclk
     * displayErrorList[6] => errShiftLoad
     * displayErrorList[7] => errSin
     ************************************************/

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) {
        AnchorPane root = new AnchorPane();

        VBox vbox1 = new VBox();
        vbox1.setSpacing(5);//Set vbox spacing

        //Handles the number of row to be added to the vbox
        for(int i = 0; i < 4; i++)
        {
            vbox1.getChildren().add(addNewRow(i));
        }

        root.getChildren().add(vbox1);
        Scene scene = new Scene(root, screenWidth, screenHeight);

        primaryStage.setTitle("HW version 600.01");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //Method creates all the nodes for a new row.
    HBox addNewRow(int rowNumber)
    {
        //Create nodes and adding correct spaceing
        HBox hbox = new HBox();
        hbox.setSpacing(5);
        RadioButton radioButton = new RadioButton();
        radioButton.setPrefHeight(25);
        TextField textField = new TextField();
        textField.setPrefWidth(40);
        Label label = new Label(Integer.toString(rowNumber + 1));
        label.setPrefHeight(25);
        HBox trailingHBox = new HBox();
        trailingHBox.setSpacing(5);
        hbox.getChildren().addAll(radioButton, textField, label, trailingHBox);

        //Event handler on textfield. Add right about of trailing textfields
        textField.setOnKeyReleased((event)->{
            if(textField.getText().trim().length() > 0 && Integer.parseInt(textField.getText()) > 0)//If textfield has some value greater than zero. I didn't catch for non integers
            {
                int tempInt = Integer.parseInt(textField.getText());
                //clear trailingHBox so that new Trailing hbox can be added
                if(trailingHBox.getChildren().size() > 0)
                {
                    trailingHBox.getChildren().clear();
                }
                //add the correct number of textFields.
                for(int i = 0; i < tempInt - 1; i++)
                {
                    TextField tempTextField = new TextField();
                    tempTextField.setPrefWidth(20);
                    trailingHBox.getChildren().add(tempTextField);
                }

                //add the blue and red textfields
                TextField textFieldBlue = new TextField();
                textFieldBlue.setPrefWidth(40);
                textFieldBlue.setStyle("-fx-background-color: BLUE");
                TextField textFieldRed = new TextField();
                textFieldRed.setPrefWidth(40);
                textFieldRed.setStyle("-fx-background-color: RED");

                trailingHBox.getChildren().addAll(textFieldBlue, textFieldRed);
            }
            else{
                trailingHBox.getChildren().clear();//clear traingHbox if it's has no value
            }
        });

        return hbox;
    }

//        public void actionPerformed (ActionEvent e)
//        {
//            if (e.getActionCommand().equals("ALL"))//mode 1
//            {
//                clearErrorLists();
//                ux.getAllButton().setBackground(ux.getPressedButtonColor());
//                testScreen(); // run first because to resetErrors() in test.
//                testTee();
//                testSensors();
//                buildErrorCodeDisplayFieldString(ts.getIndependentErrorList(), "     All Test Errors =>  ");
//                ux.getAllButton().setBackground(ux.getDefaultButtonBackgroundColor());
//            }
//            if (e.getActionCommand().equals("BASIC"))
//            {
//                clearErrorLists();
//                ux.getBasicButton().setBackground(ux.getPressedButtonColor());
//                ts.loadTestWordSequence(testByte);
//                ts.resetSequence();// Test in tee frame mode with on-board emitter
//                ts.teeSequence();
//                ts.emitterSelSequence();
//                ts.emitterFireSequence(0);
//                ts.teeShiftOutSequence(false);
//                ts.resetSequence(); // Test in tee frame mode with next board emitter
//                ts.teeSequence();
//                ts.emitterDeselSequence();
//                ts.emitterFireSequence(1);
//                ts.teeShiftOutSequence(true);
//                ts.resetSequence(); // Test the screen frame connections
//                ts.screenSequence();
//                ts.emitterSelSequence();
//                ts.emitterFireSequence(2);
//                ts.screenShiftOutSequence();
//                ts.resetSequence();// End of testing
//                buildErrorCodeDisplayFieldString(ts.getIndependentErrorList(), "     Basic Test Errors =>  ");
//                ux.getBasicButton().setBackground(ux.getDefaultButtonBackgroundColor());
//            }
//            if (e.getActionCommand().equals("TEE"))//mode 2
//            {
//                clearErrorLists();
//                ux.getTeeButton().setBackground(ux.getPressedButtonColor());
//                testTee();
//                buildErrorCodeDisplayFieldString(ts.getIndependentErrorList(), "     Tee Test Errors =>  ");
//                ux.getTeeButton().setBackground(ux.getDefaultButtonBackgroundColor());
//            }
//            if (e.getActionCommand().equals("SCREEN"))//mode 3
//            {
//                clearErrorLists();
//                ux.getScreenButton().setBackground(ux.getPressedButtonColor());
//                testScreen();
//                buildErrorCodeDisplayFieldString(ts.getIndependentErrorList(), "     Screen Test Errors =>  ");
//                ux.getScreenButton().setBackground(ux.getDefaultButtonBackgroundColor());
//            }
//            if (e.getActionCommand().equals("SENSORS"))//mode 4
//            {
//                clearErrorLists();
//                ux.getSensorsButton().setBackground(ux.getPressedButtonColor());
//                testSensors();
//                buildErrorCodeDisplayFieldString(ts.getIndependentErrorList(), "     Sensor Test Errors =>  ");
//                ux.getSensorsButton().setBackground(ux.getDefaultButtonBackgroundColor());
//            }
//            if (e.getActionCommand().equals("COMM"))//mode 5
//            {
//                clearErrorLists();
//                ux.getCommButton().setBackground(ux.getPressedButtonColor());
//                testByte = (byte) 0b10101110; // byte used for testing sensors. Active low, LSB is D1
//                commTestTicker.start();
//            }
//            if (e.getActionCommand().equals("RESET"))
//            {
//                clearErrorLists();
//                codeCat = "";
//            }
//            if (e.getActionCommand().equals("PRINT"))
//            {
//                System.out.println("PRINT button pressed");// action 2
//            }
//            ux.setCodeCat(codeCat);
//        }
//        private void testTee ()// Set CPLD state machine to the tee frame and test all the emitters...mode 2
//        {
//            for (int i = 1; i < 5; i++)
//            {
//                ts.resetSequence();        // t1-t2
//                ts.teeSequence();          // t3-t8
//                ts.emitterSelSequence();   // t9-t14
//                ts.emitterFireSequence(i); // t15-t18
//                ts.resetSequence();        // t1-t2
//            }
//        }
//        void testScreen ()// Set CPLD state machine to the screen frame and test the interconnection signals
//        {
//            ts.screenTestSequence();
//            ts.screenShiftOutSequence();
//            ts.resetSequence();
//        }
//        private void testSensors ()// Test each individual IR photodiode for correct operation...mode 4
//        {
//            for (int i = 0; i < 8; i++) // walking 1 test pattern
//            {
//                ts.resetSequence();      // t1-t2
//                ts.teeSequence();        // t3-t8
//                ts.emitterSelSequence(); // t9-t14
//                testByte = 128;
//                testByte = testByte >> i;
//                ts.loadTestWordSequence(testByte);
//                ts.emitterFireSequence(0);          // t15-t18
//                ts.teeShiftOutSequence(false); // t19-t54
//                ts.resetSequence();                // t55-t56
//            }
//            for (int i = 0; i < 8; i++) // walking 0 test pattern
//            {
//                ts.resetSequence();      // t1-t2
//                ts.teeSequence();        // t3-t8
//                ts.emitterSelSequence(); // t9-t14
//                testByte = 128;
//                testByte = testByte >> i;
//                testByte = ~testByte;
//                ts.loadTestWordSequence(testByte);
//                ts.emitterFireSequence(0);          // t15-t18
//                ts.teeShiftOutSequence(false); // t19-t54
//                ts.resetSequence();                // t55-t56
//            }
//        }
//        void testBasic
//        () // Test the majority of the Comm Board functionality. Uses testByteHigh, testByteLow, emitter, Sin...mode 5
//        {
//            ts.loadTestWordSequence(testByte);
//            ts.resetSequence();// Test in tee frame mode with on-board emitter
//            ts.teeSequence();
//            ts.emitterSelSequence();
//            ts.emitterFireSequence(0);
//            ts.teeShiftOutSequence(false);
//            ts.resetSequence();// Test in tee frame mode with next board emitter
//            ts.teeSequence();
//            ts.emitterDeselSequence();
//            ts.emitterFireSequence(1);
//            ts.teeShiftOutSequence(true);
//            ts.resetSequence(); // Test the screen frame connections
//            ts.screenSequence();
//            ts.emitterSelSequence();
//            ts.emitterFireSequence(2);
//            ts.screenShiftOutSequence();
//            ts.resetSequence(); // End of testing
//        }
//        public void clearErrorLists () // Reset all errors and set all indicators to default state before running tests
//        {
//            for (int i = 0; i < ts.getIndependentErrorList().length; i++)
//            {
//                ts.setDisplayErrorList(i, false);
//            }
//            for (int i = 0; i < getEmitterBubbleList().length; i++)
//            {
//                Bubble bubba = getEmitterBubbleList()[i];
//                bubba.setBackgroundColor(BLUE);
//                getEmitterBubbleList()[i] = bubba;
//            }
//            for (int i = 0; i < getSensorBubbleList().length; i++)
//            {
//                Bubble bubba = getSensorBubbleList()[i];
//                bubba.setBackgroundColor(BLUE);
//                setBubble(getSensorBubbleList(), i, bubba);
//            }
//            ux.setButtonColor(ux.getSensorsButton(), ux.getDefaultButtonBackgroundColor());
//            ux.setButtonColor(ux.getTeeButton(), ux.getDefaultButtonBackgroundColor());
//            ux.setButtonColor(ux.getScreenButton(), ux.getDefaultButtonBackgroundColor());
//            ux.setButtonColor(ux.getCommButton(), ux.getDefaultButtonBackgroundColor());
//            ux.setButtonColor(ux.getBasicButton(), ux.getDefaultButtonBackgroundColor());
//            ux.setButtonColor(ux.getAllButton(), ux.getDefaultButtonBackgroundColor());
//            commTestProgressBar.setValue(0);
//        }
//        public String buildErrorCodeDisplayFieldString ( boolean[] errorList, String testSource)
//        {
//            codeCat = testSource;
//            for (int i = 0; i < errorList.length; i++)
//            {
//                if (errorList[i])
//                {
//                    codeCat += (i + ", ");
//                }
//            }
//            return codeCat;
//        }
//        public Bubble[] getEmitterBubbleList () {
//        return emitterBubbleList;
//    }
//        public void setBubble (Bubble[]bubbleList,int bubbleNumber, Bubble bubble){
//        bubbleList[bubbleNumber] = bubble;
//    }
//        public Bubble[] getSensorBubbleList () {
//        return sensorBubbleList;
//    }
//        public JProgressBar getCommTestProgressBar ()
//        {
//            return commTestProgressBar;
//        }
//        public Timer getCommTestTicker ()
//        {
//            return commTestTicker;
//        }
//        public JButton getCommButton ()
//        {
//            return commButton;
//        }
//        public Color getDefaultButtonBackgroundColor () {
//        return defaultButtonBackgroundColor;
//    }
    }


