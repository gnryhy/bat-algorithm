package optimization.bat;

import java.util.Arrays;
import java.util.Random;

public class BatAlgorithm {

    private FunctionDefinition function;
    private double[][] batSolutions;        // Population/Solution (N x D)
    private double[][] velocities;        // Velocities (N x D)
    private double[] frequency;        // Frequency : 0 to Q_MAX (N x 1)
    private double[] fitness;            // Fitness (N)
    private double pulseRate[];            // Pulse Rate : 0 to 1
    private final double pulseRateInitial;
    private double loudness[];            // Loudness : A_MIN to A_MAX
    private double[] lowerBounds;        // Lower bound (1 x D)
    private double[] upperBounds;        // Upper bound (1 x D)
    private double fitnessMin;        // Minimum fitness from F
    private double[] best;            // Best solution array from X (D)
    private final int populationSize;        // Number of bats
    private final int maxNumOfIterations;        // Number of iterations
    private final double frequencyMin = 0.0;
    private final double frequencyMax = 2.0;
    private final int dimension = 2;
    private final double alpha = 0.9;
    private final Random rand = new Random();

    public BatAlgorithm(int populationSize, int maxNumOfIterations, double loudnessInitial,
                        double pulseRateInitial, FunctionDefinition function) {

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

        // Initialize Q and V
        for (int i = 0; i < populationSize; i++) {

            this.frequency[i] = 0.0;
        }
        for (int i = 0; i < populationSize; i++) {

            for (int j = 0; j < dimension; j++) {

                this.velocities[i][j] = 0.0;
            }
        }

        // Initialize X
        for (int i = 0; i < populationSize; i++) {

            for (int j = 0; j < dimension; j++) {

                this.batSolutions[i][j] =
                        lowerBounds[j] + (upperBounds[j] - lowerBounds[j]) * rand.nextDouble();
            }

            this.fitness[i] = objective(batSolutions[i]);
        }

        // Find initial best solution
        int fmin_i = 0;

        for (int i = 0; i < populationSize; i++) {

            if (fitness[i] < fitness[fmin_i]) {

                fmin_i = i;
            }
        }

        // Store minimum fitness and it's index.
        // B holds the best solution array[1xD]
        this.fitnessMin = fitness[fmin_i];
        this.best = batSolutions[fmin_i]; // (1xD)
    }

    private double objective(double[] Xi) {

        Double[] converted = Arrays.stream(Xi).boxed().toArray(Double[]::new);

        return function.getFunction().apply(converted);
    }

    private double[] simpleBounds(double[] Xi) {
        // Don't know if this should be implemented
        double[] Xi_temp = new double[dimension];
        System.arraycopy(Xi, 0, Xi_temp, 0, dimension);

        for (int i = 0; i < dimension; i++) {

            if (Xi_temp[i] < lowerBounds[i]) {

                Xi_temp[i] = lowerBounds[i];

            } else if (Xi_temp[i] > upperBounds[i]) {

                Xi_temp[i] = lowerBounds[i];
            }
        }

        return Xi_temp;
    }

    private double[] startBat() {

        // initial best is included too
        double[] convergenceValues = new double[maxNumOfIterations + 1];
        convergenceValues[0] = this.fitnessMin;

        double[][] S = new double[populationSize][dimension];

        for (int t = 0; t < maxNumOfIterations; t++) {
            // iterate bats
            for (int i = 0; i < populationSize; i++) {

                // Update frequency
                frequency[i] = frequencyMin + (frequencyMin - frequencyMax) * rand.nextDouble();
                // Update velocity
                for (int j = 0; j < dimension; j++) {

                    velocities[i][j] = velocities[i][j] + (batSolutions[i][j] - best[j]) * frequency[i];
                }
                // Update S = X + V
                for (int j = 0; j < dimension; j++) {

                    S[i][j] = batSolutions[i][j] + velocities[i][j];
                }
                // Apply bounds/limits
                S[i] = simpleBounds(S[i]);

                // Pulse rate
                if (rand.nextDouble() > pulseRate[i]) {

                    for (int j = 0; j < dimension; j++) {

                        S[i][j] = best[j] + 0.01 * rand.nextGaussian();
                    }
                }

                S[i] = simpleBounds(S[i]);

                // Evaluate new solutions
                double fnew = objective(S[i]);

                // Update if the solution improves and not too loud
                if (fnew <= fitness[i] && rand.nextDouble() < loudness[i]) {

                    batSolutions[i] = S[i];
                    fitness[i] = fnew;
                    loudness[i] = alpha * loudness[i];
                    pulseRate[i] += pulseRateInitial * (1 - Math.exp(-1 * alpha * t));
                }

                // Update the current best solution
                if (fnew <= fitnessMin) {

                    best = batSolutions[i];
                    fitnessMin = fnew;
                }
            }

            convergenceValues[t + 1] = fitnessMin;
        }
        
        return convergenceValues;
    }

    public static void main(String[] args) {

        for (FunctionDefinition fd : BenchmarkFunctions.FUNCTION_LIST) {

            double[] result_30 = new BatAlgorithm(30, 1000, 2,
                    0.1, fd).startBat();
            double[] result_40 = new BatAlgorithm(40, 1000, 2,
                    0.1, fd).startBat();
            double[] result_50 = new BatAlgorithm(50, 1000, 2,
                    0.1, fd).startBat();

            new ConvergenceChart(fd.getName(), result_30, result_40, result_50);
        }
    }
}
