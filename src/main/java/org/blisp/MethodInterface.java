package org.blisp;

public class MethodInterface {
    private final String className;
    private final String name;
    private final int argumentCount;

    public MethodInterface(String className, String name, int argumentCount)
    {
        this.className = className;
        this.name = name;
        this.argumentCount = argumentCount;
    }

    public String getClassName()
    {
        return className;
    }

    public String getName()
    {
        return name;
    }

    public int getArgumentCount()
    {
        return argumentCount;
    }
}
