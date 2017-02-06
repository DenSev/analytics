package com.densev.elastic;

import com.densev.elastic.model.Document;
import com.densev.elastic.model.Element;
import com.densev.elastic.repository.MappingFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.mockito.Mockito.when;
/**
 * Created by Dzianis_Sevastseyenk on 02/06/2017.
 */
public class MappingFactoryTest {

    @Mock
    ElementConverter elementConverter;
    @InjectMocks
    MappingFactory mappingFactory;

    @BeforeClass
    public void init(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConvert(){
        Element e= new Element();
        e.setElementName("test_element");
        Document doc = new Document();

        doc.setId("test_doc");
        doc.putElement(e);

        when(elementConverter.convert(e)).thenReturn("\"test_field\":{\n" +
            "\"index\":\"not_analyzed\",\n" +
            "\"type\":\"keyword\"\n" +
            "}");

        System.out.println(mappingFactory.getMappingsFromDocument(doc));
    }

}
