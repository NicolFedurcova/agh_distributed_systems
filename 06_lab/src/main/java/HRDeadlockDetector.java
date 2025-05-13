import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class HRDeadlockDetector {
    private int networkSize;
    private final HashMap<Integer, Node> network = new HashMap<>();

    public void printStateOfTheNetwork(){
        System.out.println("---------------------STATE OF THE NETWORK");
        for(Node node: network.values()){
            System.out.println(node);
        }
    }
    //method for loading the network with processes and resources
    public void loadNetwork(Scanner reader) {
        int lastProcessID = 0;
        int lastResourceID = 0;
        this.networkSize = Integer.parseInt(reader.nextLine());
        for (int i = 0; i < this.networkSize; i++) {
            Node node = new Node(i);
            //loading amount of processes
            int noProcesses = Integer.parseInt(reader.nextLine());
            for (int p = lastProcessID; p < noProcesses; p++) {
                Process process = new Process(p,i);
                node.addToProcesses(process);
                lastProcessID = p;
            }
            //loading amount of resources
            int noResources = Integer.parseInt(reader.nextLine());
            for (int r = lastResourceID; r < noResources; r++) {
                Resource resource = new Resource(r,i);
                node.addToResources(resource);
                lastResourceID = r;
            }

        }
    }

    //method for loading which processes are requesting which resources
    public void loadRequests(Scanner reader) {
        int noRequests = Integer.parseInt(reader.nextLine());
        for (int r = 0; r < noRequests; r++) {
            String[] requestsStr = reader.nextLine().split(",");
            //process requesting for resource
            int processID = Integer.parseInt(requestsStr[0].trim());
            int processNodeID = Integer.parseInt(requestsStr[1].trim());
            int resourceID = Integer.parseInt(requestsStr[2].trim());
            int resourceNodeID = Integer.parseInt(requestsStr[3].trim());
            //process has set that it is requesting resourceID
            Process process = this.network.get(processNodeID).getProcessByID(processID);
            process.setRequestingResourceIDAndHomeNodeID(new int[] {resourceID,resourceNodeID});
            //resource has set that it was requested by processID
            Resource resource = this.network.get(resourceNodeID).getResourceByID(resourceID);
            resource.setRequestingProcessIDAndHomeNodeID(new int[] {processID,processNodeID});
        }
    }

    //method for loading which resources were given to which processes
    public void loadGrantings(Scanner reader) {
        int noGrantings = Integer.parseInt(reader.nextLine());
        for (int g = 0; g < noGrantings; g++) {
            String[] grantingsStr = reader.nextLine().split(",");
            //resource given to process
            int resourceID = Integer.parseInt(grantingsStr[0].trim());
            int resourceNodeID = Integer.parseInt(grantingsStr[1].trim());
            int processID = Integer.parseInt(grantingsStr[2].trim());
            int processNodeID = Integer.parseInt(grantingsStr[3].trim());

            //resource has set that it was given to processID
            Resource resource = this.network.get(resourceNodeID).getResourceByID(resourceID);
            resource.setGivenToProcessIDAndHomeNodeID(new int[] {processID, processNodeID});

            //process has set that it is given resourceID
            Process process = this.network.get(processNodeID).getProcessByID(processID);
            process.setGivenResourceIDAndHomeNodeID(new int[]{resourceID, resourceNodeID});

        }
    }

    //method for loading the whole input: network, requests and grants
    public void loadInput() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter input file path:");
        String path = scanner.nextLine();
        try (Scanner reader = new Scanner(new File(path))) {
            loadNetwork(reader);
            loadRequests(reader);
            loadGrantings(reader);
        } catch (FileNotFoundException | NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        HRDeadlockDetector deadlockDetector = new HRDeadlockDetector();
        deadlockDetector.loadInput();
        deadlockDetector.printStateOfTheNetwork();
        Controller controller = new Controller(deadlockDetector.networkSize);
        controller.collectInformation(deadlockDetector.network);

    }
}
