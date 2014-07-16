/*
Author: Janet Leahy
Version: July 8, 2014
 */

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.JDialog;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Color;
import java.awt.Font;


public class MyTextDialog extends JDialog {

    public static final int WIDTH = 580;
    public static final int HEIGHT = 480;
    public static final int PADDING = 10;
    public static final int TEXT_FIELD_WIDTH = 15;
    public static final int BUTTON_WIDTH = 200;
    public static final int BUTTON_HEIGHT = 80;

    private JLabel FileNameLabel;
    private JTextField FileNameField;
    private JButton CalcButton;
    private JLabel ErrorLabel;
    private JLabel Specifications;
    private JRadioButton DegButton;
    private JRadioButton RadButton;
    private ButtonGroup ModeGroup;
    private JCheckBox InclusionBox;
    private GridBagLayout aLayout;
    private GridBagConstraints aConstraint;
    private TextCalcListener aTextListener;


    public MyTextDialog() {
	super();
	setTitle("Calculator for text files");
	setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
	setSize(WIDTH, HEIGHT);


	aConstraint = new GridBagConstraints();
	aConstraint.insets = new Insets(PADDING, PADDING, PADDING, PADDING);

	aLayout = new GridBagLayout();
	setLayout(aLayout);

	addAllWidgets();

	//creates an event listener, passing the address of the textField so
	//input can be read, as well as the address of the error label for
	// displaying messages, and this dialog, so that it can be closed
	// when the calculations are complete.
	aTextListener = new TextCalcListener(FileNameField, ErrorLabel, this);
	CalcButton.addActionListener(aTextListener);
	FileNameField.addActionListener(aTextListener);
    }


    //instantiates the buttons, labels, etc. and adds them to the layout
    public void addAllWidgets() {
	
	FileNameLabel = new JLabel("Enter a filename:");
	addWidget(FileNameLabel, 0,0,1,1);
	
	FileNameField = new JTextField(TEXT_FIELD_WIDTH);
	FileNameField.setText("Example.txt");
	addWidget(FileNameField, 0,1,1,1);

	CalcButton = new JButton("Generate output file");
	CalcButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
	addWidget(CalcButton, 1,0,1,2);

	ErrorLabel = new JLabel();
	ErrorLabel.setForeground(Color.RED);
	addWidget(ErrorLabel, 0,2,2,1);

	//creates the toggles allowing the user to choose between degrees
	//and radians, radians being the default
	RadButton = new JRadioButton("Radians");
	RadButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    aTextListener.setDegMode(false);
		}
	    });
	RadButton.setSelected(true);
	addWidget(RadButton, 0,3,1,1);

	DegButton = new JRadioButton("Degrees");
	DegButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    aTextListener.setDegMode(true);
		}
	    });
	addWidget(DegButton, 1,3,1,1);

	ModeGroup = new ButtonGroup();
	ModeGroup.add(RadButton);
	ModeGroup.add(DegButton);

	InclusionBox = new JCheckBox("Include q, theta and phi values in output file");
	InclusionBox.addItemListener(new ItemListener() {
		public void itemStateChanged(ItemEvent e) {
		    if (e.getStateChange() == ItemEvent.SELECTED) {
			aTextListener.setIncludeMode(true);
		    }
		    else {
			aTextListener.setIncludeMode(false);
		    }
		}
	    });
	addWidget(InclusionBox, 0, 4,2,1);

	Specifications = new JLabel("Specifications: ");
	Specifications.setFont(new Font("Serif", Font.BOLD, 18));
	addWidget(Specifications, 0,5,2,1);

	addWidget(new JLabel("- input file must be located in the same directory as the code"), 0,6,2,1);
	addWidget(new JLabel("- file must consist of three columns, separated by spaces"), 0,7,2,1);
	addWidget(new JLabel("- columns must be in the order: q, theta, phi"), 0,8,2,1);
	addWidget(new JLabel("- output file will be created in the same directory as the code"), 0,9,2,1);
	addWidget(new JLabel("- the name of the output file will be the input filename + Output: for example, \"test1Output.txt\""), 0,10,2,1);
    }



    //adds a single widget to the layout at (x, y) with width w and height h
    public void addWidget(Component widget, int x, int y, int w, int h) {
	aConstraint.gridx = x;
	aConstraint.gridy = y;
	aConstraint.gridwidth = w;
	aConstraint.gridheight = h;
	aLayout.setConstraints(widget, aConstraint);
	add(widget); //calls method of super class, adding widget to the dialog
    }

}
