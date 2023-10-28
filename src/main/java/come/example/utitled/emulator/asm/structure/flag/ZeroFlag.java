package come.example.utitled.emulator.asm.structure.flag;

/**
 * Арифметические операции:
     *  После выполнения арифметических операций, таких как сложение или вычитание,
     *  флаг ZF устанавливается в 0, если результат операции равен нулю, и в 1, если результат не равен нулю.
 *  Логические операции:
 *
 */
public class ZeroFlag {

    private static boolean value = true;

    public static boolean changeFlag() {
        value = !value;
        return value;
    }

    public static boolean isEnd() {
        return !value;
    }
}
