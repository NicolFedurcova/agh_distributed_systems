import java.util.LinkedList;
import java.util.Queue;

public class Node {

    public int ID;
    private int neighbourID;
    private Queue<Integer> localQueue = new LinkedList<>();
    private boolean hasToken = false;
    private Integer csExitTime = null;
    private boolean isInCS = false;


    public Node(int ID, int neighbourID) {
        this.ID = ID;
        this.neighbourID = neighbourID;
    }

    public int getNeighbourID() {
        return neighbourID;
    }

    //method for simulating entering critical section
    public void enterCS(int clock, Queue<Integer> message, int networkSize, boolean verbal){
        //if node is entering critical section after it is being loaded like that from console, message is not dsplayed
        if(verbal)System.out.println("-----------------NODE "+this.ID+" IS ENTERING CS");
        this.hasToken = true;
        this.isInCS = true;
        this.csExitTime = clock+(networkSize-2);
        //while entering CS this node receives waiting nodes from message
        if(message!=null){
            this.localQueue = message;
        }
    }

    //method for simulation of existing the critical section
    //returns Response[nodeID, request]/null nodeID of neighbour node to whom it passes granting request
    public Response exitCS(){
        System.out.println("-----------------NODE "+this.ID+" IS EXITING CS");
        this.csExitTime = null;
        this.isInCS = false;
        //if there is somebody waiting for the token, granting request is sent
        //to the neighbour of this node
        if(!this.localQueue.isEmpty()){
            this.hasToken = false;
            Integer receiver = this.localQueue.poll();
            Request request = new Request(true,receiver);
            //queue of waiting nodes is passed as part of request
            request.setWaitingQueue(this.localQueue);
            this.localQueue.clear();
            //response is a wapping class to pass both neighborID and also request
            return new Response(this.neighbourID, request);
        //if nobody is waiting it keeps the token  and returns null
        }else{
            return null;
        }
    }

    //returns neighbourID to whom pass the request next
    // or -1 if node gets token and enters CS
    //or null if node is in CS and doesnt pass request further
    public Integer getRequest(Request request, int clock, int networkSize){
        System.out.println("NODE "+this.ID+" received request "+ request);
        //this request is for passing token
        if(request.isGranting()) {
            //token is intended for this node
            if(request.getRequester() == this.ID){
                //this node enters CS
                this.enterCS(clock, request.getWaitingQueue(), networkSize, true);
                //it wont pass request to anybody, returns null
                return -1;
            //token is for somebody else, return neighbourID to whom pass the request
            }else{
                return this.neighbourID;
            }
        //this request is for asking for token
        }else{
            //if this node is in CS, it puts requesting node from request to it's queue
            if (this.isInCS) {
                this.localQueue.add(request.getRequester());
                return null;
            //if this node is not in CS and it has token, it passes token
            } else if(this.hasToken){
                //the request changes from requesting to granting
                request.setGranting(true);
                //the local queue is not checked because if node is not in cs and it has token it means it's queue is empty
                //requester in request stays the same

                //this node returns neigbour id to whom granting request is passed
                return this.neighbourID;
            //if this node is not in CS and doesn't have token it returns it's neighbor
            }else{
                return this.neighbourID;
            }
        }
    }

    public void setLocalQueue(Queue<Integer> queue){
        this.localQueue = queue;
    }

    public Integer getCsExitTime(){
        return this.csExitTime;
    }

    @Override
    public String toString() {
        return "<" +
                "ID:" + ID +
                ", localQueue: " + localQueue +
                ", neighborNode: " + neighbourID +
                '>';
    }
}
