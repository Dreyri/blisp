package org.blisp.nodes;

import java.util.List;

import com.sun.org.apache.bcel.internal.generic.ILOAD;
import org.blisp.CodeGenContext;
import org.blisp.IScope;
import org.blisp.Symbol;
import org.blisp.SymbolTable;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class Identifier implements Atomic {
    public String name;
    public IScope scope = null;

    public Identifier(String id)
    {
        name = id;
    }

    
    @Override
    public void extractClosures(List<Procedure> closures) {
    	// does not contain any closures
    }

    @Override
    public void codeGen(MethodVisitor currentMethod, List<Procedure> globalFunctions, SymbolTable symbols) {
        Symbol sym = symbols.lookup(name);
        assert sym != null : "Identifier not found";
        
        sym.load(currentMethod);
    }
}
