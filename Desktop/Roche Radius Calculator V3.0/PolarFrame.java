/*
Author: Janet Leahy
Version: Aug. 4, 2014

Class for the secondary frame, set visible by a button in the main calculator frame. Given a value of q, user is prompted for omega and phi angles (spherical polar coordinates), from which the radial distance to the specified point on the surface of the Roche lobe, R, is calculated and displayed. User is also given the option of entering a fillout factor F, which by default is set to 1.
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
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;

public class PolarFrame extends JFrame {

    public static final int WIDTH = 870;
    public static final int HEIGHT = 650;
    public static final int DESC_WIDTH = 600;
    public static final int DESC_HEIGHT = 360;
    public static final int FILL_WIDTH = 680;
    public static final int FILL_HEIGHT = 470;
    public static final int NONSYNC_WIDTH = 650;
    public static final int NONSYNC_HEIGHT = 450;
    public static final int TEXT_BUTTON_HEIGHT = 100;
    public static final int TEXT_BUTTON_WIDTH = 200;

    public static final int PADDING = 10;
    public static final int TEXT_FIELD_WIDTH = 15;

    private GridBagConstraints aConstraint;
    private GridBagLayout aLayout;

    private JDialog DescDialog;
    private MyTextDialog TextDialog;
    private JDialog FillDialog;
    private JDialog NonSyncDialog;

    private JLabel QLabel;
    private JLabel ThetaLabel;
    private JLabel PhiLabel;
    private JLabel FLabel;
    private JLabel PLabel;
    private JTextField QField;
    private JTextField ThetaField;
    private JTextField PhiField;
    private JTextField FField;
    private JTextField PField;
    private JButton CalcButton;
    private JRadioButton DegButton;
    private JRadioButton RadButton;
    private ButtonGroup ModeGroup;
    private JRadioButton FButton;
    private JRadioButton PButton;
    private JLabel RadiusLabel;
    private JLabel ErrorLabel;
    private JLabel Diagram;
    private JButton TextFileButton;
    private JButton DescButton;
    private JButton FillButton;
    private JButton NonSyncButton;
    private PolarCalcListener aPolarCalcListener;

    public PolarFrame(String title) {
	super(title);
	setSize(WIDTH, HEIGHT);
	setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
	
	createDescDialog();
	createFillDialog();
	createNonSyncDialog();
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
	aPolarCalcListener = new PolarCalcListener(QField, ThetaField, PhiField, FField, PField, RadiusLabel, ErrorLabel);
	QField.addActionListener(aPolarCalcListener);
	ThetaField.addActionListener(aPolarCalcListener);
	PhiField.addActionListener(aPolarCalcListener);
	FField.addActionListener(aPolarCalcListener);
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


	FButton = new JRadioButton("Include fillout factor");
	FButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    if (FButton.isSelected()) {
			FLabel.setVisible(true);
			FField.setVisible(true);
		    } else {
			FLabel.setVisible(false);
			FField.setVisible(false);
			FField.setText("1.000");
		    }
		}
	    });
	addWidget(FButton, 0,3,2,1);

	FLabel = new JLabel("F: ");
	FLabel.setVisible(false);
	addWidget(FLabel, 0,4,1,1);

	FField = new JTextField(TEXT_FIELD_WIDTH);
	FField.setText("1.00");
	FField.setVisible(false);
	addWidget(FField, 1,4,1,1);


	PButton = new JRadioButton("Include non-synchronous rotation");
	PButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    if (PButton.isSelected()) {
			PLabel.setVisible(true);
			PField.setVisible(true);
		    } else {
			PLabel.setVisible(false);
			PField.setVisible(false);
			PField.setText("1.000");
		    }
		}
	    });
	addWidget(PButton, 0,5,2,1);

	PLabel = new JLabel("P: ");
	PLabel.setVisible(false);
	addWidget(PLabel, 0,6,1,1);

	PField = new JTextField(TEXT_FIELD_WIDTH);
	PField.setText("1.00");
	PField.setVisible(false);
	addWidget(PField, 1,6,1,1);


	//creates the toggles allowing the user to choose between degrees
	//and radians, radians being the default
	RadButton = new JRadioButton("Radians");
	RadButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    aPolarCalcListener.setDegMode(false);
		}
	    });
	RadButton.setSelected(true);
	addWidget(RadButton, 0,7,1,1);

	DegButton = new JRadioButton("Degrees");
	DegButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    aPolarCalcListener.setDegMode(true);
		}
	    });
	addWidget(DegButton, 1,7,1,1);

	ModeGroup = new ButtonGroup();
	ModeGroup.add(RadButton);
	ModeGroup.add(DegButton);


	CalcButton = new JButton("Calculate radius");
	addWidget(CalcButton, 0,9,2,1);
	
	ErrorLabel = new JLabel("");
	ErrorLabel.setForeground(Color.RED);
	addWidget(ErrorLabel, 0,8,2,1);

	RadiusLabel = new JLabel("R(theta, phi): ");
	RadiusLabel.setFont(new Font("Serif", Font.BOLD, 18));
	addWidget(RadiusLabel, 0,10,2,1);

	Diagram = new JLabel(new ImageIcon("PolarDiagram.png"));
	addWidget(Diagram, 2,0,2,9);

	DescButton = new JButton("Description of calculations");
	DescButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    DescDialog.setVisible(true);
		}
	    });
	addWidget(DescButton, 3,9,1,1);


	TextFileButton = new JButton("Calculator for text files");
	TextFileButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    TextDialog.setVisible(true);
		}
	    });
	TextFileButton.setPreferredSize(new Dimension(TEXT_BUTTON_WIDTH, TEXT_BUTTON_HEIGHT));
	addWidget(TextFileButton, 2,9,1,3);

	FillButton = new JButton("Explanation of fillout factor");
	FillButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
		    FillDialog.setVisible(true);
		}
	    });
	addWidget(FillButton, 3,10,1,1);
    
	NonSyncButton = new JButton("Explanation of non-synchronous rotation");
	NonSyncButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent c) {
		    NonSyncDialog.setVisible(true);
		}
	    });
	addWidget(NonSyncButton, 3,11,1,1);

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
	header.setBounds(0, 15, DESC_WIDTH, 30);
	DescDialog.add(header);

	DescText.setBounds(0, 15, DESC_WIDTH-10, DESC_HEIGHT-30);
	DescDialog.add(DescText);
    }


    //creates the dialog that displays an explanation of the fillout factor.
    // summoned by FillButton
    public void createFillDialog() {
	JLabel FillText = new JLabel(new ImageIcon("FilloutFactorDescription.png"));
	FillDialog = new JDialog();
	FillDialog.setTitle("Explanation of fillout factor");
	FillDialog.setSize(FILL_WIDTH, FILL_HEIGHT);
	FillDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
	FillDialog.setLayout(null);

	JLabel header = new JLabel("Explanation of fillout factor", JLabel.CENTER);
	header.setFont(new Font("Serif", Font.BOLD, 22));
	header.setBounds(0, 10, FILL_WIDTH, 22);
	FillDialog.add(header);

	FillText.setBounds(0, 25, FILL_WIDTH-10, FILL_HEIGHT-35);
	FillDialog.add(FillText);
    }


    //creates a dialog containing an explanation of non-synchronous rotation
    // summoned by NonSyncButton
    public void createNonSyncDialog() {
	JLabel NonSyncText = new JLabel(new ImageIcon("NonSynchDescription.PNG"));

	NonSyncDialog = new JDialog();
	NonSyncDialog.setTitle("Explanation of non-synchronous rotation");
	NonSyncDialog.setSize(NONSYNC_WIDTH, NONSYNC_HEIGHT);
	NonSyncDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
	NonSyncDialog.setLayout(null);

	JLabel header = new JLabel("Explanation of non-synchronous rotation", JLabel.CENTER);
	header.setFont(new Font("Serif", Font.BOLD, 22));
	header.setBounds(0, 10, NONSYNC_WIDTH, 30);
	NonSyncDialog.add(header);

	NonSyncText.setBounds(0, 25, NONSYNC_WIDTH-10, NONSYNC_HEIGHT-40);
	NonSyncDialog.add(NonSyncText);


    }

}
