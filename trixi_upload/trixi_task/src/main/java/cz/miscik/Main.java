package cz.miscik;

import cz.miscik.parser.ParsedData;
import cz.miscik.parser.Parser;
import cz.miscik.services.TrixiServices;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import javax.xml.stream.XMLStreamException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.zip.ZipInputStream;

public class Main {

    public static void main(String[] args) throws IOException, XMLStreamException, InterruptedException {

        // Create an EntityManagerFactory and EntityManager for database operations
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("trixiPU");
        EntityManager em = emf.createEntityManager();

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

        // Import the parsed data into the database using the TrixiServices
        TrixiServices trixiServices = new TrixiServices(em);
        trixiServices.importData(data.getVillages(), data.getPartsOfVillages());

        em.close();
        emf.close();
    }
}