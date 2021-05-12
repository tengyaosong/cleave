package edu.gatech.seclass.cleave;

public class CleaveException extends Exception {
    CleaveException() {
        super("Toto, I've a feeling we're not in Kansas anymore");
    }

    CleaveException(String str) {
        super(str);
    }
}
