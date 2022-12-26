package optimization.bat;

import java.util.Arrays;
import java.util.Random;

public class BatAlgorithm {

    private FunctionDefinition function;
    private double[][] batSolutions;        // Population x Solution
    private double[][] velocities;        // Velocities
    private double[] frequency;        // Frequency -> frequencyMin to frequencyMin
    private double[] fitness;            // Fitness (N)
    private double[] pulseRate;            // Pulse Rate
    private final double pulseRateInitial; // Initial pulse rate for every bat
    private double[] loudness;            // Loudness
    private double[] lowerBounds;        // Lower bound
    private double[] upperBounds;        // Upper bound
    private double fitnessMin;        // fitness score of best solution
    private double[] best;            // Best solution
    private final int populationSize;        // Number of bats
    private final int maxNumOfIterations;        // Number of iterations
    private final double frequencyMin = 0.0; // Minimum frequency
    private final double frequencyMax = 2.0; // Maximum frequency
    private final int dimension = 2; // dimension of the problem
    private final double alpha = 0.9; // cooling factor for loudness and pulse rate
    private final Random rand = new Random();

    public BatAlgorithm(int populationSize, int maxNumOfIterations, double loudnessInitial, double pulseRateInitial,
                        FunctionDefinition function) {

        this.populationSize = populationSize;
        this.maxNumOfIterations = maxNumOfIterations;
        this.loudness = new double[populationSize];
        this.pulseRate = new double[populationSize];
        this.pulseRateInitial = pulseRateInitial;
        this.function = function;

        this.batSolutions = new double[populationSize][dimension];
        this.velocities = new double[populationSize][dimension];
        this.frequency = new double[populationSize];
        this.fitness = new double[populationSize];
        Arrays.fill(loudness, loudnessInitial);
        Arrays.fill(pulseRate, pulseRateInitial);

        // Initialize bounds
        this.lowerBounds = new double[dimension];

        for (int i = 0; i < dimension; i++) {

            this.lowerBounds[i] = function.getRange().getMin();
        }

        this.upperBounds = new double[dimension];

        for (int i = 0; i < dimension; i++) {

            this.upperBounds[i] = function.getRange().getMax();
        }

        // Initialize Frequency and Velocity
        for (int i = 0; i < populationSize; i++) {

            this.frequency[i] = 0.0;
        }
        for (int i = 0; i < populationSize; i++) {

            for (int j = 0; j < dimension; j++) {

                this.velocities[i][j] = 0.0;
            }
        }

        // Initialize Positions
        for (int i = 0; i < populationSize; i++) {

            for (int j = 0; j < dimension; j++) {

                this.batSolutions[i][j] = lowerBounds[j] + (upperBounds[j] - lowerBounds[j]) * rand.nextDouble();
            }

            this.fitness[i] = benchmarkFunction(batSolutions[i]);
        }

        // Initial best solution
        int fmin_i = 0;

        for (int i = 0; i < populationSize; i++) {

            if (fitness[i] < fitness[fmin_i]) {

                fmin_i = i;
            }
        }

        this.fitnessMin = fitness[fmin_i];
        this.best = batSolutions[fmin_i];
    }

    private double benchmarkFunction(double[] xValues) {

        return function.getFunction().apply(Arrays.stream(xValues).boxed().toArray(Double[]::new));
    }

    private double[] boundaryCheck(double[] xValues) {

        double[] tempXValues = new double[dimension];
        System.arraycopy(xValues, 0, tempXValues, 0, dimension);

        for (int i = 0; i < dimension; i++) {

            if (tempXValues[i] < lowerBounds[i]) {

                tempXValues[i] = lowerBounds[i];

            } else if (tempXValues[i] > upperBounds[i]) {

                tempXValues[i] = lowerBounds[i];
            }
        }

        return tempXValues;
    }

    public double[] runAlgorithm() {

        // initial best is included too
        double[] convergenceValues = new double[maxNumOfIterations + 1];
        convergenceValues[0] = this.fitnessMin;

        double[][] solution = new double[populationSize][dimension];

        // iterate until max iteration count is reached
        for (int t = 0; t < maxNumOfIterations; t++) {
            // iterate bats
            for (int i = 0; i < populationSize; i++) {

                // Update frequency
                frequency[i] = frequencyMin + (frequencyMin - frequencyMax) * rand.nextDouble();
                // Update velocities of every dimension
                for (int j = 0; j < dimension; j++) {

                    velocities[i][j] = velocities[i][j] + (batSolutions[i][j] - best[j]) * frequency[i];
                }
                // Update position
                for (int j = 0; j < dimension; j++) {

                    solution[i][j] = batSolutions[i][j] + velocities[i][j];
                }

                // fix bounds
                solution[i] = boundaryCheck(solution[i]);

                // if bat's pulse rate is not greater than randomized pulse;
                // move the bat around the contemporary best solution.
                if (rand.nextDouble() > pulseRate[i]) {

                    double avgLoudness = Arrays.stream(loudness).average().orElse(0);

                    for (int j = 0; j < dimension; j++) {

                        double r = rand.nextDouble(-1, 1);

                        solution[i][j] = best[j] + r * avgLoudness;
                    }
                }

                // fix bounds
                solution[i] = boundaryCheck(solution[i]);

                // Evaluate new solutions
                double newFitness = benchmarkFunction(solution[i]);

                // Update the solution if it improves and is not too loud
                if (newFitness <= fitness[i] && rand.nextDouble() < loudness[i]) {

                    batSolutions[i] = solution[i];
                    fitness[i] = newFitness;
                    // improvement decreases loudness
                    loudness[i] = alpha * loudness[i];
                    // improvement increases pulse rate
                    pulseRate[i] += pulseRateInitial * (1 - Math.exp(-1 * alpha * t));
                }

                // check if this is the new best solution
                if (newFitness <= fitnessMin) {

                    best = batSolutions[i];
                    fitnessMin = newFitness;
                }
            }

            convergenceValues[t + 1] = fitnessMin;
        }

        return convergenceValues;
    }
}
