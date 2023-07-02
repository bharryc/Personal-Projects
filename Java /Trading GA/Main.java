package com.company;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    //For reading the file
    private static boolean complete = false;
    static double[][] data;
    public static String csv;
    public static int dataRows = 0;

    public static void main(String[] args) {

        csv = "unilever.csv";
        int iterations = 100;

        Population population = new Population();
        for (int i = 1; i <= iterations; i++) {
            population.initialise();
            //every generation
            //System.out.println(population.getBestStats());
        }
        //final result
       System.out.println(population.getFinalResult());
    }

    /**
     * Reading the file to 2d array.
     */
    static void readFile(){
        if(!complete){
            try{
                File file = new File(csv);
                Scanner sc = new Scanner(file);

                sc.nextLine();

                //Gets number of rows in file, should be 1300 somethnig
                while (sc.hasNextLine()){
                    sc.nextLine();
                    dataRows++;
                }
                sc.close();

                //Adding data to array
                //Only want cols AGHIJ from file.
                int dataCols = 5;
                data = new double[dataRows][dataCols];
                sc = new Scanner(file);
                sc.nextLine();
                int i = 0;
                while (sc.hasNextLine()){
                    String[] str = sc.nextLine().split(",");
                    data[i][0] = Double.parseDouble(str[0]);
                    data[i][1] = Double.parseDouble(str[6]);
                    data[i][2] = Double.parseDouble(str[7]);
                    data[i][3] = Double.parseDouble(str[8]);
                    data[i][4] = Double.parseDouble(str[9]);
                    i++;
                }
                complete = true;
                sc.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}



