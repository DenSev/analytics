package com.densev.elastic.script;

import org.joda.time.DateTime;

import javax.script.Bindings;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

/**
 * Created by Dzianis_Sevastseyenk on 06/30/2017.
 */
public class TimestampedScript extends CompiledScript {

    private CompiledScript script;
    private DateTime timestamp;

    public TimestampedScript(CompiledScript script, DateTime timestamp) {
        this.script = script;
        this.timestamp = timestamp;
    }

    public DateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(DateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public Object eval(Bindings bindings) throws ScriptException {
        return script.eval(bindings);
    }

    @Override
    public Object eval() throws ScriptException {
        return script.eval();
    }

    @Override
    public Object eval(ScriptContext context) throws ScriptException {
        return script.eval(context);
    }

    @Override
    public ScriptEngine getEngine() {
        return script.getEngine();
    }
}
