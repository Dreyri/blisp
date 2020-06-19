package org.blisp;

public abstract class Symbol extends AFn {
    private String name;
    //private Type type;
    private boolean isGlobal;
    private IScope scope;

    public Symbol(String name, boolean global, IScope scope) {
        this.name = name;
        this.isGlobal = global;
        //this.type = null;
        this.scope = scope;
    }

    public String getName() {
        return name;
    }

    /*
    public Type getType()
    {
        return type;
    }

    public void setType(Type type)
    {
        this.type = type;
    }
     */

    public IScope getScope()
    {
        return scope;
    }

    public boolean isGlobal() {
        return isGlobal;
    }

    /*
    public boolean hasType()
    {
        return getType() != null;
    }

     */

}
