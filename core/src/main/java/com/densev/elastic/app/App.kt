package com.densev.elastic.app

import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


/**
 * Created on 20.01.2017.
 */
class App {
    internal var logger = LoggerFactory.getLogger(App::class.java)

    @Autowired
    private val mapper: ObjectMapper? = null
    @Autowired
    private val elasticClient: Client? = null

    @Throws(JsonProcessingException::class)
    fun run() {
        val r = MersenneTwister()

        var executionService: ExecutorService = Executors.newFixedThreadPool(2)

        //val doc = Jsoup.connect("http://www.pixiv.net/search.php?word=2B&s_mode=s_tag_full&order=date_d&p=110").get()
        //val elements = doc.select(".work._work")
        /*for(e in elements){
            val workRef = e.attr("href")
            val url = "http://www.pixiv.net/$workRef"
            logger.info(url)
            val work = Jsoup.connect(url).get()
            val workImage = work.select(".works_display img")
            logger.info(workImage.toString())
        }*/

        val work = Jsoup.connect("").get()

        val img = work.select("._work img")
        val imgUrl = img.attr("src")
        val splitparts = imgUrl.split("/")

        val imgFilename = splitparts[splitparts.size - 1]
        var index = splitparts.indexOf("img")
        var newUrl = splitparts[0] + "//" + splitparts[2] + "/img-original/img"
        while (index < splitparts.size - 2) {
            index++
            newUrl += "/" + splitparts[index]
        }
        val t1 = imgFilename.split(".")
        val type = t1[1]
        val nameParts = t1[0].split("_")
        val newFileName = nameParts[0] + "_" + nameParts[1] + "." + type
        newUrl += "/" + newFileName
        logger.info(newUrl)
        logger.info(splitparts.toString())

        val image = Jsoup.connect(newUrl).ignoreContentType(true).execute()

        var f: File = File("D://test.$type")
        val out = FileOutputStream(f)
        out.write(image.bodyAsBytes())
        // resultImageResponse.body() is where the image's contents are.
        out.close()

//http://i2.pixiv.net/img-original/img
        //logger.info(img.toString())

        /**/


    }

    companion object {
        internal val contextLocation = "classpath:application-context.xml"

        @Throws(JsonProcessingException::class)
        @JvmStatic
        fun main(args: Array<String>) {
            /*val context = ClassPathXmlApplicationContext(contextLocation)
            val app = context.getBean(App::class.java)
            app.run()*/
            val app = App()
            app.run()
        }
    }
}
