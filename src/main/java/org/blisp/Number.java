package org.blisp;

public class Number {
    private int value;

    public Number(int value)
    {
        this.value = value;
    }

    @Override
    public boolean equals(Object other)
    {
        if (other instanceof Number)
        {
            return this.value == ((Number) other).value;
        }
        return false;
    }

    public static Object add(Object o1, Object o2) {
        assert(o1 instanceof Number);
        assert(o2 instanceof Number);

        return new Number(((Number) o1).value + ((Number) o2).value);
    }

    public static Object sub(Object o1, Object o2)
    {
        assert(o1 instanceof Number);
        assert(o2 instanceof Number);

        return new Number(((Number) o1).value - ((Number) o2).value);
    }

    public static Object eq(Object o1, Object o2)
    {
        assert(o1 instanceof Number);
        assert(o2 instanceof Number);

        return (((Number) o1).value == ((Number) o2).value);
    }
    
    public static Object lessThan(Object o1, Object o2)
    {
        assert o1 instanceof Number;
        assert o2 instanceof Number;
        
        return ((Number) o1).value < ((Number) o2).value;
    }
    
    public static Object lessThanEq(Object o1, Object o2)
    {
        assert o1 instanceof Number;
        assert o2 instanceof Number;
        
        return ((Number) o1).value <= ((Number) o2).value;
    }
    
    public static Object greaterThan(Object o1, Object o2)
    {
        assert o1 instanceof Number;
        assert o2 instanceof Number;

        return ((Number) o1).value > ((Number) o2).value;
    }
    
    public static Object greaterThanEq(Object o1, Object o2)
    {
        assert o1 instanceof Number;
        assert o2 instanceof Number;
        
        return ((Number) o1).value >= ((Number) o2).value;
    }

    @Override
    public String toString()
    {
        return Integer.toString(value);
    }
}
