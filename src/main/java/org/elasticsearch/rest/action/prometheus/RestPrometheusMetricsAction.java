package org.elasticsearch.rest.action.prometheus;

import org.compuscene.metrics.prometheus.PrometheusMetricsCollector;
import org.elasticsearch.action.PrometheusMetricsRequest;
import org.elasticsearch.action.PrometheusMetricsResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.rest.*;
import org.elasticsearch.rest.action.support.RestToXContentListener;

import static org.elasticsearch.rest.RestRequest.Method.GET;
import static org.elasticsearch.rest.RestStatus.INTERNAL_SERVER_ERROR;
import static org.elasticsearch.rest.RestStatus.OK;

import static org.elasticsearch.action.PrometheusMetricsAction.INSTANCE;

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
        logger.trace(String.format("Received request for Prometheus metrics from %s", request.getRemoteAddress().toString()));

        PrometheusMetricsRequest metrics = new PrometheusMetricsRequest();
        client.execute(INSTANCE, metrics, new RestToXContentListener<PrometheusMetricsResponse>(channel));

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
