package org.blisp;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class FieldSymbol extends Symbol {
    String className;
    
    public FieldSymbol(String fieldName, String className)
    {
        super(fieldName);
        this.className = className;
    }
    
    @Override
    public void load(MethodVisitor mv) {
        mv.visitFieldInsn(Opcodes.GETFIELD, className, super.identifier, "Ljava/lang/Object;");
    }

    @Override
    public void store(MethodVisitor mv) {
        mv.visitFieldInsn(Opcodes.PUTFIELD, className, super.identifier, "Ljava/lang/Object;");
    }
}
