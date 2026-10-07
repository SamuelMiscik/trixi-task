package cz.miscik.trixi_task_spring;

import cz.miscik.trixi_task_spring.Service.TrixiService;
import cz.miscik.trixi_task_spring.parser.ParsedData;
import cz.miscik.trixi_task_spring.parser.Parser;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.zip.ZipInputStream;

@SpringBootApplication
public class TrixiTaskSpringApplication implements CommandLineRunner { // Implement CommandLineRunner to run code after the application context is loaded -> spring boot thing

	private final TrixiService trixiService;

	public TrixiTaskSpringApplication(TrixiService trixiService) {
		this.trixiService = trixiService;
	}

	public static void main(String[] args) {
		SpringApplication.run(TrixiTaskSpringApplication.class, args); // Start the Spring Boot application
	}

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
		trixiService.importData(data.getVillages(), data.getPartsOfVillages());
	}
}