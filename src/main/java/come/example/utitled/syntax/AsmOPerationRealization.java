package come.example.utitled.syntax;

import come.example.utitled.emulator.asm.structure.register.Register;

import java.util.function.BiFunction;

public class AsmOPerationRealization {

    public static BiFunction<Register, Register, Register> XOR_REGISTER = (reg1, reg2) -> {
        int i = (Integer) reg1.getValue() ^ (Integer) reg2.getValue();
        reg1.setValue(reg1.getRegisterType(), reg1.getValue());
        return reg1;
    };


}
