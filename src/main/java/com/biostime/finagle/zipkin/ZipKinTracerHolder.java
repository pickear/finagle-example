package com.biostime.finagle.zipkin;

import com.twitter.finagle.stats.DefaultStatsReceiver;
import com.twitter.finagle.tracing.Tracer;
import com.twitter.finagle.zipkin.thrift.ZipkinTracer;

/**
 * @author Dylan
 * @date 2016/8/23.
 */
public final class ZipKinTracerHolder {


    private final static Tracer zipkinTracer = ZipkinTracer.mk("10.50.101.72",9410,DefaultStatsReceiver.self(),1.0f);

    public static Tracer holdeTracer(){
        return zipkinTracer;
    }
    private ZipKinTracerHolder(){}
}
