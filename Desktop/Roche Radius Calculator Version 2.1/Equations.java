/*
Author: Janet Leahy
Version: July 4, 2014

Class consisting of static methods, performing mathematical functions.
 */

import java.lang.Math;

public class Equations {

    public static final double ERROR = Math.pow(10, -8);
    public static final boolean DEBUG = false;


    //calculates the potential at distance r from the center of the star, at
    //spherical polar coordinates specified by theta, phi.
    public static double OmegaR(double r, double q, double theta, double phi) {
	double potential;
	double expression;
	double denominator;

	denominator = Math.sqrt(1 - (2*r*Math.cos(phi)*Math.sin(theta)) + (r*r));
	expression = (1/denominator) - (r*Math.cos(phi)*Math.sin(theta));
	potential = (1/r) + q*(expression)+ ((q+1)*r*r*Math.sin(theta)*Math.sin(theta)/2);
	return potential;
    }


    //the function whose roots are used to find r, via Brent's method
    public static double f(double r, double q, double theta, double phi, double omega1) {
	return OmegaR(r, q, theta, phi) - omega1;
    }


    //given points (x1, y1) and (x2, y2), x2>=x1, function will linearly
    //interpolate to find the y-value for a specified xFind
    public static double interpolate(double x1, double y1, double x2, double y2, double xFind) {
	double slope;

	if (xFind == x1) {
	    //returns the correct number even if x1, x2 and xFind are the same
	    // (which will be the case if q = MAX_Q)
	    return y1;
	}
	else {
	    slope = (y2-y1)/(x2-x1);	
	    return y1 + slope*(xFind - x1);
	}
    }


    //returns the result of using the secant method on (a,ya) and (b, yb)
    //same as linear interpolation?
    public static double secant(double a, double b, double ya, double yb) {
	double term;
	term = (b-a)/(yb-ya);
	return b - yb*term;
    }


    //used in Brent's method
    public static double equation(double a, double b, double c, double ya, double yb, double yc) {
	double term1;
	double term2;
	double term3;

	term1 = a*yb*yc/((ya-yb)*(ya-yc));
	term2 = b*ya*yc/((yb-ya)*(yb-yc));
	term3 = c*ya*yb/((yc-ya)*(yc-yb));

	return term1+term2+term3;
    }


    //uses Brent's method to find the root (within ERROR) of the function
    //with q, theta, phi and Omega1 fixed, between r-values specified by
    //lower bound a and upper bound b. Returns root once found. If no root
    //is found, function returns 0. Based on Wikipedia algorithm.
    public static double findRoot(double a, double b, double q, double theta, double phi, double omega1) {
	double ya;
	double yb;
	double c;
	double yc;
	double s;
	double ys;
	double d;
	boolean mflag;
	double temp;

	ya = f(a, q, theta, phi, omega1);
	yb = f(b, q, theta, phi, omega1);

	if (ya*yb > 0) {
	    //root has not been properly bracketed, so function returns 0
	    System.out.println("Function not bracketed: ya = "+ya+" yb = "+yb);
	    return 0;
	}

	if (Math.abs(ya) < Math.abs(yb)) {
	    //swap a and b
	    temp = a;
	    a = b;
	    b = temp;
	}
	c = a;
	d = 0;//doesn't matter what d is initialized to, since mflag prevents
	// it from being called on the first iteration.
	mflag = true;

	//repeats until a root is found, or until upper and lower boundries
	//are within ERROR (10^-8) of each other.
	do {
	    //recalculates f(a), f(b) and f(c)
	    ya = f(a, q, theta, phi, omega1);
	    yb = f(b, q, theta, phi, omega1);
	    yc = f(c, q, theta, phi, omega1);

	    if ((ya == yc)||(yb == yc)) {
		//s is found using the secant method
		s = secant(a, b, ya, yb);
	    }
	    else {
		s = equation(a, b, c, ya, yb, yc);
	    }


	    if ( !(((s>b)&&(s<(3*a+b)/4) || ((s<b)&&(s>(3*a+b)/4)))) ||
		 ( mflag && (Math.abs(s-b) >= (Math.abs(b-c)/2))) ||
		 ( !mflag && (Math.abs(s-b) >= (Math.abs(c-d)/2)) ) ||
		 ( mflag && (Math.abs(b-c)< ERROR) ) ||
		 ( !mflag && (Math.abs(c-d)< ERROR)) ) {

		//we use the bisection method for s
		s = (a+b)/2;
		mflag= true;
	    }
	    else {
		mflag = false;
	    }


	    //calculate f(s)
	    ys = f(s, q, theta, phi, omega1);

	    d = c;
	    c = b;

	    if ((ya*ys) < 0) {
		b = s;
		yb = f(b, q, theta, phi, omega1);
	    }
	    else {
		a = s;
		ya = f(a, q, theta, phi, omega1);
	    }

	    if (Math.abs(ya)<Math.abs(yb)) {
		//swaps a and b
		temp = a;
		a = b;
		b = temp;
	    }

	    if (DEBUG) {
		System.out.println("Current s =  " + s);
		System.out.println("Current b =  " + b);
		System.out.println("Current a = " + a);
		System.out.println("Current c = " + c);
	    }

	} while ((Math.abs(yb)>ERROR)&&(Math.abs(ys)>ERROR)&&(Math.abs(b-a)>ERROR));
	
	if (Math.abs(yb)<=ERROR) {
	    if (DEBUG) {System.out.println("returning b= " + b);}
	    return b;
	}
	else if (Math.abs(ys)<=ERROR) {
	    if (DEBUG) {System.out.println("returning s = " + s);}
	    return s;
	}
	else {
	    if (DEBUG) {System.out.println("b-a < ERROR");}
	    return b;
	}
    }

}
