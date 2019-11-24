package org.blisp;

public interface Environment {
    void put(Symbol sym) throws RedefinitionException;
    Symbol get(String symName) throws UndefinedSymbolException;
}
