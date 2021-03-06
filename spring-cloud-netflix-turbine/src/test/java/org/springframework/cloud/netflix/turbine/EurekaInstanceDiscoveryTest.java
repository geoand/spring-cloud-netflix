/*
 * Copyright 2013-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.cloud.netflix.turbine;

import static org.mockito.Mockito.*;

import com.netflix.discovery.EurekaClient;
import org.junit.Test;

import com.netflix.appinfo.InstanceInfo;

import static org.junit.Assert.assertEquals;

/**
 * @author Spencer Gibb
 */
public class EurekaInstanceDiscoveryTest {

	@Test
	public void testGetClusterName() {
		EurekaClient eurekaClient = mock(EurekaClient.class);
		String appName = "testAppName";
		EurekaInstanceDiscovery discovery = new EurekaInstanceDiscovery(
				new TurbineProperties(), eurekaClient);
		InstanceInfo instanceInfo = InstanceInfo.Builder.newBuilder().setAppName(appName)
				.build();
		String clusterName = discovery.getClusterName(instanceInfo);
		assertEquals("clusterName is wrong", appName.toUpperCase(), clusterName);
	}

	@Test
	public void testGetClusterNameCustomExpression() {
		EurekaClient eurekaClient = mock(EurekaClient.class);
		TurbineProperties turbineProperties = new TurbineProperties();
		turbineProperties.setClusterNameExpression("aSGName");
		EurekaInstanceDiscovery discovery = new EurekaInstanceDiscovery(turbineProperties, eurekaClient);
		String asgName = "myAsgName";
		InstanceInfo instanceInfo = InstanceInfo.Builder.newBuilder()
				.setAppName("testApp").setASGName(asgName).build();
		String clusterName = discovery.getClusterName(instanceInfo);
		assertEquals("clusterName is wrong", asgName, clusterName);
	}

	@Test
	public void testGetClusterNameInstanceMetadataMapExpression() {
		EurekaClient eurekaClient = mock(EurekaClient.class);
		TurbineProperties turbineProperties = new TurbineProperties();
		turbineProperties.setClusterNameExpression("metadata['cluster']");
		EurekaInstanceDiscovery discovery = new EurekaInstanceDiscovery(turbineProperties, eurekaClient);
		String metadataProperty = "myCluster";
		InstanceInfo instanceInfo = InstanceInfo.Builder.newBuilder()
				.setAppName("testApp").add("cluster", metadataProperty).build();
		String clusterName = discovery.getClusterName(instanceInfo);
		assertEquals("clusterName is wrong", metadataProperty, clusterName);
	}

}
