package come.example.utitled.emulator;

import com.google.common.collect.Lists;
import come.example.utitled.emulator.asm.structure.Command;
import come.example.utitled.emulator.asm.structure.flag.ZeroFlag;
import come.example.utitled.emulator.asm.structure.register.Register;
import come.example.utitled.syntax.AsmOPerationRealization;
import come.example.utitled.syntax.AsmOperations;

import java.util.*;

public class Emulator {

    /** Поддерживаемые в эмуляторе регистры **/
    private final List<String> supportedFullRegisters = Lists.newArrayList("eax","esi","ecx");
    private final List<String> supportedYangRegisters = Lists.newArrayList("ax","esi");

    private AsmProgramListing asmProgramListing;
    private List<Register> registers = new ArrayList<>();
    private Iterator<Command> currentCommandIterator;
    private Iterator<Map.Entry<String, List<Command>>> functionCommandIterator;
    private ZeroFlag zeroFlag = new ZeroFlag();

    public Emulator(AsmProgramListing asmProgramListing) {
        this.asmProgramListing = asmProgramListing;
        functionCommandIterator = asmProgramListing.getFunctionCommandIterator();
        currentCommandIterator = functionCommandIterator.next().getValue().listIterator();
    }


    public Command doStep() {
        Command command;
        if (currentCommandIterator.hasNext()) {
            command = currentCommandIterator.next();
        } else {
            if (functionCommandIterator.hasNext()) {
                currentCommandIterator = functionCommandIterator.next().getValue().listIterator();
                command = currentCommandIterator.next();
            } else {
                return null;
            }
        }
        return command;
    }

    public void operate(Command command) {
        //TODO: добавляем регистры, изменяем их стотстояние
        //TODO: реализация цикла при zeroFlag==0
        if (command.getOperator().equals(AsmOperations.XOR)) {
            Register register = AsmOPerationRealization.XOR_REGISTER.apply();
        }
    }

    private Object disassembling(Command command) {
        AsmOperations operator = command.getOperator();
        switch (operator) {
            case XOR:
                if(isRegister(command.getValue2())) {
                    
                }
            case ADD:
                System.out.println(2);
            default:
                System.out.println(3);
        }
        return null;
    }

    private boolean isRegister(String name) {
        if (supportedFullRegisters.contains(name) || supportedYangRegisters.contains(name)) {
            return true;
        } else
            return false;
    }

    public void start() {
        Command command = doStep();
        while(command!=null) {
            operate(command);
            command = doStep();
        }
    }





}
