package org.blisp;

public abstract class AFn implements IFn {
    @Override
    public Object invoke() {
        return throwArity(0);
    }

    @Override
    public Object invoke(Object arg1) {
        return throwArity(1);
    }

    @Override
    public Object invoke(Object arg1, Object arg2) {
        return throwArity(2);
    }

    @Override
    public Object invoke(Object arg1, Object arg2, Object arg3) {
        return throwArity(3);
    }

    @Override
    public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4) {
        return throwArity(4);
    }

    @Override
    public Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5) {
        return throwArity(5);
    }

    public Object throwArity(int n) {
        String name = getClass().getName();
        throw new ArityException(n, name);
    }
}
