package org.example;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import io.prometheus.client.Histogram;
import io.prometheus.client.exporter.HTTPServer;
import io.prometheus.client.hotspot.DefaultExports;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

record EditorHttpServer(FileProcessor processedFile, Histogram requestHistogram) implements HttpHandler {

	public static void main(String[] args) throws Exception {
		System.out.println("org.example.EditorHttpServer");

		Histogram requestHistogram = Histogram.build().namespace("app").name("request_duration_seconds").help("Request histogram").labelNames("statusCode").register();
		DefaultExports.initialize();

		try {
			HTTPServer server = new HTTPServer("0.0.0.0", 8080, true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		int size = 1000;
		byte[] data = new byte[size];
		String fileName = "file.txt";
		Random r = new Random();
		r.nextBytes(data);
		try (FileOutputStream fos = new FileOutputStream(fileName)){
			fos.write(data);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		FileProcessor processedFile = new FileProcessor(fileName, size, 0, 0, 0);
		processedFile.start();

		var server = HttpServer.create(new InetSocketAddress(8001), 0);
		server.createContext("/work", new EditorHttpServer(processedFile, requestHistogram));
		server.start();
		var address = server.getAddress();
		System.out.printf("https://%s:%d%n", address.getHostString(), address.getPort());
		Thread.sleep(Long.MAX_VALUE);
	}

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		LocalDateTime start = LocalDateTime.now();
		int statusCode = 200;

		if (!"GET".equalsIgnoreCase(httpExchange.getRequestMethod())) {
			statusCode = 400;
		}

		long startTime = System.currentTimeMillis();
		StringBuilder answer = new StringBuilder(
				"Duration (seconds), Iteration, Checksum, First byte");

		while (System.currentTimeMillis()< startTime+10) {
			long duration = (System.currentTimeMillis() - startTime);
			answer.append("\n").append(duration).append(", ").append(processedFile.iteration).append(", ").append(processedFile.checksum).append(", ").append(processedFile.firstByte);
		}

		httpExchange.sendResponseHeaders(statusCode, 0);
		try (var stream = httpExchange.getResponseBody()) {
			stream.write(answer.toString().getBytes());
		}
		LocalDateTime end = LocalDateTime.now();
		long elapsedTime = Duration.between(start, end).toMillis();
		requestHistogram.labels(String.valueOf(statusCode)).observe(elapsedTime);
	}
}
