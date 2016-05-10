package com.alexswd;


import java.util.ArrayList;
import java.util.List;

public class Elevator implements Runnable {
    private int id;
    private int maxFloor;
    private int currentFloor;
    private Trip trip;
    private boolean occupped;
    private boolean shutdownRequested;
    private List<Trip> trips;
    private boolean serviceInProgress;
    private int serviceTime;

    public Elevator(int id, int maxFloor) {
        this.id = id;
        this.maxFloor = maxFloor;
        currentFloor = 1;
        trips = new ArrayList<Trip>();
    }

    private void reportStatus() {

    }
    public void doTrip(int fromFloor, int toFloor) {
        if( this.trip == null ) {
            this.trip = new Trip(fromFloor, toFloor);
        }
    }
    public void shutdown() {
        shutdownRequested = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMaxFloor() {
        return maxFloor;
    }

    public void setMaxFloor(int maxFloor) {
        this.maxFloor = maxFloor;
    }

    public int getCurrentFloor() {
        return currentFloor;
    }

    public void setCurrentFloor(int currentFloor) {
        this.currentFloor = currentFloor;
    }

    public Trip getTrip() {
        return trip;
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public boolean isOccupped() {
        return occupped;
    }

    public void setOccupped(boolean occupped) {
        this.occupped = occupped;
    }

    public boolean isShutdownRequested() {
        return shutdownRequested;
    }

    public void setShutdownRequested(boolean shutdownRequested) {
        this.shutdownRequested = shutdownRequested;
    }

    public boolean isServiceInProgress() {
        return serviceInProgress;
    }

    @Override
    public void run() {
        while( true ) {
            try {
                Thread.sleep(1000);
                if( shutdownRequested ) {
                    break;
                }
                if( serviceInProgress ) {
                    if( ++serviceTime < 100 ) {
                        continue;
                    }
                    else
                    {
                        serviceInProgress=false;
                    }
                }
                if( this.trip != null ) {
                    // do staff
                    if( this.trip.isStarted() ) {
                        if( this.trip.getFromFloor() < this.trip.getToFloor() ) {
                            // moving up
                            if( currentFloor < maxFloor ) {
                                currentFloor++;
                                System.out.println(String.format("Elevator %d, floor %d", id, currentFloor));
                            }
                        }
                        else {
                            // moving down
                            if( currentFloor > 1 ) {
                                --currentFloor;
                                System.out.println(String.format("Elevator %d, floor %d", id, currentFloor));
                            }
                        }
                        if( this.trip.getToFloor() == currentFloor ) {
                            this.trip.setStarted(false);
                            occupped = false;
                            trips.add(trip);
                            this.trip=null;
                            System.out.println(String.format("Elevator %d, door opened, trip ended", id));
                            if( trips.size() == 100 ) {
                                this.serviceInProgress = true;
                                this.serviceTime = 0;
                            }
                        }
                    }
                    else {
                        // moving to fromFloor
                        if( currentFloor < this.trip.getFromFloor() ) {
                            if( currentFloor < maxFloor ) {
                                ++currentFloor;
                                System.out.println(String.format("Elevator %d, floor %d", id, currentFloor));
                            }
                        }
                        else {
                            if( currentFloor > 1 ) {
                                --currentFloor;
                                System.out.println(String.format("Elevator %d, floor %d", id, currentFloor));
                            }
                        }
                        if( currentFloor == this.trip.getFromFloor() ) {
                            this.setOccupped(true);
                            this.trip.setStarted(true);
                            System.out.println(String.format("Elevator %d, door opened, trip started", id));
                        }
                    }
                }
            }
            catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}
