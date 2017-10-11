package org.elasticsearch.rest.action.prometheus;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Requests;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.plugin.prometheus.PrometheusExporterPlugin;
import org.elasticsearch.plugin.prometheus.PrometheusExporterPluginIntegTestCase;
import org.junit.Test;

import java.io.IOException;
import java.util.Iterator;

import static org.elasticsearch.test.hamcrest.ElasticsearchAssertions.assertHitCount;

public class SmokeElasticsearchIntegTestCase extends PrometheusExporterPluginIntegTestCase {

    @Test
    public void clusterShouldBeEmpty() throws Exception {
        SearchResponse searchResponse = client().prepareSearch().setQuery(QueryBuilders.matchAllQuery()).get();
        ensureGreen();
        assertHitCount(searchResponse, 0);
    }

    @Test
    public void prometheusPluginShouldBeInstalledOnEachNode() throws IOException {

        ensureGreen();
        int expectedNumberOfPrometheusPlugins = getNumberOfClusterNodes();
        int numberOfPrometheusPlugins = 0;

        NodesInfoResponse response = client().admin().cluster().nodesInfo(
                Requests.nodesInfoRequest().clear().plugins(true)
        ).actionGet();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response.toString());
        JsonNode nodes = rootNode.path(NODES);
        Iterator<String> nodeIDs = nodes.fieldNames();

        // make sure there is at least one node
        assertTrue(nodeIDs.hasNext());

        while (nodeIDs.hasNext()) {
            String nodeID = nodeIDs.next();
            Iterator<JsonNode> plugins = nodes.path(nodeID).path(PLUGINS).elements();
            // make sure each node has some plugins installed
            assertTrue(plugins.hasNext());
            while (plugins.hasNext()) {
                JsonNode plugin = plugins.next();
                // iterate util first prometheus plugin is found
                if (plugin.path(NAME).asText().equals(PrometheusExporterPlugin.NAME)) {
                    numberOfPrometheusPlugins++;
                    // prevent incorrect counting in case there would be more plugins installed on single node by error
                    break;
                }
            }
        }

        // each node is expected to have this plugin installed (no matter if it is client, data or whatever type)
        assertEquals(expectedNumberOfPrometheusPlugins, numberOfPrometheusPlugins);
    }

    @Test
    public void eachNodeCanRespondToPrometheusRESTCall() throws IOException {
        NodesInfoResponse nodeInfo = client().admin().cluster().nodesInfo(
                Requests.nodesInfoRequest().clear()
        ).actionGet();

        this.logger.info("Nodes info: \n{}", nodeInfo.toString());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(nodeInfo.toString());
        JsonNode nodes = rootNode.path(NODES);
        Iterator<String> nodeIDs = nodes.fieldNames();
        while (nodeIDs.hasNext()) {
            String nodeID = nodeIDs.next();
            String httpAddress = nodes.path(nodeID).path(HTTP_ADDRESS).asText();

            this.logger.info("Node http address: {}", httpAddress);
            String url = "http://" + httpAddress + RestPrometheusMetricsAction.PLUGIN_REST_ENDPOINT;

            try (java.util.Scanner s = new java.util.Scanner(new java.net.URL(url).openStream())) {
                System.out.println(s.useDelimiter("\\A").next());
            }
        }
    }
}
