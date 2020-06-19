package org.blisp.nodes;

import org.blisp.CodeGenContext;
import org.blisp.IFn;
import org.blisp.SymbolTable;
import org.blisp.TypeException;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.List;

// a function call basically
public class Application implements Composite {
    // either var or proc in the end for operator
    public Expression operator;
    public Expression[] operands;

    public Application(Expression operator, Expression[] operands)
    {
        this.operator = operator;
        this.operands = operands;
    }
    
    @Override
    public void extractClosures(List<Procedure> closures) {
    	operator.extractClosures(closures);
    	
    	for (Expression operand : operands)
    	{
    		operand.extractClosures(closures);
    	}
    }

    @Override
    public void codeGen(MethodVisitor currentMethod, List<Procedure> globalFunctions, SymbolTable symbols) {
        if (this.operator instanceof Identifier)
        {
            Identifier fn = (Identifier)this.operator;
            
            switch (fn.name)
            {
                case "println":
                    assert operands.length == 1 : "Print only takes one argument";
                    currentMethod.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
                    operands[0].codeGen(currentMethod, globalFunctions, symbols);
                    // convert Object to String for println
                    currentMethod.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/Object", "toString", "()Ljava/lang/String;", false);
                    currentMethod.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
                    // make sure something is on the stack
                    currentMethod.visitInsn(Opcodes.ACONST_NULL);
                    break;
                case "+":
                {
                    StringBuilder description = new StringBuilder();
                    description.append('(');
                    for (Expression op : operands)
                    {
                        description.append("Ljava/lang/Object;");
                        op.codeGen(currentMethod, globalFunctions, symbols);
                    }
                    description.append(")Ljava/lang/Object;");
                    currentMethod.visitMethodInsn(Opcodes.INVOKESTATIC, "org/blisp/Number", "add", description.toString(), false);
                }
                break;
                case "=":
                    assert operands.length == 2;
                    operands[0].codeGen(currentMethod, globalFunctions, symbols);
                    operands[1].codeGen(currentMethod, globalFunctions, symbols);
                    currentMethod.visitMethodInsn(Opcodes.INVOKESTATIC, "org/blisp/Number", "eq", "(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", false);
                    break;
                case "<": {
                    StringBuilder description = new StringBuilder();
                    description.append('(');
                    for (Expression op : operands) {
                        description.append("Ljava/lang/Object;");
                        op.codeGen(currentMethod, globalFunctions, symbols);
                    }
                    description.append(")Ljava/lang/Object;");
                    currentMethod.visitMethodInsn(Opcodes.INVOKESTATIC, "org/blisp/Number", "lessThan", description.toString(), false);
                }
                    break;
                case "<=": {
                    StringBuilder description = new StringBuilder();
                    description.append('(');
                    for (Expression op : operands) {
                        description.append("Ljava/lang/Object;");
                        op.codeGen(currentMethod, globalFunctions, symbols);
                    }
                    description.append(")Ljava/lang/Object;");
                    currentMethod.visitMethodInsn(Opcodes.INVOKESTATIC, "org/blisp/Number", "lessThanEq", description.toString(), false);
                    break;
                }
                case ">": {
                    StringBuilder description = new StringBuilder();
                    description.append('(');
                    for (Expression op : operands) {
                        description.append("Ljava/lang/Object;");
                        op.codeGen(currentMethod, globalFunctions, symbols);
                    }
                    description.append(")Ljava/lang/Object;");
                    currentMethod.visitMethodInsn(Opcodes.INVOKESTATIC, "org/blisp/Number", "greaterThan", description.toString(), false);
                    break;
                }
                case ">=": {
                    StringBuilder description = new StringBuilder();
                    description.append('(');
                    for (Expression op : operands) {
                        description.append("Ljava/lang/Object;");
                        op.codeGen(currentMethod, globalFunctions, symbols);
                    }
                    description.append(")Ljava/lang/Object;");
                    currentMethod.visitMethodInsn(Opcodes.INVOKESTATIC, "org/blisp/Number", "greaterThanEq", description.toString(), false);
                    break;
                }
                default:
                    Procedure calledProc = null;
                    
                    Integer hofRegister = symbols.lookup(fn.name);
                    if (hofRegister != null)
                    {
                        // push hof to the stack
                        currentMethod.visitVarInsn(Opcodes.ALOAD, hofRegister.intValue());
                        currentMethod.visitTypeInsn(Opcodes.CHECKCAST, "org/blisp/AFn");

                        StringBuilder description = new StringBuilder();
                        description.append('(');
                        for (Expression op : operands)
                        {
                            description.append("Ljava/lang/Object;");
                            op.codeGen(currentMethod, globalFunctions, symbols);
                        }
                        description.append(")Ljava/lang/Object;");
                        currentMethod.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "org/blisp/AFn", "invoke", description.toString(), false);
                    }
                    else {


                        for (Procedure proc : globalFunctions) {
                            if (proc.assignedName.equals(fn.name)) {
                                calledProc = proc;
                                break;
                            }
                        }

                        assert calledProc != null : "Could not find function";

                        // this means we can use staticInvoke
                        if (calledProc.captures.length == 0) {
                            for (Expression operand : operands) {
                                operand.codeGen(currentMethod, globalFunctions, symbols);
                            }

                            StringBuilder descriptorBuilder = new StringBuilder();
                            descriptorBuilder.append('(');
                            for (int i = 0; i < operands.length; ++i) {
                                descriptorBuilder.append("Ljava/lang/Object;");
                            }
                            descriptorBuilder.append(")Ljava/lang/Object;");

                            currentMethod.visitMethodInsn(Opcodes.INVOKESTATIC, calledProc.path, "invokeStatic", descriptorBuilder.toString(), false);
                        } else {
                            // this lambda has captures so we need to first load it to the stack before we can invoke it
                            operator.codeGen(currentMethod, globalFunctions, symbols);
                            currentMethod.visitTypeInsn(Opcodes.CHECKCAST, "org/blisp/AFn");

                            for (Expression operand : operands) {
                                operand.codeGen(currentMethod, globalFunctions, symbols);
                            }

                            StringBuilder descriptorBuilder = new StringBuilder();
                            descriptorBuilder.append('(');
                            for (int i = 0; i < operands.length; ++i) {
                                descriptorBuilder.append("Ljava/lang/Object;");
                            }
                            descriptorBuilder.append(")Ljava/lang/Object;");

                            currentMethod.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "org/blisp/AFn", "invoke", descriptorBuilder.toString(), false);
                        }
                    }
                    // a function defined somewhere here
            }
        }
    }

    /*
    @Override
    public void genCode(CodeGenContext ctx)
    {
    }
    */
}
