public class Response {
    public int recordedClockCounter;
    public int responserID;
    public Boolean answer;

    public Response(int recordedClockCounter, int requesterID, Boolean answer) {
        this.recordedClockCounter = recordedClockCounter;
        this.responserID = requesterID;
        this.answer = answer;
    }

    @Override
    public String toString() {
        return "<" +
                recordedClockCounter +", "+
                responserID +", "+
                answer+
                ">";
    }
}
