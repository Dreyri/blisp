package org.blisp;

public class RedefinitionException extends Exception {
    public RedefinitionException(String name)
    {
        super("Redefinition of :\"" + name + "\"");
    }
}
