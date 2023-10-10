package org.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import io.prometheus.client.Histogram;
import io.prometheus.client.exporter.HTTPServer;
import io.prometheus.client.hotspot.DefaultExports;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

record FactorialHttpServer(Histogram requestHistogram) implements HttpHandler {

	public static void main(String[] args) throws Exception {
		System.out.println("org.example.FactorialHttpServer");
		Histogram requestHistogram = Histogram.build().namespace("app").name("request_duration_seconds").help("Request histogram").labelNames("statusCode").register();
		DefaultExports.initialize();

		try {
			HTTPServer server = new HTTPServer("0.0.0.0", 8080, true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		var server = HttpServer.create(new InetSocketAddress(8001), 0);
		server.createContext("/factorial", new FactorialHttpServer(requestHistogram));
		server.start();
		var address = server.getAddress();
		System.out.printf("http://%s:%d%n", address.getHostString(), address.getPort());
		Thread.sleep(Long.MAX_VALUE);

	}

	static BigInteger factorial(int n) {
		BigInteger factorial = BigInteger.ONE;

		for (int i = n; i > 0; i--) {
			factorial = factorial.multiply(BigInteger.valueOf(i));
		}

		return factorial;
	}

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		LocalDateTime start = LocalDateTime.now();
		int statusCode = 200;

		if (!"GET".equalsIgnoreCase(httpExchange.getRequestMethod())) {
			statusCode = 400;
		}
		Random random = new Random();

		StringBuilder result = new StringBuilder(factorial(random.nextInt(2000, 3000)).toString());

		httpExchange.sendResponseHeaders(statusCode, 0);
		try (var stream = httpExchange.getResponseBody()) {
			stream.write(result.toString().getBytes());
		}
		LocalDateTime end = LocalDateTime.now();
		long elapsedTime = Duration.between(start, end).toMillis();
		requestHistogram.labels(String.valueOf(statusCode)).observe(elapsedTime);
	}
}
