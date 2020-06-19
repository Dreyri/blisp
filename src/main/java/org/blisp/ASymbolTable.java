package org.blisp;

import org.blisp.nodes.Expression;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.Arrays;
import java.util.List;

public abstract class ASymbolTable implements ISymbolTable {

    // default implementation, doesn't check whether the current type can even handle the args
    /*
    @Override
    public void emitInvoke(String name, Expression[] exprs, List<ClassWriter> classWriters, ClassWriter currentClass, MethodVisitor currentMethod)
    {
        // load this (hidden arg)
        emitLoad(name, classWriters, currentClass, currentMethod);

        StringBuilder descriptionBuilder = new StringBuilder(32);
        descriptionBuilder.append('(');
        for (Expression expr : exprs)
        {
            // load each argument
            expr.genCode(new CodeGenContext(classWriters, currentClass, currentMethod, this));
            descriptionBuilder.append('I');
        }
        descriptionBuilder.append(")I");

        String className = this.getScopeName() + name;

        currentMethod.visitMethodInsn(Opcodes.INVOKEVIRTUAL, className, "invoke", descriptionBuilder.toString(), false);
    }

     */
}
