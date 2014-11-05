/*
Author: Janet Leahy
Version: July 8, 2014
 */

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import javax.swing.ImageIcon;
import java.awt.Container;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import java.awt.Color;
import java.awt.Font;
import java.awt.Dimension;

public class MainFrame extends JFrame {

    public static final int WIDTH = 1100;
    public static final int HEIGHT = 600;
    public static final int PADDING = 10;
    public static final int QFIELD_WIDTH = 10;
    public static final int DESC_WIDTH = 620;
    public static final int DESC_HEIGHT = 460;
    public static final int COMP_WIDTH = 640;
    public static final int COMP_HEIGHT = 750;
    public static final int ABOUT_WIDTH = 250;
    public static final int ABOUT_HEIGHT = 350;
    public static final int P_BUTTON_WIDTH = 190;
    public static final int P_BUTTON_HEIGHT = 100;

    private PolarFrame SecondFrame;

    private JDialog DescDialog;
    private JDialog CompareDialog;
    private JDialog AboutDialog;

    private JLabel Graph;
    private JLabel Diagram;
    private JLabel EnterLabel;
    private JLabel ErrorLabel;
    private JTextField QField;
    private JButton CalcButton;
    private JButton DescButton;
    private JButton CompareButton;
    private JButton PolarButton;
    private JButton AboutButton;
    private CalcButtonListener aCalcButtonListener;
    private MyValuesContainer ValuesContainer;
    private GridBagConstraints aConstraint;
    private GridBagLayout aLayout;

    
    //constructor
    public MainFrame(String title, PolarFrame SecondFrame) {
	super(title);
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setSize(WIDTH, HEIGHT);
	
	this.SecondFrame = SecondFrame;
	
	createDescDialog();
	createCompareDialog();
	createAboutDialog();

	aConstraint = new GridBagConstraints();
	aConstraint.insets = new Insets(PADDING, PADDING, PADDING, PADDING);

	aLayout = new GridBagLayout();
	setLayout(aLayout);

	addAllWidgets();

	//creates the actual calculating class and links it to the text field
	// and the calculate button. Passes the address of the ErrorLabel as
	//an argument so that appropriate error messages can be displayed, as
	//well as the ValuesContainer so the calculated values can be displayed.
	aCalcButtonListener = new CalcButtonListener(QField, ErrorLabel, ValuesContainer);
	QField.addActionListener(aCalcButtonListener);
	CalcButton.addActionListener(aCalcButtonListener);
    
    }


    //creates and adds the buttons, labels, etc. to the frame
    public void addAllWidgets() {  
	Graph = new JLabel(new ImageIcon("Graph.png"));
	addWidget(Graph, 0,0,2,3);

	Diagram = new JLabel(new ImageIcon("Diagram.png"));
	addWidget(Diagram, 2,0,3,1);

	ErrorLabel = new JLabel();
	addWidget(ErrorLabel, 2,1,3,1);
	ErrorLabel.setForeground(Color.RED);

	EnterLabel = new JLabel("Enter a value for q: ");
	addWidget(EnterLabel, 2,2,1,1);

	QField = new JTextField(QFIELD_WIDTH);
	addWidget(QField, 3,2,1,1);

	CalcButton = new JButton("Calculate");
	addWidget(CalcButton, 4,2,1,1);

	DescButton = new JButton("Description of calculations");
	DescButton.addActionListener(new ActionListener()
	    {
		public void actionPerformed(ActionEvent e) {
		    DescDialog.setVisible(true);
		}
	    });
	addWidget(DescButton, 0,3,1,1);

	CompareButton = new JButton("Comparison to fitting formula");
	CompareButton.addActionListener(new ActionListener()
	    {
		public void actionPerformed(ActionEvent e) {
		    CompareDialog.setVisible(true);
		}
	    });
	addWidget(CompareButton, 0,4,1,1);

	PolarButton = new JButton("Polar coordinate calculator");
	PolarButton.addActionListener(new ActionListener()
	    {
		public void actionPerformed(ActionEvent e) {
		    SecondFrame.setVisible(true);
		}
	    });
	PolarButton.setPreferredSize(new Dimension(P_BUTTON_WIDTH, P_BUTTON_HEIGHT));
	addWidget(PolarButton, 1,3,1,3);

	AboutButton = new JButton("About software");
	AboutButton.addActionListener(new ActionListener()
	    {
		public void actionPerformed(ActionEvent e) {
		    AboutDialog.setVisible(true);
		}
	    });
	addWidget(AboutButton, 0,5,1,1);

	ValuesContainer = new MyValuesContainer();
	addWidget(ValuesContainer, 2,3,3,3);

    }


