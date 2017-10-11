package org.elasticsearch.rest.action.prometheus;

import org.compuscene.metrics.prometheus.PrometheusMetricsCollector;
import org.elasticsearch.action.NodePrometheusMetricsResponse;
import org.elasticsearch.action.NodePrometheusMetricsRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.*;
import org.elasticsearch.rest.action.support.RestResponseListener;

import static org.elasticsearch.rest.RestRequest.Method.GET;

import static org.elasticsearch.action.NodePrometheusMetricsAction.INSTANCE;

public class RestPrometheusMetricsAction extends BaseRestHandler {

    final static String PLUGIN_REST_ENDPOINT = "/_prometheus/metrics";

    @Inject
    public RestPrometheusMetricsAction(Settings settings, RestController controller, Client client) {
        super(settings, controller, client);
        controller.registerHandler(GET, PLUGIN_REST_ENDPOINT, this);
    }

    @Override
    public void handleRequest(final RestRequest request, final RestChannel channel, final Client client) {
        if (logger.isTraceEnabled()) {
            logger.trace(String.format("Received request for Prometheus metrics from %s", request.getRemoteAddress().toString()));
        }

        NodePrometheusMetricsRequest metricsRequest = new NodePrometheusMetricsRequest();
        client.execute(INSTANCE, metricsRequest, new RestResponseListener<NodePrometheusMetricsResponse>(channel) {

            @Override
            public RestResponse buildResponse(NodePrometheusMetricsResponse response) throws Exception {

                String clusterName = response.getClusterHealth().getClusterName();
                // Why not node ID?
                // See https://github.com/vvanholl/elasticsearch-prometheus-exporter/issues/63
                String nodeId = response.getNodeStats().getNode().getName(); // .getId();

                logger.info("####### Building prometheus collector for: [{}], [{}]", clusterName, nodeId);
                PrometheusMetricsCollector collector = new PrometheusMetricsCollector(clusterName, nodeId);
                collector.updateMetrics(response.getClusterHealth(), response.getNodeStats());

                return new BytesRestResponse(RestStatus.OK, collector.getCatalog().toTextFormat());
            }
        });

        /*
        if (collector == null)
            collector = new PrometheusMetricsCollector(client);

        collector.updateMetrics();

        try {
            channel.sendResponse(new BytesRestResponse(OK, collector.getCatalog().toTextFormat()));
        } catch (java.io.IOException e) {
            channel.sendResponse(new BytesRestResponse(INTERNAL_SERVER_ERROR, ""));
        }
        */
    }
}
