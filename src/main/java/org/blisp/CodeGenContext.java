package org.blisp;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

import java.util.List;

public class CodeGenContext {
    private List<ClassWriter> classWriters;
    private ClassWriter currentClass;
    private MethodVisitor currentMethod;
    private ISymbolTable symbolTable;

    public CodeGenContext(List<ClassWriter> classWriters, ClassWriter currentClass, MethodVisitor currentMethod, ISymbolTable symbolTable)
    {
        this.classWriters = classWriters;
        this.currentClass = currentClass;
        this.currentMethod = currentMethod;
        this.symbolTable = symbolTable;
    }

    public List<ClassWriter> getClassWriters()
    {
        return classWriters;
    }

    public ClassWriter getCurrentClass()
    {
        return currentClass;
    }

    public MethodVisitor getCurrentMethod()
    {
        return currentMethod;
    }

    public ISymbolTable getSymbolTable()
    {
        return symbolTable;
    }
}