    //adds a single widget to the layout at (x, y) with width w and height h
    public void addWidget(Component widget, int x, int y, int w, int h) {
	aConstraint.gridx = x;
	aConstraint.gridy = y;
	aConstraint.gridwidth = w;
	aConstraint.gridheight = h;
	aLayout.setConstraints(widget, aConstraint);
	add(widget); //calls method of super class, adding widget to MainFrame
    }


    //creates the "description" dialog box, with text stored in an image.
    public void createDescDialog() {
	JLabel DescText = new JLabel(new ImageIcon("DescriptionText.png"));
        
	DescDialog = new JDialog();
	DescDialog.setTitle("Description of calculations");
	DescDialog.setSize(DESC_WIDTH, DESC_HEIGHT);
	DescDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
	DescDialog.setLayout(null);

	JLabel header = new JLabel("Description of calculations", JLabel.CENTER);
	header.setFont(new Font("Serif", Font.BOLD, 22));
	header.setBounds(0, 15, DESC_WIDTH, 25);
	DescDialog.add(header);

	DescText.setBounds(0, 5, DESC_WIDTH, DESC_HEIGHT-20);
	DescDialog.add(DescText);

    }


    //creates the "comparison" dialog box, with text and graph. Layout is
    //handled manually (painfully)
    public void createCompareDialog() {
	JLabel CompareGraph = new JLabel(new ImageIcon("ComparisonGraph.png"));
	JLabel CompareText = new JLabel(new ImageIcon("ComparisonText.png"));

	CompareDialog = new JDialog();
	CompareDialog.setTitle("Comparison to fitting formula");
	CompareDialog.setSize(COMP_WIDTH, COMP_HEIGHT);
	CompareDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
	CompareDialog.setLayout(null);

	JLabel header = new JLabel("Comparison to fitting formula", JLabel.CENTER);
	header.setFont(new Font("Serif", Font.BOLD, 22));
	header.setBounds(0, 15, COMP_WIDTH, 30);
	CompareDialog.add(header);

	CompareGraph.setBounds(0, 45, COMP_WIDTH, 350); 
	CompareDialog.add(CompareGraph);

	CompareText.setBounds(0, 390, COMP_WIDTH, 320);
	CompareDialog.add(CompareText);

    }



    //creates the "about" dialog box, containing program info
    public void createAboutDialog() {
	AboutDialog = new JDialog();
	AboutDialog.setTitle("About software");
	AboutDialog.setSize(ABOUT_WIDTH, ABOUT_HEIGHT);
	AboutDialog.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);

	GridLayout GLayout = new GridLayout(14,1);
	AboutDialog.setLayout(GLayout);

	JLabel header = new JLabel("Roche Radius Calculator", JLabel.CENTER);
	header.setFont(new Font("Serif", Font.BOLD, 18));
	AboutDialog.add(header);

	JLabel version = new JLabel("Version " + Driver.VERSION, JLabel.CENTER);
	AboutDialog.add(version);
	AboutDialog.add(new JLabel());

	JLabel authors = new JLabel("Authors:     ", JLabel.CENTER);
	authors.setFont(new Font("Serif", Font.BOLD, 14));
	AboutDialog.add(authors);

	AboutDialog.add(new JLabel());
	AboutDialog.add(new JLabel("Main calculations: ", JLabel.CENTER));
	AboutDialog.add(new JLabel("Denis Leahy", JLabel.CENTER));
	AboutDialog.add(new JLabel("Dept. Physics and Astronomy", JLabel.CENTER));
	AboutDialog.add(new JLabel("University of Calgary", JLabel.CENTER));
	AboutDialog.add(new JLabel("Calgary, Canada", JLabel.CENTER));
	AboutDialog.add(new JLabel("leahy@ucalgary.ca", JLabel.CENTER));

	AboutDialog.add(new JLabel());
	AboutDialog.add(new JLabel("Java Programming:", JLabel.CENTER));
	AboutDialog.add(new JLabel("Janet Leahy", JLabel.CENTER));

    }

}
