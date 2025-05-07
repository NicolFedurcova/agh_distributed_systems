import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Node {

    public final int ID;
    private final Queue<Integer> localQueue = new LinkedList<Integer>();
    private Integer parentNode;
    private Integer leavingTime =null;
    private boolean hasToken = false;


    public Node(int ID) {
        this.ID = ID;
    }

    public Node(int ID, Integer parentNode) {
        this.ID = ID;
        this.parentNode = parentNode;
    }

    public void obtainInitToken(){
        this.hasToken=true;
    }
    public Integer getLeavingTime(){
        return this.leavingTime;
    }

    public void changeParentNodeTo(int parentNode){
        this.parentNode = parentNode;
    }

    public Integer getParent(){
        return this.parentNode;
    }

    //returns arrayList [parentID, permission=1/0]
    public ArrayList<Integer> receiveRequestForToken(int requestFromID){
        ArrayList<Integer> response = new ArrayList<>();
        // node adds requestingnodeID to its waiting list (even if its itself)
        this.localQueue.add(requestFromID);

        //if this node has token, and in no longer in CS(leaving time = null)
        if(this.hasToken && this.leavingTime==null && this.parentNode==null){
            System.out.println("REQUEST FOR TOKEN WAS SUCESSFUL");
            //it responds with token
            response.add(this.parentNode);
            response.add(1);
            //sets receivingNodeId as its parent (if node isnt asking itself)
            if(requestFromID!=this.ID){
                this.parentNode = requestFromID;
            }
            //returns answer
            return response;
        }
        //else this node doesn't have token or still is in CS

        //adds to respone it's parents node ID and 0 indicating no token
        response.add(this.parentNode);
        response.add(0);
        return response;
    }

    public void enterCS(int clock){
        System.out.println("NODE "+this.ID+" ENTERED CS ....................");
        //out degree of this node is zero
        this.parentNode = null;
        //remains in critical section for 3 operations
        this.leavingTime = clock+8;
        //has the token now
        this.hasToken = true;

    }

    //returns arraylist [nextInLineForTokenID, thisnodeID/null]
    public ArrayList<Integer> exitCS(){

        this.leavingTime = null;

        ArrayList<Integer> response = new ArrayList<>();
        if(!this.localQueue.isEmpty()){
            //add to return who was asking for token as first
            Integer first = this.localQueue.poll();
            response.add(first);
            //thiis node is losing the token
            this.hasToken=false;
            //that first node will become parent of this node
            this.parentNode = first;
            //if queue in this node waiting for token is not empty
            if(!this.localQueue.isEmpty()) {
                //send ID of this node to get to the queue of receiver of token
                response.add(this.ID);
            }else{
                response.add(null);
            }
        } else{
            this.hasToken=true;
            response.add(null);
            response.add(null);
        }
        System.out.println("NODE "+this.ID+" EXITED CS ....................");
        return response;

    }

    //method for sending the token to next in queue
    public Integer sendToken(int clock, Integer message){
        //if queue waiting for token is not empty
        if(!this.localQueue.isEmpty()){
            //get next waiting node
            Integer nextNodeID = this.localQueue.poll();

            //if next in line for token is this node
            if(nextNodeID==this.ID){
                //enter the node from message to the queue for token
                if(message!=null) this.localQueue.add(message);
                //return that nobody else needs to be send token to
                //because THIS is the node that will enter critical section
                this.enterCS(clock);
                return null;
            //next in line for token is another node
            } else{
                //this node is sending token to that node, so it changes it's parent
                this.parentNode = nextNodeID;
                return nextNodeID;
            }
        //nobody is waiting for the token, this node keeps the token
        } else{
            this.hasToken = true;
            this.leavingTime = null;
            return -1;
        }
    }

    @Override
    public String toString() {
        return "<" +
                "ID:" + ID +
                ", localQueue: " + localQueue +
                ", parentNode: " + parentNode +
                '>';
    }
}
