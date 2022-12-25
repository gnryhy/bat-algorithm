package optimization.bat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Supplier;

public class BenchmarkFunctions {

    private static final Function<Double[], Double> SPHERE = (arr) -> {

        double total = 0d;

        for (double x : arr) {

            total += Math.pow(x, 2);
        }

        return total;
    };

    private static final Function<Double[], Double> ELLIPTIC = (arr) -> {

        double total = 0d;
        int nMinusOne = arr.length - 1;
        int i = 1;

        for (double x : arr) {

            total += Math.pow(Math.pow(10, 6), (i - 1) * (nMinusOne)) * Math.pow(x, 2);
            i++;
        }

        return total;
    };

    private static final Function<Double[], Double> SUM_SQUARES = (arr) -> {

        double total = 0d;
        int i = 1;

        for (double x : arr) {

            total += i * Math.pow(x, 2);
            i++;
        }

        return total;
    };

    private static final Function<Double[], Double> SUM_POWER = (arr) -> {

        double total = 0d;
        int i = 1;

        for (double x : arr) {

            total += Math.pow(Math.abs(x), i + 1);
            i++;
        }

        return total;
    };

    private static final Function<Double[], Double> SCHWEFEL_2_22 = (arr) -> {

        double total = 0d;

        for (double x : arr) {

            total += Math.abs(x);
        }

        double product = 1d;

        for (double x : arr) {

            product *= Math.abs(x);
        }

        return total + product;
    };

    private static final Function<Double[], Double> SCHWEFEL_2_21 = (arr) -> {

        double max = -1;

        for (double x : arr) {

            if (x > max) {

                max = x;
            }
        }

        return max;
    };

    private static final Function<Double[], Double> STEP = (arr) -> {

        double total = 0d;

        for (double x : arr) {

            total += Math.pow(Math.ceil(x + 0.5d), 2);
        }

        return total;
    };

    private static final Function<Double[], Double> QUARTIC = (arr) -> {

        double total = 0d;
        int i = 1;

        for (double x : arr) {

            total += i * Math.pow(Math.ceil(x), 4);
            i++;
        }

        return total;
    };

    private static final Function<Double[], Double> QUARTIC_WN = (arr) -> {

        final Random random = new Random();

        double total = 0d;
        int i = 1;

        for (double x : arr) {

            total += i * Math.pow(Math.ceil(x), 4) + random.nextDouble();
            i++;
        }

        return total;
    };

    private static final Function<Double[], Double> ROSENBROCK = (arr) -> {

        double total = 0d;

        for (int i = 0; i < arr.length - 1; i++) {

            total += 100 * (arr[i + 1] - Math.pow(arr[i], 2)) + Math.pow((arr[i] - 1), 2);
        }

        return total;
    };

    private static final Function<Double[], Double> RASTRIGIN = (arr) -> {

        double total = 0d;

        for (double x : arr) {

            total += Math.pow(x, 2) - 10 * Math.cos(2 * Math.PI * x) + 10;
        }

        return total;
    };

    private static final Function<Double[], Double> NON_CONTINIOUS_RASTRIGIN = (arr) -> {

        double total = 0d;

        for (double x : arr) {

            double y = Math.abs(x) < 0.5 ? x : Math.round(2 * x) / 2;

            total += Math.pow(y, 2) - 10 * Math.cos(2 * Math.PI * y) + 10;
        }

        return total;
    };

    private static final Function<Double[], Double> GRIEWANK = (arr) -> {

        double total = 0d;
        double product = 1d;
        int i = 1;

        for (double x : arr) {

            total += Math.pow(x, 2);
            product *= Math.cos(x / Math.sqrt(i));
            i++;
        }

        return 1 + (1 / 4000 * total) + product;
    };

    private static final Function<Double[], Double> SCHWEFEL_2_26 = (arr) -> {

        double total = 0d;

        for (double x : arr) {

            total += x * Math.sin(Math.sqrt(Math.abs(x)));
        }

        return 418.98 * arr.length - total;
    };

    private static final Function<Double[], Double> ACKLEY = (arr) -> {

        double totalSquares = 0d;
        double totalCosine = 0d;

        for (double x : arr) {

            totalSquares += Math.pow(x, 2);
            totalCosine += Math.cos(2 * Math.PI * x);
        }

        return -20 * Math.exp(-0.2 * Math.sqrt((1 / arr.length) * totalSquares)) - Math.exp((1 / arr.length) * totalCosine) + 20 + Math.E;
    };

    private static final Function<Double[], Double> PENALIZED_1 = (arr) -> {

        // a 10 k 100 m 4
        final Function<Double, Double> u = (x) -> {

            if (x > 4d || x < -4d) {

                return 100 * Math.pow((x - 10), 4);

            } else if (-4d <= x && x <= 4d) {

                return 0d;

            } else {

                System.err.println("PENALIZED_1 WARNING! x = " + x);

                return 0d;
            }
        };

        final Function<Double, Double> y = (x) -> 1 + 0.25 * (x + 1);

        double y_total = 0d;
        double u_total = 0d;

        for (int i = 0; i < arr.length - 1; i++) {

            y_total += Math.pow(y.apply(arr[i]) - 1, 2) * (1 + 10 * Math.pow(Math.sin(Math.PI * y.apply(arr[i + 1])),
                    2));
        }

        for (double x : arr) {

            u_total += u.apply(x);
        }

        return (Math.PI / arr.length) * (10 * Math.pow(Math.sin(Math.PI * y.apply(arr[0])), 2) + y_total + Math.pow(y.apply(arr[arr.length - 1] - 1), 2)) + u_total;
    };

