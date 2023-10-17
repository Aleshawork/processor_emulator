package come.example.utitled.emulator.asm.structure.register;

import come.example.utitled.emulator.ArrayReference;

import java.util.Objects;

/**
 * Представляет регистр для хранения адресов и операции над ними
 */
public class RefRegister extends Register<ArrayReference>{

    /** количество считанных элементов с начала массива **/
    private int position;

    public RefRegister(RegisterName registerName, ArrayReference full, ArrayReference young) {
        super(registerName, full, young);
        position = 0;
    }

    @Override
    public ArrayReference getValue() {
        position++;
        ArrayReference arrayReference = getArrayReference();


        return null;
    }

    @Override
    void addValue(RegisterType registerType, Object value) {
        // do nothing
    }

    private ArrayReference getArrayReference() {
        if (Objects.isNull(full)) {
            return young;
        } else
            return full;
    }

}
