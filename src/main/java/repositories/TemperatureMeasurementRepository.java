package repositories;

import entities.TemperatureMeasurement;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemperatureMeasurementRepository {
    private static TemperatureMeasurementRepository temperatureMeasurementRepository;
    private Map<Instant, List<TemperatureMeasurement>> momentTemperatureMeasurementRelation;

    private TemperatureMeasurementRepository() {
        momentTemperatureMeasurementRelation = new HashMap<>();
    }

    public static TemperatureMeasurementRepository getInstance(){
        if(temperatureMeasurementRepository == null){
            temperatureMeasurementRepository = new TemperatureMeasurementRepository();
        }

        return temperatureMeasurementRepository;
    }

    public Map<Instant, List<TemperatureMeasurement>> getMomentTemperatureMeasurementRelation() {
        return momentTemperatureMeasurementRelation;
    }

    public void addRelation(Instant moment, TemperatureMeasurement temperatureMeasurement){
        if(momentTemperatureMeasurementRelation.containsKey(moment)){
            momentTemperatureMeasurementRelation.get(moment).add(temperatureMeasurement);
        }else{
            List<TemperatureMeasurement> temperatureMeasurements = new ArrayList<>();

            temperatureMeasurements.add(temperatureMeasurement);

            momentTemperatureMeasurementRelation.put(moment, temperatureMeasurements);
        }
    }
}
