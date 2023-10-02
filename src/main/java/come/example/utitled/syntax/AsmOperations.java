package come.example.utitled.syntax;

import org.apache.commons.lang3.StringUtils;

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

    public static AsmOperations readOperation(String operation) {
        return valueOf(StringUtils.upperCase(operation));
    }
}
