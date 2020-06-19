package org.blisp;

public abstract class DefineScope extends AScope {
    private boolean globalScope;

    private Symbol symbol;

    public DefineScope(IScope parentScope, boolean globalScope, Symbol symbol) {
        super(parentScope);
        this.globalScope = globalScope;
        this.symbol = symbol;
    }
}
