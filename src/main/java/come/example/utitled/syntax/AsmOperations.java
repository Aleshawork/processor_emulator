package come.example.utitled.syntax;

public enum AsmOperations {

    MOV("mov", "Переместить"),
    XOR("XOR", "Исключающее или"),
    ADD("add", "Сложение"),

    DEC("dec", "Декремент"),
    JNZ("jnz", "Условный переход")
    ;

    private String name;
    private String description;


    AsmOperations(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
