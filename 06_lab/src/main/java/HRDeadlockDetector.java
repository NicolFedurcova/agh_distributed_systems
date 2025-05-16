import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class HRDeadlockDetector {
    private int networkSize;
    private int noOfProcesses;
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
            this.noOfProcesses+=noProcesses;
            //create new process with increasing ID based on no of processes in node
            for (int p = lastProcessID; p < noProcesses+lastProcessID; p++) {
                Process process = new Process(p,i);
                node.addToProcesses(process);
            }
            lastProcessID = noProcesses+lastProcessID;
            //loading amount of resources
            int noResources = Integer.parseInt(reader.nextLine());
            //create new resource with increasing ID based on no of resources in node
            for (int r = lastResourceID; r < noResources+lastResourceID; r++) {
                Resource resource = new Resource(r,i);
                node.addToResources(resource);
            }
            lastResourceID = noResources+lastResourceID;
            network.put(node.getID(),node);
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

            Process process = this.network.get(processNodeID).getProcessByID(processID);

            //process requests a resource
            process.requestResource(resourceID, resourceNodeID);

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

            Resource resource = this.network.get(resourceNodeID).getResourceByID(resourceID);
            Process process = this.network.get(processNodeID).getProcessByID(processID);

            //process has set that it is given resourceID
            process.holdResource(resourceID, resourceNodeID);
            //resource has set that it was given to processID
            resource.setHeldBy(processID, processNodeID);

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
            printStateOfTheNetwork();
        } catch (FileNotFoundException | NumberFormatException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) { //06_lab/src/main/resources/input1.txt
        HRDeadlockDetector deadlockDetector = new HRDeadlockDetector();
        deadlockDetector.loadInput();
        Controller controller = new Controller(deadlockDetector.noOfProcesses);
        controller.collectInformation(deadlockDetector.network);

    }
}
