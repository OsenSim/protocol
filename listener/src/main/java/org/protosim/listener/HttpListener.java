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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author Arpan Mukhopadhyay
 *
 */
public class HttpListener extends ProtocoListener {

	private static final Log logger = LogFactory.getLog(HttpListener.class);
	
	private ChannelFuture chf;
	private ServerBootstrap bootstrap;
	
	/**
	 * @param name
	 * @param addr
	 * @param port
	 * @param p
	 */
	public HttpListener(String name, String addr, int port) {
		super(name, addr, port, Protocol.HTTP);
		init();
	}

	/**
	 * Initialize the server
	 */
	private void init() {
		EventLoopGroup boos = new NioEventLoopGroup(1);
		EventLoopGroup woker = new NioEventLoopGroup(5);
		bootstrap = new ServerBootstrap();
		bootstrap.group(boos, woker);
		bootstrap.channel(NioServerSocketChannel.class);
		bootstrap.handler(new LoggingHandler(LogLevel.INFO));
	}
	
	/**
	 * @return the id
	 */
	public String getId() {
		return super.getId();
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return super.getName();
	}
	/**
	 * @return the addr
	 */
	public String getAddr() {
		return super.getAddr();
	}
	/**
	 * @return the port
	 */
	public int getPort() {
		return super.getPort();
	}
	
	@Override
	public void start() {
		try {
			chf = bootstrap.bind(getPort()).sync();
			if (logger.isInfoEnabled()) logger.info(getName() + " started on " + getAddr() + ":" + getPort());
			chf = chf.channel().closeFuture();
		} catch (Throwable e) {
			if (logger.isInfoEnabled()) logger.info("Unable to start " + getName() + ". Error: ", e);
		}
	}
	
	@Override
	public void stop() {
		bootstrap.config().childGroup().shutdownGracefully();
		bootstrap.config().group().shutdownGracefully();
	}
	
	@Override
	public void sendMessage(Object m) {
		// TODO Auto-generated method stub
		
	}
}
