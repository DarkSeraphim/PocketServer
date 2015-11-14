package com.pocketserver.impl.console;

import java.util.HashMap;
import java.util.Map;

public class ArgumentParser {
    private final Map<String,Argument> options = new HashMap<>();

    public ArgumentParser(String[] args) {
        for (String arg : args) {
            Argument argument = new Argument(arg);
            options.put(arg,argument);
        }
    }

    public Map<String, Argument> getOptions() {
        return options;
    }

    public boolean isAllowed(String option) {
        return options.getOrDefault(option,new Argument("",false)).allowed;
    }

    class Argument {
        private final String argumentName;
        private final boolean allowed;

        Argument(String argumentName) {
            this(argumentName,true);
        }

        Argument(String argumentName, boolean allowed) {
            this.argumentName = argumentName;
            this.allowed = allowed;
        }

        public String getArgumentName() {
            return argumentName;
        }

        public boolean isAllowed() {
            return allowed;
        }
    }
}
