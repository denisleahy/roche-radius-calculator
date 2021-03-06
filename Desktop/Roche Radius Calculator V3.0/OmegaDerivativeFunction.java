/*
Author: Janet Leahy
Version: July 31, 2014

Class implementing function interface. Constructor is passed a value for q. Function takes x as a parameter and returns d[Omega]/dx, evaluated at x, with constructor q-value.

 */

public class OmegaDerivativeFunction implements Function {
    private double q;
    private double p;


    public OmegaDerivativeFunction(double q, double p) {
	this.q = q;
	this.p = p;
    }

    //returns the derivative of Omega with respect to x, evaluated at x.
    // for simplicity, the derivative function is split into two parts
    public double f(double x) {
	return f2(x, p) - f1(x);
    }


    public double f2(double x, double p) {
	return ((q+1)*p*p*x - q);
    }

    //f1 is split into cases, depending on the value of x
    public double f1(double x) {
	double part1 = 1/(x*x);
	double part2 = q/((1-x)*(1-x));

	if (x < 0) {
	    return -1*part1 + -1*part2;
	}
	else if (x > 0 && x < 1) {
	    return part1 + -1*part2;
	}
	else if (x > 1) {
	    return part1 + part2;
	}
	
	//derivative is undefined at x=1 and x=0, so return something huge
	else {
	    return 100000;
	}
    }


}
