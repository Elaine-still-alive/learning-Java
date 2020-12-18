package com.company;

import java.awt.geom.Rectangle2D;

public class BurningShip extends FractalGenerator{
    public static final int MAX_ITERATIONS = 2000;
    public void getInitialRange(Rectangle2D.Double range){
        range.x = -2;
        range.y = -2.5;
        range.width = 4;
        range.height = 4;
    }
    public int numIterations(double x, double y) {
        int count = 0;
        double re = 0;
        double im = 0;
        double z = 0;

        while (count < MAX_ITERATIONS && z < 4) {
            count++;

            double nextRe = Math.abs(re*re - im*im + x);
            double nextIm = Math.abs(2 * re *im + y);

            z = nextRe*nextRe + nextIm*nextIm;

            re = nextRe;
            im = nextIm;
        }
        return count < MAX_ITERATIONS ? count : -1;
    }

}

