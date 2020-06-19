package org.blisp;

import org.objectweb.asm.MethodVisitor;

import java.util.HashMap;
import java.util.Map;

// symbol table for the current .bl we're in
// this can only have methods defined
public abstract class GlobalSymbolTable implements ISymbolTable {

    Map<String, MethodInterface> methods = new HashMap<>();

    public GlobalSymbolTable()
    {}

    //@Override
    public void emitLoad(String name, MethodVisitor mv)
    {

    }

    //@Override
    public void emitInvoke(String name, int numArgs, MethodVisitor mv)
    {

    }
}
