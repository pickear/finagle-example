package com.biostime.finagle.zipkin;

import com.twitter.finagle.Service;
import com.twitter.finagle.SimpleFilter;
import com.twitter.finagle.tracing.Trace;
import com.twitter.finagle.tracing.Tracer;
import com.twitter.util.Function0;
import com.twitter.util.Future;

import java.net.InetSocketAddress;

public class ServerTracingFilter  extends SimpleFilter<byte[], byte[]>
{
	private Tracer tracer;
	private String serviceName;
	private InetSocketAddress endpoint;
	public ServerTracingFilter(Tracer tracer, String label, InetSocketAddress endpoint)
	{
		this.tracer = tracer;
		this.serviceName = label;
		this.endpoint = endpoint;
	}

	@Override
	public Future<byte[]> apply(final byte[] request, final Service<byte[], byte[]> service)
	{
		return Trace.unwind(new Function0<Future<byte[]>>(){
			@Override
			public Future<byte[]> apply()
			{
				Trace.pushTracerAndSetNextId(tracer, false);
				
			    //Trace.recordBinary("finagle.version", Init.finagleVersion());
			    //Trace.recordServiceName(serviceName);
			    //Trace.recordServerAddr(endpoint);
			    Trace.recordLocalAddr(endpoint);
			    //System.out.print("request:"+new String(request));
			   // Trace.record("test!");
			    //Trace.record(new Annotation.ServerRecv());
			    return service.apply(request);
			    //Trace.record(new Annotation.ServerSend());
			    //return rep;
			}});
	}

}
