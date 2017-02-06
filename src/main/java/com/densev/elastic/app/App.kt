package com.densev.elastic.app

import com.densev.elastic.model.TestClass
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.apache.commons.math3.random.MersenneTwister
import org.elasticsearch.action.index.IndexRequestBuilder
import org.elasticsearch.client.Client
import org.elasticsearch.client.transport.NoNodeAvailableException
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.support.ClassPathXmlApplicationContext
import org.springframework.stereotype.Component
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.concurrent.thread

/**
 * Created on 20.01.2017.
 */
@Component
class App {
    internal var logger = LoggerFactory.getLogger(App::class.java)

    @Autowired
    private val mapper: ObjectMapper? = null
    @Autowired
    private val elasticClient: Client? = null

    @Throws(JsonProcessingException::class)
    fun run() {
        try {
            val r = MersenneTwister()

            var executionService : ExecutorService = Executors.newFixedThreadPool(2)


            /**/
            executionService.submit {
                var bulk = elasticClient!!.prepareBulk()
                for(i in 1..16){
                    val doc = Jsoup.connect("").get()
                    val element = doc.select("#storytext")
                    val testData = TestClass.Builder()
                            .id(r.nextInt())
                            .test(element.html())
                            .build()
                    val indexRequest = elasticClient.prepareIndex("test-index", "test-type")
                            .setSource(mapper!!.writeValueAsString(testData))
                    bulk.add(indexRequest)
                }

                val response = bulk.get()
                logger.debug("Bulk took {}", response.tookInMillis)
                executionService.shutdownNow()
             }

        } catch (e: NoNodeAvailableException) {
            logger.error("No node available: ", e)
        }

    }

    companion object {
        internal val contextLocation = "classpath:application-context.xml"

        @Throws(JsonProcessingException::class)
        @JvmStatic fun main(args: Array<String>) {
            val context = ClassPathXmlApplicationContext(contextLocation)
            val app = context.getBean(App::class.java)
            app.run()
        }
    }
}
