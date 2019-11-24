package org.blisp;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class Blisp {

    public static ParseTree parseStream(InputStream stream) throws IOException  {
        ANTLRInputStream input = new ANTLRInputStream(stream);
        BlispLexer lexer = new BlispLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        BlispParser parser = new BlispParser(tokens);
        ParseTree tree = parser.program();

        return tree;
    }

    public static ParseTree parseFile(String filename) throws IOException {
        InputStream stream = new FileInputStream(filename);
        return parseStream(stream);
    }

    public static void main(String[] args) throws IOException {
        String inputFile = (args.length) > 0 ? args[1] : null;

        InputStream iStream = (inputFile == null) ? System.in : new FileInputStream(inputFile);

        ParseTree tree = parseStream(iStream);

        System.out.println(tree.toStringTree());

        ParseTreeWalker walker = new ParseTreeWalker();
        PrintListener printListener = new PrintListener();

        walker.walk(printListener, tree);
    }
}