package org.blisp;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class Blisp {
    public static void main(String[] args) throws IOException {
        String inputFile = (args.length) > 0 ? args[1] : null;

        InputStream iStream = (inputFile == null) ? System.in : new FileInputStream(inputFile);
        ANTLRInputStream input = new ANTLRInputStream(iStream);
        BlispLexer lexer = new BlispLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        BlispParser parser = new BlispParser(tokens);
        ParseTree tree = parser.sexpr();

        System.out.println(tree.toStringTree(parser));
    }
}