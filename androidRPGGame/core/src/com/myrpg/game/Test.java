package com.myrpg.game;

/**
 * Created by Natallia && Anton on 16.11.2017.
 */

public class Test {
    public static void main(String[] args) {
        int[][] arr = new int[3][4];
        int count = 0;
        for (int i = 0; i <arr.length ; i++) {
            for (int j = 0; j <arr[i].length ; j++) {
                arr[i][j] = count;
                System.out.print(arr[i][j]+ " ");
                count++;
            }
            System.out.println();
        }
    }
}
