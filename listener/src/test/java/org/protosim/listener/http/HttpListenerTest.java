package org.protosim.listener.http;

import org.junit.jupiter.api.Test;

class HttpListenerTest {

	@Test
	void testHttp2ClearTextConn() throws InterruptedException {
		HttpListener listener = new HttpListener("DummyListener", "0.0.0.0", 3000);
		listener.start();
	}

}
