package com.densev.elastic;

import com.densev.elastic.script.ScriptEvaluator;
import com.google.common.base.Stopwatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import javax.script.ScriptContext;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

/**
 * Created by Dzianis_Sevastseyenk on 07/11/2017.
 */
public class ScriptEvaluatorTest {

    private static final Logger LOG = LoggerFactory.getLogger(ScriptEvaluatorTest.class);

    ScriptEvaluator evaluator;

    @BeforeTest
    public void init() throws Exception {
        Stopwatch stopwatch = Stopwatch.createStarted();
        evaluator = new ScriptEvaluator();
        evaluator.init();
        //warm up
        evaluator.evalRaw("println 'Warming up the engine'");
        stopwatch.stop();
        LOG.info("Done initializing in {} ms", stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    @Test(invocationCount = 10)
    public void testLoadScripts() throws Exception {
        ScriptContext context = new SimpleScriptContext();
        Stopwatch stopwatch = Stopwatch.createStarted();
        evaluator.eval("test.groovy", context);
        stopwatch.stop();
        LOG.info("Elapsed: {} MICROSECONDS", stopwatch.elapsed(TimeUnit.MICROSECONDS));
    }

    @Test(invocationCount = 10)
    public void testEvalRaw() throws ScriptException {
        Stopwatch stopwatch = Stopwatch.createStarted();
        evaluator.evalRaw("int k = 10; for( int i = 0; i  < 10; i++){ k = k * (i + 1); }; return k;");
        stopwatch.stop();
        LOG.info("Elapsed: {} MICROSECONDS", stopwatch.elapsed(TimeUnit.MICROSECONDS));
    }

    @Test
    public void getRelativePath() throws Exception {
        Path path = Paths.get("scripts");

        Path path1 = Paths.get(this.getClass().getClassLoader().getResource("application-context.xml").toURI());
        //Path p = path.relativize(path);
        System.out.println(path.toAbsolutePath());
    }
}
