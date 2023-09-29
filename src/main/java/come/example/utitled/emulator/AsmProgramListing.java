package come.example.utitled.emulator;

import come.example.utitled.syntax.AsmArray;
import come.example.utitled.syntax.AsmData;
import come.example.utitled.syntax.AsmNumber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * Получение следующнго элемента массива из области памяти
     * @return
     */
    public Object getNextArraysValue() {
        arraysHolderCursor++;
        return arraysHolder.get(arraysHolderCursor);
    }

    /** Хранилище команд. Команды хранятся в порядке следования **/
    private List<Command> commands = new ArrayList<>();

    public void addArray(AsmArray asmArray) {
        if (arrayReferenceMap.containsKey(asmArray.getName())) {
            throw new RuntimeException(String.format("Массив с именем %s уже существует!", asmArray.getName()));
        } else {
            arrayReferenceMap.put(asmArray.getName(), new ArrayReference(asmArray.getArray().size(), arraysHolder.size()));
            arraysHolder.addAll(asmArray.getArray());
        }
    }

    /**
     * Добавление переменных и массивов в память программы
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

    public List<Command> getCommands() {
        return commands;
    }

    public void addCommands(Command command) {
        this.commands.add(command);
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
