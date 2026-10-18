package cz.miscik.trixi_task_spring;

import cz.miscik.trixi_task_spring.Service.TrixiService;
import cz.miscik.trixi_task_spring.parser.ParsedData;
import cz.miscik.trixi_task_spring.parser.Parser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.zip.ZipInputStream;

/**
 * Main application class for the TrixiTaskSpring application.
 *
 * <p>This class is responsible for bootstrapping the Spring Boot application and executing
 * the data import process after the application context is loaded.</p>
 */
@SpringBootApplication
@EnableCaching
public class TrixiTaskSpringApplication implements CommandLineRunner { // Implement CommandLineRunner to run code after the application context is loaded -> spring boot thing

	private final TrixiService trixiService;

	/**
	 * Constructor for TrixiTaskSpringApplication.
	 *
	 * @param trixiService service responsible for importing data into the database
	 */
	public TrixiTaskSpringApplication(TrixiService trixiService) {
		this.trixiService = trixiService;
	}

	/**
	 * Main method to start the Spring Boot application.
	 *
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(TrixiTaskSpringApplication.class, args); // Start the Spring Boot application
	}

	/**
	 * Method executed after the application context is loaded.
	 * It downloads a zip file, unzips it, parses the XML data, and imports it into the database.
	 *
	 * @param args command-line arguments
	 * @throws Exception if an error occurs during the download, unzip, parse, or import process
	 */
	@Override
	public void run(String... args) throws Exception {

		// Download the zip file from the URL
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create("https://www.smartform.cz/download/kopidlno.xml.zip")).GET().build();
		HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

		// Unzip the downloaded file
		ZipInputStream zipInputStream = new ZipInputStream(response.body());
		zipInputStream.getNextEntry();

		// Parse the XML data
		Parser parser = new Parser();
		ParsedData data = parser.parse(zipInputStream);

		// Import the parsed data into the database using the TrixiService
		trixiService.importData(data.obce(), data.castiObce());
	}
}