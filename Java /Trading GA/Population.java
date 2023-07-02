package com.company;
import java.util.ArrayList;
import java.util.Random;

public class Population {

    private static final int  populationSize = 100;
    private static final double mutationProb = 0.3;
    private static final double crossoverProb = 0.7;
    private Individual[] indiv = new Individual[populationSize];
    private final double[] fitness = new double[populationSize];
    public static Random random = new Random();
    Random ran = new Random();
    int bestIndex = -10; //value that isn't 0-100

    /**
     * Initialise random pop
     */
    public Population(){
        for (int i = 0; i < populationSize; i++){
            indiv[i] = new Individual();
        }
    }

    /**
     * computes the fitness, crossover and mutations
     * creates a new population in an arraylist
     * adds the mutated values and crossed values to that list
     * then iterates on that set
     */
    public void initialise() {

        calculateFittest();
        ArrayList<Individual> newPopulation = new ArrayList<>();
        while(newPopulation.size() != populationSize){
            if(ran.nextDouble() < mutationProb || newPopulation.size() == populationSize -1){
                int parent = tournament();
                newPopulation.add(Individual.Mutation(indiv[parent]));
            }else if (random.nextDouble() < crossoverProb){
                int first = tournament();
                int second = tournament();
                Individual[] crossover = Individual.crossover(indiv[first], indiv[second]);
                newPopulation.add(crossover[0]);
                newPopulation.add(crossover[1]);
            }else {
                int parent = tournament();
                newPopulation.add(Individual.cloneParent(indiv[parent]));
            }
        }
        indiv = newPopulation.toArray(new Individual[0]);
    }

    /**
     * calculates the fittest based on the fitness
     */
    public void calculateFittest(){
        for(int i = 0; i<populationSize;i++){
            fitness[i] = indiv[i].getFitness();
            if(bestIndex != -10){
                if(fitness[i] > fitness[bestIndex]){
                    bestIndex = i;
                }
            }else{
                bestIndex = i;
            }
        }
    }

    /**
     * tournament to select fittest value
     * @return index of the fittest value based on tournament selection
     */
    private int tournament() {
        int tournamentSize = 12;
        int[] tourney = new int[tournamentSize];
        for(int i = 0; i< tournamentSize; i++){
            tourney[i] = ran.nextInt(populationSize);
        }
        int fittestVal = -10;
        for (int individual: tourney){
            if(fittestVal == -10){
                fittestVal = individual;
            }else if (fitness[individual] > fitness[fittestVal]){
                fittestVal = individual;
            }
        }
        return fittestVal;
    }

    /**
     * gets the best stats of that generation
     * @return string with statists
     */
    public String getBestStats(){ return "Best: " + fitness[bestIndex] + "\n" + indiv[bestIndex].stats();}

    /**
     * gets the best overall generation
     * @return string with the best overall generation
     */
    public String getFinalResult(){
        String str;
        str = "Fitness of best solution: " + fitness[bestIndex];
        str += "\nBest weight combination: " + getBestWeights();
        return str;
    }

    /**
     * gets the best weights from the best generation.
     * @return string with the best weights from the best generation
     */
    private String getBestWeights() {return indiv[bestIndex].stats();}

}
