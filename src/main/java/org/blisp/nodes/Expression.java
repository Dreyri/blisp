package org.blisp.nodes;

import org.blisp.SymbolTable;
import org.objectweb.asm.MethodVisitor;

import java.util.List;

public interface Expression extends Node {    
    void extractClosures(List<Procedure> closures);
    
    void codeGen(MethodVisitor currentMethod, List<Procedure> globalFunctions, SymbolTable symbols);
}
