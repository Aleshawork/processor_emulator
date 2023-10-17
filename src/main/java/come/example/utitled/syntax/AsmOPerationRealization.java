package come.example.utitled.syntax;

import come.example.utitled.emulator.asm.structure.register.Register;
import come.example.utitled.emulator.asm.structure.register.RegisterName;

import java.util.function.BiFunction;

public class AsmOPerationRealization {

    public static BiFunction<Register, Register, Register> XOR_REGISTER = (reg1, reg2) -> {
        int value = (Integer) reg1.getValue() ^ (Integer) reg2.getValue();
        reg1.setValue(reg1.getRegisterType(), value);
        return reg1;
    };

    public static BiFunction<Register, Register, Register> ADD_REGISTER = (reg1, reg2) -> {
        int value = (Integer) reg1.getValue() + (Integer) reg2.getValue();
        reg1.setValue(reg1.getRegisterType(), value);
        return reg1;
    };

    public static BiFunction<Register, Register, Register> MOV_REGISTER = (reg1, reg2) -> {
        int value = (Integer) reg2.getValue();
        reg1.setValue(reg1.getRegisterType(), value);
        return reg1;
    };

    public static BiFunction<Register, Register, Register> MOV_REF_REGISTER = (reg1, reg2) -> {
        RegisterName name = reg1.getRegisterName();
        reg2.setRegisterName(name);
        return reg2;
    };




}
