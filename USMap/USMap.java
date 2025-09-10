/**
 * USMap.java- Using the StdDraw class, a simple database, and algorithms
 * to create a map of the cities in the US.
 * 
 * @author Jason He
 * @since 9/8/2025
 */ 

import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class USMap 
{
	private String citiesFile;
	private double[] latitude;
	private double[] longitude;
	private String[] city;
	
	private String[] biggestCities;
	private int[] biggestCitiesPop;
	
	public USMap()
	{
		citiesFile = "cities.txt";
		latitude = new double[1112];
		longitude = new double[1112];
		city = new String[1112];
		
		biggestCities = new String[276];
		biggestCitiesPop = new int[276];
	}
	
	public static void main(String[] args)
	{
		USMap um = new USMap();
		um.runIt();
	}
	
	public void runIt()
	{
		setupCanvas();
		openToRead(citiesFile);
		reader(citiesFile);
		bigCityReader(citiesFile);
		draw();
	}
	
	/** Set up the canvas size and scale */
	public void setupCanvas() 
	{
		StdDraw.setTitle("USMap");
		StdDraw.setCanvasSize(900, 512);
		StdDraw.setXscale(128.0, 65.0);
		StdDraw.setYscale(22.0, 52.0);
	}
	
	/** opens the file to read */
	public static java.util.Scanner openToRead(String fileName)
	{
		java.util.Scanner input = null;
		 
		try 
		{
			input = new java.util.Scanner(new java.io.File(fileName));
		}
		catch(java.io.FileNotFoundException e)
		{
			System.err.print("ERROR: Cannot open " + fileName + 
				"for reading");
			System.exit(1);		
		}
		
		return input;
	}
	
	/** actually reads the information (basic) to read */
	public void reader(String fileName)
	{
		// file, scanner stuff
		File myFile = new File(fileName);
		int counter = 0;
		
        try 
        {
			Scanner emi = new Scanner(myFile);
			while (emi.hasNextLine()) 
			{
				String line = emi.nextLine();
				String data = emi.nextLine();
			
				// latitude stuff
				String latTempCoord = data.substring(0, data.indexOf(" "));
				double latTempCoord2 = Double.parseDouble(latTempCoord);
				
				// now get rid of latitude
				data.substring( data.indexOf(" ")+1 );
				
				// longitude stuff 
				String longTemp = data.substring(0, data.indexOf(" "));
				double longTemp2 = Double.parseDouble(longTemp);
				
				// now get rid of longitude
				data.substring( data.indexOf(" ")+1 );
				
				// get city name
				String tempCity = data.substring(0, data.indexOf(" "));
				// states are garbage, dont need to extract
				
				// give the array values we just extracted
				latitude[counter] = latTempCoord2;
				longitude[counter] = longTemp2;
				city[counter] = tempCity;
				counter++;
			}
			
			emi.close();
		} 
		catch (FileNotFoundException e) 
		{
			System.out.println("File not found: cities.txt");
			e.printStackTrace();
		}
		
		
	}
	
	/// DO THIS DO THIS DO THIS DO THIS DO THIS DO THIS DO THIS DO THIS
	///  DO THIS DO THIS DO THIS DO THIS DO THIS DO THIS DO THIS DO THIS
	/// DO THIS DO THIS DO THIS DO THIS DO THIS DO THIS DO THIS DO THIS 
	/** reads the big cities in */
	public void bigCityReader(String fileName)
	{
		
	}
	
	public void draw()
    {
		// top 10 cities
        StdDraw.setPenColor(StdDraw.RED);
        
        for (int i=0; i < 10; i++)
        {
            for (int j=0; j < city.length; j++)
            {
                if (biggestCities[i].equals(city[j]))
                {
                    StdDraw.setPenRadius(0.6*Math.sqrt(biggestCitiesPop[i])/18500);
                    StdDraw.point(latitude[j], longitude[j]);
                }
            }
        }
		
		// big cities
        StdDraw.setPenColor(StdDraw.BLUE);
        
        for (int i=10; i < biggestCities.length; i++)
        {
            for (int j=0; j < city.length; j++)
            {
                if (biggestCities[i].equals(city[j]))
                {
                    StdDraw.setPenRadius(0.6*Math.sqrt(biggestCitiesPop[i])/18500);
                    StdDraw.point(latitude[j], longitude[j]);
                }
            }
        }
        
        // normal cities
        StdDraw.setPenColor(StdDraw.GRAY);
        StdDraw.setPenRadius(0.006);
        
        for (int i=0; i < latitude.length; i++)
        {
            StdDraw.point(latitude[i], longitude[i]);
        }
    }
}
}
