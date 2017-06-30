package com.densev.elastic.script;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import javafx.util.Pair;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * Created by Dzianis_Sevastseyenk on 06/30/2017.
 */
@Component
public class ScriptEvaluator {

    private static final Logger LOG = LoggerFactory.getLogger(ScriptEvaluator.class);
    private final String BASE_SCRIPT_FOLDER = "";


    private final ScriptEngine engine;

    Set<TimestampedScript> scriptCache;

    public ScriptEvaluator() {

        ScriptEngineManager manager = new ScriptEngineManager();
        engine = manager.getEngineByName("groovy");
    }

    @PostConstruct
    public void init() {

    }


    public List<Pair<String, DateTime>> loadScripts() {
        throw new UnsupportedOperationException();
    }

    public void precompileScripts() {
        List<Pair<String, DateTime>> timestampedScripts = loadScripts();
        scriptCache = timestampedScripts
            .stream()
            .map(timestampedScript -> {
                try {
                    TimestampedScript script = new TimestampedScript(
                        ((Compilable) engine).compile(timestampedScript.getKey()),
                        timestampedScript.getValue()
                    );
                    return script;
                } catch (ScriptException scriptException) {
                    LOG.error("Error compiling script {} : {}", timestampedScript, ExceptionUtils.getStackTrace(scriptException));
                    throw new RuntimeException(scriptException);
                }

            })
            .collect(Collectors.toSet());
    }

    public void eval(String scriptName, ScriptContext context) throws ScriptException {
        CompiledScript script = null;
        script.eval(context);
    }

}
