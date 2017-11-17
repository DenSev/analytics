package com.densev.elastic;

import com.densev.elastic.repository.DistributedClient;
import org.testng.annotations.Test;

/**
 * Created by Dzianis_Sevastseyenk on 10/20/2017.
 */
public class DistributedClientTest {

    @Test
    public void test() throws Exception {

        DistributedClient client = new DistributedClient("es-inventory-2.qa-21.vip.aws2", "80");
    }
}
