package org.blisp;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.util.HashMap;
import java.util.Map;

public class ProcedureScope extends AScope {
    //public Type procedureType;
    private String className;
    private Map<String, Integer> fields = new HashMap<>();
    private Map<String, Integer> arguments = new HashMap<>();

    public ProcedureScope(IScope parentScope) {
        super(parentScope);
    }

    public void pushField(String name) throws Exception
    {
        if (fields.containsKey(name) || arguments.containsKey(name))
        {
            throw new Exception("Found a duplicate symbol in the capture/argument list");
        }

        fields.put(name, fields.size());
    }

    public void pushArgument(String name) throws Exception
    {
        if (arguments.containsKey(name) || fields.containsKey(name))
        {
            throw new Exception("Found a duplicate symbol in the capture/argument list");
        }
        arguments.put(name, arguments.size());
    }

    /*
    public Type getType()
    {
        return procedureType;
    }
     */

    @Override
    public void loadIdentifier(String id, MethodVisitor mv) throws Exception
    {
        // special case for unnamed function recursion
        if (id.equals("this"))
        {
            mv.visitVarInsn(Opcodes.ALOAD, 0); // load this
        }
        else if (arguments.containsKey(id))
        {
            int register = arguments.get(id);

            mv.visitVarInsn(Opcodes.ALOAD, register);
        }
        else if (fields.containsKey(id))
        {
            int register = fields.get(id);

            mv.visitFieldInsn(Opcodes.GETFIELD, className, id, "Ljava/lang/Object;");
        }
        else
        {
            throw new Exception("Could not find the identifier " + id);
        }
    }

    @Override
    public int nextLocalRegister() {
        return arguments.size();
    }
}
