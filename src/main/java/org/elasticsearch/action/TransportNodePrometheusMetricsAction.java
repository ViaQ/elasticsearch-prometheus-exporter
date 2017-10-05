package org.elasticsearch.action;

import org.elasticsearch.action.support.ActionFilters;
import org.elasticsearch.action.support.HandledTransportAction;
import org.elasticsearch.cluster.ClusterName;
import org.elasticsearch.cluster.ClusterService;
import org.elasticsearch.cluster.metadata.IndexNameExpressionResolver;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;

public class TransportNodePrometheusMetricsAction extends HandledTransportAction<NodePrometheusMetricsRequest, NodePrometheusMetricsResponse> {

//    private final Client client;

    protected final ClusterName clusterName;
    protected final ClusterService clusterService;

    @Inject
    public TransportNodePrometheusMetricsAction(Settings settings, ClusterName clusterName, ThreadPool threadPool,
                                            /*Client client,*/
                                            ClusterService clusterService, TransportService transportService, ActionFilters actionFilters,
                                                IndexNameExpressionResolver indexNameExpressionResolver) {
        super(settings, NodePrometheusMetricsAction.NAME, threadPool, transportService, actionFilters, indexNameExpressionResolver, NodePrometheusMetricsRequest.class);
//        this.client = client;
        this.clusterName = clusterName;
        this.clusterService = clusterService;
    }

    @Override
    protected void doExecute(NodePrometheusMetricsRequest request, ActionListener<NodePrometheusMetricsResponse> listener) {
        new AsyncAction(request, listener).start();
    }

    private class AsyncAction {

        private final NodePrometheusMetricsRequest request;
        private final ActionListener<NodePrometheusMetricsResponse> listener;

        private AsyncAction(NodePrometheusMetricsRequest request, ActionListener<NodePrometheusMetricsResponse> listener) {
            this.request = request;
            this.listener = listener;
        }

        private void start() {
//            if (logger.isTraceEnabled()) {
                logger.info("Starting action");
//            }
            listener.onResponse(buildResponse());
        }

        protected NodePrometheusMetricsResponse buildResponse() {
//            if (logger.isTraceEnabled()) {
                logger.info("Return response");
//            }
            return new NodePrometheusMetricsResponse();
        }
    }
}
