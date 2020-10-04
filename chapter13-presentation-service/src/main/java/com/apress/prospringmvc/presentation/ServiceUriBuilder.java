/*
Freeware License, some rights reserved

Copyright (c) 2020 Iuliana Cosmina

Permission is hereby granted, free of charge, to anyone obtaining a copy 
of this software and associated documentation files (the "Software"), 
to work with the Software within the limits of freeware distribution and fair use. 
This includes the rights to use, copy, and modify the Software for personal use. 
Users are also allowed and encouraged to submit corrections and modifications 
to the Software for the benefit of other users.

It is not allowed to reuse,  modify, or redistribute the Software for 
commercial use in any way, or for a user's educational materials such as books 
or blog articles without prior permission from the copyright holder. 

The above copyright notice and this permission notice need to be included 
in all copies or substantial portions of the software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS OR APRESS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/
package com.apress.prospringmvc.presentation;

/**
 * Created by Iuliana Cosmina on 01/09/2020
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.Response;
import org.springframework.cloud.client.loadbalancer.reactive.ReactiveLoadBalancer;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;

@Component
public class ServiceUriBuilder {
	private static Logger logger = LoggerFactory.getLogger(ServiceUriBuilder.class);
	final ReactiveLoadBalancer.Factory<ServiceInstance>  loadBalancerfactory;

	public ServiceUriBuilder(ReactiveLoadBalancer.Factory<ServiceInstance> loadBalancerfactory) {
		this.loadBalancerfactory = loadBalancerfactory;
	}

	@PostConstruct
	public void getServiceURIs(){
		Flux.just("technews-service","newreleases-service","book-service","account-service")
				.map(serviceId -> {
					ReactiveLoadBalancer<ServiceInstance> loadBalancer = loadBalancerfactory.getInstance(serviceId);
					Flux<Response<ServiceInstance>> chosen = Flux.from(loadBalancer.choose());
					chosen.map(responseServiceInstance -> {
								ServiceInstance server = responseServiceInstance.getServer();
								var url = "http://" + server.getHost() + ':' + server.getPort();
								logger.debug("--->> {} : {}", serviceId, url);
								return url;
							}).subscribe();
					return serviceId;
				}).subscribe();
	}
}
