package org.elasticsearch.plugin.prometheus;

import java.util.Collection;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.plugins.Plugin;
import org.elasticsearch.test.ESIntegTestCase;

public class ESWithPrometheusExporterPluginITCase extends ESIntegTestCase {

    @Override
    protected Collection<Class<? extends Plugin>> nodePlugins() {
        return pluginList(org.elasticsearch.plugin.prometheus.PrometheusExporterPlugin.class);
    }

    protected Settings nodeSettings(int nodeOrdinal) {
        Settings settings = Settings.builder()
                .put(super.nodeSettings(nodeOrdinal))
                .put("http.enabled", true)
                .build();
        return settings;
    }
}
