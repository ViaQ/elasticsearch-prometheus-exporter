package org.elasticsearch.rest.action.prometheus;

import org.compuscene.metrics.prometheus.PrometheusMetricsCollector;
import org.elasticsearch.action.NodePrometheusMetrics;
import org.elasticsearch.action.NodePrometheusMetricsRequest;
import org.elasticsearch.action.NodePrometheusMetricsResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.*;
import org.elasticsearch.rest.action.support.RestResponseListener;
import org.elasticsearch.rest.action.support.RestToXContentListener;

import static org.elasticsearch.rest.RestRequest.Method.GET;

import static org.elasticsearch.action.NodePrometheusMetricsAction.INSTANCE;

public class RestPrometheusMetricsAction extends BaseRestHandler {

    final static String PLUGIN_REST_ENDPOINT = "/_prometheus/metrics";

    private PrometheusMetricsCollector collector;

    @Inject
    public RestPrometheusMetricsAction(Settings settings, RestController controller, Client client) {
        super(settings, controller, client);
        controller.registerHandler(GET, PLUGIN_REST_ENDPOINT, this);
    }

    @Override
    public void handleRequest(final RestRequest request, final RestChannel channel, final Client client) {
//        if (logger.isTraceEnabled()) {
            logger.info(String.format("Received request for Prometheus metrics from %s", request.getRemoteAddress().toString()));
//        }

        NodePrometheusMetricsRequest metrics = new NodePrometheusMetricsRequest();
        client.execute(INSTANCE, metrics, new RestResponseListener<NodePrometheusMetricsResponse>(channel) {

            @Override
            public RestResponse buildResponse(NodePrometheusMetricsResponse response) throws Exception {
                StringBuilder sb = new StringBuilder();
//                for (NodePrometheusMetrics node : response) {
                    sb.append("# Prometheus metrics").append("\n");
//                    sb.append(node.getMetrics().toString()).append("\n");
//                }
                return new BytesRestResponse(RestStatus.OK, sb.toString());
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
