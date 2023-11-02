package come.example.utitled.emulator.asm.structure.register;

import come.example.utitled.emulator.ArrayReference;
import come.example.utitled.emulator.AsmProgramContext;

import java.util.Objects;

/**
 * Представляет регистр для хранения адресов и операции над ними
 */
public class RefRegister extends Register<ArrayReference>{

    /** количество считанных элементов с начала массива **/
    private int position;

    /** ссылка хранится в full **/
    public RefRegister(RegisterName registerName, ArrayReference full, ArrayReference young) {
        super(registerName, full, young);
        position = 0;
    }

    @Override
    public ArrayReference getValue() {
        return full;
    }

    public void iterate(int delta) {
        position+=delta;
    }

    /**
     * Получение текущего значения
     * @return
     */
    public Object getCurrentValue() {
        int firstPosition = young.getFirstPosition();
        int size = young.getSize();
        int currentPosition = firstPosition + position;
        if (currentPosition > size) {
            throw new ArrayIndexOutOfBoundsException();
        }
        return AsmProgramContext.getArraysHolder().get(position);
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

    public int getPosition() {
        return position;
    }
}
