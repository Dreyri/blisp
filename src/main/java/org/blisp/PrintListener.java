package org.blisp;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;

public class PrintListener implements BlispListener {
    @Override
    public void enterProgram(BlispParser.ProgramContext ctx) {
        System.err.println("Entered parse");
    }

    @Override
    public void exitProgram(BlispParser.ProgramContext ctx) {
        System.err.println("Exited parse");
    }

    @Override
    public void enterFundecl(BlispParser.FundeclContext ctx) {
        System.err.println("Entered function declaration");
    }

    @Override
    public void exitFundecl(BlispParser.FundeclContext ctx) {
        System.err.println("Exited function declaration");
    }

    @Override
    public void enterVardecl(BlispParser.VardeclContext ctx) {
        System.err.println("Entered variable declaration");
    }

    @Override
    public void exitVardecl(BlispParser.VardeclContext ctx) {
        System.err.println("Exited variable declaration");
    }

    @Override
    public void enterSexpr(BlispParser.SexprContext ctx) {
        System.err.print("Entering sexpr");
    }

    @Override
    public void exitSexpr(BlispParser.SexprContext ctx) {
        System.err.println("Exiting sexpr");
    }

    @Override
    public void enterItem(BlispParser.ItemContext ctx) {
        System.err.println("Entering item");
    }

    @Override
    public void exitItem(BlispParser.ItemContext ctx) {
        System.err.println("Exiting item");
    }

    @Override
    public void enterList(BlispParser.ListContext ctx) {
        System.err.println("Entering list");
    }

    @Override
    public void exitList(BlispParser.ListContext ctx) {
        System.err.println("Exiting list");
    }

    @Override
    public void enterAtom(BlispParser.AtomContext ctx) {
        System.err.println("Entering atom");
    }

    @Override
    public void exitAtom(BlispParser.AtomContext ctx) {
        System.err.println("Exiting atom");
    }

    @Override
    public void visitTerminal(TerminalNode node) {
        System.err.println("Terminal: " + node.getText());
    }

    @Override
    public void visitErrorNode(ErrorNode node) {
        System.err.println("Error: " + node.getText());
    }

    @Override
    public void enterEveryRule(ParserRuleContext ctx) {

    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {

    }
}
