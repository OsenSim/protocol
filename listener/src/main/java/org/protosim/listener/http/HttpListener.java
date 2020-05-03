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
package org.protosim.listener.http;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.protosim.listener.ProtocoListener;
import org.protosim.listener.Protocol;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;

/**
 * @author Arpan Mukhopadhyay
 *
 */
public class HttpListener extends ProtocoListener {

	private static final Log logger = LogFactory.getLog(HttpListener.class);
	
	private ChannelFuture chf;
	private ServerBootstrap bootstrap;
	private boolean sslEnabled;
	private String cert;
	private String key;
	private String pass;
	
	private SslContext sslCtx = null;
	
	/**
	 * 
	 * @param name
	 * @param addr
	 * @param port
	 */
	public HttpListener(String name, String addr, int port) {
		super(name, addr, port, Protocol.HTTP);
	}
	
	/**
	 * 
	 * @param name
	 * @param addr
	 * @param port
	 * @param sslEnabled
	 */
	public HttpListener(String name, String addr, int port, boolean sslEnabled) {
		super(name, addr, port, Protocol.HTTP);
		this.sslEnabled = sslEnabled;
		init();
	}

	/**
	 * 
	 * @param name
	 * @param addr
	 * @param port
	 * @param sslEnabled
	 * @param cert
	 * @param key
	 * @param pass
	 */
	public HttpListener(String name, String addr, int port, boolean sslEnabled, String cert, String key, String pass) {
		super(name, addr, port, Protocol.HTTP);
		this.sslEnabled = sslEnabled;
		this.cert = cert;
		this.key = key;
		this.pass = pass;
	}

	/**
	 * Initialize the server
	 */
	private void init() {
		EventLoopGroup boos = new NioEventLoopGroup(1);
		EventLoopGroup woker = new NioEventLoopGroup(5);
		bootstrap = new ServerBootstrap();
		bootstrap.group(boos, woker);
		bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
		bootstrap.channel(NioServerSocketChannel.class);
		if (logger.isDebugEnabled()) bootstrap.handler(new LoggingHandler(LogLevel.INFO));
		bootstrap.childHandler(new Http2ServerInitializer(null));
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
