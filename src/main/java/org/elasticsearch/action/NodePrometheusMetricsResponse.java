/*
 * Copyright [2016] [Vincent VAN HOLLEBEKE]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.elasticsearch.action;

import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.action.admin.cluster.node.stats.NodeStats;
import org.elasticsearch.action.admin.indices.stats.IndicesStatsResponse;
import org.elasticsearch.action.admin.indices.stats.PackageAccessHelper;
import org.elasticsearch.action.admin.indices.stats.ShardStats;
import org.elasticsearch.action.support.DefaultShardOperationFailedException;
import org.elasticsearch.action.support.broadcast.BroadcastResponse;
import org.elasticsearch.action.support.broadcast.BroadcastShardResponse;
import org.elasticsearch.common.Nullable;
import org.elasticsearch.common.io.stream.StreamInput;
import org.elasticsearch.common.io.stream.StreamOutput;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Action response class for Prometheus Exporter plugin.
 */
public class NodePrometheusMetricsResponse extends ActionResponse {
    private ClusterHealthResponse clusterHealth;
    private NodeStats nodeStats;
    @Nullable
    private IndicesStatsResponse indicesStats;

    public NodePrometheusMetricsResponse() {
    }

    public NodePrometheusMetricsResponse(ClusterHealthResponse clusterHealth, NodeStats nodesStats,
                                         @Nullable IndicesStatsResponse indicesStats) {
        this.clusterHealth = clusterHealth;
        this.nodeStats = nodesStats;
        this.indicesStats = indicesStats;
    }

    public  NodePrometheusMetricsResponse readNodePrometheusMetrics(StreamInput in) throws IOException {
        NodePrometheusMetricsResponse metrics = new NodePrometheusMetricsResponse();
        metrics.readFrom(in);
        return metrics;
    }

    public ClusterHealthResponse getClusterHealth() {
        return this.clusterHealth;
    }

    public NodeStats getNodeStats() {
        return this.nodeStats;
    }

    @Nullable
    public IndicesStatsResponse getIndicesStats() {
        return this.indicesStats;
    }

    @Override
    public void readFrom(StreamInput in) throws IOException {
        super.readFrom(in);
        clusterHealth = ClusterHealthResponse.readResponseFrom(in);
        nodeStats = NodeStats.readNodeStats(in);
        BroadcastResponse br = new BroadcastResponse();
        br.readFrom(in);
        ShardStats[] ss = in.readArray(ShardStats::readShardStats, (size) -> new ShardStats[size]);
        indicesStats = PackageAccessHelper.createIndicesStatsResponse(
                ss, br.getTotalShards(), br.getSuccessfulShards(), br.getFailedShards(),
                Arrays.asList(br.getShardFailures())
        );
    }

    @Override
    public void writeTo(StreamOutput out) throws IOException {
        super.writeTo(out);
        clusterHealth.writeTo(out);
        nodeStats.writeTo(out);
        if (indicesStats != null) {
            //indicesStats.writeTo(out);
            ((BroadcastResponse) indicesStats).writeTo(out);
            out.writeArray(indicesStats.getShards());
        }
    }
}
