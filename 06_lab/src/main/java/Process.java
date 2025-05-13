public class Process {
    private int ID;
    private int homeNodeID;
    private int[] requestingResourceIDAndHomeNodeID;
    private int [] givenResourceIDAndHomeNodeID;

    public Process(int ID, int homeNodeID) {
        this.ID = ID;
        this.homeNodeID = homeNodeID;

    }

    public int getID() {
        return ID;
    }

    public int[] getRequestingResourceIDAndHomeNodeID() {
        return requestingResourceIDAndHomeNodeID;
    }

    public void setRequestingResourceIDAndHomeNodeID(int[] requestingResourceIDAndHomeNodeID) {
        this.requestingResourceIDAndHomeNodeID = requestingResourceIDAndHomeNodeID;
    }

    public int[] getGivenResourceIDAndHomeNodeID() {
        return givenResourceIDAndHomeNodeID;
    }

    public void setGivenResourceIDAndHomeNodeID(int[] givenResourceIDAndHomeNodeID) {
        this.givenResourceIDAndHomeNodeID = givenResourceIDAndHomeNodeID;
    }

    @Override
    public String toString() {
        return givenResourceIDAndHomeNodeID[0]+" -> ("+ID+") -> "+requestingResourceIDAndHomeNodeID[0];
    }
}
