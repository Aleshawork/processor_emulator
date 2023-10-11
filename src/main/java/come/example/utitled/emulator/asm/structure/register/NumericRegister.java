package come.example.utitled.emulator.asm.structure.register;

/**
 * Представляет регистр для хранения целых чисел и операции над ними
 */
public class NumericRegister extends Register<Integer> implements RegisterOperations{

    public NumericRegister(RegisterName registerName, Integer full, Integer young) {
        super(registerName, full, young);
    }

    @Override
    void addValue(RegisterType registerType, Object value) {
        if (RegisterType.FULL.equals(registerType)) {
            this.full += (Integer) value;
        } else {
            this.young += (Integer) value;
        }
    }

    @Override
    public void mov(Object value) {
        this.setValue(this.getRegisterType(), (Integer) value);
    }

    @Override
    public void xor(Object value) {

    }

    @Override
    public void add(Object value) {

    }

    @Override
    public void dec(Object value) {

    }

    @Override
    public void jnz(Object value) {

    }
}
