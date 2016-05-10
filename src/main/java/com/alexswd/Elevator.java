package com.alexswd;


public class Elevator implements Runnable {
    private int id;
    private int maxFloor;
    private int currentFloor;
    private Trip trip;
    private boolean occupped;
    private boolean shutdownRequested;

    public Elevator(int id, int maxFloor) {
        this.id = id;
        this.maxFloor = maxFloor;
        currentFloor = 1;
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

    @Override
    public void run() {
        while( true ) {
            try {
                Thread.sleep(1000);
                if( this.trip != null ) {
                    // do staff
                }
            }
            catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}
