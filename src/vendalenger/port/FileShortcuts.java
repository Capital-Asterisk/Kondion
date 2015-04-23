/*
 * Copyright 2015 Neal Nicdao
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package vendalenger.port;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.MalformedInputException;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class FileShortcuts {

	public static String sep = System.getProperty("line.separator");

	/**
	 * Download a file from the URL.
	 *
	 * @exception IOException
	 *                when https:// Use http:// downloadFile only uses http
	 * @param http
	 *            The URL of the file to download.
	 * @param path
	 *            Destination of the file to download.
	 * @return
	 */
	public static boolean downloadFile(String http, String path) {
		URLConnection urlc;
		int i = 0;
		try {
			urlc = new URL(http).openConnection();
			File file = new File(path);
			BufferedInputStream bis = new BufferedInputStream(
					urlc.getInputStream());
			BufferedOutputStream bos = new BufferedOutputStream(
					new FileOutputStream(file.getName()));
			while ((i = bis.read()) != -1) {
				bos.write(i);
			}
			bos.close();
			bis.close();
			return true;
		} catch (MalformedInputException e) {
			System.err.println("ERROR: MalformedInputException: "
					+ e.getCause());
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			System.err.println("ERROR: IOException: " + e.getCause());
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Read through a text file and return it as a string.
	 *
	 * @throws IOException
	 */
	public static String readTextFile(File textFile) throws IOException {
		String file = "";
		FileReader reader = new FileReader(textFile);
		BufferedReader buffer = new BufferedReader(reader);
		String line = "";
		while ((line = buffer.readLine()) != null) {
			file += line + sep;
		}
		reader.close();
		return file;
	}

	/**
	 * Extract files from a zip file
	 *
	 * @param zip
	 *            The path the the zip file.
	 * @param path
	 *            Destinations of where the extracted contents will go.
	 */
	public static boolean unZip(String zip, String path) {
		try {
			ZipFile zipFile = new ZipFile(zip);
			zipFile.extractAll(path);
			return true;
		} catch (ZipException e) {
			System.err.println("FAILED TO UNZIP");
			System.err.println("ERROR: ZipException: " + e.getCause());
			e.printStackTrace();
			return false;
		}
	}
}
