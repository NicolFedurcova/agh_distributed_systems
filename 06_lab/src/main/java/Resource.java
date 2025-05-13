public class Resource {
    private int ID;
    private int homeNodeID;
    private int[] requestingProcessIDAndHomeNodeID;
    private int[] givenToProcessIDAndHomeNodeID;

    public Resource(int ID, int homeNodeID) {
        this.ID = ID;
        this.homeNodeID = homeNodeID;
    }

    public int getID() {
        return ID;
    }

    public int[] getRequestingProcessIDAndHomeNodeID() {
        return requestingProcessIDAndHomeNodeID;
    }

    public void setRequestingProcessIDAndHomeNodeID(int [] requestingProcessIDAndHomeNodeID) {
        this.requestingProcessIDAndHomeNodeID = requestingProcessIDAndHomeNodeID;
    }

    public int[] getGivenToProcessIDAndHomeNodeID() {
        return givenToProcessIDAndHomeNodeID;
    }

    public void setGivenToProcessIDAndHomeNodeID(int[] givenToProcessIDAndHomeNodeID) {
        this.givenToProcessIDAndHomeNodeID = givenToProcessIDAndHomeNodeID;
    }

    @Override
    public String toString() {
        return requestingProcessIDAndHomeNodeID[0]+" -> ["+ID+"] -> "+givenToProcessIDAndHomeNodeID[0];
    }
}
