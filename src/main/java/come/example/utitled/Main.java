package come.example.utitled;

import come.example.utitled.emulator.AsmProgramListing;
import come.example.utitled.emulator.Emulator;
import come.example.utitled.emulator.ProgramParser;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        AsmProgramListing asmProgramListing = new AsmProgramListing();
        ProgramParser programParser = new ProgramParser(asmProgramListing);
        asmProgramListing = programParser.parse();

        Emulator emulator = new Emulator(asmProgramListing);
        emulator.start();
    }
}
