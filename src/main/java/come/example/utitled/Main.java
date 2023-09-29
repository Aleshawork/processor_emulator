package come.example.utitled;

import come.example.utitled.emulator.AsmProgramListing;
import come.example.utitled.emulator.ProgramParser;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        ProgramParser programParser = null;
        try {
            AsmProgramListing asmProgramListing = new AsmProgramListing();
            programParser = new ProgramParser(asmProgramListing);
            programParser.parse();
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
