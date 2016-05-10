package com.alexswd;

import java.util.ArrayList;
import java.util.List;

public class ElevatorController {
    private List<Elevator> elevators;
    private int maxFloor;
    public ElevatorController(int maxFloor, int elevatorNumber) {
        this.maxFloor = maxFloor;
        elevators = new ArrayList<Elevator>();
        for(int i=0;i<elevatorNumber;++i) {
            Elevator elevator = new Elevator(i+1, maxFloor);
            new Thread(elevator).start();
            elevators.add(elevator);
        }

    }
    public void requestElevator(int fromFloor, int toFloor) {
        Elevator bestCandidate = null;
        int bestDistance = maxFloor;
        boolean movingThrough = false;
        for(int i=0;i<elevators.size();++i) {
            Elevator e = elevators.get(i);
            if( !e.isServiceInProgress() ) {
                Trip t = e.getTrip();
                if (t == null) {
                    if (!movingThrough) {
                        // elevator stopped
                        // calculate distance
                        int d = Math.abs(e.getCurrentFloor() - toFloor);
                        if (d < bestDistance) {
                            bestCandidate = e;
                            bestDistance = d;
                        }
                    }
                } else {
                    if (t.isStarted()) {
                        // elevator occupped and moving to its target floor
                        if (t.getFromFloor() < t.getToFloor()) {
                            // moving up
                            if (e.getCurrentFloor() < fromFloor && t.getToFloor() >= fromFloor) {
                                // elevator will stop on required floor
                                bestCandidate = e;
                                movingThrough = true;
                                bestDistance = 0;
                                // no need to continue, moving through elevator will be used
                                break;
                            }
                        } else {
                            // moving down
                            if (e.getCurrentFloor() > fromFloor && t.getToFloor() <= fromFloor) {
                                // elevator will stop on required floor
                                bestCandidate = e;
                                movingThrough = true;
                                bestDistance = 0;
                                // no need to continue, moving through elevator will be used
                                break;
                            }
                        }
                    }
                }
            }
        }
        if( bestCandidate != null ) {
            bestCandidate.setTrip(new Trip(fromFloor,toFloor));
        }
    }
    public static void main(String[] args) {
        ElevatorController ec = new ElevatorController(33, 10);
        ec.requestElevator(2,4);
        ec.requestElevator(5,17);
        ec.requestElevator(33,1);
    }
}
