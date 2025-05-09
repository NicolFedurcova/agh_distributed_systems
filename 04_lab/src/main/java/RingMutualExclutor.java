import java.util.*;

public class RingMutualExclutor {
    private Node nodeInCs = null;
    private int clock;
    private final HashMap<Integer, Node> network = new HashMap<>();
    private final HashMap<Integer, Boolean> satisfied = new HashMap<>();
    private final Queue<Request> requests = new LinkedList<>();
    private int networkSize;

    //method for creation of nodes and saving references to them to map
    public void createNetwork(Integer tokenOwner, Queue<Integer> tokenOwnerWaitQueue){
        for (int i = 0; i < networkSize; i++) {
            int next = -1;
            if(i< networkSize -1){
                //any node is connected to the next node (except last node)
                next = i+1;
            }else{
                //last node is connected to the first node
                next = 0;
            }
            Node node = new Node(i,next);
            //if this node is currect token owner
            if(tokenOwner!=null && i==tokenOwner){
                //if this node has already queue, it's loaded
                if(tokenOwnerWaitQueue!=null) node.setLocalQueue(tokenOwnerWaitQueue);
                //selected node ebters critical section
                node.enterCS(this.clock,null, this.networkSize, false);
                this.nodeInCs = node;
            }
            this.network.put(i,node);
        }

    }

    //method for loading the requests
    private void loadRequests(Scanner scanner) {
        System.out.println("Enter list of indexes(IDs) of nodes requesting for critical section separated by \",\": ");
        String[] requestsStr = scanner.nextLine().split(",");
        for (int i = 0; i < requestsStr.length; i++) {
            Integer requester = Integer.parseInt(requestsStr[i].trim());
            Request request = new Request(false, requester);
            this.requests.add(request);
            this.satisfied.put(requester, false);
        }
        System.out.println("REQUESTS: "+ this.requests);
    }

    //metod for loading the whole input
    public void loadInput(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter number of nodes in unidirectional ring topology: (int) ");
        this.networkSize = Integer.parseInt(scanner.nextLine());
        System.out.println("Is any node owner of the token? (y/n) ");
        String isTokenOwner = scanner.nextLine();
        Integer tokenOwner = null;
        Queue<Integer> tokenOwnerWaitQueue = null;
        if(isTokenOwner.equalsIgnoreCase("y")){
            System.out.println("Enter index(ID) of token owner");
            tokenOwner = Integer.parseInt(scanner.nextLine());
            System.out.println("Has it any nodes waiting for token in its queue? (y/n) ");
            String hasWaitingNodes = scanner.nextLine();
            if(hasWaitingNodes.equalsIgnoreCase("y")){
                tokenOwnerWaitQueue = new LinkedList<>();
                System.out.println("Enter list of indexes(IDs) of waiting nodes divided by \",\": ");
                String[] waitingNodesStr = scanner.nextLine().split(",");
                for (int i = 0; i < waitingNodesStr.length; i++) {
                    if(waitingNodesStr[i]!="")tokenOwnerWaitQueue.add(Integer.parseInt(waitingNodesStr[i].trim()));
                }
            }
        }
        //creation of the network
        createNetwork(tokenOwner,tokenOwnerWaitQueue);
        //loading the requests
        loadRequests(scanner);
        //printing what was inputed
        printStateOfNetwork();
    }

    public boolean areAllRequestsSatisifed(){
        for(boolean val : satisfied.values()){
            if(!val){
                return false;
            }
        }
        return true;
    }

    public void printStateOfNetwork(){
        System.out.println("        STATE OF THE NETWORK");
        for(Node node:this.network.values()){
            System.out.println("        "+node);
        }
        if(nodeInCs!=null) System.out.println("IN CRITICAL SECTION IS "+this.nodeInCs.ID);
    }

    //returns neighbourID to whom pass request
    // or null if received request and nobody is waiting
    //or -1 if smebody entered CS
    public Integer searchForReceiverOfRequest(Integer neighbourID, Request request){
        if(neighbourID==null){
            //receiver of request was found and nobody else is waiting for token of Node is in cs
            this.tryToExitCS();
            return null;
        } else if (neighbourID==-1) {
            //receiver of request was found and receiver entered CS
            this.nodeInCs = this.network.get(request.getRequester());
            return -1;
        }
        //operation will hapen, clock is increased
        this.clock+=1;
        Node next = this.network.get(neighbourID);
        Integer nextNeighbourID = next.getRequest(request, this.clock, this.networkSize);

        return searchForReceiverOfRequest(nextNeighbourID, request);
    }

    //method to check if node can leave critical section
    public void tryToExitCS(){
        if(this.nodeInCs!=null) {
            Integer whenToleave = this.nodeInCs.getCsExitTime();
            if (whenToleave != null && whenToleave <= this.clock) {
                //during exiting CS the node returns next node in line to pass granting message
                // and message in which is it's ID in case it doesnt have empty queue
                this.printStateOfNetwork();
                Response response = this.nodeInCs.exitCS();

                //if response!=null then somebody is waiting for token and granting request is passed
                if (response != null) {
                    searchForReceiverOfRequest(response.getNeighbourID(), response.getRequest());
                }
                //else nobody is waiting for the token, no message is passed and token stays with the node
                //that was previously in critical section
            }
        }
    }

    public void runSimulation(){
        //the simulation time starts
        this.clock = 0;

        //we take requests from queue
        while(!this.requests.isEmpty()){
            //node in Critical section tries to exit
            this.tryToExitCS();

            Request currentRequest = this.requests.poll();
            //set current request as requesting (it was not set while loading input for simplicity of input)
            currentRequest.setGranting(false);
            Node requester = this.network.get(currentRequest.getRequester());
            //operation is about to happen, clock counter changes
            this.clock+=1;
            //if nobody is in critical section, it means it's beggining of computation and nobody has token
            if(this.nodeInCs==null){
                requester.enterCS(this.clock,null,this.networkSize, true);
                this.nodeInCs = requester;
            //somebody is/was in CS, needs to request token
            }else {
                this.searchForReceiverOfRequest(requester.getNeighbourID(), currentRequest);
            }
        }
        //after taking all requests we simulate 30 time unites to let all requests be fullfiled
        for (int i = 0; i < 30; i++) {
            //if the every request was satisfied program stops
            if(this.areAllRequestsSatisifed()){
                return;
            }else{
                this.clock+=1;
                this.tryToExitCS();
            }
        }
    }

    public static void main(String[] args) {
        RingMutualExclutor exclutor = new RingMutualExclutor();
        exclutor.loadInput();
        exclutor.runSimulation();


    }
}
