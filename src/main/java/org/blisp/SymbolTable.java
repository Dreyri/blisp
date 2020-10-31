package org.blisp;

import org.blisp.nodes.Class;
import org.blisp.nodes.Define;

import java.util.*;

public class SymbolTable {
    Deque<Scope> scopes = new ArrayDeque<>();
    
    public SymbolTable()
    {}
    
    public void pushScope()
    {
        int register = 0;
        if (!scopes.isEmpty())
        {
            register = scopes.getLast().getNextRegister();
        }
        scopes.push(new Scope(register));
    }
    
    public void popScope()
    {
        scopes.pop();
    }

    /**
     *
     * @return gives back the register assigned to the current symbol
     */
    public Symbol pushLocal(String ident)
    {
        return scopes.getLast().pushLocal(ident);
    }
    
    public Symbol pushField(String fieldName, String className)
    {
        return scopes.getLast().pushField(fieldName, className);
    }
    
    public Symbol lookup(String ident)
    {
        for (Scope scope : scopes)
        {
            Symbol sym = scope.get(ident);
            if (sym != null)
            {
                return sym;
            }
        }
        
        return null;
    }
}
