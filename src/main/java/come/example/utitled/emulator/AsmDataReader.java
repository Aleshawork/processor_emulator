package come.example.utitled.emulator;


import come.example.utitled.syntax.AsmArray;
import come.example.utitled.syntax.AsmData;
import come.example.utitled.syntax.AsmNumber;
import come.example.utitled.syntax.AssemblerType;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AsmDataReader {

    private final AsmProgramListing programListing;

    public AsmDataReader(AsmProgramListing programListing) {
        this.programListing = programListing;
    }

    // пробелы в массиве между значениями не допускаются
    Pattern arrayPattern = Pattern.compile("(\\w+)\\s+(\\w{2})\\s+(\\d+(?:,\\d+)+)");


    /**
     * Считывает массивы и переменные.
     * @param line
     * @return
     */
    public AsmData readData(String line) {
        Matcher arrayMatcher = arrayPattern.matcher(line);
        AsmData asmData;
        String[] splitArray = StringUtils.split(line, " ");
        if (arrayMatcher.matches()) {
            asmData = new AsmArray(readType(splitArray[1]), splitArray[0], List.of(StringUtils.split(splitArray[2], ",")));
        } else {
            asmData = new AsmNumber(readType(splitArray[1]), splitArray[0], splitArray[2]);
        }
        return asmData;
    }

    private AssemblerType readType(String type) {
        return AssemblerType.readAsmType(type);
    }
}
