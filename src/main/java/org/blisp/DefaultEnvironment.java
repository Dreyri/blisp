package org.blisp;

import java.util.HashMap;

public class DefaultEnvironment implements Environment {
    HashMap<String, Symbol> symbols = new HashMap<>();

    public DefaultEnvironment() {
    }

    @Override
    public void put(Symbol sym) throws RedefinitionException {
        if (symbols.containsKey(sym.getName()))
        {
            throw new RedefinitionException();
        }
        // unimplemented
    }

    @Override
    public Symbol get(String symName) throws UndefinedSymbolException {
        throw new UndefinedSymbolException();
    }
}
