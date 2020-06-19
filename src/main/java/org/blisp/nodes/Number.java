package org.blisp.nodes;

import java.util.List;

import org.blisp.CodeGenContext;
import org.blisp.SymbolTable;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class Number implements Atomic {
    public int value;

    public Number(int num)
    {
        value = num;
    }
    
    @Override
    public void extractClosures(List<Procedure> closures) {
    	// noop does not contain closures
    }

    @Override
    public void codeGen(MethodVisitor currentMethod, List<Procedure> globalFunctions, SymbolTable symbols) {
        currentMethod.visitTypeInsn(Opcodes.NEW, "org/blisp/Number");
        currentMethod.visitInsn(Opcodes.DUP);
        currentMethod.visitIntInsn(Opcodes.SIPUSH, value);
        currentMethod.visitMethodInsn(Opcodes.INVOKESPECIAL, "org/blisp/Number", "<init>", "(I)V", false);
    }

    /*
    @Override
    public void genCode(CodeGenContext ctx)
    {
        MethodVisitor mv = ctx.getCurrentMethod();

        mv.visitTypeInsn(Opcodes.NEW, Number.class.getName()); // push our ths object
        mv.visitInsn(Opcodes.DUP); // don't know why this is needed but asmifier had it
        mv.visitIntInsn(Opcodes.BIPUSH, value); // push our argument to stack
        mv.visitMethodInsn(Opcodes.INVOKESPECIAL, Number.class.getName(), "<init>", "(I)V", false); // invoke the constructor
        // result is on the stack now, I hope
    }
    */
}
