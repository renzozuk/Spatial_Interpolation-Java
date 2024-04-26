package br.ufrn.dimap.services;

import br.ufrn.dimap.entities.KnownPoint;
import br.ufrn.dimap.entities.UnknownPoint;
import br.ufrn.dimap.repositories.LocationRepository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.concurrent.locks.Lock;

import static java.nio.file.Files.createFile;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.newBufferedReader;
import static java.nio.file.Files.newBufferedWriter;

public class FileManagementService {
    private static final String HOME = "src//main//resources//";
    private static final Path exportationPath = Path.of(HOME + "output//exported_locations.csv");

    public static void importKnownLocations(String dataPath) throws IOException {
        LocationRepository locationRepository = LocationRepository.getInstance();

        BufferedReader bufferedReader = newBufferedReader(Path.of(HOME + dataPath));

        String line;

        while((line = bufferedReader.readLine()) != null){
            String[] information = line.split(";");

            locationRepository.addKnownPoint(new KnownPoint(Double.parseDouble(information[0]), Double.parseDouble(information[1]), Double.parseDouble(information[2])));
        }

        bufferedReader.close();
    }

    public static void importKnownLocations(Lock lock, String dataPath) throws IOException {
        lock.lock();

        LocationRepository locationRepository = LocationRepository.getInstance();

        lock.unlock();

        BufferedReader bufferedReader = newBufferedReader(Path.of(HOME + dataPath));

        String line;

        while((line = bufferedReader.readLine()) != null){
            String[] information = line.split(";");

            try{
                locationRepository.addKnownPoint(new KnownPoint(Double.parseDouble(information[0]), Double.parseDouble(information[1]), Double.parseDouble(information[2])));
            }catch(NumberFormatException ignored){}
        }

        bufferedReader.close();
    }

    public static void importRandomData() throws IOException {
        importKnownLocations("databases//random_data.csv");
    }

    public static void importRandomData(Lock lock) throws IOException, InterruptedException {
        importKnownLocations(lock, "databases//random_data.csv");
    }

    public static void importTrueData() throws IOException {
        importKnownLocations("databases//true_data.csv");
    }

    public static void importTrueData(Lock lock) throws IOException, InterruptedException {
        importKnownLocations(lock, "databases//true_data.csv");
    }

    public static void importUnknownLocations() throws IOException {
        LocationRepository locationRepository = LocationRepository.getInstance();

        BufferedReader bufferedReader = newBufferedReader(Path.of(HOME + "unknown_locations.csv"));

        String line;

        while((line = bufferedReader.readLine()) != null){
            String[] information = line.split(";");

            locationRepository.addUnknownPoint(new UnknownPoint(Double.parseDouble(information[0]), Double.parseDouble(information[1])));
        }

        bufferedReader.close();
    }

    public static void importUnknownLocations(Lock lock) throws IOException {
        lock.lock();

        LocationRepository locationRepository = LocationRepository.getInstance();

        lock.unlock();

        BufferedReader bufferedReader = newBufferedReader(Path.of(HOME + "unknown_locations.csv"));

        String line;

        while((line = bufferedReader.readLine()) != null){
            String[] information = line.split(";");

            locationRepository.addUnknownPoint(new UnknownPoint(Double.parseDouble(information[0]), Double.parseDouble(information[1])));
        }

        bufferedReader.close();
    }

    public static void exportInterpolations(Collection<UnknownPoint> unknownPoints) throws IOException, InterruptedException {
        if(!exists(exportationPath)){
            createFile(exportationPath);
        }

        BufferedWriter bufferedWriter = newBufferedWriter(exportationPath, StandardOpenOption.APPEND);

        for(UnknownPoint unknownPoint : unknownPoints){
            writeLine(bufferedWriter, unknownPoint);
        }

        bufferedWriter.close();
    }

    public static void exportInterpolations(Lock lock, Collection<UnknownPoint> unknownPoints) throws IOException, InterruptedException {
        lock.lock();

        if(!exists(exportationPath)){
            createFile(exportationPath);
        }

        lock.unlock();

        BufferedWriter bufferedWriter = newBufferedWriter(exportationPath, StandardOpenOption.APPEND);

        for(UnknownPoint unknownPoint : unknownPoints){
            writeLine(bufferedWriter, unknownPoint);
        }

        bufferedWriter.close();
    }

    private static void writeLine(BufferedWriter bufferedWriter, UnknownPoint unknownPoint) throws IOException {
        bufferedWriter.write(String.format("%.6f", unknownPoint.getLatitude()));
        bufferedWriter.write(";");
        bufferedWriter.write(String.format("%.6f", unknownPoint.getLongitude()));
        bufferedWriter.write(";");
        bufferedWriter.write(String.format("%.1f", unknownPoint.getTemperature()));
        bufferedWriter.newLine();
    }
}