package org.blisp;

import org.objectweb.asm.MethodVisitor;

public abstract class AScope implements IScope {
    private IScope parentScope;

    public AScope(IScope parentScope)
    {
        this.parentScope = parentScope;
    }

    @Override
    public IScope getParentScope()
    {
        return parentScope;
    }

    @Override
    public Symbol lookup(String name) throws Exception {
        return parentScope.lookup(name);
    }

    @Override
    public void loadIdentifier(String name, MethodVisitor mv) throws Exception
    {
        getParentScope().loadIdentifier(name, mv);
    }
}
