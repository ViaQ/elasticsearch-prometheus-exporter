package org.elasticsearch.action;

import org.elasticsearch.client.ElasticsearchClient;

public class PrometheusRequestBuilder extends ActionRequestBuilder<PrometheusMetricsRequest, PrometheusMetricsResponse, PrometheusRequestBuilder>{

    public PrometheusRequestBuilder(ElasticsearchClient client, PrometheusMetricsAction action) {
        super(client, action, new PrometheusMetricsRequest());
    }
}
