package org.blisp;

public class ArityException extends IllegalArgumentException {
    public ArityException(int passedArgs, String fnName)
    {
        super("Wrong number of args (" + passedArgs + ") passed to " + fnName);
    }
}
