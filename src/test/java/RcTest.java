import come.example.utitled.emulator.Emulator;
import come.example.utitled.emulator.asm.structure.BinarCommand;
import come.example.utitled.emulator.asm.structure.Command;
import come.example.utitled.syntax.AsmOperations;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class RcTest {

    @Test
    public void binaryToDecimalTest() {
        assertEquals(Emulator.binaryToDecimal("0111"), "7");
    }

    @Test
    public void getRegisterNumberByCommandValueTest() {
        assertEquals(Emulator.getRegisterNumberByCommandValue("dx", null), "3");
        assertEquals(Emulator.getRegisterNumberByCommandValue("4", null), "4");
        assertEquals(Emulator.getRegisterNumberByCommandValue("112", null), "112");
    }

    @Test
    public void calculateRCTest() {
        Command command = new BinarCommand(AsmOperations.ADD, "ax", "dx");
        assertEquals(Emulator.calculateRC(command, null), "0x 03 5 2 3");


//        Command command2 = new BinarCommand(AsmOperations.MOV, "dx", "[esi]");
//        assertEquals(Emulator.calculateRC(command2), "0x 01 7 3 4");

    }

}
