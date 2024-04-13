package services;

import entities.Interpolation;
import entities.Point;
import entities.TemperatureMeasurement;
import repositories.TemperatureMeasurementRepository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class FileManagementService {
    private static final String HOME = "src//main//resources//";

    public static void importDatabase(String databaseName) throws IOException {
        File path = new File(HOME + "databases//" + databaseName);
        String[] contents = path.list();

        for (String content : Objects.requireNonNull(contents)) {
            BufferedReader reader = Files.newBufferedReader(Paths.get(path.getAbsolutePath() + "//" + content));
            List<String> lines = new ArrayList<>();
            String line;

            while((line = reader.readLine()) != null){
                lines.add(line);
            }

            reader.close();

            if(databaseName.startsWith("Brazil")){
                readBrazilianDatabaseFile(lines);
            }

            if(databaseName.startsWith("Uruguay")){
                readUruguayanDatabaseFile(lines);
            }
        }
    }

    private static void readBrazilianDatabaseFile(List<String> lines){
        String[] stateRelation = lines.get(1).split(";");
        String[] stationRelation = lines.get(2).split(";");
        String[] latitudeRelation = lines.get(4).replace(",", ".").split(";");
        String[] longitudeRelation = lines.get(5).replace(",", ".").split(";");

        for(String line : lines.subList(9, lines.size())){
            String[] information = line.split(";");

            try{
                double temperature = Double.parseDouble(information[7].replace(",", "."));

                addMeasurementToRepository(stationRelation[1] + " - " + stateRelation[1],
                        Double.parseDouble(latitudeRelation[1]), Double.parseDouble(longitudeRelation[1]),
                        temperature, information[0], information[1].substring(0, 4));
            }catch(ArrayIndexOutOfBoundsException | NumberFormatException ignored){}
        }
    }

    private static void readUruguayanDatabaseFile(List<String> lines){
        for(String line : lines.subList(1, lines.size())){
            String[] information = line.split(",");

            try{
                switch(information[1]){
                    case "CARRASCO":
                        addMeasurementToRepository("Aeropuerto - Carrasco (Uruguay)",
                                -34.833, -56.013,
                                Double.parseDouble(information[2]),
                                Instant.parse(information[0]));
                        break;
                    case "aeropuertomelillag3":
                        addMeasurementToRepository("Aeropuerto - Melilla (Uruguay)",
                                -34.790, -56.266,
                                Double.parseDouble(information[2]),
                                Instant.parse(information[0]));
                        break;
                }
            }catch(ArrayIndexOutOfBoundsException ignored){}
        }
    }

    private static void addMeasurementToRepository(String name, double latitude, double longitude, double temperature, Instant moment){
        TemperatureMeasurementRepository temperatureMeasurementRepository = TemperatureMeasurementRepository.getInstance();

        temperatureMeasurementRepository.addRelation(moment, new TemperatureMeasurement(name, latitude, longitude, temperature));
    }

    private static void addMeasurementToRepository(String name, double latitude, double longitude, double temperature, String date, String time){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/ddHHmm");
        LocalDateTime localDateTime = LocalDateTime.parse(date + time, dateTimeFormatter);
        ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.of("Etc/GMT"));
        Instant moment = zonedDateTime.toInstant();

        addMeasurementToRepository(name, latitude, longitude, temperature, moment);
    }

    public static List<Point> importLocations() throws FileNotFoundException {
        File locationsFile = new File(HOME + "locations.csv");

        List<Point> locations = new ArrayList<>();

        if(locationsFile.exists()){
            Scanner reader = new Scanner(locationsFile);

            String line;

            while(reader.hasNextLine()){
                line = reader.nextLine();

                String[] information = line.split(";");

                locations.add(new Point(information[0], Double.parseDouble(information[1]), Double.parseDouble(information[2])));
            }

            reader.close();
        }

        return locations;
    }

    public static void exportInterpolation(Interpolation interpolation) {
        try {
            FileWriter writer = new FileWriter(HOME + "output//" +
                    interpolation.getMainTemperatureMeasurement().getPoint().getName() + "_" +
                    interpolation.getMainTemperatureMeasurement().getPoint().getLatitude() + "_" +
                    interpolation.getMainTemperatureMeasurement().getPoint().getLongitude() + ".csv",
                    true);

            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            bufferedWriter.write(interpolation.getMoment() + ";" + interpolation.getMainTemperatureMeasurement().getTemperature() + "\n");

            bufferedWriter.close();

            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}