    private static final Function<Double[], Double> PENALIZED_2 = (arr) -> {

        // a 5 k 100 m 4
        final Function<Double, Double> u = (x) -> {

            if (x > 5 || x < -5) {

                return 100 * Math.pow((x - 5), 4);

            } else if (-5 <= x && x <= 5) {

                return 0d;

            } else {

                System.err.println("PENALIZED_2 WARNING! x = " + x);

                return 0d;
            }
        };

        double x_total = 0d;
        double u_total = 0d;

        for (int i = 0; i < arr.length - 1; i++) {

            x_total += Math.pow(arr[i] - 1, 2) * (1 + Math.pow(Math.sin(3 * Math.PI * arr[i + 1]), 2));
        }

        for (double x : arr) {

            u_total += u.apply(x);
        }

        return 0.1 * (Math.pow(Math.sin(Math.PI * arr[0]), 2) + x_total + Math.pow(arr[arr.length - 1] - 1, 2) * (1 + Math.pow(Math.sin(2 * Math.PI * arr[arr.length - 1]), 2))) + u_total;
    };

    private static final Function<Double[], Double> ALPINE = (arr) -> {

        double total = 0d;

        for (double x : arr) {

            total += Math.abs(x * Math.sin(x) + 0.1 * x);
        }

        return total;
    };

    private static final Function<Double[], Double> LEVY = (arr) -> {

        double total = 0d;

        for (int i = 0; i < arr.length - 1; i++) {

            total += Math.pow(arr[i] - 1, 2) * (1 + Math.pow(Math.sin(3 * Math.PI * arr[i + 1]), 2));
        }

        total += Math.pow(Math.sin(3 * Math.PI * arr[0]), 2) + Math.abs(arr[arr.length - 1] - 1) *
                (1 + Math.pow(Math.sin(3 * Math.PI * arr[arr.length - 1]), 2));

        return total;
    };

    private static final Function<Double[], Double> WEIERSTRASS = (arr) -> {

        double a = 0.5;
        double b = 3;
        int kMax = 20;

        double leftTotal = 0d;
        double rightTotal = 0d;

        for (int i = 0; i < arr.length; i++) {

            for (int k = 0; k <= kMax; k++) {

                leftTotal += Math.pow(a, k) * Math.cos(2 * Math.PI * Math.pow(b, k) * (arr[i] + 0.5));
            }
        }

        for (int k = 0; k <= kMax; k++) {

            rightTotal += Math.pow(a, k) * Math.cos(2 * Math.PI * Math.pow(b, k) * 0.5);
        }

        return rightTotal - arr.length * leftTotal;
    };

    private static final Function<Double[], Double> SCHAFFER = (arr) -> {

        double squaresTotal = 0d;

        for (double x : arr) {

            squaresTotal += Math.pow(x, 2);
        }

        return 0.5d + ((Math.pow(Math.sin(Math.sqrt(squaresTotal)), 2) - 0.5d) / Math.pow((1 + 0.001 * squaresTotal),
                2));

    };

    private static final ValueRange[] VALUE_RANGES = new ValueRange[]{new ValueRange(-100, 100), new ValueRange(-100,
            100), new ValueRange(-10, 10), new ValueRange(-10, 10), new ValueRange(-10, 10),
            new ValueRange(-100,
                    100), new ValueRange(-100, 100), new ValueRange(-1.28, 1.28), new ValueRange(-1.28, 1.28),
            new ValueRange(-10, 10), new ValueRange(-5.12, 5.12), new ValueRange(-5.12, 5.12), new ValueRange(-600,
            600), new ValueRange(-500, 500), new ValueRange(-32, 32), new ValueRange(-50, 50),
            new ValueRange(-50, 50), new ValueRange(-10, 10), new ValueRange(-10, 10), new ValueRange(-0.5, 0.5),
            new ValueRange(-100, 100)};

    private static final Function<Double[], Double>[] FUNCTIONS = new Function[]{SPHERE, ELLIPTIC, SUM_SQUARES,
            SUM_POWER, SCHWEFEL_2_22, SCHWEFEL_2_21, STEP, QUARTIC, QUARTIC_WN, ROSENBROCK, RASTRIGIN,
            NON_CONTINIOUS_RASTRIGIN, GRIEWANK, SCHWEFEL_2_26, ACKLEY, PENALIZED_1, PENALIZED_2, ALPINE, LEVY,
            WEIERSTRASS, SCHAFFER};

    private static final String[] FUNCTION_NAMES = new String[]{"SPHERE", "ELLIPTIC", "SUM_SQUARES",
            "SUM_POWER", "SCHWEFEL_2_22", "SCHWEFEL_2_21", "STEP", "QUARTIC", "QUARTIC_WN", "ROSENBROCK", "RASTRIGIN",
            "NON_CONTINIOUS_RASTRIGIN", "GRIEWANK", "SCHWEFEL_2_26", "ACKLEY",
            "PENALIZED_1", "PENALIZED_2", "ALPINE", "LEVY",
            "WEIERSTRASS", "SCHAFFER"};

    private static final Supplier<List<FunctionDefinition>> FUNCTION_PROVIDER = () -> {

        List<FunctionDefinition> result = new ArrayList<>();

        for (int i = 0; i < VALUE_RANGES.length; i++) {

            result.add(new FunctionDefinition(FUNCTION_NAMES[i], FUNCTIONS[i], VALUE_RANGES[i]));
        }

        return result;
    };

    public static final List<FunctionDefinition> FUNCTION_LIST = FUNCTION_PROVIDER.get();

}
