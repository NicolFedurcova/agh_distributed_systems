public class Resource {
    private int ID;
    private int homeNodeID;
    private String heldBy = null; // Format: "procID@nodeID"

    public Resource(int ID, int homeNodeID) {
        this.ID = ID;
        this.homeNodeID = homeNodeID;
    }

    public int getID() {
        return ID;
    }


    public void setHeldBy(int procID, int nodeID) {
        this.heldBy = procID + "@" + nodeID;
    }

    public String getHeldBy() { return heldBy; }

    @Override
    public String toString() {
        return "Resource" + ID + " in home node (@) " + homeNodeID + " held by: " + heldBy+"\n";
    }
}
