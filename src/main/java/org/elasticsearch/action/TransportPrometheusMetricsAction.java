package org.elasticsearch.action;

import org.elasticsearch.action.support.ActionFilters;
import org.elasticsearch.action.support.HandledTransportAction;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.metadata.IndexNameExpressionResolver;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;

public class TransportPrometheusMetricsAction extends HandledTransportAction<PrometheusMetricsRequest, PrometheusMetricsResponse> {

    private final Client client;

    @Inject
    public TransportPrometheusMetricsAction(Settings settings, ThreadPool threadPool, Client client,
                                            TransportService transportService, ActionFilters actionFilters,
                                            IndexNameExpressionResolver indexNameExpressionResolver) {
        super(settings, PrometheusMetricsAction.NAME, threadPool, transportService, actionFilters, indexNameExpressionResolver, PrometheusMetricsRequest.class);
        this.client = client;
    }

    @Override
    protected void doExecute(PrometheusMetricsRequest request, ActionListener<PrometheusMetricsResponse> actionListener) {

    }
}
