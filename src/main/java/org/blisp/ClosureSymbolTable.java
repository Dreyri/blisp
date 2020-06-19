package org.blisp;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.HashMap;
import java.util.Map;

public class ClosureSymbolTable implements ISymbolTable {
    // we don't have access to anything but globals
    private ISymbolTable globals;
    private MethodInterface thisMethod;

    Map<String, FieldSymbol> fields = new HashMap<>();

    private static class FieldSymbol
    {
        public final String name;
        final int index;
        final int args;

        public FieldSymbol(String name, int index)
        {
            this(name, index, -1);
        }

        public FieldSymbol(String name, int index, int args)
        {
            this.name = name;
            this.index = index;
            this.args = args;
        }
    }

    public ClosureSymbolTable(ISymbolTable table, MethodInterface thisMethod)
    {
        this.globals = table;
        this.thisMethod = thisMethod;
    }

    /*
    //@Override
    public String getScopeName()
    {

    }

     */

    /*
    //@Override
    public void emitLoad(String name, MethodVisitor mv) {
        if (name.equals(thisMethod.getName()))
        {
            // load this
            mv.visitVarInsn(Opcodes.ALOAD, 0);
        }
        else if (fields.containsKey(name))
        {
            FieldSymbol sym = fields.get(name);

            mv.visitVarInsn(Opcodes.ALOAD, 0); // load this
            mv.visitFieldInsn(Opcodes.GETFIELD, className, sym.name, "I"); // retrieve field from this
        }
        else
        {
            globals.emitLoad(name, mv);
        }
    }

    @Override
    public void emitInvoke(String name, int numArgs, MethodVisitor mv) {
        StringBuilder descriptorBuilder = new StringBuilder(32);
        descriptorBuilder.append('(');

        for (int i = 0; i < numArgs; ++i)
        {
            descriptorBuilder.append('I');
        }

        descriptorBuilder.append(")I");

        if (name.equals(thisMethod.getName())) {
            if (thisMethod.getArgumentCount() != numArgs)
            {
                throw new ArityException(numArgs, thisMethod.getArgumentCount(), thisMethod.getName());
            }
            // load this
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            // and invoke again
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, thisMethod.getClassName(), "invoke", descriptorBuilder.toString(), false);
        }
        else if (fields.containsKey(name))
        {
            FieldSymbol sym = fields.get(name);
        }
    }

     */
}
