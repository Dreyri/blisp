package org.blisp.nodes;

import org.blisp.CodeGenContext;
import org.blisp.LetScope;
import org.blisp.SymbolTable;
import org.blisp.TypeException;
import org.objectweb.asm.MethodVisitor;

import java.util.List;

public class Let implements Composite {
    public Binding[] bindings;
    public Expression expression;
    public LetScope scope;

    public Let(Binding[] bindings, Expression expression)
    {
        this.bindings = bindings;
        this.expression = expression;
    }
    
    @Override
    public void extractClosures(List<Procedure> closures) {
    	for (Binding b : bindings)
    	{
    		b.extractClosures(closures);
    	}
    	
    	expression.extractClosures(closures);
    	
    }

    @Override
    public void codeGen(MethodVisitor currentMethod, List<Procedure> globalFunctions, SymbolTable symbols) {

    }

    /*
    @Override
    public void genCode(CodeGenContext ctx)
    {
        // generate all the bindings
    }
    */
}
