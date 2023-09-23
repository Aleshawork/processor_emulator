package come.example.utitled;

import come.example.utitled.emulator.ProgramParser;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        ProgramParser programParser = null;
        try {
            programParser = new ProgramParser();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            programParser.parse();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
