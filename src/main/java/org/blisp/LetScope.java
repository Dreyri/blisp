package org.blisp;

import org.blisp.nodes.Binding;
import org.blisp.nodes.Let;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.HashMap;
import java.util.Map;

public class LetScope implements IScope {
    private IScope parentScope;
    private int nextRegister;
    private Let letNode;

    private Map<String, Integer> bindings = new HashMap<>();

    public LetScope(Let node, IScope parentScope)
    {
        this.parentScope = parentScope;
        this.nextRegister = this.parentScope.nextLocalRegister();
    }

    public void pushBinding(String name) throws Exception
    {
        if (bindings.containsKey(name))
        {
            throw new Exception("duplicate bound variable in let form");
        }
        else
        {
            bindings.put(name, nextRegister + bindings.size());
        }
    }

    @Override
    public IScope getParentScope() {
        return parentScope;
    }

    @Override
    public Symbol lookup(String name) throws Exception {
        return null;
    }

    @Override
    public void loadIdentifier(String name, MethodVisitor mv) throws Exception {
        if (bindings.containsKey(name))
        {
            mv.visitVarInsn(Opcodes.ALOAD, bindings.get(name));
        }
        else
        {
            getParentScope().loadIdentifier(name, mv);
        }
    }

    @Override
    public int nextLocalRegister() {
        return this.nextRegister + bindings.size();
    }
}
