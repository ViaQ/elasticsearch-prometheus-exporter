package org.elasticsearch.action;

import org.elasticsearch.client.ElasticsearchClient;

public class PrometheusMetricsAction extends Action<PrometheusMetricsRequest, PrometheusMetricsResponse, PrometheusRequestBuilder> {

    public static final PrometheusMetricsAction INSTANCE = new PrometheusMetricsAction();
    public static final String NAME = "cluster:data/read/metrics"; // TODO: find good name

    private PrometheusMetricsAction() {
        super(NAME);
    }

    @Override
    public PrometheusMetricsResponse newResponse() {
        return new PrometheusMetricsResponse();
    }

    @Override
    public PrometheusRequestBuilder newRequestBuilder(ElasticsearchClient client) {
        return new PrometheusRequestBuilder(client, this);
    }
}
