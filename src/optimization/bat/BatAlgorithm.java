package optimization.bat;

import java.util.Random;
import java.util.Arrays;

public class BatAlgorithm {

    private double[][] batSolutions;        // Population/Solution (N x D)
    private double[][] velocities;        // Velocities (N x D)
    private double[][] frequency;        // Frequency : 0 to Q_MAX (N x 1)
    private double[] fitness;            // Fitness (N)
    private double pulseRate;            // Pulse Rate : 0 to 1
    private double amplitude;            // Loudness : A_MIN to A_MAX
    private double[][] lowerBounds;        // Lower bound (1 x D)
    private double[][] upperBounds;        // Upper bound (1 x D)
    private double fitnessMin;        // Minimum fitness from F
    private double[] best;            // Best solution array from X (D)
    private final int populationSize;        // Number of bats
    private final int maxNumOfIterations;        // Number of iterations
    private final double frequencyMin = 0.0;
    private final double frequencyMax = 2.0;
    private final double amplitudeMin;
    private final double amplitudeMax;
    private final double pulseRateMin;
    private final double pulseRateMax;
    private final int dimension = 10;
    private final Random rand = new Random();

    public BatAlgorithm(int populationSize, int maxNumOfIterations, double amplitudeMin, double amplitudeMax,
                        double pulseRateMin, double pulseRateMax) {

        this.populationSize = populationSize;
        this.maxNumOfIterations = maxNumOfIterations;
        this.pulseRateMax = pulseRateMax;
        this.pulseRateMin = pulseRateMin;
        this.amplitudeMax = amplitudeMax;
        this.amplitudeMin = amplitudeMin;

        this.batSolutions = new double[populationSize][dimension];
        this.velocities = new double[populationSize][dimension];
        this.frequency = new double[populationSize][1];
        this.fitness = new double[populationSize];
        this.pulseRate = (pulseRateMax + pulseRateMin) / 2;
        this.amplitude = (amplitudeMin + amplitudeMax) / 2;

        // Initialize bounds
        this.lowerBounds = new double[1][dimension];
        for (int i = 0; i < dimension; i++) {
            this.lowerBounds[0][i] = -2.0;
        }
        this.upperBounds = new double[1][dimension];
        for (int i = 0; i < dimension; i++) {
            this.upperBounds[0][i] = 2.0;
        }

        // Initialize Q and V
        for (int i = 0; i < populationSize; i++) {
            this.frequency[i][0] = 0.0;
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
                        lowerBounds[0][j] + (upperBounds[0][j] - lowerBounds[0][j]) * rand.nextDouble();
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
        double sum = 0.0;
        for (int i = 0; i < Xi.length; i++) {
            sum = sum + Xi[i] * Xi[i];
        }
        return sum;
    }

    private double[] simpleBounds(double[] Xi) {
        // Don't know if this should be implemented
        double[] Xi_temp = new double[dimension];
        System.arraycopy(Xi, 0, Xi_temp, 0, dimension);

        for (int i = 0; i < dimension; i++) {
            if (Xi_temp[i] < lowerBounds[0][i]) {
                Xi_temp[i] = lowerBounds[0][i];
            } else {
                continue;
            }
        }

        for (int i = 0; i < dimension; i++) {
            if (Xi_temp[i] > upperBounds[0][i]) {
                Xi_temp[i] = lowerBounds[0][i];
            } else {
                continue;
            }
        }
        return Xi_temp;
    }

    private void startBat() {

        double[][] S = new double[populationSize][dimension];
        int n_iter = 0;

        // Loop for all iterations/generations(MAX)
        for (int t = 0; t < maxNumOfIterations; t++) {
            // Loop for all bats(N)
            for (int i = 0; i < populationSize; i++) {

                // Update frequency (Nx1)
                frequency[i][0] = frequencyMin + (frequencyMin - frequencyMax) * rand.nextDouble();
                // Update velocity (NxD)
                for (int j = 0; j < dimension; j++) {
                    velocities[i][j] = velocities[i][j] + (batSolutions[i][j] - best[j]) * frequency[i][0];
                }
                // Update S = X + V
                for (int j = 0; j < dimension; j++) {
                    S[i][j] = batSolutions[i][j] + velocities[i][j];
                }
                // Apply bounds/limits
                batSolutions[i] = simpleBounds(batSolutions[i]);
                // Pulse rate
                if (rand.nextDouble() > pulseRate) {
                    for (int j = 0; j < dimension; j++) {
                        batSolutions[i][j] = best[j] + 0.001 * rand.nextGaussian();
                    }
                }


                // Evaluate new solutions
                double fnew = objective(batSolutions[i]);

                // Update if the solution improves or not too loud
                if (fnew <= fitness[i] && rand.nextDouble() < amplitude) {
                    batSolutions[i] = S[i];
                    fitness[i] = fnew;
                }

                // Update the current best solution
                if (fnew <= fitnessMin) {
                    best = batSolutions[i];
                    fitnessMin = fnew;
                }
            } // end loop for N
            n_iter = n_iter + populationSize;
        } // end loop for MAX

        System.out.println("Number of evaluations : " + n_iter);
        System.out.println("Best = " + Arrays.toString(best));
        System.out.println("fmin = " + fitnessMin);
    }

    public static void main(String[] args) {
        new BatAlgorithm(2000, 100, 0.0, 1.0, 0.0, 1.0).startBat();
    }
}
