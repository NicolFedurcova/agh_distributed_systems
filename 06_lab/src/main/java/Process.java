import java.util.HashSet;
import java.util.Set;

public class Process {
    private int ID;
    private int homeNodeID;
    private Set<String> requestedResources = new HashSet<>();
    private Set<String> heldResources = new HashSet<>();

    public Process(int ID, int homeNodeID) {
        this.ID = ID;
        this.homeNodeID = homeNodeID;

    }

    public int getID() {
        return ID;
    }

    public int getHomeNodeID() { return homeNodeID; }

    public void requestResource(int resID, int nodeID) {
        requestedResources.add(resID + "@" + nodeID);
    }

    public void holdResource(int resID, int nodeID) {
        heldResources.add(resID + "@" + nodeID);
    }

    public Set<String> getRequestedResources() { return requestedResources; }

    @Override
    public String toString() {
        return "Process " + ID + " in node (@)" + homeNodeID + " holds=" + heldResources + ", requests=" + requestedResources+"\n";
    }
}
