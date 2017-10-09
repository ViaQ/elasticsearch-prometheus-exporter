package org.elasticsearch.prometheus;

import org.elasticsearch.common.inject.AbstractModule;

// TODO: Do we need this module if we have the service?
public class PrometheusMetricsCollectionModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(PrometheusMetricsCollectorService.class).asEagerSingleton();
    }
}
