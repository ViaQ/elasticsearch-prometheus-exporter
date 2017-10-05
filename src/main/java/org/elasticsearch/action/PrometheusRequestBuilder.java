package org.elasticsearch.action;

import org.elasticsearch.action.support.master.MasterNodeReadOperationRequestBuilder;
import org.elasticsearch.client.ElasticsearchClient;

public class PrometheusRequestBuilder extends MasterNodeReadOperationRequestBuilder<PrometheusMetricsRequest, PrometheusMetricsResponse, PrometheusRequestBuilder> {

    public PrometheusRequestBuilder(ElasticsearchClient client, PrometheusMetricsAction action) {
        super(client, action, new PrometheusMetricsRequest().local(true));
    }
}
