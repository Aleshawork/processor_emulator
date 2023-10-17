package come.example.utitled.emulator;

import com.google.common.collect.Lists;
import come.example.utitled.emulator.asm.structure.BinarCommand;
import come.example.utitled.emulator.asm.structure.Command;
import come.example.utitled.emulator.asm.structure.flag.ZeroFlag;
import come.example.utitled.emulator.asm.structure.register.NumericRegister;
import come.example.utitled.emulator.asm.structure.register.RefRegister;
import come.example.utitled.emulator.asm.structure.register.Register;
import come.example.utitled.emulator.asm.structure.register.RegisterName;
import come.example.utitled.syntax.AsmOPerationRealization;
import come.example.utitled.syntax.AsmOperations;
import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class Emulator {

    /** Поддерживаемые в эмуляторе регистры **/
    private final List<String> supportedFullRegisters = Lists.newArrayList("eax","esi","ecx");
    private final List<String> supportedYangRegisters = Lists.newArrayList("ax","esi");

    private AsmProgramContext asmProgramContext;

    /** наименование регистра - Регистр **/
    private Map<RegisterName, Register> registers = new HashMap<>();
    private Iterator<Command> currentCommandIterator;
    private Iterator<Map.Entry<String, List<Command>>> functionCommandIterator;
    private ZeroFlag zeroFlag = new ZeroFlag();

    public Emulator(AsmProgramContext asmProgramContext) {
        this.asmProgramContext = asmProgramContext;
        functionCommandIterator = asmProgramContext.getFunctionCommandIterator();
        currentCommandIterator = functionCommandIterator.next().getValue().listIterator();
    }


    public void start() {
        Command command = doStep();
        while(command!=null) {
            processCommand(command);
            command = doStep();
        }
    }

    /**
     * Получение очередной команды на выполнение из контекста
     * @return Command
     */
    public Command doStep() {
        Command command;
        if (currentCommandIterator.hasNext()) {
            command = currentCommandIterator.next();
        } else {
            if (functionCommandIterator.hasNext()) {
                currentCommandIterator = functionCommandIterator.next().getValue().listIterator();
                command = currentCommandIterator.next();
            } else {
                return null;
            }
        }
        return command;
    }

    public void operate(Command command) {
        //TODO: добавляем регистры, изменяем их стотстояние
        //TODO: реализация цикла при zeroFlag==0
        if (command.getOperator().equals(AsmOperations.XOR)) {
            //Register register = AsmOPerationRealization.XOR_REGISTER.apply();
        }
    }

    /**
     * Определение типов аргументов
     * @param command
     * 1. регистр и регистр
     * 2. регистр и ссылка на массив
     * 3. регистр и значение
     *  3.1 []
     *  3.2 численное значение
     * 4. регистр (унарная операция)
     */
    private void processCommand(Command command) {
        if (command instanceof BinarCommand) {
            Register firstRegister;
            if (isRegister(command.getValue1()) && isRegister(command.getValue2())) {
                // 1
                firstRegister = processNumericRegister(command.getValue1(), 0);
                firstRegister = calculate(command.getOperator(), firstRegister, processNumericRegister(command.getValue2(), 0));

            } else if (isRegister(command.getValue1()) && isReference(command.getValue2())) {
                RefRegister refRegister = processRefRegister(command.getValue2());
                firstRegister = processNumericRegister(command.getValue1(), 0);
                firstRegister = calculate(command.getOperator(), firstRegister, refRegister);


            } else if (isRegister(command.getValue1()) && isValue(command.getValue2())) {
                // 3

            } else {
                System.out.println(4);
                // do something
            }
        } else {
            // унарная команда
            // инкримент, декримент, условный переход

        }

    }

    /**
     * Выполняет операцию ASM
     * @param operator наименование операции
     * @param firstRegister первый регистр
     * @param secondRegister второй регистр
     * @return изменившийся регистр
     */
    private Register calculate(AsmOperations operator, Register firstRegister, Register secondRegister) {
        Register register = null;
        switch (operator) {
            case XOR:
                register = AsmOPerationRealization.XOR_REGISTER.apply(firstRegister, secondRegister);
                break;
            case ADD:
                register = AsmOPerationRealization.ADD_REGISTER.apply(firstRegister, secondRegister);
                break;
            case MOV:
                register = secondRegister instanceof RefRegister ? AsmOPerationRealization.MOV_REF_REGISTER.apply(firstRegister, secondRegister) :
                        AsmOPerationRealization.MOV_REGISTER.apply(firstRegister, secondRegister);
                registers.replace(register.getRegisterName(), register);
            default:
                System.out.println(3);
        }
        firstRegister = register;
        return firstRegister;
    }

    /**
     * Проверка на допустимость проверяемого регистра в рамках эмулятора.
     * @param name имя регистра в команде
     * @return
     * @see Emulator#supportedFullRegisters 32 битные регистры
     * @see Emulator#supportedYangRegisters 16 битные регистры
     */
    private boolean isRegister(String name) {
        if (supportedFullRegisters.contains(name) || supportedYangRegisters.contains(name)) {
            return true;
        } else
            return false;
    }

    /**
     *
     * @param name
     * @return
     */
    private boolean isReference(String name) {
        if (asmProgramContext.hasArray(name)) {
            return true;
        } else
            return false;
    }

    private boolean isValue(String name) {
        return name.contains("[") || (!isReference(name) && !isRegister(name));
    }

    /**
     * Устанавливает переданное значение найденному в контексте по имени регитру и возвращает его.
     * Если регистр отсутствует в контексте, то добавляет в контекст и устанавливает указанное значение
     * @param name имя регистра
     * @param value значение регистра
     * @return Register
     */
    public NumericRegister processNumericRegister(String name, Integer value) {
        NumericRegister numericRegister;
        if (registers.containsKey(name)) {
            return (NumericRegister) registers.get(name);
        } else {
            if (supportedYangRegisters.contains(name) && !registers.containsKey(name)) {
                numericRegister = new NumericRegister(RegisterName.valueOf(StringUtils.toRootUpperCase(name)), null, value);
            } else if (supportedFullRegisters.contains(name) && !registers.containsKey(name)) {
                numericRegister = new NumericRegister(RegisterName.valueOf(StringUtils.toRootUpperCase(name)), value, null);
            } else {
                throw new RuntimeException(String.format("Проблема инициализации регистра %s", name));
            }
            registers.put(RegisterName.valueOf(StringUtils.toRootUpperCase(name)), numericRegister);
        }
        return numericRegister;
    }

    /**
     * Обработчик ссылочных регистров.
     * Если регистр отсутствует в контексте, то добавляет.
     * @param name имя регистра
     * @return Register
     */
    public RefRegister processRefRegister(String name) {
        RefRegister refRegister;

        if (AsmProgramContext.hasArray(name)) {
            ArrayReference arrayReference = AsmProgramContext.getArrayReferenceByName(name);
            refRegister = new RefRegister(RegisterName.REF, arrayReference, null );
        } else {
            throw new RuntimeException(String.format("Массив не найден :%s", name));
        }

        return refRegister;
    }






}
