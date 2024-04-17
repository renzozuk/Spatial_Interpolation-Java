package entities;

import repositories.TemperatureMeasurementRepository;
import util.MomentConverter;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Interpolation {
    private TemperatureMeasurement mainTemperatureMeasurement;
    private Instant moment;
    private Map<TemperatureMeasurement, Double> placeDistanceRelation;

    public Interpolation(Point mainPlace, Instant moment){
        this.moment = moment;

        try{
            placeDistanceRelation = TemperatureMeasurementRepository.getInstance().getMomentTemperatureMeasurementRelation().get(moment)
                    .stream()
                    .collect(Collectors.toMap(
                            tm -> tm,
                            tm -> tm.getPoint().getDistanceFromAnotherPoint(mainPlace)
                    ));
        }catch(NullPointerException ignored){}

        mainTemperatureMeasurement = new TemperatureMeasurement(mainPlace, calculateTemperatureForMainPlace());
    }

    public Interpolation(Point mainPlace, String moment){
        this(mainPlace, MomentConverter.mapToInstant(moment));
    }

    public Interpolation(String name, double latitude, double longitude, Instant moment){
        this(new Point(/*name, */latitude, longitude), moment);
    }

    public Interpolation(String name, double latitude, double longitude, String moment){
        this(name, latitude, longitude, MomentConverter.mapToInstant(moment));
    }

    public TemperatureMeasurement getMainTemperatureMeasurement() {
        return mainTemperatureMeasurement;
    }

    public Instant getMoment() {
        return moment;
    }

    public Map<TemperatureMeasurement, Double> getPlaceDistanceRelation() {
        return placeDistanceRelation;
    }

    public double calculateTemperatureForMainPlace(){
        double numerator = 0.0;
        double denominator = 0.0;
        double powerParameter = 2.5;

//        try{
            for(TemperatureMeasurement measurement : placeDistanceRelation.keySet()){
                numerator += measurement.getTemperature() / Math.pow(placeDistanceRelation.get(measurement), powerParameter);
                denominator += 1 / Math.pow(placeDistanceRelation.get(measurement), powerParameter);
            }
//        }catch(NullPointerException ignored){}

        return numerator / denominator;
    }

//    @Override
//    public String toString() {
//        StringBuilder result = new StringBuilder();
//
//        result.append(mainTemperatureMeasurement).append(" on ");
//
//        Pattern minusThreePattern = Pattern.compile(".*(DF|GO|TO|PA|AP|MA|PI|CE|RN|PB|PE|AL|SE|BA|ES|MG|RJ|SP|PR|SC|RS|\\(Argentina\\)|\\(Uruguay\\))$");
//        Matcher minusThreeMatcher = minusThreePattern.matcher(mainTemperatureMeasurement.getPoint().getName());
//
//        Pattern minusFourPattern = Pattern.compile(".*(RR|AM|RO|MT|MS)$");
//        Matcher minusFourMatcher = minusFourPattern.matcher(mainTemperatureMeasurement.getPoint().getName());
//
//        Pattern minusFivePattern = Pattern.compile(".*(AC)$");
//        Matcher minusFiveMatcher = minusFivePattern.matcher(mainTemperatureMeasurement.getPoint().getName());
//
//        if(minusThreeMatcher.matches()){
//            result.append(DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.of("Etc/GMT+3")).format(moment))
//                    .append(" at ")
//                    .append(DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.of("Etc/GMT+3")).format(moment))
//                    .append(" (UTC-03:00)");
//        }else if(minusFourMatcher.matches()){
//            result.append(DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.of("Etc/GMT+4")).format(moment))
//                    .append(" at ")
//                    .append(DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.of("Etc/GMT+4")).format(moment))
//                    .append(" (UTC-04:00)");
//        }else if(minusFiveMatcher.matches()){
//            result.append(DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.of("Etc/GMT+5")).format(moment))
//                    .append(" at ")
//                    .append(DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.of("Etc/GMT+5")).format(moment))
//                    .append(" (UTC-05:00)");
//        }else{
//            result.append(DateTimeFormatter.ofPattern("yyyy-MM-dd").withZone(ZoneId.of("Etc/GMT")).format(moment))
//                    .append(" at ")
//                    .append(DateTimeFormatter.ofPattern("HH:mm").withZone(ZoneId.of("Etc/GMT")).format(moment))
//                    .append(" (UTC+00:00)");
//        }
//
//        result.append("\n\n");
//
//        return result.toString();
//    }
}
