package org.blisp.nodes;

import org.blisp.Environment;

public interface Expression {
    void compile(Environment env);
}
