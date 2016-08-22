package smaant.java;

import java.util.ArrayList;

public class Matrix {

    public static void printProductMatrix(Integer[] coll, boolean quiet) {
        final Integer[][] matrix = new Integer[coll.length][coll.length];
        final int maxNum = coll[(coll.length-1)];
        final int maxWidth = String.valueOf(maxNum * maxNum).length();
        final String formatStr = "%" + maxWidth + "s ";

        final StringBuilder sb = new StringBuilder();

        sb.append(String.format(formatStr, ""));
        for(int i = 0; i < coll.length; i++) {
            sb.append(String.format(formatStr, coll[i]));
        }
        sb.append("\n");

        for(int i = 0; i < coll.length; i++) {
            sb.append(String.format(formatStr, coll[i]));
            for(int j = 0; j < coll.length; j++) {
                if (j < i) {
                    sb.append(String.format(formatStr, matrix[j][i]));
                } else {
                    Integer prod = coll[i] * coll[j];
                    sb.append(String.format(formatStr, prod));
                    matrix[i][j] = prod;
                }
            }
            sb.append("\n");
        }
        if (!quiet) {
            System.out.println(sb.toString());
        }
    }

}
