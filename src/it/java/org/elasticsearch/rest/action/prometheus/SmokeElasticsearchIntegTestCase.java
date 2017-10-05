package org.elasticsearch.rest.action.prometheus;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoRequest;
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.plugin.prometheus.ESWithPrometheusExporterPluginITCase;

import java.util.Iterator;

import static org.elasticsearch.test.hamcrest.ElasticsearchAssertions.assertHitCount;

public class SmokeElasticsearchIntegTestCase extends ESWithPrometheusExporterPluginITCase {

    public void testClusterIsEmpty() throws Exception {
        SearchResponse searchResponse = client().prepareSearch().setQuery(QueryBuilders.matchAllQuery()).get();
        ensureGreen();
        assertHitCount(searchResponse, 0);

        ClusterHealthResponse clusterHealthResponse = client().admin().cluster().health(
                new ClusterHealthRequest()
        ).actionGet();

        this.logger.info("Nodes settings: \n{}", clusterHealthResponse.toString());
        assertTrue(clusterHealthResponse.getIndices().isEmpty());

        NodesInfoResponse nodeInfo = client().admin().cluster().nodesInfo(
                new NodesInfoRequest().clear()
        ).actionGet();

        this.logger.info("Nodes info: \n{}", nodeInfo.toString());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(nodeInfo.toString());
        JsonNode nodes = rootNode.path("nodes");
        Iterator<String> nodeIDs = nodes.fieldNames();
        while (nodeIDs.hasNext()) {
            String nodeID = nodeIDs.next();
            String httpAddress = nodes.path(nodeID).path("http_address").asText();

            this.logger.info("Node http address: {}", httpAddress);

            try (java.util.Scanner s = new java.util.Scanner(
                    new java.net.URL("http://" + httpAddress + RestPrometheusMetricsAction.PLUGIN_REST_ENDPOINT)
                            .openStream())) {
                System.out.println(s.useDelimiter("\\A").next());
            }

        }

    }
}
