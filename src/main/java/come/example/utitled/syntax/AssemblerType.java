package come.example.utitled.syntax;

/**
 * Типы данных во входящей программе
 */
public enum AssemblerType {

    DB("db", 1, "Байт"),
    DW("dw", 2, "Слово");

    private String name;
    private int size;
    private String description;

    AssemblerType(String name, int size, String description) {
        this.name = name;
        this.size = size;
        this.description = description;
    }
}
