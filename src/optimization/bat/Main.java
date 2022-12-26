package optimization.bat;

public class Main {

    public static void main(String[] args) {

        for (FunctionDefinition fd : BenchmarkFunctions.FUNCTION_LIST) {

            double[] result_30 = new BatAlgorithm(30, 1000, 2, 0.1, fd).runAlgorithm();
            double[] result_40 = new BatAlgorithm(40, 1000, 2, 0.1, fd).runAlgorithm();
            double[] result_50 = new BatAlgorithm(50, 1000, 2, 0.1, fd).runAlgorithm();

            new ConvergenceChart(fd.getName(), result_30, 30);
            new ConvergenceChart(fd.getName(), result_40, 40);
            new ConvergenceChart(fd.getName(), result_50, 50);
        }
    }
}
