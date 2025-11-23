package me.guntxjakka.MazeSolve.Utils;

public class TwoDArray {
    public static void printIntTwoDArray(Integer[][] x){
        for(int i = 0; i < x.length; i++){
            for (int j = 0; j < x[i].length; j++){
                System.out.print(x[i][j]);
                if (j != x[i].length - 1) System.out.print(" ");
            }
            System.out.println();
        }
    }
}
