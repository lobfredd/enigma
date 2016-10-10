package enigma;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Fredrik on 10.10.2016.
 */
public class Rotor {
    private List<Character> wiring;
    private char notchLetter;
    private int notchAlphabetIndex;

    Rotor(String wiring, char notch) {
        this.wiring = wiring.chars().mapToObj(i -> (char) i).collect(Collectors.toList());
        this.notchAlphabetIndex = notch - 0x41;
        this.notchLetter = this.wiring.get(this.notchAlphabetIndex);
    }

    char passThrough(char c){
        return wiring.get(c - 0x41);
    }

    char passThroughInverse(char c){
        return (char) (wiring.indexOf(c) + 0x41);
    }

    boolean isOnNotch(){
        return addNumberToLetter(wiring.get(0), notchAlphabetIndex) == notchLetter;
    }

    void rotate(){
        Collections.rotate(wiring, -1); // rotate rotor IN
        wiring.replaceAll(c -> addNumberToLetter(c, -1)); // rotate rotor OUT
    }

    void setRingStellung(char stellung){
        int offset = stellung - 0x41;
        Collections.rotate(wiring, offset); // rotate rotor IN
        wiring.replaceAll(c -> addNumberToLetter(c, offset)); // rotate rotor OUT
    }

    void setGrundstellung(char stellung){
        int offset = stellung - 0x41;
        Collections.rotate(wiring, -offset); // rotate rotor IN
        wiring.replaceAll(c -> addNumberToLetter(c, -offset)); // rotate rotor OUT
    }

    private char addNumberToLetter(char c, int value){
        return (char) ((((c - 0x41) + 26 + value) % 26) + 0x41);
    }
}
