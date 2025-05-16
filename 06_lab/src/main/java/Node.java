import java.util.HashMap;

public class Node {
    private int ID;
    private HashMap<Integer, Process> processes = new HashMap<>();
    private HashMap<Integer, Resource> resources = new HashMap<>();

    public Node(int ID) {
        this.ID = ID;
    }

    public int getID() {
        return ID;
    }

    public Process getProcessByID(int processID) {
        return this.processes.get(processID);
    }

    public Resource getResourceByID(int resourceID) {
        return resources.get(resourceID);
    }


    public HashMap<Integer, Resource> getResources() {
        return resources;
    }

    public void addToResources(Resource resource) {
        this.resources.put(resource.getID(), resource);
    }

    public HashMap<Integer, Process> getProcesses() {
        return processes;
    }

    public void addToProcesses(Process process) {
        this.processes.put(process.getID(), process);
    }

    @Override
    public String toString() {
        return "{ NODE " + ID +": \n"+
                "processes:\n" + processes.values() +
                "\nresources:\n" + resources.values() +
                "}";
    }
}
