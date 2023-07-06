package com.company;
import java.util.Random;

public class Individual {

    static int numOfWeights = 4;
    protected double[] weights;
    protected int[] max = new int[2];
    static Random ran = new Random();

    public Individual(){

        //Randomized weights generated from 0 to 1 to 2dp.
        //Max[0] = maxBuy, Max[1] = maxSell -- max amount of stocks to buy or sell.
        weights = new double[numOfWeights];
        for(int i = 0; i<numOfWeights; i++){
            double value = Population.random.nextInt(101);
            weights[i] = value/100;
        }
        max[0] = Population.random.nextInt(10) + 1;
        max[1] = Population.random.nextInt(10) + 1;
    }

    /**
     * Calculates fitness, starts at point where all cols have a value
     * And the buying and selling of stocks
     * Sells all stocks at the end that are left over.
     * @return the fitness value
     */
    public double getFitness(){
        Main.readFile();
        double budget = 3000.0;
        int stocks = 0;

        for (int i = 29; i<Main.dataRows;i++){
            double buy = 0.0;
            double sell = 0.0;
            double hold = 0.0;

            for (int j = 0; j<weights.length; j++){
                if(Main.data[i][j+1] == 0.0){
                    hold += weights[j];
                }else if (Main.data[i][j+1] == 1.0) {
                    buy += weights[j];
                }else if (Main.data[i][j+1] == 2.0){
                    sell += weights[j];
                }
            }

            double price = Main.data[i][0];
            int maxBuy = max[0];
            int maxSell = max[1];

            if(buy > sell && buy > hold){
                int amountToBuy = (int) (budget/price);
                if(amountToBuy > maxBuy){
                    amountToBuy = maxBuy;
                }
                stocks += amountToBuy;
                budget -= amountToBuy * price;
            }else if (sell > buy && sell > hold ){
                int amountToSell = stocks;
                if(amountToSell > maxSell){
                    amountToSell = maxSell;
                }
                stocks -= amountToSell;
                budget += price * amountToSell;
            }
        }
        budget += stocks * Main.data[Main.dataRows-1][0];
        return budget;
    }

    /**
     * Performs crossover on the weights
     * @param first the first individual to be crossed
     * @param second the second individual to be crossed with
     * @return array with the new crossed values
     */
    public static Individual[] crossover(Individual first, Individual second) {
        Individual firstClone = cloneParent(first);
        Individual secondClone = cloneParent(second);

        int crossOverPoint = ran.nextInt(numOfWeights -2);
        for(int i = 0; i<numOfWeights; i++){
            if (i < crossOverPoint){
                firstClone.weights[i] = second.weights[i];
            }else{
                secondClone.weights[i] = first.weights[i];
            }
        }
        return new Individual[] {
                firstClone, secondClone
        };
    }

    /**
     * Performs one point mutation on a parent and clone
     * @param parent value to be mutated
     * @return clone of the parent with the mutation completed
     */
    public static Individual Mutation(Individual parent){
        Individual clone = cloneParent(parent);
        int select = ran.nextInt(numOfWeights+2 );
        if (select < numOfWeights){
            double mutation = ((double) ran.nextInt(50))/100;
            double val;
            if(ran.nextBoolean()){
                val = clone.weights[select] + mutation;
            }else {
                val = clone.weights[select] - mutation;
            }

            if(val > 1 ){
                val = 1;
            }
            if (val < 0){
                val = 0;
            }
            clone.weights[select] = val;

        }else{
            boolean mut = ran.nextBoolean();
            int maxSelect = select - numOfWeights;
            if(!(clone.max[maxSelect] == 1) && !mut){
                clone.max[maxSelect]--;
            }else if (mut){
                clone.max[maxSelect]++;
            }
        }
        return clone;
    }

    /**
     * Clones the parent
     * @param parent parent to be cloned
     * @return cloned parent - offspring
     */
    static Individual cloneParent(Individual parent) {
        Individual clone = new Individual();
        clone.weights = parent.weights.clone();
        clone.max = parent.max.clone();
        return clone;
    }

    /**
     * Gathers the stats of the generation.
     * @return string containing the stats of generation
     */
    public String stats(){
        StringBuilder str = new StringBuilder();
        for (double weight : weights) {
            str.append(weight).append(", ");
        }
        str.append("\nMax amount to buy: ").append(max[0]);
        str.append(" -- Max amount to sell: ").append(max[1]);
        return str.toString();
    }

}
