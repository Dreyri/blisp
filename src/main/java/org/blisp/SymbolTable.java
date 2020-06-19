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
    public int pushSymbol(String sym)
    {
        int register = scopes.getLast().push(sym);
        return register;
    }
    
    public Integer lookup(String localName)
    {
        for(Scope scope : scopes)
        {
            Integer value = scope.get(localName);
            if (value != null)
            {
                return value;
            }
        }

        // not found in local scope
        return null;
    }
}
