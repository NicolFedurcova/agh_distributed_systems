public class Request {
    public int recordedClockCounter;
    public int requesterID;

    public Request(int recordedClockCounter, int requesterID) {
        this.recordedClockCounter = recordedClockCounter;
        this.requesterID = requesterID;
    }

    @Override
    public String toString() {
        return "<" +
                recordedClockCounter +", "+
                requesterID +
                ">";
    }
}
