package org.blisp;

import org.objectweb.asm.ClassWriter;

import java.util.HashMap;

public class ByteCode {
    public String moduleName;
    public ClassWriter moduleClass;
    
    public HashMap<String, ClassWriter> functionClasses;
    
    public ByteCode(String moduleName, ClassWriter moduleClass, HashMap<String, ClassWriter> functionClasses)
    {
        this.moduleName = moduleName;
        this.moduleClass = moduleClass;
        this.functionClasses = functionClasses;
    }
}
