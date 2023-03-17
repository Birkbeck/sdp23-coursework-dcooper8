package sml;

import sml.instruction.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Scanner;

import static sml.Registers.Register;

/**
 * This class ....
 * <p>
 * The translator of a <b>S</b><b>M</b>al<b>L</b> program.
 *
 * @author ...
 */
public final class Translator {

    private final String fileName; // source file of SML code

    // line contains the characters in the current line that's not been processed yet
    private String line = "";

    public Translator(String fileName) {
        this.fileName =  fileName;
    }

    // translate the small program in the file into lab (the labels) and
    // prog (the program)
    // return "no errors were detected"

    public void readAndTranslate(Labels labels, List<Instruction> program) throws IOException {
        try (var sc = new Scanner(new File(fileName), StandardCharsets.UTF_8)) {
            labels.reset();
            program.clear();

            // Each iteration processes line and reads the next input line into "line"
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                String label = getLabel();

                Instruction instruction = getInstruction(label);
                if (instruction != null) {
                    if (label != null)
                        labels.addLabel(label, program.size());
                    program.add(instruction);
                }
            }
        }
    }

    /**
     * Translates the current line into an instruction with the given label
     *
     * @param label the instruction label
     * @return the new instruction
     * <p>
     * The input line should consist of a single SML instruction,
     * with its label already removed.
     */
    private Instruction getInstruction(String label) {
        if (line.isEmpty())
            return null;

        String opcode = scan();
        switch (opcode) {
            case AddInstruction.OP_CODE -> {
                String r = scan();
                String s = scan();
                return new AddInstruction(label, Register.valueOf(r), Register.valueOf(s));
            }

            // TODO: add code for all other types of instructions
            // TODO: Then, replace the switch by using the Reflection API
            public class Translator {
                public static Instruction translate(String instructionString) throws TranslationException {
                    String[] tokens = instructionString.split("\\s+");
                    String opcode = tokens[0].toUpperCase();

                    try {
                        Class<?> instructionClass = Class.forName("com.example.Instructions." + opcode + "Instruction");
                        Constructor<?>[] constructors = instructionClass.getConstructors();
                        for (Constructor<?> constructor : constructors) {
                            Class<?>[] parameterTypes = constructor.getParameterTypes();
                            if (parameterTypes.length == tokens.length - 1) {
                                Object[] arguments = new Object[parameterTypes.length];
                                for (int i = 0; i < arguments.length; i++) {
                                    arguments[i] = parseParameter(parameterTypes[i], tokens[i + 1]);
                                }
                                return (Instruction) constructor.newInstance(arguments);
                            }
                        }
                        throw new TranslationException("Invalid number of operands for " + opcode + " instruction: " + instructionString);
                    } catch (ClassNotFoundException e) {
                        throw new TranslationException("Unknown instruction: " + instructionString);
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                        throw new TranslationException("Error creating instruction: " + instructionString, e);
                    }
                }

                private static Object parseParameter(Class<?> parameterType, String token) throws TranslationException {
                    if (parameterType == Register.class) {
                        return parseRegister(token);
                    } else if (parameterType == int.class) {
                        return parseAddress(token);
                    } else {
                        throw new TranslationException("Unsupported parameter type: " + parameterType.getName());
                    }
                }

            }

                private static Register parseRegister(String token) throws TranslationException {
                    try {
                        return Register.valueOf(token.toUpperCase());
                    } catch (IllegalArgumentException e) {
                        throw new TranslationException("Invalid register name: " + token);
                    }
                }

                private static int parseAddress(String token) throws TranslationException {
                    try {
                        return Integer.parseInt(token);
                    } catch (NumberFormatException e) {
                        throw new TranslationException("Invalid address: " + token);
                    }
                }
            }
            // TODO: Next, use dependency injection to allow this machine class
            //       to work with different sets of opcodes (different CPUs)
            public interface OpcodeSet {
                Instruction decode(int opcode);
            }

            public class Machine {
                private OpcodeSet opcodeSet;
                private Map<String, Integer> labels;
                private List<Instruction> program;
                private Map<Register, Integer> registers;
                private int programCounter;

                public Machine(OpcodeSet opcodeSet, Map<String, Integer> labels, List<Instruction> program, Map<Register, Integer> registers) {
                    this.opcodeSet = opcodeSet;
                    this.labels = labels;
                    this.program = program;
                    this.registers = registers;
                    this.programCounter = 0;
                }

                public void run() {
                    while (programCounter < program.size()) {
                        Instruction instr = program.get(programCounter);
                        instr.execute(this);
                        programCounter++;
                    }
                }

                public int getAddress(String label) {
                    return labels.get(label);
                }

                public int getRegisterValue(Register register) {
                    return registers.getOrDefault(register, 0);
                }

                public void setRegisterValue(Register register, int value) {
                    registers.put(register, value);
                }

                public OpcodeSet getOpcodeSet() {
                    return opcodeSet;
                }
            }

            default -> {
                System.out.println("Unknown instruction: " + opcode);
            }
        }
        return null;
    }


    private String getLabel() {
        String word = scan();
        if (word.endsWith(":"))
            return word.substring(0, word.length() - 1);

        // undo scanning the word
        line = word + " " + line;
        return null;
    }

    /*
     * Return the first word of line and remove it from line.
     * If there is no word, return "".
     */
    private String scan() {
        line = line.trim();

        for (int i = 0; i < line.length(); i++)
            if (Character.isWhitespace(line.charAt(i))) {
                String word = line.substring(0, i);
                line = line.substring(i);
                return word;
            }

        return line;
    }
}