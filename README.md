roche-radius-calculator
=======================

The purpose of this tool is to calculate the radius of the Roche Lobe.

It does this for any specified direction, also gives some commonly used values of radius. 
The calculator works for any mass ratio q between 0.01 and 100.
The coordinates are spherical-polar (R, theta, phi) centered on one star (M1), with the
x-axis (theta=pi/2, phi=0) pointing towards the other star (M2). 
The mass ratio is defined as q=M2/M1.
Distances are given in units of the binary separation, a.
A circular orbit is assumed.
Values were calculated for about 150 different values of q, accurate to 8 digits, and stored in
a Table. The calculator interpolates for intermediate values of q.
The accuracy is 8 digits at a Tabulated value, but is at least 4 digits for intermediate
values of q.

For a physical system, one knows the actual values of the masses M1 and M2.
Either you specify the binary separation a, or calculate it from the orbital
period P, using Kepler's law.
