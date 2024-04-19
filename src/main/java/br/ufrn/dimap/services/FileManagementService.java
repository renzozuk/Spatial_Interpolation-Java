package br.ufrn.dimap.services;

import br.ufrn.dimap.entities.KnownPoint;
import br.ufrn.dimap.entities.UnknownPoint;
import br.ufrn.dimap.repositories.LocationRepository;
import org.openjdk.jmh.annotations.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.concurrent.Semaphore;

import static java.nio.file.Files.createFile;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.newBufferedReader;
import static java.nio.file.Files.newBufferedWriter;

public class FileManagementService {
    private static final String HOME = "src//main//resources//";

    public static void importKnownLocations(Semaphore semaphore, String dataPath) throws IOException, InterruptedException {
        semaphore.acquire();

        LocationRepository locationRepository = LocationRepository.getInstance();

        semaphore.release();

        BufferedReader bufferedReader = newBufferedReader(Path.of(HOME + dataPath));

        String line;

        while((line = bufferedReader.readLine()) != null){
            String[] information = line.split(";");

            locationRepository.addKnownPoint(new KnownPoint(Double.parseDouble(information[0]), Double.parseDouble(information[1]), Double.parseDouble(information[2])));
        }

        bufferedReader.close();
    }

    public static void importRandomData(Semaphore semaphore) throws IOException, InterruptedException {
        importKnownLocations(semaphore, "databases//random_data.csv");
    }

    public static void importTrueData(Semaphore semaphore) throws IOException, InterruptedException {
        importKnownLocations(semaphore, "databases//true_data.csv");
    }

    public static void importUnknownLocations(Semaphore semaphore) throws IOException, InterruptedException {
        semaphore.acquire();

        LocationRepository locationRepository = LocationRepository.getInstance();

        semaphore.release();

        BufferedReader bufferedReader = newBufferedReader(Path.of(HOME + "unknown_locations.csv"));

        String line;

        while((line = bufferedReader.readLine()) != null){
            String[] information = line.split(";");

            locationRepository.addUnknownPoint(new UnknownPoint(Double.parseDouble(information[0]), Double.parseDouble(information[1])));
        }

        bufferedReader.close();
    }

    public static void exportInterpolations(Semaphore semaphore, List<UnknownPoint> unknownPoints) throws IOException, InterruptedException {
        Path filePath = Path.of(HOME + "output//exported_locations.csv");

        semaphore.acquire();

        if(!exists(filePath)){
            createFile(filePath);
        }

        semaphore.release();

        BufferedWriter bufferedWriter = newBufferedWriter(filePath, StandardOpenOption.APPEND);

        for(UnknownPoint unknownPoint : unknownPoints){
            writeLine(bufferedWriter, unknownPoint);
        }

        bufferedWriter.close();
    }

    public static void exportInterpolations(Semaphore semaphore) throws IOException, InterruptedException {
        Path filePath = Path.of(HOME + "output//exported_locations.csv");

        semaphore.acquire();

        if(!exists(filePath)){
            createFile(filePath);
        }

        semaphore.release();

        BufferedWriter bufferedWriter = newBufferedWriter(filePath, StandardOpenOption.APPEND);

        for(UnknownPoint unknownPoint : LocationRepository.getInstance().getUnknownPoints()){
            writeLine(bufferedWriter, unknownPoint);
        }

        bufferedWriter.close();
    }

    private static void writeLine(BufferedWriter bufferedWriter, UnknownPoint unknownPoint) throws IOException {
        bufferedWriter.write(String.format("%.4f", unknownPoint.getLatitude()));
        bufferedWriter.write(";");
        bufferedWriter.write(String.format("%.4f", unknownPoint.getLongitude()));
        bufferedWriter.write(";");
        bufferedWriter.write(String.format("%.1f", unknownPoint.getTemperature()));
        bufferedWriter.newLine();
    }
}