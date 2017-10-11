package org.elasticsearch.prometheus;

import org.compuscene.metrics.prometheus.PrometheusMetricsCollector;
import org.elasticsearch.cluster.ClusterName;
import org.elasticsearch.common.component.AbstractLifecycleComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;

import static org.elasticsearch.common.settings.Settings.Builder.EMPTY_SETTINGS;

public class PrometheusMetricsCollectorService extends AbstractLifecycleComponent<PrometheusMetricsCollectorService> {

    private PrometheusMetricsCollector collector;

    public PrometheusMetricsCollectorService() {
        super(EMPTY_SETTINGS);
    }

    @Inject
    public PrometheusMetricsCollectorService(final Settings settings, ClusterName clusterName) {
        super(settings);
        // TODO: get nodeId
        this.collector = new PrometheusMetricsCollector(clusterName.toString(), nodeName(),"nodeId");
    }

//    public String getMetrics() {
//        return collector.toString();
//    }

//    public void updateMetrics() {
//        collector.updateMetrics();
//    }

    @Override
    protected void doStart() {
    }

    @Override
    protected void doStop() {

    }

    @Override
    protected void doClose() {
        // do we need to release any resources associated with Prometheus collector?
    }
}
