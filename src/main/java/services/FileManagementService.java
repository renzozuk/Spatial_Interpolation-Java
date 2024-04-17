package services;

import entities.KnownPoint;
import entities.UnknownPoint;
import repositories.LocationRepository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public class FileManagementService {
    private static final String HOME = "src//main//resources//";

    public static void importKnownLocations(String dataPath) throws IOException {
        LocationRepository locationRepository = LocationRepository.getInstance();

        BufferedReader bufferedReader = Files.newBufferedReader(Path.of(HOME + dataPath));

        String line;

        while((line = bufferedReader.readLine()) != null){
            String[] information = line.split(";");

            locationRepository.addKnownPoint(new KnownPoint(Double.parseDouble(information[0]), Double.parseDouble(information[1]), Double.parseDouble(information[2])));
        }

        bufferedReader.close();
    }

    public static void importRandomData() throws IOException {
        importKnownLocations("databases//random_data.csv");
    }

    public static void importTrueData() throws IOException {
        importKnownLocations("databases//true_data.csv");
    }

    public static void importUnknownLocations() throws IOException {
        LocationRepository locationRepository = LocationRepository.getInstance();

        BufferedReader bufferedReader = Files.newBufferedReader(Path.of(HOME + "unknown_locations.csv"));

        String line;

        while((line = bufferedReader.readLine()) != null){
            String[] information = line.split(";");

            locationRepository.addUnknownPoint(new UnknownPoint(Double.parseDouble(information[0]), Double.parseDouble(information[1])));
        }

        bufferedReader.close();
    }

    public static void exportInterpolations(List<UnknownPoint> unknownPoints) throws IOException {
        FileWriter writer = new FileWriter(HOME + "output//exported_locations.csv", true);

        BufferedWriter bufferedWriter = new BufferedWriter(writer);

        for(UnknownPoint unknownPoint : unknownPoints){
            bufferedWriter.write(String.format("%.4f", unknownPoint.getLatitude()));
            bufferedWriter.write(";");
            bufferedWriter.write(String.format("%.4f", unknownPoint.getLongitude()));
            bufferedWriter.write(";");
            bufferedWriter.write(String.format("%.1f", unknownPoint.getTemperature()));
            bufferedWriter.newLine();
        }

        bufferedWriter.close();

        writer.close();
    }

    public static void exportInterpolations() throws IOException {
        FileWriter writer = new FileWriter(HOME + "output//exported_locations.csv", true);

        BufferedWriter bufferedWriter = new BufferedWriter(writer);

        for(UnknownPoint unknownPoint : LocationRepository.getInstance().getUnknownPoints()){
            bufferedWriter.write(String.format("%.4f", unknownPoint.getLatitude()));
            bufferedWriter.write(";");
            bufferedWriter.write(String.format("%.4f", unknownPoint.getLongitude()));
            bufferedWriter.write(";");
            bufferedWriter.write(String.format("%.1f", unknownPoint.getTemperature()));
            bufferedWriter.newLine();
        }

        bufferedWriter.close();

        writer.close();
    }
}