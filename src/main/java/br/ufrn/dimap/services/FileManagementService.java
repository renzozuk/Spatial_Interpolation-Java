package br.ufrn.dimap.services;

import br.ufrn.dimap.entities.KnownPoint;
import br.ufrn.dimap.entities.UnknownPoint;
import br.ufrn.dimap.repositories.LocationRepository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Collection;

import static java.nio.file.Files.createFile;
import static java.nio.file.Files.exists;
import static java.nio.file.Files.newBufferedReader;
import static java.nio.file.Files.newBufferedWriter;

public class FileManagementService {
    private static final String HOME = "src//main//resources//";
    private static Path exportationPath = Path.of(HOME + "output//exported_locations.csv");

    public static void importRandomData() throws IOException {
        importKnownLocations("databases//random_data.csv");
    }

    public static void importTrueData() throws IOException {
        importKnownLocations("databases//true_data.csv");
    }

    public static void importKnownLocations(String dataPath) throws IOException {
        importKnownLocations(LocationRepository.getInstance(), dataPath);
    }

    public static void importKnownLocations(LocationRepository locationRepository, String dataPath) throws IOException {
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

    public static void importUnknownLocations() throws IOException {
        LocationRepository locationRepository = LocationRepository.getInstance();

        importUnknownLocations(locationRepository);
    }

    public static void importUnknownLocations(LocationRepository locationRepository) throws IOException {
        BufferedReader bufferedReader = newBufferedReader(Path.of(HOME + "unknown_locations.csv"));

        String line;

        while((line = bufferedReader.readLine()) != null){
            String[] information = line.split(";");

            locationRepository.addUnknownPoint(new UnknownPoint(Double.parseDouble(information[0]), Double.parseDouble(information[1])));
        }

        bufferedReader.close();
    }

    public static void defineExportationPath() {
        ZonedDateTime zdt = Instant.now().atZone(ZoneId.of("Etc/GMT+0"));
        exportationPath = Path.of(HOME + "output//exported_locations_" + String.format("%04d.%02d.%02d-%02d.%02d.%02d.csv", zdt.getYear(), zdt.getMonthValue(), zdt.getDayOfMonth(), zdt.getHour(), zdt.getMinute(), zdt.getSecond()));
    }

    public static void exportInterpolations(Collection<UnknownPoint> unknownPoints) throws IOException, InterruptedException {
        if(!exists(exportationPath)){
            createFile(exportationPath);
        }

        writeLines(exportationPath, unknownPoints);
    }

    private static void writeLines(Path path, Collection<UnknownPoint> unknownPoints) throws IOException {
        BufferedWriter bufferedWriter = newBufferedWriter(path, StandardOpenOption.APPEND);

        for(UnknownPoint unknownPoint : unknownPoints){
            writeLine(bufferedWriter, unknownPoint);
        }

        bufferedWriter.close();
    }

    private static void writeLine(BufferedWriter bufferedWriter, UnknownPoint unknownPoint) throws IOException {
        bufferedWriter.write(String.format("%.6f;%.6f;%.1f", unknownPoint.getLatitude(), unknownPoint.getLongitude(), unknownPoint.getTemperature()));
        bufferedWriter.newLine();
    }
}