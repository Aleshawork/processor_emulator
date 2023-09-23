package come.example.utitled.emulator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.regex.Pattern;

import static come.example.utitled.emulator.Configuration.PROGRAM_ENCODING;

public class ProgramParser {

    private static final String SECTION = "section";
    private static final String SECTION_DATA = ".data";
    private static final String SECTION_TEXT = ".text";
    private static final String GLOBAL = "global";

    private Logger logger = LoggerFactory.getLogger(ProgramParser.class);

    private BufferedReader bufferedReader;

    public ProgramParser() throws FileNotFoundException {
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
            return line;
        } else return null;
    }


    public AsmProgramListing parse() throws IOException {
        String line = readNext();
        if (line.contains(SECTION) && line.contains(SECTION_DATA)) {
            while(!line.contains(SECTION_TEXT)) {
                line = readNext();

            }
        }




        return new AsmProgramListing();
    }

}
