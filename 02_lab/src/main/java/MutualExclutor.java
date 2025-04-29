//Write a program that ensures mutual
//exclusion using Ricart-Agrawala
//algorithm for multiple CS requests in
//a distributed system with at least 4 to
//6 different processes in as many
//sites.

import java.io.*;
import java.util.*;

public class MutualExclutor {



    public void sendMessage(int source, int destination, int[] timeStamp ){

    }


    public static void main(String[] args) { //02_lab/src/main/resources/input.txt
        MutualExclutor exclutor = new MutualExclutor();
        System.out.println("Choose mode: \"step\" for step by step input, \"file\" for input file");
        Scanner scanner = new Scanner(System.in);
        String mode = scanner.nextLine();

        if(mode.equals("step")){
            // Number of nodes
            System.out.print("Enter number of nodes: ");
            int n = Integer.parseInt(scanner.nextLine());
            Network network = new Network(n);

            // Timestamp counters
            System.out.print("Enter timestamp counters separated by commas: ");
            String[] timestampStr = scanner.nextLine().split(",");
            int[] timestamps = new int[timestampStr.length];
            for (int i = 0; i < timestampStr.length; i++) {
                timestamps[i] = Integer.parseInt(timestampStr[i].trim());
            }

            for (int node_idx=0; node_idx<n; node_idx++){
                Node node = new Node(node_idx, timestamps[node_idx],n);
                network.addNode(node);
            }

            // Is a node in the critical section?
            System.out.print("Is any node in the critical section? (y/n): ");
            String inCritical = scanner.nextLine();

            ArrayList<Integer> approvalRequestsToCriticalNode = new ArrayList<>();

            if (inCritical.equalsIgnoreCase("y")) {
                System.out.print("Enter ID of node in critical section: ");
                int criticalNodeId = Integer.parseInt(scanner.nextLine());

                System.out.print("Enter IDs of nodes that requested approval from the node in critical section (comma-separated): ");
                String[] requestIdsStr = scanner.nextLine().split(",");
                for(int i = 0; i < requestIdsStr.length; i++) {
                    approvalRequestsToCriticalNode.add(Integer.parseInt(requestIdsStr[i].trim()));
                }
                Node nodeInCS = network.getNode(criticalNodeId);
                nodeInCS.listOfRequests = approvalRequestsToCriticalNode;
            }

            // Number of waiting nodes
            System.out.print("Enter number of nodes waiting for critical section: ");
            int w = Integer.parseInt(scanner.nextLine());

            //HashMap<Integer, boolean[]> nodesWaitingForCS = new HashMap<>();
            for (int i = 0; i < w; i++) {
                System.out.print("Enter ID of waiting node " + (i + 1) + ": ");
                int waitingNodeId = Integer.parseInt(scanner.nextLine());

                System.out.print("Enter list of booleans (true/false) for approvals separated by commas: ");
                String[] approvalsStr = scanner.nextLine().split(",");

                boolean[] approvals = new boolean[n];
                for (int j = 0; j < approvalsStr.length; j++) {
                    approvals[j]=Boolean.parseBoolean(approvalsStr[j].trim());
                }

                Node nodeWaitingToGetInCS = network.getNode(waitingNodeId);
                nodeWaitingToGetInCS.goAheadResponses = approvals;

                //nodesWaitingForCS.put(waitingNodeId, approvals);
            }

            // Requests to be made
            System.out.println("Enter requests to be made (format: timestamp,nodeID). Type 'done' to finish:");
            ArrayList<Request> requests = new ArrayList<>();
            while (true) {
                String line = scanner.nextLine().trim();
                if (line.equalsIgnoreCase("done")) break;

                String[] parts = line.split(",");
                int timestamp = Integer.parseInt(parts[0].trim());
                int requesterId = Integer.parseInt(parts[1].trim());
                requests.add(new Request(timestamp, requesterId));
            }

            network.soutStatusOfNetwork();
            System.out.println("Requests to be made:");
            for (Request req : requests) {
                System.out.println("Timestamp: " + req.timeStampCounter + ", Node ID: " + req.requesterID);
            }


        } else if (mode.equals("file")) {
            System.out.println("Enter file path:");
            String path = scanner.nextLine();

            try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
                // Number of nodes
                int n = Integer.parseInt(reader.readLine().trim());
                Network network = new Network(n);

                // Timestamp counters
                String[] timestampStr = reader.readLine().trim().split(",");
                int[] timestamps = new int[timestampStr.length];
                for (int i = 0; i < timestampStr.length; i++) {
                    timestamps[i] = Integer.parseInt(timestampStr[i].trim());
                }

                for (int node_idx=0; node_idx<n; node_idx++){
                    Node node = new Node(node_idx, timestamps[node_idx],n);
                    network.addNode(node);
                }

                // Is a node in the critical section?
                String inCritical = reader.readLine().trim();
                ArrayList<Integer> approvalRequestsToCriticalNode = new ArrayList<>();

                if (inCritical.equalsIgnoreCase("y")) {
                    int criticalNodeId = Integer.parseInt(reader.readLine().trim());
                    String[] requestIdsStr = reader.readLine().trim().split(",");
                    for (int i = 0; i < requestIdsStr.length; i++) {
                        approvalRequestsToCriticalNode.add(Integer.parseInt(requestIdsStr[i].trim()));
                    }
                    Node nodeInCS = network.getNode(criticalNodeId);
                    nodeInCS.listOfRequests = approvalRequestsToCriticalNode;
                }

                // Number of nodes waiting
                int w = Integer.parseInt(reader.readLine().trim());
                //HashMap<Integer, List<Boolean>> waitingNodesApprovals = new HashMap<>();

                for (int i = 0; i < w; i++) {
                    int waitingNodeId = Integer.parseInt(reader.readLine().trim());
                    String[] approvalsStr = reader.readLine().trim().split(",");
                    boolean[] approvals = new boolean[n];
                    for (int j = 0; j < approvalsStr.length; j++) {
                        approvals[j] = Boolean.parseBoolean(approvalsStr[j].trim());
                    }
                    Node nodeWaitingToGetInCS = network.getNode(waitingNodeId);
                    nodeWaitingToGetInCS.goAheadResponses = approvals;
                    //waitingNodesApprovals.put(waitingNodeId, approvals);
                }

                // Requests (until end of file)
                List<Request> requests = new ArrayList<>();
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) continue;
                    String[] parts = line.trim().split(",");
                    int timestamp = Integer.parseInt(parts[0].trim());
                    int requesterId = Integer.parseInt(parts[1].trim());
                    requests.add(new Request(timestamp, requesterId));
                }

                network.soutStatusOfNetwork();
                System.out.println("Requests to be made:");
                for (Request req : requests) {
                    System.out.println("Timestamp: " + req.timeStampCounter + ", Node ID: " + req.requesterID);
                }

            } catch (IOException e) {
                System.err.println("Error reading file: " + e.getMessage());
            }

            scanner.close();



        }else{
            System.out.println("Mode was not inputed correctly");
        }

    }
}

