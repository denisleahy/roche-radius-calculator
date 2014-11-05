/*
Author: Janet Leahy
Version: Aug.1, 2014


Program runs a GUI-based calculator for Roche radii, associated potentials and other relevant values relating to the Roche surface.
The user is prompted for a value for q (mass ratio of the stars, M2/M1), which is then used to provide values for RL1=X1, X2, X3,
Omega1, Omega2, Omega3 (potentials at X1, X2 and X3, respectively), Rbk, Ry, Rz, Area, Volume and Req. The first 9 of these values
are found by solving for the roots of appropriate equations, resulting in exact values. The remaining values, Area, Volume and Req,
are found using a pre-calculated table. If the input value of q is not found in the table, the output values for these properties 
are calculated using linear interpolation. Additional buttons are provided to display information: a description of the calculations,
a comparison to the Eggleton formula and the version/authors of the software.

A second calculator is available, accessed through a button on the main window. Here, the user is prompted for three values: the
mass ratio q, and the angle theta, phi, in polar coordinates. If desired, a fillout factor F, with range [0.1, 1], and/or a
non-synchronous rotation factor p, with range [0, 2], can be input.
The calculator returns the radial distance to the surface (or sub-surface, if provided with an F) of the
Roche lobe at the given angle. This value is found by substituting q, theta, phi, F, p and Omega1 values
into an equation and solving for the root, R. Because the root-finding formula fails in a small region about the point RL1, if
theta and phi are within a degree of (90, 0), the radius RL1 is simply returned. The user is permitted to choose between using
degrees and radians. Again, there are buttons on the window that provides a more detailed description of the calculations. 

From the second calculator widow, there is the option of inputting a table of q, theta and phi values. If desired, F and/or p columns
can also be read from the file. The program will output a table of corresponding R(q, theta, phi) values as a separate file. If
the name of the input file is "Example.txt", the name of the output file will be "ExampleOutput.txt". Note that, unless provided,
the fillout factor F and rotation factor p are both assumed to be 1.

The table of pre-calculated values, "RocheTable.txt", can also be viewed directly. The columns, from left to right, are: q, RL1,
Omega1, X2, Omega2, X3, Omega3, Rbk, Ry, Rz, Area, Volume and Req.

Note:
- all files must remain with original names in the same folder as the code
- the value of q entered must be between 0.01 and 100 inclusive
- the value of the fillout factor F must be between 0.1 and 1 inclusive
- the output values displayed are all rounded half-up to 7 decimal places, as the numbers in the table have only 7 decimal places.
- for a filename to be input in the second calculator, the file must be a .txt file, located in the same directory as the code.
- when a filename is input, the columns must be in the order q, theta, phi, (F), (p). F is always the fourth column, while p can
  be either the fourth or fifth column, depending on whether or not F is included.


Latest Version 3.0 updates:
-includes fillout factor F in the polar coordinate calculator and text file calculator
-includes non-synchronous rotation factor p in both the polar coordinate calulator and the text file calculator
-all values, with the exception of Area, Volumn and Req, are now found directly using root-finding of equations, rather than being
  interpolated from a pre-calculated table. As a result, error due to interpolation is eliminated (except for Area Vol and Req).



~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

For questions, contact:
Prof. Denis Leahy
Dept. of Physics and Astronomy
University of Calgary
leahy@ucalgary.ca

 */

public class Driver {

    public static final String VERSION = "3.0, Aug. 2014";

    public static void main (String[] args) {
	//instantiates the secondary frame
	PolarFrame aPolarFrame = new PolarFrame("Polar coordinate calculator");
	//instantiates the main frame, passing it the address of the
	//secondary frame, and makes it visible
	    MainFrame aFrame = new MainFrame("Roche Radius Calculator", aPolarFrame);
	aFrame.setVisible(true);

    }

}
