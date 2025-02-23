package capers;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;

/** Represents a dog that can be serialized.
 * @author Sean Dooher
*/
public class Dog implements Serializable { // FIXME

    /** Folder that dogs live in. */
    static final File DOG_FOLDER = new File(Main.CAPERS_FOLDER, "dogs");

    /**
     * Creates a dog object with the specified parameters.
     * @param name Name of dog
     * @param breed Breed of dog
     * @param age Age of dog
     */
    public Dog(String name, String breed, int age) {
        _age = age;
        _breed = breed;
        _name = name;
    }

    /**
     * Reads in and deserializes a dog from a file with name NAME in DOG_FOLDER.
     *
     * @param name Name of dog to load
     * @return Dog read from file
     */
    public static Dog fromFile(String name) {
        File dogFile = Utils.join(DOG_FOLDER, name + ".txt");
        Dog dog = Utils.readObject(dogFile, Dog.class);
        return dog;
    }

    /**
     * Increases a dog's age and celebrates!
     */
    public void haveBirthday() {
        _age += 1;
//        System.out.println(toString());
        saveDog();
        System.out.println("Happy birthday! Woof! Woof!");
    }

    /**
     * Saves a dog to a file for future use.
     */
    public void saveDog() {
        File dogFile = Utils.join(DOG_FOLDER, _name + ".txt");
        try {
            dogFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Utils.writeObject(dogFile, this);
        System.out.println(this.toString());
    }

    @Override
    public String toString() {
        return String.format(
            "Woof! My name is %s and I am a %s! I am %d years old! Woof!",
            _name, _breed, _age);
    }

    /** Age of dog. */
    private int _age;
    /** Breed of dog. */
    private String _breed;
    /** Name of dog. */
    private String _name;
}
