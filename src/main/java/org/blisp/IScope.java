package org.blisp;

import org.objectweb.asm.MethodVisitor;

public interface IScope {
    IScope getParentScope();

    Symbol lookup(String name) throws Exception;

    void loadIdentifier(String name, MethodVisitor method) throws Exception;

    // used by scopes to increment their registers
    int nextLocalRegister();

}
