package com.densev.elastic;

import com.densev.elastic.model.ElasticType;
import com.densev.elastic.model.Element;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
/**
 * Created by Dzianis_Sevastseyenk on 02/06/2017.
 */
//@ContextConfiguration(locations = {"classpath:application-context.xml"})
public class ElementConverterTest extends AbstractTestNGSpringContextTests{

    @Autowired
    ElementConverter converter;

    @BeforeClass
    public void init(){
        MockitoAnnotations.initMocks(this);
        /*when(provider.getFactory()).thenReturn(jsonFactory);
        when(jsonFactory.createObjectBuilder()).thenCallRealMethod();*/

    }

    @Test(enabled = false)
    public void testConverter() {
        Element e = new Element();
        e.setAnalyzed(false);
        e.setElementName("test_field");
        e.setElementType(ElasticType.KEYWORD);

        String expectedField = "\"test_field\":{\n" +
            "\"index\":\"not_analyzed\",\n" +
            "\"type\":\"keyword\"\n" +
            "}";
        assertEquals(converter.convert(e),expectedField);

    }
}
