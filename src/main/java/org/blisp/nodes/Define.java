package org.blisp.nodes;

import java.util.List;

import org.blisp.SymbolTable;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

public class Define {
    public Identifier name;
    public Procedure proc;

    public Define(Identifier id, Procedure proc)
    {
        this.name = id;
        this.proc = proc;
        
        proc.assignedName = this.name.name;
    }
    
    public ClassWriter genCode(ClassWriter parentCw, String className, String moduleName, List<Procedure> globalFunctions)
    {
        SymbolTable symbolTable = new SymbolTable();

        if (name.name.equals("main"))
        {
            assert proc.captures.length == 0 : "main function cannot capture anything";
            assert proc.arguments.length == 0 : "main function does not take any arguments";

            // we use the parentCw here because we're generating the static main function, this function does not need its own function object
            MethodVisitor entry = parentCw.visitMethod(Opcodes.ACC_PUBLIC, "bl_main", "()V", null, null);
            entry.visitCode();

            symbolTable.pushScope();
            // no need to push String[] args to the stack since it's not passed to this function
            proc.expression.codeGen(entry, globalFunctions, symbolTable);
            // this function isn't supposed to return anything
            entry.visitInsn(Opcodes.POP);
            entry.visitInsn(Opcodes.RETURN);

            /*
            entry.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            entry.visitLdcInsn(name.name + " invoked");
            entry.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
            entry.visitInsn(Opcodes.ACONST_NULL);
            entry.visitInsn(Opcodes.POP);
            entry.visitInsn(Opcodes.RETURN);
            entry.visitMaxs(0, 0);
             */

            symbolTable.popScope();
            entry.visitMaxs(0, 0);
            entry.visitEnd();
            
            MethodVisitor main = parentCw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC + Opcodes.ACC_VARARGS, "main", "([Ljava/lang/String;)V", null, null);
            main.visitCode();
            symbolTable.pushScope();
            symbolTable.pushSymbol("__args");
            main.visitTypeInsn(Opcodes.NEW, moduleName);
            main.visitInsn(Opcodes.DUP);
            main.visitMethodInsn(Opcodes.INVOKESPECIAL, moduleName, "<init>", "()V", false);
            main.visitVarInsn(Opcodes.ASTORE, 1);
            main.visitVarInsn(Opcodes.ALOAD, 1);
            main.visitMethodInsn(Opcodes.INVOKEVIRTUAL, moduleName, "bl_main", "()V", false);
            main.visitInsn(Opcodes.RETURN);
            main.visitMaxs(2, 2);
            symbolTable.popScope();
            main.visitEnd();
            
            return null;
        }
        else {
            //parentCw.visitInnerClass(proc.path, className, proc.assignedName, Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC);
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            cw.visit(Opcodes.V1_6, Opcodes.ACC_PUBLIC + Opcodes.ACC_SUPER, className, null, "org/blisp/AFn", null);

            {
                assert proc.captures.length == 0 : "Doesn't handle captures yet, they will be implemented as simple fields";
                MethodVisitor constructor = cw.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
                constructor.visitCode();
                constructor.visitVarInsn(Opcodes.ALOAD, 0);
                constructor.visitMethodInsn(Opcodes.INVOKESPECIAL, "org/blisp/AFn", "<init>", "()V", false);
                constructor.visitInsn(Opcodes.RETURN);
                constructor.visitMaxs(0, 0);
                constructor.visitEnd();
            }

            // generate invokeStatic
            if (this.proc.captures.length == 0) {
                StringBuilder signatureBuilder = new StringBuilder(64);
                signatureBuilder.append('(');

                // everything takes java.lang.Object
                for (Identifier arg : this.proc.arguments) {
                    signatureBuilder.append("Ljava/lang/Object;");
                }

                signatureBuilder.append(")Ljava/lang/Object;");

                String signature = signatureBuilder.toString();

                MethodVisitor invokeStatic = cw.visitMethod(Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC, "invokeStatic", signature, null, null);

                invokeStatic.visitCode();
                symbolTable.pushScope();
                // push all locals into symboltable
                for (Identifier arg : proc.arguments)
                {
                    symbolTable.pushSymbol(arg.name);
                }

                Procedure self = new Procedure(proc.captures, proc.arguments, proc.expression);
                // bring self in scope for functions
                self.assignedName = "self";
                self.path = proc.path;
                globalFunctions.add(self);

                proc.expression.codeGen(invokeStatic, globalFunctions, symbolTable);
                invokeStatic.visitInsn(Opcodes.ARETURN);
                //this.proc.expression.codeGen(invokeStatic, globalFunctions);
                globalFunctions.remove(self);
                symbolTable.popScope();
                invokeStatic.visitMaxs(0, 0);
                invokeStatic.visitEnd();
            }

            // generate the non static invoke method
            if (this.proc.captures.length == 0)
            {
                // invoke through invokeStatic
                StringBuilder description = new StringBuilder();
                description.append('(');
                for (int i = 0; i < this.proc.arguments.length; ++i)
                {
                    description.append("Ljava/lang/Object;");
                }
                description.append(")Ljava/lang/Object;");
                MethodVisitor invoke = cw.visitMethod(Opcodes.ACC_PUBLIC, "invoke", description.toString(), null, null);
                invoke.visitCode();

                symbolTable.pushScope();
                symbolTable.pushSymbol("self");
                for (Identifier arg : this.proc.arguments)
                {
                    symbolTable.pushSymbol(arg.name);
                }
                
                this.proc.expression.codeGen(invoke, globalFunctions, symbolTable);

                /*
                for (int i = 0; i < this.proc.arguments.length; ++i)
                {
                    // this is aload0
                    invoke.visitVarInsn(Opcodes.ALOAD, i+1);
                }
                invoke.visitMethodInsn(Opcodes.INVOKESTATIC, this.proc.path, "invokeStatic", description.toString(), false);
                 */
                invoke.visitInsn(Opcodes.ARETURN);
                invoke.visitMaxs(0, 0);
                invoke.visitEnd();
            }
            else {
                assert false : "Not implemented";
            }

            cw.visitEnd();
            return cw;
        }
    }
    
    public void extractClosures(List<Procedure> closures)
    {
        // skip the lambda bound to this define as we're at global scope
    	proc.expression.extractClosures(closures);
    }

    /*
    @Override
    public void genCode(CodeGenContext ctx) {
    }
    */
}
