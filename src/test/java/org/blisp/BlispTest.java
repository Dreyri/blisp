package org.blisp;

import org.antlr.v4.runtime.tree.ParseTree;
import org.blisp.nodes.Class;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class BlispTest {
    @Test
    void parseFunction() throws IOException {
        File f = new File(getClass().getClassLoader().getResource("fun.bl").getFile());
        FileInputStream stream = new FileInputStream(f);
        Class c = Blisp.parseStream(stream);
    }

    @Test
    void codeGen() throws IOException
    {
        File f = new File(getClass().getClassLoader().getResource("fun.bl").getFile());
        FileInputStream stream = new FileInputStream(f);
        Class c = Blisp.parseStream(stream);
        
        ByteCode asmBytes = c.codeGen("fun");
    }
}
