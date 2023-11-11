package come.example.utitled.emulator.asm.structure;

import come.example.utitled.syntax.AsmOperations;

public abstract class Command {

    protected AsmOperations operator;

    public abstract String getValue1();
    public abstract String getValue2();
    public abstract void setValue1(String command);
    public abstract void setValue2(String command);

    public AsmOperations getOperator() {
        return operator;
    }

    public String commandToString() {
        return String.format("%s %s %s", getOperator().getName(), getValue1(), getValue2());
    }

}
