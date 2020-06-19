package org.blisp.nodes;

import java.util.List;

public class Binding implements Node {
    public String name;
    public Expression expr;

    public Binding(String name, Expression e)
    {
        this.name = name;
        this.expr = e;
    }
    
    public void extractClosures(List<Procedure> closures) {
    	expr.extractClosures(closures);
    }
}
