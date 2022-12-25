package optimization.bat;

public class ValueRange {

    private double min;
    private double max;

    public ValueRange(double min, double max) {

        // swap
        if (min > max) {

            min = max + -min + (max = min);
        }

        this.min = min;
        this.max = max;
    }

    public boolean isInRange(double test) {

        return min <= test && max >= test;
    }

    public double getMin() {

        return min;
    }

    public double getMax() {

        return max;
    }

    public void setMin(double min) {

        this.min = min;
    }

    public void setMax(double max) {

        this.max = max;
    }
}
