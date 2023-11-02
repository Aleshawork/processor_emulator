package come.example.utitled.emulator.asm.structure;

import come.example.utitled.syntax.AsmOperations;

public class UnarCommand extends Command {

    private String value1;

    public UnarCommand(AsmOperations operator, String value1) {
        this.operator = operator;
        this.value1 = value1;
    }
    @Override
    public String getValue1() {
        return value1;
    }

    @Override
    public String getValue2() {
        return null;
    }

    @Override
    public void setValue1(String command) {
        this.value1 = value1;
    }

    @Override
    public void setValue2(String command) {
        // do nothing
    }

}
