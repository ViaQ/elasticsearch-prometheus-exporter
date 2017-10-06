package org.elasticsearch.action;

import org.elasticsearch.action.support.nodes.BaseNodeResponse;
import org.elasticsearch.cluster.node.DiscoveryNode;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;

import java.io.IOException;

public class NodePrometheusMetrics extends BaseNodeResponse {

    private String metrics;

    NodePrometheusMetrics() {
    }

    public NodePrometheusMetrics(DiscoveryNode node, String metrics) {
        super(node);
        this.metrics = metrics;
    }

    public String getMetrics() {
        return this.metrics;
    }

    public static NodePrometheusMetrics readNodePrometheusMetrics(StreamInput in) throws IOException {
        NodePrometheusMetrics metrics = new NodePrometheusMetrics();
        metrics.readFrom(in);
        return metrics;
    }

    @Override
    public void readFrom(StreamInput in) throws IOException {
        super.readFrom(in);
        metrics = in.readString();
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        super.writeTo(out);
        out.writeString(metrics);
    }
}
