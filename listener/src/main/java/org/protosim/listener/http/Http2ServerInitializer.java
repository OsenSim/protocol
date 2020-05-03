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

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpMessage;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.ssl.SslContext;

/**
 * @author Arpan Mukhopadhyay
 *
 */
public class Http2ServerInitializer extends ChannelInitializer<SocketChannel> {

	private static final Log logger = LogFactory.getLog(Http2ServerInitializer.class);
//	private static final UpgradeCodecFactory upgradeCodeFactory = new UpgradeCodecFactory() {
//		@Override
//		public UpgradeCodec newUpgradeCodec(CharSequence protocol) {
//			if (AsciiString.contentEquals(Http2CodecUtil.HTTP_UPGRADE_PROTOCOL_NAME, protocol)) {
//				return new Http2ServerUpgradeCodec(connectionHandler);
//			} else {
//				return null;
//			}
//		}
//	};
	private SslContext sslCtx;
	
	/**
	 * @param sslCtx
	 */
	public Http2ServerInitializer(SslContext sslCtx) {
		this.sslCtx = sslCtx;
	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		if (null == sslCtx) 
			addClearTextHandler(ch);
		else
			addSslHandler(ch);
	}

	/**
	 * Add clear text handler to the current channel
	 * @param channel
	 */
	private void addClearTextHandler(SocketChannel channel) {
		final HttpServerCodec httpServerCode = new HttpServerCodec();
//		final HttpServerUpgradeHandler upgradeHandler = new HttpServerUpgradeHandler(httpServerCode, upgradeCodeFactory);
		channel.pipeline().addLast(new SimpleChannelInboundHandler<HttpMessage>() {

			@Override
			protected void channelRead0(ChannelHandlerContext ctx, HttpMessage msg) throws Exception {
				if (logger.isInfoEnabled()) logger.info("Message received from " + channel.remoteAddress() + " " + msg);
			}
		});
	}
	
	/**
	 * Add ssl handler to the current channel
	 * @param channel
	 */
	private void addSslHandler(SocketChannel channel) {
		if (logger.isInfoEnabled()) logger.info("HTTP/HTTP2 over SSL is not supported");
//		channel.pipeline().addLast(sslCtx.newHandler(channel.alloc(), null));
	}
}
