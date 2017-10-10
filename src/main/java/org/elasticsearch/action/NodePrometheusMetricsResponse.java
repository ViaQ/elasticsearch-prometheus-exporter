package org.elasticsearch.action;

import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;

import java.io.IOException;

public class NodePrometheusMetricsResponse extends ActionResponse {

    private String clusterHealth;
    private String nodeStats;

    public NodePrometheusMetricsResponse() {
    }

    public NodePrometheusMetricsResponse(String clusterHealth, String nodesStats) {
        this.clusterHealth = clusterHealth;
        this.nodeStats = nodesStats;
    }

    public String getClusterHealth() {
        return this.clusterHealth;
    }

    public String getNodeStats() {
        return this.nodeStats;
    }

    public static NodePrometheusMetricsResponse readNodePrometheusMetrics(StreamInput in) throws IOException {
        NodePrometheusMetricsResponse metrics = new NodePrometheusMetricsResponse();
        metrics.readFrom(in);
        return metrics;
    }

    @Override
    public void readFrom(StreamInput in) throws IOException {
        super.readFrom(in);
        clusterHealth = in.readString();
        nodeStats = in.readString();
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        super.writeTo(out);
        out.writeString(clusterHealth);
        out.writeString(nodeStats);
    }
}
