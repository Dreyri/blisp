package org.blisp.nodes;

import org.blisp.ByteCode;
import org.blisp.ClassScope;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Class {
    public Define[] definitions;

    public ClassScope scope = null;

    public Class(Define[] definitions)
    {
        this.definitions = definitions;
    }
    
    public ByteCode codeGen(String className)
    {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);

        // chosen 1_6 because stack maps are optional, see visitFrame
        cw.visit(Opcodes.V1_6, Opcodes.ACC_PUBLIC + Opcodes.ACC_SUPER, className, null, "java/lang/Object", new String[]{"org/blisp/Entry"});

        {
            MethodVisitor constructor = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
            constructor.visitCode();
            constructor.visitVarInsn(Opcodes.ALOAD, 0); // load this to stack
            constructor.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false);
            constructor.visitInsn(Opcodes.RETURN);
            constructor.visitMaxs(1, 1);
            constructor.visitEnd();
        }

        ArrayList<Procedure> anonymousLambdas = new ArrayList<>();
        
        for (Define def : definitions)
        {
            def.extractClosures(anonymousLambdas);
        }
        
        ArrayList<Define> definedLambdas = new ArrayList<>();

        // give all lambdas a name before continuing
        for (int i = 0; i < anonymousLambdas.size(); ++i)
        {
            String lambdaName = String.format("__lambda_%d", i);
            definedLambdas.add(new Define(new Identifier(lambdaName), anonymousLambdas.get(i)));
        }
        
        ArrayList<Procedure> globalFunctions = new ArrayList<>();
        
        for (Define def : definedLambdas)
        {
            // the path is the fully qualified type name e.g. java/lang/Object, fun$__lambda_1
            def.proc.path = String.format("%s$%s", className, def.proc.assignedName);
            globalFunctions.add(def.proc);
        }
        
        for (Define def : definitions)
        {
            def.proc.path = String.format("%s$%s", className, def.proc.assignedName);
            globalFunctions.add(def.proc);
        }

        HashMap<String, ClassWriter> functionClasses = new HashMap<>();

        for (Define def : definedLambdas)
        {
            String innerClassName = String.format("%s$%s", className, def.name.name);
            //cw.visitInnerClass(innerClassName, className, def.name.name, Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC);
            ClassWriter functionCw = def.genCode(cw, innerClassName, className, globalFunctions);
            if (functionCw != null)
            {
                functionClasses.put(innerClassName, functionCw);
            }
        }

        for (Define def : definitions) {
            assert def.proc.captures.length == 0 : "Global procedures may not contain captures";
            String innerClassName = String.format("%s$%s", className, def.name.name);
            ClassWriter functionCw = def.genCode(cw, innerClassName, className, globalFunctions);
            if (functionCw != null)
            {
                functionClasses.put(innerClassName, functionCw);
            }
        }

        cw.visitEnd();

        return new ByteCode(className, cw, functionClasses);
    }
}
