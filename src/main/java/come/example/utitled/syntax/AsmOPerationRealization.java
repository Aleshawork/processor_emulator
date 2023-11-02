package come.example.utitled.syntax;

import come.example.utitled.emulator.AsmProgramContext;
import come.example.utitled.emulator.asm.structure.register.NumericRegister;
import come.example.utitled.emulator.asm.structure.register.RefRegister;
import come.example.utitled.emulator.asm.structure.register.Register;
import come.example.utitled.emulator.asm.structure.register.RegisterName;

import java.util.function.BiFunction;
import java.util.function.Function;

public class AsmOPerationRealization {

    public static BiFunction<Register, Register, Register> XOR_REGISTER = (reg1, reg2) -> {
        int value = (Integer) reg1.getValue() ^ (Integer) reg2.getValue();
        reg1.setValue(reg1.getRegisterType(), value);
        return reg1;
    };

    public static BiFunction<Register, Register, Register> ADD_REGISTER = (reg1, reg2) -> {
        int value;
        if (reg2 instanceof RefRegister) {
            RefRegister ref = (RefRegister) reg2;
            value = (Integer) reg1.getValue() + Integer.valueOf((String) AsmProgramContext.arraysHolder.get(ref.getValue().getFirstPosition() + ref.getPosition()));
        } else {
            value = (Integer) reg1.getValue() + (Integer) reg2.getValue();
        }
        reg1.setValue(reg1.getRegisterType(), value);
        return reg1;
    };

    public static BiFunction<Register, Register, Register> MOV_REGISTER = (reg1, reg2) -> {
        int value = (Integer) reg2.getValue();
        reg1.setValue(reg1.getRegisterType(), value);
        return reg1;
    };

    public static BiFunction<Register, Integer, Register> MOVE_VALUE = (reg, value) -> {
        reg.setValue(reg.getRegisterType(), value);
        return reg;
    };

    public static BiFunction<Register, Integer, Register> ADD_VALUE = (reg, value) -> {
        reg.setValue(reg.getRegisterType(), (Integer)reg.getValue() + value);
        return reg;
    };

    public static BiFunction<Register, Register, Register> MOV_REF_REGISTER = (reg1, reg2) -> {
        RegisterName name = reg1.getRegisterName();
        reg2.setRegisterName(name);
        return reg2;
    };

    public static Function<Register, Register> DEC_VALUE = (reg) -> {
        reg.setValue(reg.getRegisterType(), (Integer) reg.getValue() - 1);
        return reg;
    };




}
