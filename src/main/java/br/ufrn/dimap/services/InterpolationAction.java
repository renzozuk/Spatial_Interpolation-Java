package br.ufrn.dimap.services;

import br.ufrn.dimap.entities.UnknownPoint;
import br.ufrn.dimap.repositories.LocationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.logging.Logger;

public class InterpolationAction extends RecursiveAction {
    private final List<UnknownPoint> unknownPoints;
    private final int subListsQuantity;

    private final int THRESHOLD = Runtime.getRuntime().availableProcessors();
    private static final Logger logger = Logger.getAnonymousLogger();

    public InterpolationAction() {
        this.unknownPoints = LocationRepository.getInstance().getUnknownPointsAsList();
        this.subListsQuantity = 0;
    }

    public InterpolationAction(List<UnknownPoint> unknownPoints, int subListsQuantity) {
        this.unknownPoints = unknownPoints;
        this.subListsQuantity = subListsQuantity;
    }

    @Override
    protected void compute() {
        if(subListsQuantity < THRESHOLD){
            ForkJoinTask.invokeAll(createSubtasks());
        }else{
            processing(unknownPoints);
        }
    }

    private List<InterpolationAction> createSubtasks() {
        List<InterpolationAction> subtasks = new ArrayList<>();

        subtasks.add(new InterpolationAction(unknownPoints.subList(0, unknownPoints.size() / 2), subListsQuantity + 2));
        subtasks.add(new InterpolationAction(unknownPoints.subList(unknownPoints.size() / 2, unknownPoints.size()), subListsQuantity + 2));

        return subtasks;
    }

    private void processing(List<UnknownPoint> unknownPoints) {
        InterpolationService.assignTemperatureToUnknownPoints(unknownPoints);
        logger.info("This result was processed by " + Thread.currentThread().getName());
    }
}
