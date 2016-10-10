package enigma;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Fredrik on 08.10.2016.
 */
public class Enigma {
    private static final String[] ROTORS = {
            "EKMFLGDQVZNTOWYHXUSPAIBRCJ", // Standard rotor I
            "AJDKSIRUXBLHWTMCQGZNPYFVOE", // Standard rotor II
            "BDFHJLCPRTXVZNYEIWGAKMUSQO" // Standard rotor III
    };
    private static final char[] ROTOR_NOTCHES = {'Q', 'E', 'V'}; // rotor I, II, III's notches
    private static final String REFLECTOR_B = "YRUHQSLDPXNGOKMIEBFZCWVJAT"; // Standard reflector B

    private Rotor rot1;
    private Rotor rot2;
    private Rotor rot3;
    private List<Character> reflector;

    public Enigma(String rotorOrder, String ringStellung, String grundStellung){
        int[] rotOrder = Arrays.stream(rotorOrder.split("")).mapToInt(i -> Integer.parseInt(i) - 1).toArray();

        rot1 = new Rotor(ROTORS[rotOrder[0]], ROTOR_NOTCHES[rotOrder[0]]);
        rot2 = new Rotor(ROTORS[rotOrder[1]], ROTOR_NOTCHES[rotOrder[1]]);
        rot3 = new Rotor(ROTORS[rotOrder[2]], ROTOR_NOTCHES[rotOrder[2]]);
        reflector = REFLECTOR_B.chars().mapToObj(i -> (char) i).collect(Collectors.toList());

        rot1.setRingStellung(ringStellung.charAt(0));
        rot2.setRingStellung(ringStellung.charAt(1));
        rot3.setRingStellung(ringStellung.charAt(2));

        rot1.setGrundstellung(grundStellung.charAt(0));
        rot2.setGrundstellung(grundStellung.charAt(1));
        rot3.setGrundstellung(grundStellung.charAt(2));
    }

    public String encrypt(String plainText){
        StringBuilder cipher = new StringBuilder();

        for (char c : plainText.toUpperCase().toCharArray()) {
            cipher.append(encrypt(c));
        }

        return cipher.toString();
    }

    public String decrypt(String cipherText){
        return encrypt(cipherText);
    }

    private char encrypt(char c){
        rotateRotors();
        c = rot3.passThrough(c);
        c = rot2.passThrough(c);
        c = rot1.passThrough(c);

        c = reflector.get(c - 0x41);

        c = rot1.passThroughInverse(c);
        c = rot2.passThroughInverse(c);
        return rot3.passThroughInverse(c);
    }

    private void rotateRotors(){
        if (rot2.isOnNotch()) {
            rot1.rotate();
            rot2.rotate();
        }
        if (rot3.isOnNotch()){
            rot2.rotate();
        }
        rot3.rotate();
    }
}