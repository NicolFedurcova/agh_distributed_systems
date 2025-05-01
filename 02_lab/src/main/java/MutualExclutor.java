//Write a program that ensures mutual
//exclusion using Ricart-Agrawala
//algorithm for multiple CS requests in
//a distributed system with at least 4 to
//6 different processes in as many
//sites.

import java.io.*;
import java.util.*;

public class MutualExclutor {
    public boolean isRequestSucessful(Node requester){
        boolean isSucessful = true;
        for(int goAhead:requester.goAheadResponses){
            if (goAhead==0){
                isSucessful=false;
            }
        }
        return isSucessful;
    }

    public void executeRequests(Network network, ArrayList<Request> requests){
        HashMap<Integer, Boolean> requesters = new HashMap<>();

        for(Request request : requests){
            Node requester = network.getNode(request.requesterID);
            //requesting all nodes in network for approval to go in critical section
            for(int destinationNodeID : network.nodes.keySet()){
                if(destinationNodeID!=requester.ID){
                    //sends request
                    Boolean response = requester.sendRequestForCS(network.getNode(destinationNodeID));
                    //process response
                    requester.processResponse(response, destinationNodeID);
                }
            }

            if(isRequestSucessful(requester)){
                network.soutStatusOfNetwork();
                requester.enterCS();
            }

        }
    }

    public void loadInputFromConsole(Network network, int n, ArrayList<Request> requests, Scanner scanner){
        System.out.print("Is at least one node's timestamp higher than 0? (y/n): ");
        String varyingTimestamps = scanner.nextLine();

        if (varyingTimestamps.equalsIgnoreCase("y")) {
            // clockCounters vary
            System.out.print("Enter timestamp counters (integers) separated by commas: ");
            String[] timestampStr = scanner.nextLine().split(",");
            int[] timestamps = new int[timestampStr.length];
            for (int i = 0; i < timestampStr.length; i++) {
                timestamps[i] = Integer.parseInt(timestampStr[i].trim());
            }

            for (int node_idx = 0; node_idx < n; node_idx++) {
                Node node = new Node(node_idx, timestamps[node_idx], n);
                network.addNode(node);
            }
        } else {
            // all clockCounters are 0
            for (int node_idx = 0; node_idx < n; node_idx++) {
                Node node = new Node(node_idx,n);
                network.addNode(node);
            }
        }

        // Is a node in the critical section?
        System.out.print("Is any node in the critical section? (y/n): ");
        String inCritical = scanner.nextLine();

        ArrayList<Integer> approvalRequestsToCriticalNode = new ArrayList<>();

        if (inCritical.equalsIgnoreCase("y")) {
            System.out.print("Enter ID of node in critical section: (int)");
            int criticalNodeId = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter IDs (integers) of nodes that requested approval from the node in critical section (comma-separated): ");
            String[] requestIdsStr = scanner.nextLine().split(",");
            for(int i = 0; i < requestIdsStr.length; i++) {
                approvalRequestsToCriticalNode.add(Integer.parseInt(requestIdsStr[i].trim()));
            }

            Node nodeInCS = network.getNode(criticalNodeId);
            nodeInCS.isInCS = true;
            nodeInCS.listOfRequests = approvalRequestsToCriticalNode;
        }

        // Number of waiting nodes
        System.out.print("Enter number of nodes waiting for critical section: (int)");
        int w = Integer.parseInt(scanner.nextLine());

        for (int i = 0; i < w; i++) {
            System.out.print("Enter ID of waiting node: (int)");
            int waitingNodeId = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter clockCounter (time) when node with ID"+ waitingNodeId+"requested to enter CS: (int)");
            int requestClockCounter = Integer.parseInt(scanner.nextLine());

            System.out.print("Enter list of integers (1/0) = received approvals separated by commas: ");
            String[] approvalsStr = scanner.nextLine().split(",");
            int[] approvals = new int[n];
            for (int j = 0; j < approvalsStr.length; j++) {
                approvals[j]=Integer.parseInt(approvalsStr[j].trim());
            }

            Node nodeWaitingToGetInCS = network.getNode(waitingNodeId);
            nodeWaitingToGetInCS.goAheadResponses = approvals;
            nodeWaitingToGetInCS.wantsToGetInCS = true;
            nodeWaitingToGetInCS.lastSentRequest = new Request(requestClockCounter, waitingNodeId);

            //nodesWaitingForCS.put(waitingNodeId, approvals);
        }

        // Requests to be made
        System.out.println("Enter requests to be made (format: timestamp,nodeID). Type 'done' to finish:");
        requests = new ArrayList<>();
        while (true) {
            String line = scanner.nextLine().trim();
            if (line.equalsIgnoreCase("done")) break;

            String[] parts = line.split(",");
            int timestamp = Integer.parseInt(parts[0].trim());
            int requesterId = Integer.parseInt(parts[1].trim());
            requests.add(new Request(timestamp, requesterId));
        }
    }

