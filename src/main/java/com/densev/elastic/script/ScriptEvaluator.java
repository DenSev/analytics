package com.densev.elastic.script;


import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.PostConstruct;
import javax.script.Compilable;
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


    private final ScriptEngine engine;
    private String baseScriptFolder = "scripts";
    private String scannedFileExtension = ".groovy";
    private String encoding = "UTF-8";

    private Map<String, TimestampedScript> scriptCache;

    public ScriptEvaluator() {

        ScriptEngineManager manager = new ScriptEngineManager();
        engine = manager.getEngineByName("groovy");
    }

    @PostConstruct
    public void init() {
        precompileScripts();
    }


    public Map<String, Pair<String, DateTime>> loadScripts() {

        try (Stream<Path> paths = Files.walk(Paths.get(baseScriptFolder))) {
            return paths.filter(Files::isRegularFile)
                .filter(path -> !path.endsWith(scannedFileExtension))
                .map(this::loadScript)
                .map(pair -> {
                    try {
                        String scriptname = pair.getKey().getName();
                        String script = IOUtils.toString(new FileInputStream(pair.getKey()), encoding);

                        return new SimpleImmutableEntry<>(scriptname, Pair.of(script, pair.getValue()));
                    } catch (IOException e) {
                        LOG.error("Error while reading script file.", e);
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toMap(Entry::getKey, Entry::getValue));
        } catch (IOException e) {
            LOG.error("Error while loading scripts.", e);
            throw new RuntimeException(e);
        }
    }

    public Pair<File, DateTime> loadScript(Path path) {
        File script = path.toFile();
        return Pair.of(script, new DateTime(script.lastModified()));
    }

    public void precompileScripts() {
        Map<String, Pair<String, DateTime>> timestampedScripts = loadScripts();
        scriptCache = timestampedScripts
            .entrySet()
            .stream()
            .map(timestampedScript -> {
                try {
                    String scriptName = timestampedScript.getKey();
                    Pair<String, DateTime> script = timestampedScript.getValue();

                    return new TimestampedScript(
                        scriptName,
                        ((Compilable) engine).compile(script.getKey()),
                        script.getValue()
                    );
                } catch (ScriptException scriptException) {
                    LOG.error("Error compiling script {} : {}", timestampedScript, ExceptionUtils.getStackTrace(scriptException));
                    throw new RuntimeException(scriptException);
                }

            })
            .collect(Collectors.toMap(TimestampedScript::getScriptName, timestampedScript -> timestampedScript));
    }

    public Object eval(String scriptName, ScriptContext context) throws ScriptException, FileNotFoundException {
        if (!this.scriptCache.containsKey(scriptName)) {
            throw new FileNotFoundException("Script with name " + scriptName + " wasn't found in script cache. " +
                "Please check if you are referencing correct script.");
        }
        return this.scriptCache.get(scriptName).eval(context);
    }

    public Object evalRaw(String script) throws ScriptException {
        return engine.eval(script);
    }

    public String getBaseScriptFolder() {
        return baseScriptFolder;
    }

    public void setBaseScriptFolder(String baseScriptFolder) {
        this.baseScriptFolder = baseScriptFolder;
    }

    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public String getScannedFileExtension() {
        return scannedFileExtension;
    }

    public void setScannedFileExtension(String scannedFileExtension) {
        this.scannedFileExtension = scannedFileExtension;
    }
}
