/*
Author: Janet Leahy
Version: July 8, 2014

Class for the secondary frame, set visible by a button in the main calculator frame. Given a value of q, user is prompted for omega and phi angles (spherical polar coordinates), from which the radial distance to the specified point on the surface of the Roche lobe, R, is calculated and displayed.
 */

import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.ButtonGroup;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.ImageIcon;
import java.awt.Component;
import java.awt.Color;
import java.awt.Font;

public class PolarFrame extends JFrame {

    public static final int WIDTH = 850;
    public static final int HEIGHT = 500;
    public static final int DESC_WIDTH = 630;
    public static final int DESC_HEIGHT = 420;
    public static final int PADDING = 10;
    public static final int TEXT_FIELD_WIDTH = 15;

    private GridBagConstraints aConstraint;
    private GridBagLayout aLayout;

    private JDialog DescDialog;
    private MyTextDialog TextDialog;

    private JLabel QLabel;
    private JLabel ThetaLabel;
    private JLabel PhiLabel;
    private JTextField QField;
    private JTextField ThetaField;
    private JTextField PhiField;
    private JButton CalcButton;
    private JRadioButton DegButton;
    private JRadioButton RadButton;
    private ButtonGroup ModeGroup;
    private JLabel RadiusLabel;
    private JLabel ErrorLabel;
    private JLabel Diagram;
    private JButton TextFileButton;
    private JButton DescButton;
    private PolarCalcListener aPolarCalcListener;

    public PolarFrame(String title) {
	super(title);
	setSize(WIDTH, HEIGHT);
	setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	
	createDescDialog();
	TextDialog = new MyTextDialog();

	aConstraint = new GridBagConstraints();
	aConstraint.insets = new Insets(PADDING, PADDING, PADDING, PADDING);

	aLayout = new GridBagLayout();
	setLayout(aLayout);

	addAllWidgets();

	//creates the actual calculating class and links it to the text fields
	// and the calculate button. Passes the address of the RadiusLabel as
	//an argument so that results can be displayed. ErrorLabel will
	//display any potential error messages.
	aPolarCalcListener = new PolarCalcListener(QField, ThetaField, PhiField, RadiusLabel, ErrorLabel);
	QField.addActionListener(aPolarCalcListener);
	ThetaField.addActionListener(aPolarCalcListener);
	PhiField.addActionListener(aPolarCalcListener);
	CalcButton.addActionListener(aPolarCalcListener);
    }


    //creates all the buttons, fields, etc. and adds them to the PolarFrame
    public void addAllWidgets() {
	QLabel = new JLabel("Q: ");
	addWidget(QLabel, 0,0,1,1);

	QField = new JTextField(TEXT_FIELD_WIDTH);
	addWidget(QField, 1,0,1,1);

	ThetaLabel = new JLabel("Theta: ");
	addWidget(ThetaLabel, 0,1,1,1);

	ThetaField = new JTextField(TEXT_FIELD_WIDTH);
	addWidget(ThetaField, 1,1,1,1);

	PhiLabel = new JLabel("Phi: ");
	addWidget(PhiLabel, 0,2,1,1);

	PhiField = new JTextField(TEXT_FIELD_WIDTH);
	addWidget(PhiField, 1,2,1,1);

	CalcButton = new JButton("Calculate radius");
	addWidget(CalcButton, 0,3,2,1);
	
	ErrorLabel = new JLabel("");
	ErrorLabel.setForeground(Color.RED);
	addWidget(ErrorLabel, 0,4,2,1);

	RadiusLabel = new JLabel("R(theta, phi): ");
	RadiusLabel.setFont(new Font("Serif", Font.BOLD, 18));
	addWidget(RadiusLabel, 0,5,2,1);

	Diagram = new JLabel(new ImageIcon("PolarDiagram.png"));
	addWidget(Diagram, 2,0,1,6);

	DescButton = new JButton("Description of calculations");
	DescButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    DescDialog.setVisible(true);
		}
	    });
	addWidget(DescButton, 2,6,1,1);

	//creates the toggles allowing the user to choose between degrees
	//and radians, radians being the default
	RadButton = new JRadioButton("Radians");
	RadButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    aPolarCalcListener.setDegMode(false);
		}
	    });
	RadButton.setSelected(true);
	addWidget(RadButton, 0,6,1,1);

	DegButton = new JRadioButton("Degrees");
	DegButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    aPolarCalcListener.setDegMode(true);
		}
	    });
	addWidget(DegButton, 1,6,1,1);

	ModeGroup = new ButtonGroup();
	ModeGroup.add(RadButton);
	ModeGroup.add(DegButton);

	TextFileButton = new JButton("Calculator for text files");
	TextFileButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    TextDialog.setVisible(true);
		}
	    });
	addWidget(TextFileButton, 2,7,1,1);

    }



    //adds a single widget to the layout at (x, y) with width w and height h
    public void addWidget(Component widget, int x, int y, int w, int h) {
	aConstraint.gridx = x;
	aConstraint.gridy = y;
	aConstraint.gridwidth = w;
	aConstraint.gridheight = h;
	aLayout.setConstraints(widget, aConstraint);
	add(widget); //calls method of super class, adding widget to PolarFrame
    }


    //creates the description dialog, which can be summoned by the DescButton
    public void createDescDialog() {
	JLabel DescText = new JLabel(new ImageIcon("PolarDescText.png"));
        
	DescDialog = new JDialog();
	DescDialog.setTitle("Description of calculations");
	DescDialog.setSize(DESC_WIDTH, DESC_HEIGHT);
	DescDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
	DescDialog.setLayout(null);

	JLabel header = new JLabel("Description of calculations", JLabel.CENTER);
	header.setFont(new Font("Serif", Font.BOLD, 22));
	header.setBounds(0, 15, DESC_WIDTH, 22);
	DescDialog.add(header);

	DescText.setBounds(0, 25, DESC_WIDTH, DESC_HEIGHT-35);
	DescDialog.add(DescText);

    }


}
