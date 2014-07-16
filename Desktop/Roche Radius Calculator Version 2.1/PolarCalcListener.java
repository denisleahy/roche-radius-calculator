/*
Author: Janet Leahy
Version: July 5, 2014

Class containing code that responds to the "calculate radius" button being pressed in the polar coordinate calculator.
 */

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.text.DecimalFormat;
import java.lang.NumberFormatException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.lang.Math;

public class PolarCalcListener implements ActionListener {

    //if theta-90 and phi are less than this (in radians), RL1 is returned.
    public static final double RANGE = .02;  //approx. 1 degree

    private boolean degMode;

    private JTextField QField;
    private JTextField ThetaField;
    private JTextField PhiField;
    private JLabel RadiusLabel;
    private JLabel ErrorLabel;
    private DecimalFormat f;


    public PolarCalcListener(JTextField QField, JTextField ThetaField, JTextField PhiField, JLabel RadiusLabel, JLabel ErrorLabel) {
	this.QField = QField;
	this.ThetaField = ThetaField;
	this.PhiField = PhiField;
	this.RadiusLabel = RadiusLabel;
	this.ErrorLabel = ErrorLabel;

	//sets the desired number of decimal places (now 7)
	f = new DecimalFormat("##.0000000");
	
    }

    //this method is called by the radio buttons in the polar frame
    public void setDegMode(boolean setting) {
	degMode = setting;
    }


    public void actionPerformed(ActionEvent e) {
	double q;
	double theta;
	double phi;
	double radius;

	try {
	    q = Double.parseDouble(QField.getText());
	    theta = Double.parseDouble(ThetaField.getText());
	    phi = Double.parseDouble(PhiField.getText());

	    radius = findR(q, theta, phi, degMode);

	    //displays the result
	    RadiusLabel.setText("R(theta, phi):  " + f.format(radius));

	    //resets the ErrorLabel
	    ErrorLabel.setText("");
	}
	catch(NumberFormatException ex) {
	    ErrorLabel.setText("All values entered must be numeric.");
	}
	catch(NullPointerException ex) {
	    ErrorLabel.setText("The value for q is outside the bounds of the table.");
	}
	catch(ThetaOutOfBoundsException ex) {
	    String range;
	    if (degMode) {range = "0 and 180 degrees";}
	    else {range = "0 and pi radians";}
	    ErrorLabel.setText("The value for theta must be between "+range);
	}
	catch(IOException ex) {
	    ex.printStackTrace();
	}
    }



    public static double findR(double q, double theta, double phi, boolean degModeOn) throws NumberFormatException, NullPointerException, ThetaOutOfBoundsException, IOException {
	double [] values;
	double lowerBound;
	double upperBound;
	double omega1;
	double radius;

	//converts degrees to radians if degree mode was selected
	if (degModeOn) {
	    theta = theta*Math.PI/180;
	    phi = phi*Math.PI/180;
	}

	//ensures theta is in the correct range
	if (theta%(Math.PI*2) > Math.PI) {
	    throw new ThetaOutOfBoundsException();
	}

	//calls the static method of CalcButtonListener for the input q.
	//method returns an array of pertinent values, from which we can
        //access the ones we need.
        values = CalcButtonListener.findValues(q);

	//because of the nature of the equation, the root finding method
	//does not work at coordinates very close to the point, so we must
	//deal with this case seperately, simply returning RL1.
	if (Math.abs((theta-Math.PI/2)%(Math.PI*2))<RANGE && Math.abs(phi%(Math.PI*2))<RANGE) {
	    radius = values[CalcButtonListener.RL1_INDEX];
	}

	else {
	    //the radius will be between Rz and RL1 inclusive, so we will set
	    //these bounds to be .9Rz and 1.1RL1
	    lowerBound = .99*values[CalcButtonListener.RZ_INDEX];
	    upperBound = 1.01*values[CalcButtonListener.RL1_INDEX];
	    omega1 = values[CalcButtonListener.OMEGA1_INDEX];
	    
	    //finds the root of the equation using Brent's method in Equations
	    radius = Equations.findRoot(lowerBound, upperBound, q, theta, phi, omega1);
	    }

	return radius;
    }



}
