/*
Author: Janet Leahy
Version: July 4, 2014

Class containing code that responds to the calculate button being pressed. Takes input from the QField, finds the corresponding values from the table, and updates the displayed values in ValueContainer
 */

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.text.JTextComponent;
import javax.swing.JLabel;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.lang.NullPointerException;
import java.lang.NumberFormatException;
import java.text.DecimalFormat;


public class CalcButtonListener implements ActionListener {

    public static final String FILENAME = "RocheTable.txt";
    public static final int NUM_OF_VALUES = 12;
    public static final double MAX_Q = 100;
    public static final double MIN_Q = 0.01;

    //constants to store the column labels
    public static final int RL1_INDEX = 0;
    public static final int OMEGA1_INDEX = 1;
    public static final int X2_INDEX = 2;
    public static final int OMEGA2_INDEX = 3;
    public static final int X3_INDEX = 4;
    public static final int OMEGA3_INDEX = 5;
    public static final int RBK_INDEX = 6;
    public static final int RY_INDEX = 7;
    public static final int RZ_INDEX = 8;
    public static final int AREA_INDEX = 9;
    public static final int VOL_INDEX = 10;
    public static final int REQ_INDEX = 11;

    private JTextComponent QTextComponent;
    private JLabel ErrorLabel;
    private MyValuesContainer ValuesContainer;
    private FileReader fr;
    private BufferedReader br;
    private DecimalFormat f;

    //stores the address of the input field for q, so that the input value can
    // be accessed when the calculate (actionPerformed) function is called. Also
    // stores the address of a label, so that error messages can be displayed, 
    // and the address of the grid of values to be updated.
    public CalcButtonListener(JTextComponent aTextComponent, JLabel anErrorLabel, MyValuesContainer aContainer) {
	this.QTextComponent = aTextComponent;
	this.ErrorLabel = anErrorLabel;
	this.ValuesContainer = aContainer;

	//specifies the desired number of decimal places (now 7)
	f = new DecimalFormat("##.0000000");

	//readers first created here so potential exceptions are caught early
	try {
	    fr = new FileReader(FILENAME);
	    br = new BufferedReader(fr);
	}
	catch (FileNotFoundException ex) {
	    System.out.println(FILENAME + " not found in directory. Program will terminate.");
	    System.exit(0);
	}

    }


    //function runs when Calculate button is pressed or a value is entered in
    // the QField. Uses the table to find the values of Ry, RL1, etc. and
    //updates the values container in the main frame.
    public void actionPerformed(ActionEvent e) {
	double[] values;

	try {
	    values = findValues(Double.parseDouble(QTextComponent.getText()));

	    updateDisplay(values); //updates the display
	    
	    ErrorLabel.setText(""); //clears the error label until next mistake
	}
	catch (NumberFormatException ex) {
	    ErrorLabel.setText("The value for q must be numeric.");
	}
	catch (NullPointerException ex){
	    //error is thrown when br.readLine() returns null, ie. end of table
	    // is reached (q>100), or when q < 0.01. q=100 is already dealt with
	    ErrorLabel.setText("The value for q is outside the bounds of the table.");
	}
	catch (IOException ex) {
	    ex.printStackTrace();
	}

    }


    //given a q, uses table to fill in the entries of a values array, using
    //interpolation as necessary. static method, so can be accessed without
    //specifically needing to create a CalcButtonListener object.
    public static double[] findValues(double q) throws IOException, NumberFormatException, NullPointerException {
	double[] values;
	String[] lowerLine;
	String[] upperLine;
	String tempLine;
        double qLower;
	double qUpper;
	double vLower;
	double vUpper;

	FileReader fr;
	BufferedReader br;

	fr = new FileReader(FILENAME);
	br = new BufferedReader(fr); //re-instantiates reader at beginning
	    
	//table does not deal with q<0.01, so we must get rid of that case
	if (q < MIN_Q) {
	    throw new NullPointerException();
	}

        //creates an array to store the 12 values to be calculated
	values = new double[NUM_OF_VALUES];

	upperLine = new String[NUM_OF_VALUES+1];
	lowerLine = new String[NUM_OF_VALUES+1];

	//initializes upperLine to an array of 0's
	for (int i=0; i<upperLine.length; i++) {
	    upperLine[i] = "0";
	}
        //initializes qUpper to the q-value in upperLine
	qUpper = Double.parseDouble(upperLine[0]);

	//finds the upper-bound row on the table, splits it by spaces into
	// nine strings and stores the results in upperLine
	while (qUpper <= q) {
	    //copies contents of upperLine into lowerLine
	    for (int i=0; i<upperLine.length; i++) {
	        lowerLine[i] = upperLine[i];
	    }
	    //moves upperLine forward one row on the table
	    tempLine = br.readLine();
		
	    //This if statement deals with the case where q is the last
	    //column in the table. upperLine remains the same as lowerLine
	    //so as not to throw a null pointer exception unnecessarily.
	    if (tempLine == null && q == MAX_Q) {
	        break;
	    }

	    for (int i=0; i<upperLine.length; i++) {
	        upperLine[i] = tempLine.split("[ ]+")[i+1]; //tempLine.split[0] = "" from \n
	    }
	    //updates qUpper before next comparison with q
	    qUpper = Double.parseDouble(upperLine[0]);
	}
       	qLower = Double.parseDouble(lowerLine[0]);


	//linearly interpolates corresponding terms from the lists in
	//double form and stores the results in the values array
	for (int i=0; i < NUM_OF_VALUES; i++) {
	    vLower = Double.parseDouble(lowerLine[i+1]); //i+1 necessary because 1st column of table is for q-values
	    vUpper = Double.parseDouble(upperLine[i+1]);
	    //calls the static interpolate method in class Equations and stores the result in values[i]
	    values[i] = Equations.interpolate(qLower, vLower, qUpper, vUpper, q);
	}

	return values;
    }



    //rounds each element in values to 7 decimal places (half-up),
    //converts each value to a String and updates the display
    public void updateDisplay(double[] values) {

	ValuesContainer.setRL1(f.format(values[RL1_INDEX]));
        ValuesContainer.setOmega1(f.format(values[OMEGA1_INDEX]));
        ValuesContainer.setX2(f.format(values[X2_INDEX]));
        ValuesContainer.setOmega2(f.format(values[OMEGA2_INDEX]));
        ValuesContainer.setX3(f.format(values[X3_INDEX]));
        ValuesContainer.setOmega3(f.format(values[OMEGA3_INDEX]));
        ValuesContainer.setRbk(f.format(values[RBK_INDEX]));
	ValuesContainer.setRy(f.format(values[RY_INDEX]));
	ValuesContainer.setRz(f.format(values[RZ_INDEX]));
	ValuesContainer.setArea(f.format(values[AREA_INDEX]));
	ValuesContainer.setVol(f.format(values[VOL_INDEX]));
	ValuesContainer.setReq(f.format(values[REQ_INDEX]));
    }

}
