package com.movie.icon;

import java.io.IOException;
import java.util.List;

public class MovieIconer {
	
	
	public static void main(String []args) {
		
		// Extract all the movie names
		List<String> movieNames = MovieIconerService.getMovieNames(args[0]);
		
		// Download and save the icon
	
			try {
				MovieIconerService.getMovieIconFromWeb(movieNames,args[1]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		
		
	}

}
