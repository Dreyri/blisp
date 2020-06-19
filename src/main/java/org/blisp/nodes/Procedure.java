package org.blisp.nodes;

import org.blisp.*;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import static org.objectweb.asm.Opcodes.*;

import java.util.List;

// important to note that procedure is an expression!!!!!
// this means that this actually instantiates a new instance of this type
// and the real definition of the type it instantiates must be lifted from this literal to the global scope
public class Procedure implements Composite {
    public Identifier[] captures;
    public Identifier[] arguments;
    public Expression expression;
    public ProcedureScope scope = null;
    
    public String assignedName = null;
    public String path = null;

    public Procedure(Identifier[] captures, Identifier[] args, Expression expr)
    {
        this.captures = captures;
        this.arguments = args;
        this.expression = expr;
    }

    public void extractClosures(List<Procedure> closures)
    {
        // this method does not get called from within a define call
        closures.add(this);
    	expression.extractClosures(closures);
    }

    @Override
    public void codeGen(MethodVisitor currentMethod, List<Procedure> globalFunctions, SymbolTable symbols) {
        currentMethod.visitTypeInsn(Opcodes.NEW, path);
        currentMethod.visitInsn(DUP);

        StringBuilder description = new StringBuilder();
        description.append('(');
        for (Expression capture : captures)
        {
            description.append("Ljava/lang/Object;");
            capture.codeGen(currentMethod, globalFunctions, symbols);
        }
        description.append(")V");

        currentMethod.visitMethodInsn(INVOKESPECIAL, path, "<init>", description.toString(), false);
        // function object is now on the stack
    }
    
    /*
    @Override
    public void genCode(CodeGenContext ctx)
    {
    	ClassWriter cw = ctx.getCurrentClass();
    	cw.visitInnerClass(name, outerName, innerName, access);
    }
    */

    /*
    public void genCode(String className, SymbolTable symbols)
    {
        //ClosureSymbolTable closureTable = new ClosureSymbolTable(symbols);

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        cw.visit(V1_8, ACC_PUBLIC, className, null, "org.blisp.AFn", null);

        for (Identifier capture : captures) {
            cw.visitField(ACC_PUBLIC, capture.name, "I", null, null);
        }

        {
            // generate constructor
            StringBuilder argsBuilder = new StringBuilder(32);

            for (Identifier capture : captures)
            {
                argsBuilder.append("I");
            }


            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "<init>", "(" + argsBuilder.toString() + ")V", null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0); // push this to the stack
            mv.visitMethodInsn(INVOKESPECIAL, Type.getInternalName(AFn.class), "<init>", "()V", false);

            for (int i = 0; i < captures.length; ++i)
            {
                mv.visitVarInsn(ALOAD, 0);
                Identifier currVar = captures[i];
                // load our integers
                mv.visitVarInsn(ILOAD, i + 1);
                mv.visitFieldInsn(PUTFIELD, className, currVar.name, "I");
            }
            mv.visitInsn(RETURN);
            mv.visitMaxs(0, 0); // these are ignored and get computed
            mv.visitEnd();
        }

        {
            // generate invoke

            StringBuilder argsBuilder = new StringBuilder(32);
            for (Identifier arg : arguments)
            {
                argsBuilder.append("I");
            }

            MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "invoke", "(" + argsBuilder.toString() + ")V", null, null);
            mv.visitCode();

            // do expression
        }


    }
     */
}
