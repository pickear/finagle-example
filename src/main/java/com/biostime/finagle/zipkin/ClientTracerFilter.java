package com.biostime.finagle.zipkin;

import com.twitter.finagle.Service;
import com.twitter.finagle.SimpleFilter;
import com.twitter.finagle.thrift.ThriftClientRequest;
import com.twitter.finagle.tracing.Trace;
import com.twitter.finagle.tracing.Tracer;
import com.twitter.util.Function0;
import com.twitter.util.Future;

/**
 * 
 * @Project:finagle-zipkin 
 * @Title: ClientTracerFilter.java
 * @Package com.mama100.common.finagle.zipkin
 * @Description: TODO
 * @See:
 * @author: hg
 * @Email:  huangguang@biostime.com
 * @modified: 
 * @date 2015年7月24日 下午3:54:40
 * @CopyEdition:广州合爱信息技术版权所有
 * @since V1.0
 */
public class ClientTracerFilter extends SimpleFilter<ThriftClientRequest, byte[]>{

	private Tracer tracer;
	private String serviceName;

	
	public ClientTracerFilter(Tracer _tracer,String _serviceName){
		this.tracer = _tracer;
		this.serviceName = _serviceName;
	}
	
	@Override
	public Future<byte[]> apply(final ThriftClientRequest request,
			final Service<ThriftClientRequest, byte[]> service) {
		
		return Trace.unwind(new Function0<Future<byte[]>>(){
			@Override
			public Future<byte[]> apply()
			{
				//  Trace.pushTracer(tracer);
			   // Trace.recordBinary("finagle.version", Init.finagleVersion());
			    //Trace.recordServiceName(serviceName);
				  //Trace.pushTracerAndSetNextId(tracer, false);
				   // 
				  Trace.pushTracerAndSetNextId(tracer, false);
				  Trace.recordServiceName(serviceName);
				  Trace.recordRpc(serviceName);
				    System.out.println(serviceName);
				    //System.out.print("request:"+new String(request));
				    Trace.record("test!");
				    //Trace.record(new Annotation.ServerSend());
				    //Trace.record(new Annotation.ServerRecv());
				    Trace.recordBinary("keys", "123");
				return service.apply(request);
			}});
	}


}


