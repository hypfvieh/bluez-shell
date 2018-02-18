package com.github.hypfvieh.commands.base;

import java.util.ArrayList;
import java.util.List;

/**
 * Specify arguments for the command line.
 */
public class CommandArg {
    static final List<CommandArg> NO_ARGS = new ArrayList<>();
    
    private String argName;
    private boolean optional;
    
    public CommandArg(String _argName, boolean _optional) {
        argName = _argName;
        optional = _optional;
    }

    public String getArgName() {
        return argName;
    }

    public void setArgName(String _argName) {
        argName = _argName;
    }

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean _optional) {
        optional = _optional;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (optional) sb.append("[");
        sb.append(argName);
        if (optional) sb.append("]");
        
        return sb.toString();
    }
    
    
}
