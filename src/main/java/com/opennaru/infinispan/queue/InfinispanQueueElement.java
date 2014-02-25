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

import java.io.Serializable;

/**
 * 
 * @author Junshik Jeon(service@opennaru.com, nameislocus@gmail.com)
 *
 */
public class InfinispanQueueElement implements Serializable {
	private static final long serialVersionUID = 1L;
	private Object message;
	private String nextId;

	public InfinispanQueueElement() {

	}
 
	public InfinispanQueueElement(Object message) {
		super();
		this.message = message;
	}

	public Object getMessage() {
		return message;
	}

	public void setMessage(Object message) {
		this.message = message;
	}

	public String getNextId() {
		return nextId;
	}

	public void setNextId(String nextId) {
		this.nextId = nextId;
	}

	@Override
	public String toString() {
		return "JDGQueue [message=" + message + ", nextId=" + nextId + "]";
	}
	
}
