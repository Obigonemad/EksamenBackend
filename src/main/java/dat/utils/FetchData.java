package dat.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import dat.dtos.DoctorDTO;
import dat.dtos.PackingItemsDTO;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class FetchData {

    public static PackingItemsDTO fetch(String category) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            // Create an HttpClient instance
            HttpClient client = HttpClient.newHttpClient();

            // Create a request with the specified category
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://packingapi.cphbusinessapps.dk/packinglist/" + category))
                    .GET()
                    .build();

            // Send the request and get the response
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // Check the status code and parse the response if successful
            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), PackingItemsDTO.class);
            } else {
                System.out.println("GET request failed. Status code: " + response.statusCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return null or an empty object if the fetch fails
        return null;
    }

}
