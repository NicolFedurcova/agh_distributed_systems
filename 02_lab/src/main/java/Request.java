public class Request {
    public int timeStampCounter;
    public int requesterID;

    public Request(int timeStampCounter, int requesterID) {
        this.timeStampCounter = timeStampCounter;
        this.requesterID = requesterID;
    }

    @Override
    public String toString() {
        return "<" +
                timeStampCounter +", "+
                requesterID +
                ">";
    }
}