    public void loadInputFromFile(Network network,int n, ArrayList<Request> requests, BufferedReader reader) throws IOException {
        //Is at least one node's timestamp higher than 0?
        String varyingTimestamps = reader.readLine().trim();

        if (varyingTimestamps.equalsIgnoreCase("y")) {
            // clockCounters vary
            String[] timestampStr = reader.readLine().trim().split(",");
            int[] timestamps = new int[timestampStr.length];
            for (int i = 0; i < timestampStr.length; i++) {
                timestamps[i] = Integer.parseInt(timestampStr[i].trim());
            }

            for (int node_idx = 0; node_idx < n; node_idx++) {
                Node node = new Node(node_idx, timestamps[node_idx], n);
                network.addNode(node);
            }
        } else{
            //all clockCounters are 0
            for (int node_idx = 0; node_idx < n; node_idx++) {
                Node node = new Node(node_idx, n);
                network.addNode(node);
            }
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
            nodeInCS.isInCS = true;
        }

        // Number of nodes waiting
        int w = Integer.parseInt(reader.readLine().trim());

        for (int i = 0; i < w; i++) {
            //node ID
            int waitingNodeId = Integer.parseInt(reader.readLine().trim());
            //clockCounter when node requested for entering CS
            int requestClockCounter = Integer.parseInt(reader.readLine().trim());
            //list of awarded approvals
            String[] approvalsStr = reader.readLine().trim().split(",");
            int[] approvals = new int[n];
            for (int j = 0; j < approvalsStr.length; j++) {
                approvals[j] = Integer.parseInt(approvalsStr[j].trim());
            }
            Node nodeWaitingToGetInCS = network.getNode(waitingNodeId);
            nodeWaitingToGetInCS.goAheadResponses = approvals;
            nodeWaitingToGetInCS.wantsToGetInCS = true;
            nodeWaitingToGetInCS.lastSentRequest = new Request(requestClockCounter,waitingNodeId);
        }

        // Requests (until end of file)
        requests = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.trim().split(",");
            int timestamp = Integer.parseInt(parts[0].trim());
            int requesterId = Integer.parseInt(parts[1].trim());
            requests.add(new Request(timestamp, requesterId));
        }
    }


    public static void main(String[] args) { //02_lab/src/main/resources/input.txt
        MutualExclutor exclutor = new MutualExclutor();
        System.out.println("Choose mode: \"step\" for step by step input, \"file\" for input file");
        Scanner scanner = new Scanner(System.in);
        String mode = scanner.nextLine();
        ArrayList<Request> requests = new ArrayList<>();
        Network network = new Network();

        if(mode.equals("step")){
            // Number of nodes
            System.out.print("Enter number of nodes: (int)");
            int n = Integer.parseInt(scanner.nextLine());
            network = new Network(n);

            //loading information from console
            exclutor.loadInputFromConsole(network,n,requests,scanner);
            scanner.close();

            network.soutStatusOfNetwork();
            System.out.println("Requests to be made:");
            for (Request req : requests) {
                System.out.println("<" + req.recordedClockCounter + ", " + req.requesterID+">");
            }
        } else if (mode.equals("file")) {
            System.out.println("Enter file path:");
            String path = scanner.nextLine();

            try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
                // Number of nodes
                int n = Integer.parseInt(reader.readLine().trim());
                network = new Network(n);

                //loading information from file
                exclutor.loadInputFromFile(network, n, requests, reader);

                network.soutStatusOfNetwork();
                System.out.println("Requests to be made:");
                for (Request req : requests) {
                    System.out.println("<" + req.recordedClockCounter + ", " + req.requesterID+">");
                }
            } catch (IOException e) {
                System.err.println("Error reading file: " + e.getMessage());
            }
        }else{
            System.out.println("Mode was not inputed correctly");
            return;
        }

        //execution of requests
        exclutor.executeRequests(network, requests);

    }
}

