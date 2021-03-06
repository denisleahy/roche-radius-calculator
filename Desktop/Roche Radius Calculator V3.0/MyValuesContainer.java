/*
Author: Janet Leahy
Version: June 26, 2014

Class for a 4x3 container of labels, displaying the 12 calculated Roche values.
Modified by the CalcButtonListener class and instantiated+displayed by the
MyFrame class.
 */

import java.awt.Container;
import java.awt.Component;
import javax.swing.JLabel;
import java.awt.GridLayout;


public class MyValuesContainer extends Container {

    private JLabel RL1;
    private JLabel Omega1;
    private JLabel X2;
    private JLabel Omega2;
    private JLabel X3;
    private JLabel Omega3;
    private JLabel Rbk;
    private JLabel Ry;
    private JLabel Rz;
    private JLabel Area;
    private JLabel Vol;
    private JLabel Req;
    //private JLabel ReqE;
    //private JLabel VolE;
    
    private GridLayout aLayout;


    public MyValuesContainer() {
	aLayout = new GridLayout(4,3,15,15);
	setLayout(aLayout);

	//instantiates the labels
	RL1 = new JLabel("RL1:      ");
	Omega1 = new JLabel("Omega1:      ");
	X2 = new JLabel("X2:      ");
	Omega2 = new JLabel("Omega2:      ");
	X3 = new JLabel("X3:      ");
	Omega3 = new JLabel("Omega3:      ");
	Rbk = new JLabel("Rbk:      ");
	Ry = new JLabel("Ry:      ");
	Rz = new JLabel("Rz:      ");
	Area = new JLabel("Area:      ");
	Vol = new JLabel("Vol:      ");
	Req = new JLabel("Req:      ");
	//ReqE = new JLabel("ReqE:  ");
	//VolE = new JLabel("VolE:  ");
	
	//adds the labels to the grid
	add(RL1);
	add(X2);
	add(X3);
	add(Omega1);
	add(Omega2);
	add(Omega3);
	add(Rbk);
	add(Ry);
	add(Rz);
	add(Area);
	add(Vol);
	add(Req);
	//add(new JLabel("Calculated from Eggleton"));
	//add(new JLabel("  -------------------"));
	//add(VolE);
	//add(ReqE);

    }


    //set methods for the variable labels, to update the displayed values
    public void setRL1(String newValue) {
	RL1.setText("RL1:  " + newValue);
    }

    public void setOmega1(String newValue) {
	Omega1.setText("Omega1:  " + newValue);
    }

    public void setX2(String newValue) {
	X2.setText("X2:  " + newValue);
    }

    public void setOmega2(String newValue) {
	Omega2.setText("Omega2:  " + newValue);
    }

    public void setX3(String newValue) {
	X3.setText("X3:  " + newValue);
    }

    public void setOmega3(String newValue) {
	Omega3.setText("Omega3:  " + newValue);
    }

    public void setRbk(String newValue) {
	Rbk.setText("Rbk:  " + newValue);
    }

    public void setRy(String newValue) {
	Ry.setText("Ry:  " + newValue);
    }

    public void setRz(String newValue) {
	Rz.setText("Rz:  " + newValue);
    }

    public void setArea(String newValue) {
	Area.setText("Area:  " + newValue);
    }

    public void setReq(String newValue) {
	Req.setText("Req:  " + newValue);
    }

    public void setVol(String newValue) {
	Vol.setText("Vol:  " + newValue);
    }

    /*
    public void setReqE(String newValue) {
	ReqE.setText("ReqE: " + newValue);
    }

    public void setVolE(String newValue) {
	VolE.setText("VolE: " + newValue);
	}*/



}
