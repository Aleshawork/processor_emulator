package come.example.utitled.emulator;

import come.example.utitled.syntax.AsmData;
import come.example.utitled.syntax.AsmOperations;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

import static come.example.utitled.emulator.Configuration.PROGRAM_ENCODING;

public class ProgramParser {

    private static final String SECTION = "section";
    private static final String SECTION_DATA = ".data";
    private static final String SECTION_TEXT = ".text";
    private static final String GLOBAL = "global";
    private static final String RET = "ret";
    private final AsmProgramListing asmProgramListing;
    private final AsmDataReader asmDataReader;


    private Logger logger = LoggerFactory.getLogger(ProgramParser.class);

    private BufferedReader bufferedReader;

    public ProgramParser(AsmProgramListing asmProgramListing) throws FileNotFoundException {
        this.asmProgramListing = asmProgramListing;
        asmDataReader = new AsmDataReader(asmProgramListing);
        try {
            File file = new File(Configuration.PROGRAM_PATH);
            FileInputStream fileInputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, PROGRAM_ENCODING);
            this.bufferedReader = new BufferedReader(inputStreamReader);
        } catch (FileNotFoundException | UnsupportedEncodingException e) {
            logger.error("Файл с программой не найден!");
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


    public AsmProgramListing parse() throws IOException {
        String line = readNext();
        if (line.contains(SECTION) && line.contains(SECTION_DATA)) {
            line = readNext();
            while(!line.contains(SECTION_TEXT)) {
                asmProgramListing.addData(asmDataReader.readData(line));
                line = readNext();
            }

        }
        if (line.contains(SECTION) && line.contains(SECTION_TEXT)) {
            line = readNext();
            if (line.contains(GLOBAL)) {
                asmProgramListing.setMainFunctionName(StringUtils.split(line, " ")[1]);
                line = readNext();
            } else {
                throw  new RuntimeException("Program doesn't has global function in program!");
            }
            if (!line.contains(asmProgramListing.getMainFunctionName())) {
                throw new RuntimeException("Doesn't find global function in code!");
            } else {
                while((line = readNext()) != null) {
                    if (line.contains(":")) {
                        // todo:: обработка цикла
                        System.out.println(1);
                    } else if (line.contains(RET)) {
                        break;
                    } else {
                        Command command = null;
                        String[] s = line.split(" ");
                        if (s.length == 2) {
                            command = new UnarCommand(AsmOperations.readOperation(s[0]), s[1]);
                        } else if (s.length == 3) {
                            command = new BinarCommand(AsmOperations.readOperation(s[0]), s[1], s[2]);
                        } else {
                            // do nothing
                        }
                        asmProgramListing.addCommands(command);
                    }
                }
            }
        } else {
            throw new RuntimeException("Section .text doesn't exists !");
        }

        return new AsmProgramListing();
    }

}
