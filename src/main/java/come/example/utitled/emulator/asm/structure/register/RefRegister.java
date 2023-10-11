package come.example.utitled.emulator.asm.structure.register;

import come.example.utitled.emulator.ArrayReference;
import come.example.utitled.emulator.AsmProgramListing;

/**
 * Представляет регистр для хранения адресов и операции над ними
 */
public class RefRegister extends Register<ArrayReference>{

    public RefRegister(RegisterName registerName, ArrayReference full, ArrayReference young) {
        super(registerName, full, young);
    }

    @Override
    void addValue(RegisterType registerType, Object value) {
        // do nothing
    }
}
