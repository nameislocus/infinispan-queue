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

import javax.transaction.TransactionManager;

import org.infinispan.Cache;
import org.infinispan.CacheImpl;

/**
 * Infinispan의 Key/Value를 이용한 Linked List형태의 Queue 자료구조 구현 
 *  
 * @author Junshik Jeon(service@opennaru.com, nameislocus@gmail.com)
 */
public class InfinispanQueue {
	// Queue의 Tail을 가리키는 포인터
	private static String QUEUE_TAIL = "QUEUE_TAIL";
	// Queue의 Head를 가리키는 포인터
	private static String QUEUE_HEAD = "QUEUE_HEAD";
	// Queue의 크기
	private static String QUEUE_SIZE = "QUEUE_SIZE";
 	
	/**
	 * Queue 포인터 초기화
	 * 
	 * @param cache
	 */
	public void initialize(Cache<String, Object> cache) {
		String key = UUIDFactory.generateGuid();
		
		if( cache.get(QUEUE_TAIL) == null )
			cache.put(QUEUE_TAIL, key);
		
		if( cache.get(QUEUE_HEAD) == null )
			cache.put(QUEUE_HEAD, key);
		
		if( cache.get(QUEUE_SIZE) == null )
			cache.put(QUEUE_SIZE, 0);
	}
	
	/**
	 * Queue에 Element Offer
	 * 
	 * @param cache
	 * @param element
	 * @return
	 */
	public boolean offer(Cache<String, Object> cache, InfinispanQueueElement element) {
		// 다음 Queue Element를 가리킬 포인터를 생성
		String nextKey = UUIDFactory.generateGuid();
		element.setNextId(nextKey);
		
		// 트랜잭션 사용
		@SuppressWarnings("rawtypes")
		TransactionManager tm = ((CacheImpl) cache).getAdvancedCache().getTransactionManager();
		
		try {
		    tm.begin();
		    // Queue의 Tail에 Element 추가
			String tailKey = (String) cache.get(QUEUE_TAIL);
			cache.put(tailKey, element);
			
			cache.put(QUEUE_TAIL, nextKey);
			cache.put(QUEUE_SIZE, (Integer) cache.get(QUEUE_SIZE) + 1);
		    tm.commit(); 
		} catch (Exception e) {
		    if (tm != null) {
		        try {
		            tm.rollback();
		        } catch (Exception e1) {}
		    } 
		}		
		
		return false;
	}
	
	/**
	 * Queue의 Element Poll
	 * 
	 * @param cache
	 * @return
	 */
	public InfinispanQueueElement poll(Cache<String, Object> cache) {
		
		// 트랜잭션 사용
		@SuppressWarnings("rawtypes")
		TransactionManager tm = ((CacheImpl) cache).getAdvancedCache().getTransactionManager();
		InfinispanQueueElement element = null;
		
		try {
		    tm.begin();
		    // Queue의 Head의 포인터의 값을 get
			String key = (String) cache.get(QUEUE_HEAD);
			element = (InfinispanQueueElement) cache.get(key);
			
			// Queue Head 포인터값 Update & 제거
			if( element != null ) {
				cache.put(QUEUE_HEAD, element.getNextId());
				cache.put(QUEUE_SIZE, (Integer) cache.get(QUEUE_SIZE) - 1);
				cache.remove(key);
			}
		    tm.commit(); 
		} catch (Exception e) {
		    if (tm != null) {
		        try {
		            tm.rollback();
		        } catch (Exception e1) {}
		    } 
		}		

		return element;
	}
	
	/**
	 * Queue에 들어있는 데이터 출력
	 * 
	 * @param cache
	 */
	public void printInfo(Cache<String, Object> cache) {
		System.out.println("========= Queue Info =========");
		System.out.println("Queue Size=" + (Integer) cache.get(QUEUE_SIZE));
		String headKey = (String) cache.get(QUEUE_HEAD);
		System.out.println("Queue Head=" + headKey);
		System.out.println("Queue Tail=" + cache.get(QUEUE_TAIL));
		
		InfinispanQueueElement element = new InfinispanQueueElement();
		while(true) {
			element = (InfinispanQueueElement) cache.get(headKey);
			if( element == null ) {
				break;
			}
			System.out.println("Queue> " + element);
			headKey = element.getNextId();
		}
		System.out.println("========= Queue END =========");
		
	}
}
