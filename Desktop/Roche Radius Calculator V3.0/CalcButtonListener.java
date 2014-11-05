/*
Author: Janet Leahy
Version: Aug. 1, 2014

Class containing code that responds to the calculate button being pressed. Takes input from the QField, finds the corresponding values using root-finding of the d[Omega]/dx function (Area, Volume and Req still come from the table), and updates the displayed values in ValueContainer. In the main calculator for all q, F and p are both assumed to be 1.
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
import java.lang.Math;


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
	    //using input q and p = 1, finds the required values
	    values = calculateValues(Double.parseDouble(QTextComponent.getText()), 1);

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


    //given q, uses the derivative equation of Omega vs x to find RL1, X2 and
    // X3, which are the roots in (0, 1), (-infinity, 0) and (1, infinity).
    //The potentials at these points can then be found using the regular
    // OmegaR function. Area, volume and Req must be read from the table.
    public static double[] calculateValues(double q, double p) throws IOException, NumberFormatException, NullPointerException {
	double [] values;
	double [] tableValues;
	OmegaDerivativeFunction fun;


	//table does not deal with q<0.01, so we must get rid of that case
	if (q < MIN_Q) {
	    throw new NullPointerException();
	}

        //creates an array to store the 12 values to be found
	values = new double[NUM_OF_VALUES];
	tableValues = new double[NUM_OF_VALUES];

	//instantiates a new OmegaDerivativeFunction object, with the right q.
	// note that p is assumed to be 1.
	fun = new OmegaDerivativeFunction(q, p);


	//finds RL1, X2 and X3 using root-finding algorithm
	//bounds for RL1 are (0,1) and bounds for X2/X3 are [-1, 0) and (1, 2]
	values[RL1_INDEX] = Equations.findRoot(0.000001, .999999,fun);
	values[X2_INDEX] = Equations.findRoot(-1, .999999, fun);
	values[X3_INDEX] = Equations.findRoot(1.000001, 2, fun);

	//calculates the omega values at those points
	values[OMEGA1_INDEX] = calculateOmegaX(values[RL1_INDEX], q, p);
	values[OMEGA2_INDEX] = calculateOmegaX(values[X2_INDEX], q, p);
	values[OMEGA3_INDEX] = calculateOmegaX(values[X3_INDEX], q, p);


	//X2 is defined to be the one of X3 and X2 with the higher potential,
	// so we exchange the radii and potentials if necessary
	if (values[OMEGA2_INDEX] < values[OMEGA3_INDEX]) {
	    double temp;

	    //swap X2 and X3
	    temp = values[X2_INDEX];
	    values[X2_INDEX] = values[X3_INDEX];
	    values[X3_INDEX] = temp;

	    //swap Omega2 and Omega3
	    temp = values[OMEGA2_INDEX];
	    values[OMEGA2_INDEX] = values[OMEGA3_INDEX];
	    values[OMEGA3_INDEX] = temp;
	}


	//uses the polar coordinate calculator's method to calculate Rbk/Ry/Rz
	// note: since this is for the surface, F = 1.
	values[RBK_INDEX] = PolarCalcListener.calculateR(q, Math.PI/2, Math.PI, 1, p, values[RL1_INDEX], values[OMEGA1_INDEX]);
	values[RY_INDEX] = PolarCalcListener.calculateR(q, Math.PI/2, Math.PI/2, 1, p, values[RL1_INDEX], values[OMEGA1_INDEX]);
	values[RZ_INDEX] = PolarCalcListener.calculateR(q, 0, Math.PI/2, 1, p, values[RL1_INDEX], values[OMEGA1_INDEX]);


	//the remaining values (area, volume, Req) must be found using the table
	tableValues = readValues(q);
	values[AREA_INDEX] = tableValues[AREA_INDEX];
	values[VOL_INDEX] = tableValues[VOL_INDEX];
	values[REQ_INDEX] = tableValues[REQ_INDEX];

	return values;
    }


    //uses the static OmegaR function along the x-axis (r = |x|, theta
    //= PI/2 or 3*PI/2, phi = 0 or PI, p = p) to find Omegas 1, 2 and 3.
    //Separate cases adjust angles to deal with positive and negative radii.
    public static double calculateOmegaX(double x, double q, double p) {
	if (x < 0) {
	    return OmegaFunction.OmegaR(Math.abs(x), q, Math.PI/2, Math.PI, p);
	}
	else {
	    return OmegaFunction.OmegaR(x, q, Math.PI/2, 0, p);
	}
    }



    //given a q, uses table to fill in the entries of a values array, using
    //interpolation as necessary. Static method, so can be accessed without
    //specifically needing to create a CalcButtonListener object.
    public static double[] readValues(double q) throws IOException, NumberFormatException, NullPointerException {
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

        //creates an array to store the 12 values to be found
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
