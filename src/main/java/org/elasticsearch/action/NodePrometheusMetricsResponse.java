package org.elasticsearch.action;

import org.elasticsearch.action.support.nodes.BaseNodesResponse;
import org.elasticsearch.cluster.ClusterName;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;

import java.io.IOException;

public class NodePrometheusMetricsResponse extends BaseNodesResponse<NodePrometheusMetrics> {

    NodePrometheusMetricsResponse() {
    }

    public NodePrometheusMetricsResponse(ClusterName clusterName, NodePrometheusMetrics[] nodes) {
        super(clusterName, nodes);
    }

    @Override
    public void readFrom(StreamInput in) throws IOException {
        super.readFrom(in);
        nodes = new NodePrometheusMetrics[in.readVInt()];
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = NodePrometheusMetrics.readNodePrometheusMetrics(in);
        }
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        super.writeTo(out);
        out.writeVInt(nodes.length);
        for (NodePrometheusMetrics node : nodes) {
            node.writeTo(out);
        }
    }

}
