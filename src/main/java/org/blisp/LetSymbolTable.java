package org.blisp;

import org.blisp.nodes.Binding;
import org.blisp.nodes.Expression;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LetSymbolTable implements ISymbolTable {
    private ISymbolTable parent;
    private int nextRegister;

    private Map<String, SymbolEntry> symbols = new HashMap<>();

    private static class SymbolEntry
    {
        private Expression expr;
        private int register;

        public SymbolEntry(Expression expr, int register)
        {
            this.expr = expr;
            this.register = register;
        }
    }

    public LetSymbolTable(Binding[] bindings, ISymbolTable parent)
    {
        this.parent = parent;
        //this.nextRegister = parent.nextRegister();
    }

    /*
    @Override
    public String getScopeName() {
        return parent.getScopeName() + "let$";
    }

    public void emitStore(String name, Expression expr, List<ClassWriter> classWriters, ClassWriter currentClass, MethodVisitor currentMethod)
    {

    }

    @Override
    public void emitLoad(String name, List<ClassWriter> classWriters, ClassWriter currentClass, MethodVisitor currentMethod) {
        if (symbols.containsKey(name))
        {

        }
    }

    @Override
    public void emitInvoke(String name, Expression[] args, List<ClassWriter> classWriters, ClassWriter currentClass, MethodVisitor mv) {

    }

     */
}
