package come.example.utitled.emulator;

import come.example.utitled.syntax.AsmOperations;

public abstract class Command {

    protected AsmOperations operator;

    public abstract String getValue1();
    public abstract String getValue2();

    public AsmOperations getOperator() {
        return operator;
    }

}
