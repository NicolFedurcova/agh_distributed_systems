import java.io.*;
import java.util.*;

public class Raymond {

    private Node root;
    private final HashMap<Integer, Node> nodes = new HashMap<>();
    private final HashMap<Integer, Boolean> satisfied = new HashMap<>();
    private final Queue<Integer> requests = new LinkedList<>();
    private int clock = 0;


    public void loadNetwork(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter file path: "); //03_lab/src/main/resources/input1.txt
        String path = scanner.nextLine();
        int matrixSize = -1;
        int counter = 0;
        Node root = null;

        try (Scanner reader = new Scanner(new File(path))) {
            while (reader.hasNextLine()) {
                String row = reader.nextLine();
                //assuming the elements of matrix are separated by one space
                String[] rowArrayStr = row.split(" ");
                if (matrixSize == -1) {
                    matrixSize = rowArrayStr.length;
                }

                //as default we create node with parent null
                Node node = new Node(counter, null);
                for (int i = 0; i < matrixSize; i++) {

                    int parent = Integer.parseInt(rowArrayStr[i]);

                    //if current value is not zero and no parent node was set up we set up parent node
                    //if a parnent of node was already set up we ignore this edge as it is incorrect input
                    if (parent!=0 && node.getParent()==null) {
                        node.changeParentNodeTo(i);
                    }
                }

                if(node.getParent()==null){
                    //no parent was set up so this is root that is not in critical section
                    node.obtainInitToken();
                    this.root = node;

                }
                this.nodes.put(node.ID, node);
                counter++;
            }
        }  catch (FileNotFoundException | NumberFormatException e) {
            throw new RuntimeException(e);
        }

    }

    public void loadRequests(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter requests in order as they came separated by \",\" (even if it is one request e.g: 1, ): ");
        String[] requestsStr = scanner.nextLine().split(",");
        for (int i = 0; i < requestsStr.length; i++) {
            int id = Integer.parseInt(requestsStr[i].trim());
            this.requests.add(id);
            this.satisfied.put(id,false);
        }


    }

    //method simulating the process of finding receiver of the token starts
    public Integer findFinalReceiverOfToken(Integer nextReceiverID, Integer message){
        System.out.println("Token is being passed to "+ nextReceiverID);
        Node receiver = this.nodes.get(nextReceiverID);

        //operation will be done, clock is incremented
        this.clock+=1;

        //get ID of token that is waiting for token as next
        Integer nextInLineID = receiver.sendToken(this.clock, message);

        //the receiver was waiting next
        if(nextInLineID==null){
            this.root = receiver;

            //receiver is market as satisfied if is in the list of requesting nodes
            System.out.println("RECEIVER ID"+ receiver.ID);
            if(this.satisfied.containsKey(receiver.ID)){
                this.satisfied.put(receiver.ID,true);
                System.out.println("STATE OF NODES:");
                for(Node node: this.nodes.values()){
                    System.out.println(node);
                }
            }else{
                System.err.println("RECEIVER OF TOKEN WAS NOT AMONG LOADED REQUESTERS!!");
            }
            //inside sendToken() node entered critical section, clock is incremented
            this.clock+=1;
            return receiver.ID;
        }
        return this.findFinalReceiverOfToken(nextInLineID, message);

    }

    //this methos servers for checking if node can exit critical section
    public void tryToExitCS(){
        Integer whenToleave = this.root.getLeavingTime();
        if(whenToleave!=null && whenToleave<=this.clock){
            //during exiting CS the node returns next node in line
            // and message in which is it's ID in case it doesnt have empty queue
            ArrayList<Integer> result = this.root.exitCS();
            Integer receiverOfTokenID = result.get(0);
            Integer message = result.get(1);

            //if there is somebody waiting for the token
            if(receiverOfTokenID!=null ){
                //the process of finding receiver of the token starts
                Integer rootID =findFinalReceiverOfToken(receiverOfTokenID, message);

            }
        }
    }

    //this method server fo asking for token
    //first param is who is asking
    //Second param is for whom is asking
    public boolean askForToken(Node requester, Integer requesterChildID){
        System.out.println("REQUESTER "+requester.ID+" asking for token for "+requesterChildID);
        //operation will be done, clock is incremented
        this.clock+=1;

        //when the time changes, root checks if it can leave Critical Section
        this.tryToExitCS();

        //send requester is requesting itself for token, requester will respond with it's parent ID
        ArrayList<Integer> response = requester.receiveRequestForToken(requesterChildID);

        //parent of requester is null and token is passed (1)
        if(response.get(0)==null && response.get(1)==1){
            System.out.println("TOKEN IS BEING SENT TO FINAL RECEIVER");
            //requester is given token to send further because
            // nobody else is waiting for token so message is null
            requester.sendToken(this.clock, null);
            return true;
        //parent of requester is null and token is not passed
        } else if (response.get(0)==null) {
            return false;
        }

        //next will parent of this requester ask for token for requester
        Node parent = this.nodes.get(response.get(0));
        return this.askForToken(parent, requester.ID);
    }

    public boolean areAllRequestsSatisifed(){
        for(boolean val : satisfied.values()){
            if(!val){
                return false;
            }
        }
        return true;
    }
    public void startSimulation(){
        //for each step the clock increments as simulation of timw
        while(!requests.isEmpty()){
            Integer requesterID = requests.poll();
            Node requester = nodes.get(requesterID);
            this.clock+=1;
            //node asks for token for itself
            boolean wasGivenToken = this.askForToken(requester, requesterID);
        }

        //after taking all requests we simulate 30 time unites to let all requests be fullfiled
        for (int i = 0; i < 30; i++) {
            //if the every request was satisfied program stops
            if(areAllRequestsSatisifed()){
                return;
            }else{
                this.clock+=1;
                this.tryToExitCS();
            }
        }
    }



    public static void main(String[] args) {
        Raymond exclutor = new Raymond();
        exclutor.loadNetwork();
        for (Node node : exclutor.nodes.values()) {
            System.out.println("Node " + node.ID + " -> Parent: " + node.getParent());
        }
        exclutor.loadRequests();
        exclutor.startSimulation();

    }
}
