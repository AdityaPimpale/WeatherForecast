package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Scanner;

public class WeatherApp {

    private static final String API_KEY = "abbd3b99fc18d3e152351d13c53cbe35";

    public static void main(String[] args) {
        WeatherApp weatherApp = new WeatherApp();
        Scanner scanner = new Scanner(System.in);
        int option;

        do {
            System.out.println("1. Get weather");
            System.out.println("2. Get Wind Speed");
            System.out.println("3. Get Pressure");
            System.out.println("0. Exit");

            System.out.print("Enter your choice: ");
            option = scanner.nextInt();

            switch (option) {
                case 1:
                    weatherApp.getWeatherData(scanner);
                    break;
                case 2:
                    weatherApp.getWindSpeedData(scanner);
                    break;
                case 3:
                    weatherApp.getPressureData(scanner);
                    break;
                case 0:
                    System.out.println("Exiting the program.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (option != 0);

        scanner.close();
    }

    private void getWeatherData(Scanner scanner) {
        String date = promptForDate(scanner);
        String apiResponse = fetchAPIResponse(date);
        double temperature = extractTemperature(apiResponse);
        System.out.println("Weather data for " + date + ": Temperature = " + temperature + " °C");
    }

    private void getWindSpeedData(Scanner scanner) {
        String date = promptForDate(scanner);
        String apiResponse = fetchAPIResponse(date);
        double windSpeed = extractWindSpeed(apiResponse);
        System.out.println("Wind Speed data for " + date + ": Wind Speed = " + windSpeed + " m/s");
    }

    private void getPressureData(Scanner scanner) {
        String date = promptForDate(scanner);
        String apiResponse = fetchAPIResponse(date);
        double pressure = extractPressure(apiResponse);
        System.out.println("Pressure data for " + date + ": Pressure = " + pressure + " hPa");
    }

    private String promptForDate(Scanner scanner) {
        System.out.print("Enter the date: ");
        return scanner.next();
    }

    private String fetchAPIResponse(String date) {
        String apiUrl = "https://samples.openweathermap.org/data/2.5/forecast/hourly?q=London,us&appid=" + API_KEY;

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            return response.toString();
        } catch (IOException e) {
            System.out.println("Error fetching data from the API: " + e.getMessage());
            return null;
        }
    }

    private double extractTemperature(String apiResponse) {

        JSONObject jsonObject = new JSONObject(apiResponse);
        JSONArray hourlyArray = jsonObject.getJSONArray("list");
        if (hourlyArray.length() > 0) {
            JSONObject hourlyData = hourlyArray.getJSONObject(0);
            JSONObject main = hourlyData.getJSONObject("main");
            return main.getDouble("temp");
        }
        return 0;
    }

    private double extractWindSpeed(String apiResponse) {

        JSONObject jsonObject = new JSONObject(apiResponse);
        JSONArray hourlyArray = jsonObject.getJSONArray("list");
        if (hourlyArray.length() > 0) {
            JSONObject hourlyData = hourlyArray.getJSONObject(0);
            JSONObject wind = hourlyData.getJSONObject("wind");
            return wind.getDouble("speed");
        }
        return 0;
    }

    private double extractPressure(String apiResponse) {

        JSONObject jsonObject = new JSONObject(apiResponse);
        JSONArray hourlyArray = jsonObject.getJSONArray("list");
        if (hourlyArray.length() > 0) {
            JSONObject hourlyData = hourlyArray.getJSONObject(0);
            JSONObject main = hourlyData.getJSONObject("main");
            return main.getDouble("pressure");
        }
        return 0;
    }
}

