package org.blisp;

public class UndefinedSymbolException extends Exception {
    public UndefinedSymbolException(String name)
    {
        super("Could not find symbol \"" + name + "\"");
    }
}
