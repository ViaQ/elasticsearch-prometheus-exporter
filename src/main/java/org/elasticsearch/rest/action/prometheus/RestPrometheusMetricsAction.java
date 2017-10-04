package org.elasticsearch.rest.action.prometheus;

import org.compuscene.metrics.prometheus.PrometheusMetricsCollector;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.ESLoggerFactory;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.settings.SettingsFilter;
import org.elasticsearch.rest.*;

import static org.elasticsearch.rest.RestRequest.Method.GET;
import static org.elasticsearch.rest.RestStatus.INTERNAL_SERVER_ERROR;
import static org.elasticsearch.rest.RestStatus.OK;


public class RestPrometheusMetricsAction extends BaseRestHandler {

    final static String PLUGIN_REST_ENDPOINT = "/_prometheus/metrics";

    private final static ESLogger logger = ESLoggerFactory.getLogger(RestPrometheusMetricsAction.class.getSimpleName());

    private PrometheusMetricsCollector collector;

    @Inject
    public RestPrometheusMetricsAction(Settings settings, Client client, RestController controller, SettingsFilter settingsFilter) {
        super(settings, controller, client);
        controller.registerHandler(GET, PLUGIN_REST_ENDPOINT, this);
    }

    @Override
    public void handleRequest(final RestRequest request, final RestChannel channel, final Client client) {
        logger.trace(String.format("Received request for Prometheus metrics from %s", request.getRemoteAddress().toString()));

        if (collector == null)
            collector = new PrometheusMetricsCollector(client);

        collector.updateMetrics();

        try {
            channel.sendResponse(new BytesRestResponse(OK, collector.getCatalog().toTextFormat()));
        } catch (java.io.IOException e) {
            channel.sendResponse(new BytesRestResponse(INTERNAL_SERVER_ERROR, ""));
        }
    }
}
