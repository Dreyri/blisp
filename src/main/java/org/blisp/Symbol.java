package org.blisp;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

public abstract class Symbol {
    public String identifier;
    
    public Symbol(String identifier)
    {
        this.identifier = identifier;
    }
    
    public abstract void load(MethodVisitor mv);
    public abstract void store(MethodVisitor mv);
}
