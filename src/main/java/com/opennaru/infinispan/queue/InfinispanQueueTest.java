/*
 * Opennaru, Inc. http://www.opennaru.com/
 *  
 * Copyright 2014 Opennaru, Inc. and/or its affiliates.
 * All rights reserved by Opennaru, Inc.
 * 
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.opennaru.infinispan.queue;

import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;

/**
 * Infinispan Queue 테스트 프로그램
 * 
 * @author Junshik Jeon(service@opennaru.com, nameislocus@gmail.com)
 *
 */
public class InfinispanQueueTest {

	public static void main(String args[]) throws Exception {
		System.setProperty("java.net.preferIPv4Stack", "true");
		System.setProperty("jgroups.bind_addr", "127.0.0.1");
		System.setProperty("jgroups.udp.mcast_addr", "228.2.2.2");
		System.setProperty("jgroups.udp.mcast_port", "46655");

		EmbeddedCacheManager manager = new DefaultCacheManager(
				"infinispan-distribution.xml");
		Cache<String, Object> cache = manager.getCache("testCache");

		InfinispanQueue queue = new InfinispanQueue();

		queue.initialize(cache);
 
		queue.printInfo(cache);

		// 1000개 Offer
		long start = System.currentTimeMillis();
		for( int i = 0; i < 1000; i++ ) {
			queue.offer(cache, new InfinispanQueueElement("ABCDEF" + i));
		}
		long end = System.currentTimeMillis();
		System.out.println("queue offer 1000 time=" + (end - start) );

		queue.printInfo(cache);

		// 1개 Offer
		start = System.currentTimeMillis();
		queue.offer(cache, new InfinispanQueueElement("123456"));
		end = System.currentTimeMillis();
		System.out.println("queue offer 1 time=" + (end - start) );
		
		// 1000개 Poll
		start = System.currentTimeMillis();
		for( int i = 0; i < 1000; i++ ) {
			queue.poll(cache);
			//System.out.println(queue.poll(cache));
		}
		end = System.currentTimeMillis();
		System.out.println("queue poll 1000 time=" + (end - start) );
		
		// 1개 Poll
		System.out.println(queue.poll(cache));
		// 1개 Poll == null
		System.out.println(queue.poll(cache));

		queue.printInfo(cache);

	}
}
