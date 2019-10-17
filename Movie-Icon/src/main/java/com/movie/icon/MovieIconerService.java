package com.movie.icon;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import net.sf.image4j.codec.ico.ICOEncoder;

public class MovieIconerService {

	private final static Logger logger = Logger.getLogger(MovieIconerService.class);

	public static List<String> getMovieNames(String movieFolderPath) {

		System.out.println("First argument:" + movieFolderPath);
		List<String> movieNames = new ArrayList<>();

		File file = new File(movieFolderPath);

		File[] folders = file.listFiles(new FileFilter() {

			@Override
			public boolean accept(File file) {
				return StringUtils.equals(file.getName(), "Icons") ? false : file.isDirectory();
			}
		});

		for (File folder : folders) {
			movieNames.add(folder.getName());
		}

		return movieNames;
	}

	public static void getMovieIconFromWeb(List<String> movies, String iconFolderPath) throws IOException {

		// URL url = new
		// URL("http://www.iconarchive.com/download/i62543/ampeross/qetto/icon-developer.ico");
		// FileUtils.copyURLToFile(url, new File("D:\\Java\\Movies\\Icons\\icon.ico"));

		movies.forEach((movieName) -> {
			Document document;
			try {
				System.out.println("Retriving Icon:" + movieName);
					 
			
				movieName = movieName.replaceAll("[^a-zA-Z0-9]+"," "); 
				System.out.println("String to search:" + movieName);
				document = Jsoup.connect("http://www.iconarchive.com/search?q=" + movieName).get();
				
				Elements links = document.getElementsByClass("detail");

				if (StringUtils.isEmpty(links.html()))
					return; // If no result is found

				String downloadLink = StringUtils.equals(links.get(0).child(0).text(), "ICO") ? links.get(0).child(0).attr("href") : links.get(0).child(1).attr("href");

				System.out.println("Download link::" + downloadLink);
				URL url = new URL("http://www.iconarchive.com" + downloadLink);
				FileUtils.copyURLToFile(url, new File(iconFolderPath + "\\" + movieName + ".ico"));

			} catch (IOException e) {
				System.out.println("Exception ::" + e);
			}

		});

		System.out.println("Execution finished");

	}

	public static String getFirstIndex(String movieName) {
		return movieName.split("\\.")[0];
	}

	public static void saveImage(String imageUrl, String destinationFile) throws IOException {
		URL url = new URL(imageUrl);
		InputStream is = url.openStream();
		OutputStream os = new FileOutputStream(destinationFile);

		byte[] b = new byte[2048];
		int length;

		while ((length = is.read(b)) != -1) {
			os.write(b, 0, length);
		}

		is.close();
		os.close();
	}
}
