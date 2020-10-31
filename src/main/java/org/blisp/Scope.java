package org.blisp;

import java.util.HashMap;

public class Scope {
    private HashMap<String, Symbol> symbols = new HashMap<>();
    private int nextRegister;
    
    public Scope(int nextRegister)
    {
        this.nextRegister = nextRegister;
    }
    
    public int getNextRegister()
    {
        return this.nextRegister;
    }
    
    public Symbol pushLocal(String ident)
    {
        assert !symbols.containsKey(ident) : "Shadowing variable in scope";
        Symbol sym = new LocalSymbol(ident, nextRegister);
        symbols.put(ident, sym);
        ++nextRegister;
        
        return sym;
    }
    
    public Symbol pushField(String ident, String className)
    {
        assert !symbols.containsKey(ident) : "Shadowing variable in scope";
        Symbol sym = new FieldSymbol(ident, className);
        symbols.put(ident, sym);
        return sym;
    }
    
    public Symbol get(String ident)
    {
        return symbols.get(ident);
    }
}
