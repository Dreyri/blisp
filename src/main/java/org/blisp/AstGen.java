package org.blisp;

import org.antlr.v4.runtime.misc.NotNull;
import org.antlr.v4.runtime.tree.ParseTree;
import org.blisp.nodes.*;
import org.blisp.nodes.Class;
import org.blisp.nodes.Number;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class AstGen
{
    public Class parse(ParseTree tree)
    {
        ClassVisitor classVisit = new ClassVisitor();
        Class c = classVisit.visit(tree);
        return c;
    }

    private static class ClassVisitor extends BlispBaseVisitor<Class>
    {
        @Override
        public Class visitScmClass(@NotNull BlispParser.ScmClassContext ctx)
        {
            DefineVisitor visitor = new DefineVisitor();
            Define[] defines = ctx.scmDefine().stream().map(def -> def.accept(visitor)).toArray(Define[]::new);

            return new Class(defines);
        }
    }

    private static class DefineVisitor extends BlispBaseVisitor<Define>
    {
        @Override
        public Define visitScmDefine(@NotNull BlispParser.ScmDefineContext ctx)
        {
            VarVisitor varVisitor = new VarVisitor();
            Identifier id = ctx.var.accept(varVisitor);
            ExpressionVisitor exprVisitor = new ExpressionVisitor();
            ProcVisitor procVisitor = new ProcVisitor();
            Procedure proc = ctx.proc.accept(procVisitor);

            return new Define(id, proc);
        }
    }

    private static class VarVisitor extends BlispBaseVisitor<Identifier>
    {
        @Override
        public Identifier visitScmVar(@NotNull BlispParser.ScmVarContext ctx)
        {
            return new Identifier(ctx.getText());
        }
    }

    private static class ExpressionVisitor extends BlispBaseVisitor<Expression>
    {
        @Override
        public Expression visitScmExp(@NotNull BlispParser.ScmExpContext ctx)
        {
            BlispParser.ScmAtomicContext atomicContext = ctx.scmAtomic();

            if (atomicContext != null)
            {
                AtomicVisitor atomicVisitor = new AtomicVisitor();
                return atomicContext.accept(atomicVisitor);
            }

            BlispParser.ScmCompositeContext compositeContext = ctx.scmComposite();

            if (compositeContext != null) {
                CompositeVisitor compositeVisitor = new CompositeVisitor();
                return compositeContext.accept(compositeVisitor);
            }

            // I've made the decision defines can only appear at global scope
            /*
            BlispParser.ScmDefineContext defineContext = ctx.scmDefine();

            if (defineContext != null)
            {
                DefineVisitor defineVisitor = new DefineVisitor();
                return defineContext.accept(defineVisitor);
            }
             */

            return null;
        }
    }

    private static class AtomicVisitor extends BlispBaseVisitor<Atomic>
    {
        @Override
        public Atomic visitScmAtomic(@NotNull BlispParser.ScmAtomicContext ctx)
        {
            BlispParser.ScmVarContext varContext = ctx.scmVar();

            if (varContext != null)
            {
                VarVisitor varVisitor = new VarVisitor();
                return varContext.accept(varVisitor);
            }

            BlispParser.ScmNumberContext numberContext = ctx.scmNumber();

            if (numberContext != null)
            {
                NumberVisitor numberVisitor = new NumberVisitor();
                return numberContext.accept(numberVisitor);
            }

            return null;
        }
    }

    private static class NumberVisitor extends BlispBaseVisitor<Number>
    {
        @Override
        public Number visitScmNumber(@NotNull BlispParser.ScmNumberContext ctx)
        {
            int num = Integer.parseInt(ctx.getText());
            return new Number(num);
        }
    }

    private static class CompositeVisitor extends BlispBaseVisitor<Composite>
    {
        @Override
        public Composite visitScmComposite(@NotNull BlispParser.ScmCompositeContext ctx)
        {
            BlispParser.ScmProcContext procContext = ctx.scmProc();

            if (procContext != null)
            {
                ProcVisitor procVisitor = new ProcVisitor();
                return procContext.accept(procVisitor);
            }

            BlispParser.ScmAppContext appContext = ctx.scmApp();

            if (appContext != null)
            {
                AppVisitor appVisitor = new AppVisitor();
                return appContext.accept(appVisitor);
            }

            BlispParser.ScmLetContext letContext = ctx.scmLet();

            if (letContext != null)
            {
                LetVisitor letVisitor = new LetVisitor();
                return letContext.accept(letVisitor);
            }

            BlispParser.ScmIfContext ifContext = ctx.scmIf();

            if (ifContext != null)
            {
                IfVisitor ifVisitor = new IfVisitor();
                return ifContext.accept(ifVisitor);
            }

            return null;
        }
    }

    private static class ProcVisitor extends BlispBaseVisitor<Procedure>
    {
        @Override
        public Procedure visitScmProc(@NotNull BlispParser.ScmProcContext ctx)
        {
            VarVisitor varVisitor = new VarVisitor();
            Identifier[] captures = ctx.captures.stream().map(var -> var.accept(varVisitor)).toArray(Identifier[]::new);
            Identifier[] args = ctx.args.stream().map(var -> var.accept(varVisitor)).toArray(Identifier[]::new);

            ExpressionVisitor expressionVisitor = new ExpressionVisitor();
            Expression expr = ctx.expression.accept(expressionVisitor);
            //Expression[] expressions = ctx.expressions.stream().map(expr -> expr.accept(expressionVisitor)).toArray(Expression[]::new);

            return new Procedure(captures, args, expr);
        }
    }

    private static class AppVisitor extends BlispBaseVisitor<Application>
    {
        @Override
        public Application visitScmApp(@NotNull BlispParser.ScmAppContext ctx)
        {
            ExpressionVisitor expressionVisitor = new ExpressionVisitor();
            List<Expression> exprs = ctx.scmExp().stream().map(expr -> expr.accept(expressionVisitor)).collect(toList());

            return new Application(exprs.stream().findFirst().get(), exprs.stream().skip(1).toArray(Expression[]::new));
        }
    }

    private static class LetVisitor extends BlispBaseVisitor<Let>
    {
        @Override
        public Let visitScmLet(@NotNull BlispParser.ScmLetContext ctx)
        {
            BindingVisitor bindingVisitor = new BindingVisitor();
            Binding[] bindings = ctx.scmBinding().stream().map(bind -> bind.accept(bindingVisitor)).toArray(Binding[]::new);

            ExpressionVisitor expressionVisitor = new ExpressionVisitor();
            Expression expr = ctx.scmExp().accept(expressionVisitor);
            return new Let(bindings, expr);
        }
    }

    private static class BindingVisitor extends BlispBaseVisitor<Binding>
    {
        @Override
        public Binding visitScmBinding(@NotNull BlispParser.ScmBindingContext ctx)
        {
            VarVisitor varVisitor = new VarVisitor();
            ExpressionVisitor expressionVisitor = new ExpressionVisitor();

            String name = ctx.name.getText();
            Expression expr = ctx.expr.accept(expressionVisitor);

            return new Binding(name, expr);
        }
    }

    private static class IfVisitor extends  BlispBaseVisitor<If>
    {
        @Override
        public If visitScmIf(@NotNull BlispParser.ScmIfContext ctx)
        {
            ExpressionVisitor expressionVisitor = new ExpressionVisitor();

            Expression cond = ctx.scmExp(0).accept(expressionVisitor);
            Expression trueCase = ctx.scmExp(1).accept(expressionVisitor);
            Expression falseCase = ctx.scmExp(2).accept(expressionVisitor);

            return new If(cond, trueCase, falseCase);
        }
    }
}