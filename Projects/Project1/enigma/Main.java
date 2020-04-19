package enigma;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import static enigma.EnigmaException.*;

/** Enigma simulator.
 *  @author Ayela Chughtai
 */
public final class Main {

    /** Process a sequence of encryptions and decryptions, as
     *  specified by ARGS, where 1 <= ARGS.length <= 3.
     *  ARGS[0] is the name of a configuration file.
     *  ARGS[1] is optional; when present, it names an input file
     *  containing messages.  Otherwise, input comes from the standard
     *  input.  ARGS[2] is optional; when present, it names an output
     *  file for processed messages.  Otherwise, output goes to the
     *  standard output. Exits normally if there are no errors in the input;
     *  otherwise with code 1. */
    public static void main(String... args) {
        try {
            new Main(args).process();
            return;
        } catch (EnigmaException excp) {
            System.err.printf("Error: %s%n", excp.getMessage());
        }
        System.exit(1);
    }

    /** Check ARGS and open the necessary files (see comment on main). */
    Main(String[] args) {
        if (args.length < 1 || args.length > 3) {
            throw error("Only 1, 2, or 3 command-line arguments allowed");
        }
        _config = getInput(args[0]);
        if (args.length > 1) {
            _input = getInput(args[1]);
        } else {
            _input = new Scanner(System.in);
        }
        if (args.length > 2) {
            _output = getOutput(args[2]);
        } else {
            _output = System.out;
        }
    }

    /** Return a Scanner reading from the file named NAME. */
    private Scanner getInput(String name) {
        try {
            return new Scanner(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Return a PrintStream writing to the file named NAME. */
    private PrintStream getOutput(String name) {
        try {
            return new PrintStream(new File(name));
        } catch (IOException excp) {
            throw error("could not open %s", name);
        }
    }

    /** Configure an Enigma machine from the contents of configuration
     *  file _config and apply it to the messages in _input, sending the
     *  results to _output. */
    private void process() {
        Machine machine = readConfig();
        String input = "";
        while (_input.hasNextLine()) {
            if (input.isEmpty()) {
                input = _input.nextLine();
            }
            if (input.charAt(0) == '*') {
                setUp(machine, input);
                if (_input.hasNextLine()) {
                    input = _input.nextLine();
                    while (!input.startsWith("*")) {
                        printMessageLine(machine.convert(input));
                        if (_input.hasNextLine()) {
                            input = _input.nextLine();
                        } else {
                            break;
                        }
                    }
                }
            } else {
                throw new EnigmaException("Setting line must "
                        + "begin with an asterisk");
            }
        }
    }


    /** Return an Enigma machine configured from the contents of configuration
     *  file _config. */
    private Machine readConfig() {
        try {
            if (_config.hasNextLine()) {
                _alphabet = new Alphabet(_config.nextLine().trim());
            } else {
                throw new EnigmaException("Wrong configuration");
            }
            int numRotors;
            int pawls;
            if (_config.hasNextInt()) {
                numRotors = _config.nextInt();
            } else {
                throw new EnigmaException("Wrong configuration");
            }
            if (_config.hasNextInt()) {
                pawls = _config.nextInt();
            } else {
                throw new EnigmaException("Wrong configuration");
            }
            ArrayList<Rotor> allRotors = new ArrayList<>();
            while (_config.hasNext()) {
                allRotors.add(readRotor());
            }
            return new Machine(_alphabet, numRotors, pawls, allRotors);
        } catch (NoSuchElementException excp) {
            throw error("configuration file truncated");
        }
    }

    /** Return a rotor, reading its description from _config. */
    private Rotor readRotor() {
        Rotor rotor = null;
        try {
            if (_config.hasNextLine() && _config.hasNext()) {
                String numRotors = _config.next();
                String rotorTypeAndNotches = _config.next();
                char rotorType = rotorTypeAndNotches.charAt(0);
                String rotorNotches = rotorTypeAndNotches.substring(1);
                String cycles = "";
                while (_config.hasNext("[(].*[)]")) {
                    cycles += _config.next();
                }
                for (int i = 1; i < rotorNotches.length(); i++) {
                    if (!_alphabet.contains(rotorNotches.charAt(i))) {
                        throw new EnigmaException("Notch not in alphabet");
                    }
                }
                if (rotorType == 'M') {
                    rotor = new MovingRotor(numRotors,
                            new Permutation(cycles, _alphabet), rotorNotches);
                } else if (rotorType == 'N') {
                    rotor = new FixedRotor(numRotors,
                            new Permutation(cycles, _alphabet));
                } else if (rotorType == 'R') {
                    rotor = new Reflector(numRotors,
                            new Permutation(cycles, _alphabet));
                } else {
                    throw new EnigmaException("Rotor type unknown");
                }
            }

        } catch (NoSuchElementException excp) {
            throw error("bad rotor description");
        }
        return rotor;
    }

    /** Set M according to the specification given on SETTINGS,
     *  which must have the format specified in the assignment. */
    private void setUp(Machine M, String settings) {
        String[] rotorInfo = settings.trim().split(" ");
        String[] rotorNames = new String[M.numRotors()];
        String cycles = "";
        for (int i = 1; i < rotorInfo.length; i++) {
            if (i <= M.numRotors()) {
                rotorNames[i - 1] = rotorInfo[i];
            } else if (i > M.numRotors() + 1) {
                cycles += (rotorInfo[i]);
            }
        }
        for (int i = 0; i < rotorNames.length; i++) {
            for (int j = i + 1; j < rotorNames.length; j++) {
                if (rotorNames[i].equals(rotorNames[j])) {
                    throw new EnigmaException("Repeated rotors");
                }
            }
        }
        M.insertRotors(rotorNames);
        if (!M.rotorSlots()[0].reflecting()) {
            throw new EnigmaException("First rotor must be a reflector");
        }
        for (int i = 0; i < M.numRotors() - M.numPawls(); i++) {
            if (M.rotorSlots()[i].rotates()) {
                throw new EnigmaException("Wrong number of arguments");
            }
        }

        M.setRotors(rotorInfo[M.numRotors() + 1]);
        try {
            if (!rotorInfo[1 + M.numRotors() + 1].contains("(")) {
                String ringSetting = rotorInfo[1 + M.numRotors() + 1];
                if (ringSetting.length() > 0) {
                    M.setRings(ringSetting);
                }
            }
            M.setPlugboard(new Permutation(cycles, _alphabet));
        } catch (ArrayIndexOutOfBoundsException ignored) {
            M.setPlugboard(new Permutation("", _alphabet));
        }
    }

    /** Print MSG in groups of five (except that the last group may
     *  have fewer letters). */
    private void printMessageLine(String msg) {
        StringBuilder output = new StringBuilder();
        int counter = 5;
        for (int i = 0; i < msg.length(); i++) {
            if (_alphabet.contains(msg.charAt(i))) {
                output.append(msg.charAt(i));
                counter -= 1;
                if (counter == 0) {
                    output.append(" ");
                    counter = 5;
                }
            } else {
                throw new EnigmaException("Letter not in alphabet");
            }
        }
        _output.println(output);
    }

    /** Alphabet used in this machine. */
    private Alphabet _alphabet;

    /** Source of input messages. */
    private Scanner _input;

    /** Source of machine configuration. */
    private Scanner _config;

    /** File for encoded/decoded messages. */
    private PrintStream _output;
}
