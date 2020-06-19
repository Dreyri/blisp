package org.blisp.nodes;

import java.util.List;

import org.blisp.CodeGenContext;
import org.blisp.SymbolTable;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class If implements Composite {
    public Expression condition;
    public Expression then;
    public Expression else_; // never null in lisp

    public If(Expression cond, Expression then_, Expression else__)
    {
        condition = cond;
        then = then_;
        else_ = else__;
    }
    
    @Override
    public void extractClosures(List<Procedure> closures) {
    	condition.extractClosures(closures);
    	then.extractClosures(closures);
    	else_.extractClosures(closures);
    }

    @Override
    public void codeGen(MethodVisitor currentMethod, List<Procedure> globalFunctions, SymbolTable symbols) {
        Label conditionFalse = new Label();
        Label afterFalse = new Label();

        condition.codeGen(currentMethod, globalFunctions, symbols);
        currentMethod.visitTypeInsn(Opcodes.CHECKCAST, "java/lang/Boolean");
        currentMethod.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Boolean", "booleanValue", "()Z", false);

        currentMethod.visitJumpInsn(Opcodes.IFEQ, conditionFalse);
        
        then.codeGen(currentMethod, globalFunctions, symbols);
        
        currentMethod.visitJumpInsn(Opcodes.GOTO, afterFalse);
        currentMethod.visitLabel(conditionFalse);

        //currentMethod.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        else_.codeGen(currentMethod, globalFunctions, symbols);
        currentMethod.visitLabel(afterFalse);
        //currentMethod.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
    }

    /*
    @Override
    public void genCode(CodeGenContext ctx) throws Exception {
        MethodVisitor mv = ctx.getCurrentMethod();

        condition.genCode(ctx);
        // assume that the result of this is a boolean because I'm tired of typechecking in a quasi dynamic language
        Label condition_false = new Label();
        Label after_false = new Label();
        mv.visitJumpInsn(Opcodes.IFEQ, condition_false); // ifeq comparse the stack to 0, so a false value would evaluate to true

        then.genCode(ctx); // arrive here if the condition is true
        // if we arrived in the if branch then we want to jump to after the else branch
        mv.visitJumpInsn(Opcodes.GOTO, after_false);

        mv.visitLabel(condition_false); // insert the label at this position
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null); // this is required for some unknown reason
        else_.genCode(ctx);
        mv.visitLabel(after_false);
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
    }
    */
}
