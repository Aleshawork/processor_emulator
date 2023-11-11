package come.example.utitled.emulator;

import com.google.common.collect.Lists;
import come.example.utitled.emulator.asm.structure.BinarCommand;
import come.example.utitled.emulator.asm.structure.Command;
import come.example.utitled.emulator.asm.structure.flag.ZeroFlag;
import come.example.utitled.emulator.asm.structure.register.NumericRegister;
import come.example.utitled.emulator.asm.structure.register.RefRegister;
import come.example.utitled.emulator.asm.structure.register.Register;
import come.example.utitled.emulator.asm.structure.register.RegisterName;
import come.example.utitled.emulator.exceptions.NotFoundBinaryCommandException;
import come.example.utitled.emulator.exceptions.NotFoundBinaryValueCommandException;
import come.example.utitled.emulator.exceptions.NotFountUnarCommandAsmException;
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
            Map<RegisterName, Register> registerNameRegisterMap = processCommand(command);
            outRegistersData(registerNameRegisterMap, command.commandToString());
            command = doStep();
        }
    }

    public void outRegistersData(Map<RegisterName, Register> registers, String command) {
        System.out.println(String.format("Команда на выполнение: %s", command));
        registers.entrySet()
                .forEach(register -> {
                    if (register.getValue() instanceof RefRegister) {
                        System.out.println(String.format("%s  :  %s", register.getKey().name(), AsmProgramContext.getRefRegValue(((RefRegister)register.getValue()).getValue(), (RefRegister) register.getValue())));
                    } else {
                        System.out.println(String.format("%s  :  %s", register.getKey().name(), register.getValue().getValue()));
                    }
                });

        System.out.println("-----------------------------------------------------");
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


    /**
     * Определение типов аргументов
     * @param command
     * 1. регистр и регистр
     * 2. регистр и ссылка на массив
     * 3. регистр и значение || ссылка
     *  3.1 []
     *  3.2 [array]
     *  3.3 численное значение
     * 4. регистр (унарная операция)
     */
    private Map<RegisterName, Register> processCommand(Command command) {
        Register firstRegister;
        if (command instanceof BinarCommand) {
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
                firstRegister = processNumericRegister(command.getValue1(), 0);
                if (command.getValue2().contains("[")) {
                    String argsName = StringUtils.replaceEach(command.getValue2(), new String[]{"[","]"}, new String[]{"", ""});
                    // если [array]
                    if (isRegister(argsName)) {
                        firstRegister = calculate(command.getOperator(), firstRegister, registers.get(RegisterName.valueOf(StringUtils.toRootUpperCase(argsName))));
                    } else {
                        firstRegister = calculateWithValue(command.getOperator(), firstRegister, Integer.parseInt((String) asmProgramContext.getDataByName(argsName).getValue()));
                    }
                } else {

                    firstRegister = calculateWithValue(command.getOperator(), firstRegister, Integer.valueOf(command.getValue2()));

                }

            } else {
                System.out.println(4);
                // do something
            }
        } else {
            if (command.getOperator().equals(AsmOperations.JNZ)) {
                if (!ZeroFlag.isEnd()) {
                    //TODO: переставить итератор на начало цикла
                    currentCommandIterator = asmProgramContext.getCommands().get(command.getValue1()).iterator();
                    return registers;

                } else {
                    // do nothing
                    System.out.println("end");
                    return registers;
                }
            }
            firstRegister = processNumericRegister(command.getValue1(), 0);
            firstRegister = calculateUnarCommand(command.getOperator(), firstRegister);
            // унарная команда
            // инкримент, декримент, условный переход

        }
        return registers;

    }

    private Register calculateUnarCommand(AsmOperations operator, Register register) {
        switch (operator) {
            case DEC:
                register = AsmOPerationRealization.DEC_VALUE.apply(register);
                NumericRegister numericRegister = (NumericRegister) register;
                if (register.getValue().equals(0)) {
                    ZeroFlag.changeFlag();
                }
                break;
            default:
                throw new NotFountUnarCommandAsmException(String.format("Не найдена бинарная опреация: %s", operator.getName()));
        }
        return register;
    }

    private Register calculateWithValue(AsmOperations operator, Register firstRegister, Integer value) {
        Register register = null;
        switch (operator) {
            case MOV:
                register = AsmOPerationRealization.MOVE_VALUE.apply(firstRegister, value);
                break;
            case ADD:
                if (firstRegister instanceof RefRegister) {
                    RefRegister ref = (RefRegister) firstRegister;
                    ref.iterate(value/2);
                    register = ref;
                } else {
                    register = AsmOPerationRealization.ADD_VALUE.apply(firstRegister, value);
                }
                break;
            default:
                throw new NotFoundBinaryValueCommandException(String.format("Не найдена бинарная опреация: %s", operator.getName()));
        }
        return register;
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
                break;
            default:
                throw new NotFoundBinaryCommandException(String.format("Не найдена бинарная опреация: %s", operator.getName()));
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
    public Register processNumericRegister(String name, Integer value) {
        NumericRegister numericRegister;
        if (registers.containsKey(RegisterName.valueOf(StringUtils.toRootUpperCase(name)))) {
            return  registers.get(RegisterName.valueOf(StringUtils.toRootUpperCase(name)));
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
