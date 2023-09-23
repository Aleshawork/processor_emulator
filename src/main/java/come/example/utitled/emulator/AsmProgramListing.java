package come.example.utitled.emulator;

import come.example.utitled.syntax.AsmNumber;

import java.util.ArrayList;
import java.util.List;

public class AsmProgramListing {

    private List<Object> arraysHolder = new ArrayList<>();
    private int arraysHolderCursor = 0;

    private List<AsmNumber> asmNumberList = new ArrayList<>();

    public Object getNextArraysValue() {
        arraysHolderCursor++;
        return arraysHolder.get(arraysHolderCursor);
    }




}
