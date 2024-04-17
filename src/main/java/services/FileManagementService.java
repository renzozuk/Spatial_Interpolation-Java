package services;

import entities.Interpolation;
import entities.Point;
import entities.TemperatureMeasurement;
import repositories.TemperatureMeasurementRepository;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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
            System.out.println(content);

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
//        switch (lines.get(1).split(": ")[1]) {
//            case "A001", "A042", "A045", "A046", "A047":
//                name.append(" - DF");
//                break;
//            case "A002", "A003", "A005", "A011", "A012", "A013", "A014", "A015", "A016", "A017", "A022", "A023", "A024", "A025", "A026", "A027", "A028", "A029", "A031", "A032", "A033", "A034", "A035", "A036", "A037", "A056":
//                name.append(" - GO");
//                break;
//            case "A009", "A010", "A018", "A019", "A020", "A021", "A038", "A039", "A040", "A041", "A043", "A044", "A048", "A049", "A050", "A051", "A052", "A053", "A054", "A055":
//                name.append(" - TO");
//                break;
//            case "A101", "A109", "A110", "A111", "A112", "A113", "A117", "A119", "A120", "A121", "A122", "A123", "A124", "A125", "A126", "A128", "A133", "A134", "A144":
//                name.append(" - AM");
//                break;
//            case "A102", "A104", "A108", "A136", "A137", "A138", "A140":
//                name.append(" - AC");
//                break;
//            case "A135":
//                name.append(" - RR");
//                break;
//            case "A201", "A202", "A209", "A210", "A211", "A212", "A213", "A214", "A215", "A216", "A226", "A227", "A228", "A229", "A230", "A231", "A232", "A233", "A234", "A235", "A236", "A239", "A240", "A241", "A245", "A246", "A247", "A248", "A250", "A252", "A253", "A254", "A256":
//                name.append(" - PA");
//                break;
//            case "A203", "A204", "A205", "A206", "A207", "A217", "A218", "A219", "A220", "A221", "A222", "A223", "A224", "A225", "A237", "A238", "A255":
//                name.append(" - MA");
//                break;
//            case "A242", "A244", "A249", "A251":
//                name.append(" - AP");
//                break;
//            case "A303", "A323", "A327", "A353", "A355", "A356", "A371":
//                name.append(" - AL");
//                break;
//            case "A304", "A316", "A317", "A318", "A340", "A344", "A367", "A372":
//                name.append(" - RN");
//                break;
//            case "A305", "A306", "A314", "A315", "A319", "A324", "A325", "A332", "A339", "A342", "A347", "A358", "A359", "A360", "A368", "A369":
//                name.append(" - CE");
//                break;
//            case "A307", "A309", "A322", "A328", "A329", "A341", "A349", "A350", "A351", "A357", "A366", "A370":
//                name.append(" - PE");
//                break;
//            case "A308", "A311", "A312", "A326", "A330", "A331", "A335", "A336", "A337", "A343", "A345", "A346", "A354", "A361", "A363", "A364", "A365", "A374", "A375", "A376", "A377":
//                name.append(" - PI");
//                break;
//            case "A310", "A313", "A320", "A321", "A333", "A334", "A348", "A352", "A373":
//                name.append(" - PB");
//                break;
//            case "A401", "A402", "A404", "A405", "A406", "A407", "A408", "A410", "A412", "A413", "A414", "A415", "A416", "A418", "A422", "A423", "A424", "A425", "A426", "A427", "A428", "A429", "A430", "A431", "A432", "A433", "A434", "A436", "A437", "A438", "A439", "A440", "A441", "A442", "A443", "A444", "A445", "A446", "A447", "A448", "A450", "A452", "A455", "A456", "A458":
//                name.append(" - BA");
//                break;
//            case "A409", "A417", "A419", "A421", "A451", "A453":
//                name.append(" - SE");
//                break;
//            case "A502", "A505", "A506", "A507", "A508", "A509", "A510", "A511", "A512", "A513", "A514", "A515", "A516", "A517", "A518", "A519", "A520", "A521", "A522", "A523", "A524", "A525", "A526", "A527", "A528", "A529", "A530", "A531", "A532", "A533", "A534", "A535", "A536", "A537", "A538", "A539", "A540", "A541", "A542", "A543", "A544", "A545", "A546", "A547", "A548", "A549", "A550", "A551", "A552", "A553", "A554", "A555", "A556", "A557", "A559", "A560", "A561", "A562", "A563", "A564", "A565", "A566", "A567", "A568", "A569", "A570", "A571", "F501":
//                name.append(" - MG");
//                break;
//            case "A601", "A602", "A603", "A604", "A606", "A607", "A608", "A609", "A610", "A611", "A618", "A619", "A620", "A621", "A624", "A625", "A626", "A627", "A628", "A629", "A630", "A636", "A637", "A652", "A659", "A667":
//                name.append(" - RJ");
//                break;
//            case "A612", "A613", "A614", "A615", "A616", "A617", "A622", "A631", "A632", "A633", "A634", "A657":
//                name.append(" - ES");
//                break;
//            case "A701", "A705", "A706", "A707", "A708", "A711", "A712", "A713", "A714", "A715", "A716", "A718", "A725", "A726", "A727", "A728", "A733", "A734", "A735", "A736", "A737", "A738", "A739", "A740", "A741", "A744", "A746", "A747", "A748", "A753", "A755", "A762", "A763", "A764", "A765", "A766", "A768", "A769", "A770", "A771":
//                name.append(" - SP");
//                break;
//            case "A702", "A703", "A704", "A709", "A710", "A717", "A719", "A720", "A721", "A722", "A723", "A724", "A730", "A731", "A732", "A742", "A743", "A749", "A750", "A751", "A752", "A754", "A756", "A758", "A759", "A760", "A761":
//                name.append(" - MS");
//                break;
//            case "A801", "A802", "A803", "A804", "A805", "A808", "A809", "A810", "A811", "A812", "A813", "A826", "A827", "A828", "A829", "A830", "A831", "A832", "A833", "A834", "A836", "A837", "A838", "A839", "A840", "A844", "A852", "A853", "A854", "A856", "A878", "A879", "A880", "A881", "A882", "A883", "A884", "A886", "A887", "A889", "A893", "A894", "A897", "A899", "B807":
//                name.append(" - RS");
//                break;
//            case "A806", "A814", "A815", "A816", "A817", "A841", "A845", "A848", "A851", "A857", "A858", "A859", "A860", "A861", "A862", "A863", "A864", "A865", "A866", "A867", "A868", "A870", "A895", "A898":
//                name.append(" - SC");
//                break;
//            case "A807", "A818", "A819", "A820", "A821", "A822", "A823", "A824", "A825", "A835", "A842", "A843", "A846", "A849", "A850", "A855", "A869", "A871", "A872", "A873", "A874", "A875", "A876", "B803", "B804", "B806":
//                name.append(" - PR");
//                break;
//            case "A901", "A902", "A903", "A904", "A905", "A906", "A907", "A908", "A909", "A910", "A911", "A912", "A913", "A914", "A915", "A916", "A917", "A919", "A920", "A921", "A922", "A923", "A924", "A926", "A927", "A928", "A929", "A930", "A931", "A932", "A933", "A934", "A935", "A936", "A937", "A942", "A943", "A944":
//                name.append(" - MT");
//                break;
//            case "A925", "A938", "A939", "A940":
//                name.append(" - RO");
//                break;
//            default:
//                name.append(" (Brazil)");
//        }

        for(String line : lines.subList(11, lines.size())){
            String[] information = line.split(";");

            try{
                addMeasurementToRepository(lines.getFirst().split(": ")[1],
                        Double.parseDouble(lines.get(2).split(": ")[1]), Double.parseDouble(lines.get(3).split(": ")[1]),
                        Double.parseDouble(information[1]), Instant.parse(information[0]));
            }catch(ArrayIndexOutOfBoundsException | NumberFormatException ignored){}
        }

        System.out.println((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024));
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

        temperatureMeasurementRepository.addRelation(moment, new TemperatureMeasurement(/*name, */latitude, longitude, temperature));
    }

    private static void addMeasurementToRepository(String name, double latitude, double longitude, double temperature, String date, String time){
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-ddHHmm");
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

                locations.add(new Point(/*information[0], */Double.parseDouble(information[1]), Double.parseDouble(information[2])));
            }

            reader.close();
        }

        return locations;
    }

    public static void exportInterpolation(Interpolation interpolation) {
        try {
            FileWriter writer = new FileWriter(HOME + "output//" +
//                    interpolation.getMainTemperatureMeasurement().getPoint().getName() + "_" +
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