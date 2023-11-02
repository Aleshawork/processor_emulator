package come.example.utitled.emulator.asm.structure.register;

import come.example.utitled.syntax.AsmOperations;

public interface RegisterOperations {
    void mov(Object value);
    void xor(Object value);
    void add(Object value);

    void dec(Object value);
    void jnz(Object value);

}
