package come.example.utitled;

import come.example.utitled.emulator.AsmProgramContext;
import come.example.utitled.emulator.Emulator;
import come.example.utitled.emulator.ProgramParser;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        AsmProgramContext asmProgramContext = new AsmProgramContext();
        ProgramParser programParser = new ProgramParser(asmProgramContext, "program.txt");
        asmProgramContext = programParser.parse();

        Emulator emulator = new Emulator(asmProgramContext);
        emulator.start();
    }
}
