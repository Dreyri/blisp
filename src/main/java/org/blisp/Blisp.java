package org.blisp;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.blisp.nodes.Class;
import org.objectweb.asm.ClassWriter;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public final class Blisp {

    public static Class parseStream(InputStream stream) throws IOException  {
        ANTLRInputStream input = new ANTLRInputStream(stream);
        BlispLexer lexer = new BlispLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        BlispParser parser = new BlispParser(tokens);

        ParseTree tree = parser.scmClass();

        AstGen astGen = new AstGen();
        Class c = astGen.parse(tree);

        return c;
    }

    public static Class parseFile(String filename) throws IOException {
        InputStream stream = new FileInputStream(filename);
        return parseStream(stream);
    }

    public static void main(String[] args) throws IOException, NoSuchMethodException, InstantiationException, IllegalAccessException, InvocationTargetException {
        String inputFile = (args.length) > 0 ? args[0] : null;

        InputStream iStream = (inputFile == null) ? System.in : new FileInputStream(inputFile);

        Class c = parseStream(iStream);
        ByteCode classBytes = c.codeGen("fun");

        FileOutputStream output = new FileOutputStream(String.format("%s.class", classBytes.moduleName));
        output.write(classBytes.moduleClass.toByteArray());
        output.close();
        
        for (Map.Entry<String, ClassWriter> inners : classBytes.functionClasses.entrySet())
        {
            output = new FileOutputStream(String.format("%s.class", inners.getKey()));
            output.write(inners.getValue().toByteArray());
            output.close();
        }

        BlispClassLoader classLoader = new BlispClassLoader();

        for (Map.Entry<String, ClassWriter> entry : classBytes.functionClasses.entrySet())
        {
            classLoader.defineClass(entry.getKey(), entry.getValue().toByteArray());
        }

        java.lang.Class<?> funClass = classLoader.defineClass(classBytes.moduleName, classBytes.moduleClass.toByteArray());

        Entry entry = (Entry)funClass.getConstructor().newInstance();
        entry.bl_main();
    }
}