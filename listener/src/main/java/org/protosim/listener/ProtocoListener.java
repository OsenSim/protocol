/*
 * Copyright 2020 original author(s).
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.protosim.listener;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * @author Arpan Mukhopadhyay
 *
 */
public abstract class ProtocoListener implements Listener {

	private String id;
	private String name;
	private String addr;
	private int port;
	
	/**
	 * 
	 */
	public ProtocoListener(String name, String addr, int port, Protocol p) {
		this.id = RandomStringUtils.randomAlphanumeric(10);
		this.name = StringUtils.isBlank(name) ? p.getName() + "-srv-" + this.id : name;
		this.addr = StringUtils.isBlank(addr) ? "0.0.0.0" : addr;
		this.port = port;
	}

	/**
	 * Return the unique id of the {@code ProcotolServer}
	 * @return the id
	 */
	public String getId() {
		return id;
	}
	
	/**
	 * Return the reference name of the {@code ProtocolServer}
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return the port of the {@code ProtocolServer}
	 * @return the port
	 */
	public int getPort() {
		return port;
	}

	/**
	 * Return the address of the {@code ProtocolServer}
	 * @return the addr
	 */
	public String getAddr() {
		return addr;
	}

	/**
	 * Initiate a message transmission from server
	 * over an active channel
	 * @param m the message
	 */
	public abstract void sendMessage(Object m);
}
