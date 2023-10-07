package come.example.utitled.emulator;

import come.example.utitled.syntax.AsmOperations;

/**
 * Команда перехода
 */
public class TransitionCommand extends Command{

    private String nameFunctionRef;

    public TransitionCommand(AsmOperations operations, String nameFunctionRef) {
        this.operator = operations;
        this.nameFunctionRef = nameFunctionRef;
    }


    @Override
    public String getValue1() {
        return nameFunctionRef;
    }

    @Override
    public String getValue2() {
       return null;
    }

    @Override
    public void setValue1(String command) {
        this.nameFunctionRef = command;
    }

    @Override
    public void setValue2(String command) {
        // do nothing
    }
}
