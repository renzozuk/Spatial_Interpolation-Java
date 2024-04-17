package repositories;

import entities.TemperatureMeasurement;

import java.time.Instant;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TemperatureMeasurementRepository {
    private static TemperatureMeasurementRepository temperatureMeasurementRepository;
    private Map<Instant, Set<TemperatureMeasurement>> momentTemperatureMeasurementRelation;

    private TemperatureMeasurementRepository() {
        momentTemperatureMeasurementRelation = new HashMap<>();
    }

    public static TemperatureMeasurementRepository getInstance(){
        if(temperatureMeasurementRepository == null){
            temperatureMeasurementRepository = new TemperatureMeasurementRepository();
        }

        return temperatureMeasurementRepository;
    }

    public Map<Instant, Set<TemperatureMeasurement>> getMomentTemperatureMeasurementRelation() {
        return momentTemperatureMeasurementRelation;
    }

    public void addRelation(Instant moment, TemperatureMeasurement temperatureMeasurement){
        if(momentTemperatureMeasurementRelation.containsKey(moment)){
            momentTemperatureMeasurementRelation.get(moment).add(temperatureMeasurement);
        }else{
            Set<TemperatureMeasurement> temperatureMeasurements = new HashSet<>();

            temperatureMeasurements.add(temperatureMeasurement);

            momentTemperatureMeasurementRelation.put(moment, temperatureMeasurements);
        }
    }
}
