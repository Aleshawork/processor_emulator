package come.example.utitled.emulator.asm.structure.register;

import java.util.Objects;

/**
 * Регистр full представляет полный 32 битный регистр
 * регистр young - младший 16 битный.
 * В данной реализации значения могут хранится только в одном из full или young,
 * второй зануляется.
 */
public abstract class Register<T> {

    private RegisterName registerName;

    private RegisterType registerType;

    /** Весь регистр **/
    protected T full;

    /** Младший регистр **/
    protected T young;

    public Register(RegisterName registerName, T full, T young) {
        this.registerName = registerName;
        if (Objects.isNull(full) && !Objects.isNull(young)) {
            this.young = young;
            registerType = RegisterType.YOUNG;
        } else if (!Objects.isNull(full) && Objects.isNull(young)) {
            this.full = full;
            registerType = RegisterType.FULL;
        } else {
            throw new RuntimeException("Ошибка добавления регистра в контекст!");
        }
    }

    public void setValue(RegisterType registerType, T value) {
       if (RegisterType.FULL.equals(registerType)) {
           this.full = value;
           this.young = null;
       } else {
           this.young = value;
           this.full = null;
       }
    }

    public RegisterType getRegisterType() {
        return this.registerType;
    }

    public abstract T getValue();

    public RegisterName getRegisterName() {
        return registerName;
    }

    abstract void addValue(RegisterType registerType, Object value);
}
