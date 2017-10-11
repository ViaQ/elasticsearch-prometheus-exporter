package org.elasticsearch.action;

import org.elasticsearch.cluster.ClusterName;
import org.elasticsearch.prometheus.PrometheusMetricsCollectorService;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.cluster.node.stats.NodesStatsRequest;
import org.elasticsearch.action.admin.cluster.node.stats.NodesStatsResponse;
import org.elasticsearch.action.support.ActionFilters;
import org.elasticsearch.action.support.HandledTransportAction;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.metadata.IndexNameExpressionResolver;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.threadpool.ThreadPool;
import org.elasticsearch.transport.TransportService;

public class TransportNodePrometheusMetricsAction extends HandledTransportAction<NodePrometheusMetricsRequest, NodePrometheusMetricsResponse> {

    protected final ClusterName clusterName;
    protected final PrometheusMetricsCollectorService prometheusService;
    protected final Client client;

    @Inject
    public TransportNodePrometheusMetricsAction(Settings settings, ClusterName clusterName, ThreadPool threadPool, Client client,
                                                TransportService transportService, ActionFilters actionFilters,
                                                IndexNameExpressionResolver indexNameExpressionResolver,
                                                PrometheusMetricsCollectorService prometheusService) {
        super(settings, NodePrometheusMetricsAction.NAME, threadPool, transportService, actionFilters, indexNameExpressionResolver, NodePrometheusMetricsRequest.class);
        this.client = client;
        this.clusterName = clusterName;
        this.prometheusService = prometheusService;
    }

    @Override
    protected void doExecute(NodePrometheusMetricsRequest request, ActionListener<NodePrometheusMetricsResponse> listener) {
        new AsyncAction(request, listener).start();
    }

    private class AsyncAction {

        private final ActionListener<NodePrometheusMetricsResponse> listener;
        private final ClusterHealthRequest healthRequest;
        private final NodesStatsRequest nodesStatsRequest;
        private ClusterHealthResponse clusterHealthResponse;

        private AsyncAction(NodePrometheusMetricsRequest request, ActionListener<NodePrometheusMetricsResponse> listener) {
            this.listener = listener;
            this.healthRequest = new ClusterHealthRequest();
            this.nodesStatsRequest = new NodesStatsRequest("_local").all();
//            this.nodesStatsRequest = new NodesStatsRequest("_local").clear(); // used for testing only
        }

        private void start() {
            client.admin().cluster().health(healthRequest, clusterHealthResponseActionListener);
        }

        private ActionListener<NodesStatsResponse> nodesStatsResponseActionListener = new ActionListener<NodesStatsResponse>() {
            @Override
            public void onResponse(NodesStatsResponse nodeStats) {
                listener.onResponse(buildResponse(clusterHealthResponse, nodeStats));
            }

            @Override
            public void onFailure(Throwable throwable) {
                listener.onFailure(new ElasticsearchException("Nodes stats request failed"));
            }
        };

        private ActionListener<ClusterHealthResponse> clusterHealthResponseActionListener = new ActionListener<ClusterHealthResponse>() {
            @Override
            public void onResponse(ClusterHealthResponse response) {
                clusterHealthResponse = response;
                client.admin().cluster().nodesStats(nodesStatsRequest, nodesStatsResponseActionListener);
            }

            @Override
            public void onFailure(Throwable throwable) {
                listener.onFailure(new ElasticsearchException("Cluster health request failed"));
            }
        };

//        protected String buildClusterHealthResponse(ClusterHealthResponse response) {
//            return response.toString();
//        }

//        protected String buildNodesStatsResponse(NodesStatsResponse response) {
//            return response.toString();
//        }

        protected NodePrometheusMetricsResponse buildResponse(ClusterHealthResponse clusterHealth, NodesStatsResponse nodesStats) {

//            String clusterHealthFormattedText = buildClusterHealthResponse(clusterHealth);
//            String nodesStatsFormattedText = buildNodesStatsResponse(nodesStats);
            NodePrometheusMetricsResponse response = new NodePrometheusMetricsResponse(clusterHealth, nodesStats.getAt(0));

            if (logger.isTraceEnabled()) {
                logger.trace("Return response: [{}]", response);
            }

            return response;
        }
    }
}
