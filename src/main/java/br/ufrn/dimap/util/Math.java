package br.ufrn.dimap.util;

public class Math {
    public static double pow(double x, int y) {
        if (y < 0) {
            return 1 / pow(x, -y);
        }

        double result = 1.0;

        for (int i = 0; i < y; i++) {
            result *= x;
        }

        return result;
    }
}
