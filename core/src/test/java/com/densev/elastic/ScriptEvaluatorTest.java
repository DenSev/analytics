package com.densev.elastic;

import com.densev.elastic.script.ScriptEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.script.ScriptContext;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;

import static org.testng.Assert.assertEquals;

/**
 * Created by Dzianis_Sevastseyenk on 07/11/2017.
 */
public class ScriptEvaluatorTest {

    private static final Logger LOG = LoggerFactory.getLogger(ScriptEvaluatorTest.class);

    ScriptEvaluator evaluator;

    @BeforeTest
    public void init() throws Exception {
        evaluator = new ScriptEvaluator();
        evaluator.init();
        //warm up
        evaluator.evalRaw("println 'Warming up the engine'");
    }

    @Test
    public void testLoadScripts() throws Exception {
        ScriptContext context = new SimpleScriptContext();
        final int expectedValue = 3;
        final int actualValue = (int) evaluator.eval("test.groovy", context);
        assertEquals(actualValue, expectedValue);
    }

    @Test
    public void testEvalRaw() throws ScriptException {
        final int expectedValue = 10 * 2 * 3 * 4 * 5 * 6 * 7 * 8 * 9 * 10;
        final int actualValue = (int) evaluator.evalRaw("int k = 10; for( int i = 1; i  <= 10; i++){ k = k * i }; return k;");
        assertEquals(actualValue, expectedValue);
    }
}
