package come.example.utitled.emulator;

import come.example.utitled.syntax.AsmOperations;

public class BinarCommand extends Command {

    private String value1;
    private String value2;

    public BinarCommand(AsmOperations operations, String value1, String value2) {
        this.operator = operations;
        this.value1 = value1;
        this.value2 = value2;
    }

    @Override
    public String getValue1() {
        return value1;
    }

    @Override
    public String getValue2() {
        return value2;
    }

    @Override
    public void setValue1(String command) {
        this.value1 = command;
    }

    @Override
    public void setValue2(String command) {
        this.value2 = command;
    }
}
