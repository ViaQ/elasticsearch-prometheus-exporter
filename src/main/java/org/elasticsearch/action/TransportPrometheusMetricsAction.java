package org.elasticsearch.action;

import org.elasticsearch.action.support.ActionFilters;
import org.elasticsearch.action.support.HandledTransportAction;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.ClusterName;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.metadata.IndexNameExpressionResolver;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;

public class TransportPrometheusMetricsAction extends HandledTransportAction<PrometheusMetricsRequest, PrometheusMetricsResponse> {

//    private final Client client;

    protected final ClusterName clusterName;
    protected final ClusterService clusterService;

    @Inject
    public TransportPrometheusMetricsAction(Settings settings, ClusterName clusterName, ThreadPool threadPool,
                                            /*Client client,*/
                                            ClusterService clusterService, TransportService transportService, ActionFilters actionFilters,
                                            IndexNameExpressionResolver indexNameExpressionResolver) {
        super(settings, PrometheusMetricsAction.NAME, threadPool, transportService, actionFilters, indexNameExpressionResolver, PrometheusMetricsRequest.class);
//        this.client = client;
        this.clusterName = clusterName;
        this.clusterService = clusterService;
    }

    @Override
    protected void doExecute(PrometheusMetricsRequest request, ActionListener<PrometheusMetricsResponse> listener) {
        new AsyncAction(request, listener).start();
    }

    private class AsyncAction {

        private AsyncAction(PrometheusMetricsRequest request, ActionListener<PrometheusMetricsResponse> listener) {

        }

        private void start() {
            logger.info("We are here!");
        }
    }
}
