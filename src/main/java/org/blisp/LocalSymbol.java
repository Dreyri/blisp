package org.blisp;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class LocalSymbol extends Symbol{
    int register_;

    public LocalSymbol(String identifier, int register_)
    {
        super(identifier);
        this.register_ = register_;
    }
    
    @Override
    public void load(MethodVisitor mv) {
        mv.visitVarInsn(Opcodes.ALOAD, this.register_);
    }

    @Override
    public void store(MethodVisitor mv) {
        mv.visitVarInsn(Opcodes.ASTORE, this.register_);
    }
}
