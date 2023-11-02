package come.example.utitled.emulator;

import come.example.utitled.emulator.asm.structure.BinarCommand;
import come.example.utitled.emulator.asm.structure.Command;
import come.example.utitled.emulator.asm.structure.TransitionCommand;
import come.example.utitled.emulator.asm.structure.UnarCommand;
import come.example.utitled.syntax.AsmOperations;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

import static come.example.utitled.emulator.Configuration.PROGRAM_ENCODING;

public class ProgramParser {
    private static final String[] replaceableCharacters = {";", ",", ":"};
    private static final String[] substituteCharacters = { "",  "",  ""};
    private static final String SECTION = "section";
    private static final String SECTION_DATA = ".data";
    private static final String SECTION_TEXT = ".text";
    private static final String GLOBAL = "global";
    private static final String RET = "ret";
    private final AsmProgramContext asmProgramContext;
    private final AsmDataReader asmDataReader;


    private Logger logger = LoggerFactory.getLogger(ProgramParser.class);

    private BufferedReader bufferedReader;

    public ProgramParser(AsmProgramContext asmProgramContext) throws FileNotFoundException {
        this.asmProgramContext = asmProgramContext;
        asmDataReader = new AsmDataReader(asmProgramContext);
        try {
            File file = new File(Configuration.PROGRAM_PATH);
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, PROGRAM_ENCODING);
            this.bufferedReader = new BufferedReader(inputStreamReader);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            logger.error("File  doesn't exist!");
            throw new FileNotFoundException();
        }
    }

    private String readNext() throws IOException {
        String line;
        if ((line = bufferedReader.readLine()) != null) {
            while(line.equals("") && line != null) {
                line = bufferedReader.readLine();
            }
            return StringUtils.normalizeSpace(line);
        } else return null;
    }


    /**
     * Считывание проограммы Asm из файла
     * @return заполненный контекст программы
     * @throws IOException
     */
    public AsmProgramContext parse() throws IOException {
        String function;
        String line = readNext();
        if (line.contains(SECTION) && line.contains(SECTION_DATA)) {
            line = readNext();
            while(!line.contains(SECTION_TEXT)) {
                asmProgramContext.addData(asmDataReader.readData(line));
                line = readNext();
            }
        }
        if (line.contains(SECTION) && line.contains(SECTION_TEXT)) {
            line = readNext();
            if (line.contains(GLOBAL)) {
                function = StringUtils.split(line, " ")[1];
                asmProgramContext.setMainFunctionName(function);
                line = readNext();
            } else {
                throw  new RuntimeException("Program doesn't has global function in program!");
            }
            if (!line.contains(asmProgramContext.getMainFunctionName())) {
                throw new RuntimeException("Doesn't find global function in code!");
            } else {
                while((line = readNext()) != null) {
                    if (line.contains(":")) {
                        function = StringUtils.split(line, ":")[0];
                    } else if (line.contains(RET)) {
                        break;
                    } else {
                        Command command = null;
                        String[] s = line.split(" ");
                        if (line.contains(AsmOperations.JNZ.getName())) {
                            command = new TransitionCommand(AsmOperations.readOperation(s[0]), s[1]);
                        } else if (s.length == 2) {
                            command = new UnarCommand(AsmOperations.readOperation(s[0]), s[1]);
                        } else if (s.length == 3) {
                            command = new BinarCommand(AsmOperations.readOperation(s[0]), s[1], s[2]);
                        } else {
                            throw new RuntimeException("Command reading error!");
                        }
                        asmProgramContext.addCommands(function, normalizeCommand(command));
                    }
                }
            }
        } else {
            throw new RuntimeException("Section \".text\" doesn't exists !");
        }

        return this.asmProgramContext;
    }

    private Command normalizeCommand(Command command) {
      command.setValue1(StringUtils.replaceEach(command.getValue1(), replaceableCharacters, substituteCharacters));
      command.setValue2(StringUtils.replaceEach(command.getValue2(), replaceableCharacters, substituteCharacters));
      return command;
    }

}
