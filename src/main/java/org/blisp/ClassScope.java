package org.blisp;

import org.blisp.nodes.Procedure;

import java.util.HashMap;
import java.util.Map;

public abstract class ClassScope implements IScope {

    private Map<String, Symbol> symbols = new HashMap<>();

    public ClassScope()
    {
    }

    /*
    public void pushSymbol(String name, SymbolType type)
    {
        //symbols.put(name, new Symbol(name, true, true, ))
    }
    */

    @Override
    public IScope getParentScope() {
        return null;
    }
}
