/*
Author: Janet Leahy
Version: July 8, 2014

 */

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.text.JTextComponent;
import javax.swing.JDialog;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.lang.NullPointerException;
import java.text.DecimalFormat;

public class TextCalcListener implements ActionListener {

    public static final String APPEND = "Output.txt";

    public static final int Q_INDEX = 0;
    public static final int THETA_INDEX = 1;
    public static final int PHI_INDEX = 2;
    public static final int NUM_INPUTS = 3; //q, theta and phi

    private JLabel ErrorLabel;
    private JTextComponent InputField;
    private JDialog InputDialog;
    private FileReader fr;
    private BufferedReader br;
    private FileWriter fw;
    private PrintWriter pw;
    private DecimalFormat f;

    private boolean degModeOn;
    private boolean includeModeOn;


    public TextCalcListener(JTextComponent anInputField, JLabel anErrorLabel, JDialog anInputDialog) {
	this.InputField = anInputField;
	this.ErrorLabel = anErrorLabel;
	this.InputDialog = anInputDialog;

	degModeOn = false;
	includeModeOn = false;

	f = new DecimalFormat("##.0000000"); //rounds to 7 decimal places
    }


    public void setDegMode(boolean newMode) {
	this.degModeOn = newMode;
    }

    public void setIncludeMode(boolean newMode) {
	this.includeModeOn = newMode;
    }


    public void actionPerformed(ActionEvent e) {
	String filename;
	String outputFilename;
	String tempLine;
	double tempRadius;
	String [] splitLine;
	double [] inputValues;
	
	try {
	    filename = InputField.getText();
	    fr = new FileReader(filename);
	    br = new BufferedReader(fr);

	    outputFilename = filename.substring(0, filename.indexOf('.')) + APPEND;
	    fw = new FileWriter(outputFilename);
	    pw = new PrintWriter(fw);

	    inputValues = new double[NUM_INPUTS];

	    tempLine = br.readLine();

	    //reads the file line-by-line, finding and writing the radii,
	    //until the end of the document is reached
	    while (tempLine != null) {
		splitLine = tempLine.split("[ ]+");
		for (int i=0; i<NUM_INPUTS; i++) {
		    //converts the values into double form and stores them in
		    //inputValues array.
		    inputValues[i] = Double.parseDouble(splitLine[i]);
		}

		//if the user desires, the columns from the old table are
		//included in the output file
		if (includeModeOn) {
		    pw.print(tempLine);
		    pw.print("  ");
		}

		//finds the radius based on the values read
		tempRadius = PolarCalcListener.findR(inputValues[Q_INDEX], inputValues[THETA_INDEX], inputValues[PHI_INDEX], degModeOn);
		//writes the radius to the output file
		pw.println(f.format(tempRadius));

		//moves tempLine forward by 1 line
		tempLine = br.readLine();
	    }

	    //disposes of the file readers and writers, no longer needed
	    fr.close();
	    fw.close();

	    //clears error label after a successful round -not strictly necessary
	    ErrorLabel.setText("");
	    //hides the input dialog, which is (presumably) no longer needed
	    InputDialog.setVisible(false);

	}
	catch (FileNotFoundException ex) {
	    ErrorLabel.setText("The file could not be found in the current directory");
	}
	catch (IOException ex) {
	    ex.printStackTrace();
	}
	catch (NullPointerException ex) {
	    ErrorLabel.setText("Please enter a filename");
	}
	catch (NumberFormatException ex) {
	    ErrorLabel.setText("The values in the table must be numeric");
	}
	catch(ThetaOutOfBoundsException ex) {
	    String range;
	    if (degModeOn) {range = "0 and 180 degrees";}
	    else {range = "0 and pi radians";}
	    ErrorLabel.setText("The value for theta must be between "+range);
	}

    }


}
