package org.elasticsearch.action;

import org.elasticsearch.action.support.master.MasterNodeReadRequest;

public class PrometheusMetricsRequest extends MasterNodeReadRequest<PrometheusMetricsRequest> {

//    public PrometheusMetricsRequest() {
//    }

    @Override
    public ActionRequestValidationException validate() {
        return null;
    }
}
