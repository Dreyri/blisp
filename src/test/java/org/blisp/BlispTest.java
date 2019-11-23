package org.blisp;

import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class BlispTest {
    @Test
    void parseFunction() throws IOException {
        File f = new File(getClass().getClassLoader().getResource("fun.bl").getFile());
        FileInputStream stream = new FileInputStream(f);
        ParseTree tree = Blisp.parseStream(stream);
    }
}
