/*
Author: Janet Leahy
Version: July 8, 2014


Program runs a GUI-based calculator for Roche radii, associated potentials and other relevant values relating to the Roche surface.
The user is prompted for a value for q (mass ratio of the stars, M2/M1), which is then used to provide values for RL1=X1, X2, X3,
Omega1, Omega2, Omega3 (potentials at X1, X2 and X3, respectively), Rbk, Ry, Rz, Area, Volume and Req. These values are found using
a pre-calculated table. If the input value of q is not found in the table, the output values are calculated by linear interpolation.
Additional buttons are provided to display information: a description of the calculations, a comparison to the Eggleton formula and
the version/authors of the software.

A second calculator is available, accessed through a button on the main window. Here, the user is prompted for three values: the
mass ratio q, and the angle theta, phi, in polar coordinates. The calculator returns the radial distance to the surface of the
Roche lobe at the given angle. This value is found by substituting q, theta, phi and Omega1 values (found by the main calculator)
into an equation and solving for the root, R. Because the root-finding formula fails in a small region about the point RL1, if
theta and phi are within a degree of (90, 0), the radius RL1 is simply returned. The user is permitted to choose between using
degrees and radians. Again, there is a button on the window that provides a more detailed description of the calculations. 


The table of pre-calculated values, "RocheTable.txt", can also be viewed directly. The columns, from left to right, are: q, RL1,
Omega1, X2, Omega2, X3, Omega3, Rbk, Ry, Rz, Area, Volume and Req.

Note:
- all files must remain with original names in the same folder as the code
- the value of q entered must be between 0.01 and 100 inclusive
- the output values displayed are all rounded half-up to 7 decimal places, as the numbers in the table have only 7 decimal places.
- for a filename to be input in the second calculator, the file must be a .txt file, located in the same directory as the code.


Latest Version 2.1 updates:
Second, R(q, theta, phi), calculator now has the ability to take a .txt file as input. Given a filename, for example "test1.txt",
with three columns of entries (in the order q, theta, phi) separated by spaces, the program will create a new file,
"test1Output.txt" whose single column consists of the calculated R(q, theta, phi) value for each row. If desired, the values
taken for q, theta and phi can be copied into the output file as well.


~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

For questions, contact:
Prof. Denis Leahy
Dept. of Physics and Astronomy
University of Calgary
leahy@ucalgary.ca

 */

public class Driver {

    public static final String VERSION = "2.1, July 2014";

    public static void main (String[] args) {
	//instantiates the secondary frame
	PolarFrame aPolarFrame = new PolarFrame("Polar coordinate calculator");
	//instantiates the main frame, passing it the address of the
	//secondary frame, and makes it visible
	    MainFrame aFrame = new MainFrame("Roche Radius Calculator", aPolarFrame);
	aFrame.setVisible(true);

    }

}
