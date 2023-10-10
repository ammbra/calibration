package org.example;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class FileProcessor extends Thread {

	int size;
	String name;
	long iteration;
	int checksum;
	int firstByte;

	public FileProcessor(String name, int size, long iteration, int checksum, int firstByte) {
		this.size = size;
		this.name = name;
		this.iteration = iteration;
		this.checksum = checksum;
		this.firstByte = firstByte;
	}

	@Override
	public void run() {
		byte[] buffer = new byte[size];
		try {
			while (true) {
				FileInputStream fis = new FileInputStream(name);
				fis.read(buffer);
				fis.close();
				long sum = 0;
				for (int i = 0; i < size; i++) {
					sum = (sum + (buffer[i] & 0x00FF)) % Integer.MAX_VALUE;
				}
				checksum = (int) sum;
				firstByte = buffer[0] & 0x00FF;
				FileOutputStream fos = new FileOutputStream(name);
				fos.write(buffer, 1, size - 1);
				fos.write(buffer, 0, 1);
				fos.close();
				iteration++;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
