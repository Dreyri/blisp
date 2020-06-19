package org.blisp;

public interface IFn {
    Object invoke();
    Object invoke(Object arg1);
    Object invoke(Object arg1, Object arg2);
    Object invoke(Object arg1, Object arg2, Object arg3);
    Object invoke(Object arg1, Object arg2, Object arg3, Object arg4);
    Object invoke(Object arg1, Object arg2, Object arg3, Object arg4, Object arg5);
}
