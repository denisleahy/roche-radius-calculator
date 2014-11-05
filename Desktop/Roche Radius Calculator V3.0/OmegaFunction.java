/*
Author: Janet Leahy
Version: July 31, 2014

Class implementing function interface. Constructor is passed (assumed valid) values for q, theta, phi, F and p. Using these and a provided radius r, the class's function returns the potential, omega, at the polar coordinate r, theta, phi, with mass ratio q, fillout factor F and non-synchronous rotation factor p.
 */

import java.lang.Math;


public class OmegaFunction implements Function {
    private double q;
    private double theta;
    private double phi;
    private double F;
    private double p;
    private double omega1;


    public OmegaFunction(double q, double theta, double phi, double F, double p, double omega1) {
	this.q = q;
	this.theta = theta;
	this.phi = phi;
	this.F = F;
	this.p = p;
	this.omega1 = omega1;
    }


    //the function whose roots are used to find r, via Brent's method
    public double f(double r) {
	return OmegaR(r, q, theta, phi, p) - OmegaF(q, F, omega1);
    }



    //calculates the potential at distance r from the center of the star, at
    //spherical polar coordinates specified by theta, phi and with rotation p.
    public static double OmegaR(double r, double q, double theta, double phi, double p) {
	double potential;
	double expression;
	double denominator;

	denominator = Math.sqrt(1 - (2*r*Math.cos(phi)*Math.sin(theta)) + (r*r));
	expression = (1/denominator) - (r*Math.cos(phi)*Math.sin(theta));
	potential = (1/r) + q*(expression)+ ((q+1)*p*p*r*r*Math.sin(theta)*Math.sin(theta)/2);
	return potential;
    }


    //calculates the potential at a point F*RL1 units along the x-axis
    public static double OmegaF(double q, double F, double omega1) {
	double term = (q*q)/(2*(1+q));

	return ((omega1 + term)/F) - term;
    }


}
