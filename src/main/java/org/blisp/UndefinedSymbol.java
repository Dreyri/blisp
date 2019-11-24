package org.blisp;

public class UndefinedSymbol extends Symbol {

    public UndefinedSymbol(String name) {
        super(name);
    }

    public String toString() {
        return "Undefined";
    }
}
