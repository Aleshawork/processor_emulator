package come.example.utitled.emulator;

import com.google.common.collect.Lists;
import come.example.utitled.syntax.AsmArray;
import come.example.utitled.syntax.AsmData;
import come.example.utitled.syntax.AsmNumber;

import java.util.*;

/** Хранилище контекста программы на Asm **/
public class AsmProgramListing {

    /** Хранилище массивов**/
    private List<Object> arraysHolder = new ArrayList<>();

    /** Ссылки на расположение массивов в хранилище **/
    private Map<String, ArrayReference> arrayReferenceMap = new HashMap();

    /** Текущее положение **/
    private int arraysHolderCursor = 0;

    /** Имя функции точки входа **/
    private String mainFunctionName;

    /** Хранилище переменных **/
    private List<AsmNumber> asmNumberList = new ArrayList<>();
    
    /** Хранилище команд. Команды хранятся в порядке следования **/
    private Map<String, List<Command>> commands = new LinkedHashMap<>();

    /**
     * Получение следующнго элемента массива из контекста
     * @return
     */
    public Object getNextArraysValue() {
        arraysHolderCursor++;
        return arraysHolder.get(arraysHolderCursor);
    }

    public void addArray(AsmArray asmArray) {
        if (arrayReferenceMap.containsKey(asmArray.getName())) {
            throw new RuntimeException(String.format("Массив с именем %s уже существует!", asmArray.getName()));
        } else {
            arrayReferenceMap.put(asmArray.getName(), new ArrayReference(asmArray.getArray().size(), arraysHolder.size()));
            arraysHolder.addAll(asmArray.getArray());
        }
    }

    /**
     * Добавление переменных и массивов в контекст
     * @param asmData
     */
    public void addData(AsmData asmData) {
        if (asmData instanceof AsmArray) {
            addArray((AsmArray) asmData);
        } else {
            this.asmNumberList.add((AsmNumber) asmData);
        }
    }

    public String getMainFunctionName() {
        return mainFunctionName;
    }

    public void setMainFunctionName(String mainFunctionName) {
        this.mainFunctionName = mainFunctionName;
    }


    public void addCommands(String functionName, Command command) {
        if (this.commands.containsKey(functionName)) {
            this.commands.get(functionName).add(command);
        } else {
            this.commands.put(functionName, Lists.newArrayList(command));
        }
    }

    public Map<String, List<Command>> getCommands() {
        return commands;
    }

    public List<Command> commandsByFunction(String functionName) {
        return this.commands.get(functionName);
    }

    class ArrayReference {
        private int size;
        private int firstPosition;


        public ArrayReference(int size, int firstPosition) {
            this.size = size;
            this.firstPosition = firstPosition;
        }

        public int getSize() {
            return size;
        }

        public int getFirstPosition() {
            return firstPosition;
        }
    }


}
