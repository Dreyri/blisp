package org.blisp;

import java.util.HashMap;

public class Scope {
    private HashMap<String, Integer> symbols = new HashMap<>();
    private int nextRegister;
    
    public Scope(int nextRegister)
    {
        this.nextRegister = nextRegister;
    }
    
    public int getNextRegister()
    {
        return this.nextRegister;
    }
    
    public int push(String sym)
    {
        assert !symbols.containsKey(sym) : "Shadowing local variable";
        symbols.put(sym, nextRegister);
        int register = nextRegister;
        ++nextRegister;
        return register;
    }
    
    public Integer get(String sym)
    {
        return symbols.get(sym);
    }
}
