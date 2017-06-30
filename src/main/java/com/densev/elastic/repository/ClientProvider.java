package com.densev.elastic.repository;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.NoNodeAvailableException;

/**
 * Created by Dzianis_Sevastseyenk on 06/30/2017.
 */
public interface ClientProvider {

    Client getClient();

    void reinit(NoNodeAvailableException nnae);
}
