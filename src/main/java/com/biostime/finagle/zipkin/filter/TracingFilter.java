package com.biostime.finagle.zipkin.filter;

import com.twitter.finagle.Service;
import com.twitter.finagle.SimpleFilter;
import com.twitter.finagle.tracing.Trace;
import com.twitter.util.Future;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TracingFilter extends SimpleFilter<byte[], byte[]>{

    private final static Logger log = LoggerFactory.getLogger(TracingFilter.class);

	@Override
	public Future<byte[]> apply(final byte[] request, final Service<byte[], byte[]> service)
	{

        log.info("[server filter]traceId : {},spanId : {}", Trace.id().traceId().toString(), Trace.id().spanId().toString());

        /*Trace.letTracerAndNextId(tracer,true,new Function0<Object>(){
            @Override
            public Object apply() {
                return null;
            }
        });*/
        return service.apply(request);
	}

}
