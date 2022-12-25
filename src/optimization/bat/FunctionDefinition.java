package optimization.bat;

import java.util.function.Function;

public class FunctionDefinition {

    private String name;
    private Function<Double[], Double> function;
    private ValueRange range;

    public FunctionDefinition(String name, Function<Double[], Double> function, ValueRange range) {

        this.name = name;
        this.function = function;
        this.range = range;
    }

    public String getName() {

        return name;
    }

    public Function<Double[], Double> getFunction() {

        return function;
    }

    public ValueRange getRange() {

        return range;
    }
}
