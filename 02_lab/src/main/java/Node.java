import java.util.ArrayList;
import java.util.Arrays;

public class Node {
    private final int sizeOfNetwork;
    //based on Lampold Clock model
    public int localClockCounter = 0;
    public int ID;

    public ArrayList<Integer> listOfRequests = new ArrayList<>();
    public int[] goAheadResponses;

    public boolean wantsToGetInCS = false;
    public Request lastSentRequest;
    public boolean isInCS = false;

    public int csExitTime = 4; //if node is at CS at time 0 it will exist after 4 time units

    public Node(int ID, int sizeOfNetwork) {
        this.ID = ID;
        this.goAheadResponses = new int[sizeOfNetwork];
        this.goAheadResponses[ID] =1;
        this.sizeOfNetwork = sizeOfNetwork;
    }

    public Node(int ID, int localClockCounter, int sizeOfNetwork) {
        this.ID = ID;
        this.localClockCounter = localClockCounter;
        this.goAheadResponses = new int[sizeOfNetwork];
        this.goAheadResponses[ID] =1;
        this.sizeOfNetwork = sizeOfNetwork;
    }

    public Response sendRequestForCS(Node destinationNode, Request request) {
        //incrementing local lock before sending the request
        this.localClockCounter += 1;

        this.wantsToGetInCS = true;
        this.lastSentRequest = request;

        //send reques by calling receiveRequest function in destination node
        return destinationNode.receiveRequest(request);
    }

    public Response receiveRequest(Request request) {
        //if localClockCounter of sender is > then localClockCounter of recipient
        //recipient clock will be updated to assure consistency
        this.localClockCounter = Math.max(this.localClockCounter, request.recordedClockCounter);

        //incrementing local lock after synchronization
        // because message was received
        this.localClockCounter += 1;

        //if this node is in critical section
        if (this.isInCS) {
            //saves the requesting node in it's own list of requests
            this.listOfRequests.add(request.requesterID);
            return sendResponse(null);

            //if this node wants to get in the critical section and has already sent a request
        } else if (this.wantsToGetInCS) {
            int requestClockCounter = this.lastSentRequest.recordedClockCounter;
            //if the requsting node's timestampcounter < this node's
            //timestampcounter at the time of requesting for critical section
            if (request.recordedClockCounter < requestClockCounter) {
                //node gives permission for entering CS
                return sendResponse(true);
            } else {
                //else stores ID of requesting node in it's local listOfRequests
                this.listOfRequests.add(request.requesterID);
                return sendResponse(null);
            }

            //this node is not in CS neither is it interested in entering CS
        } else {
            return sendResponse(true);
        }
    }

    public Response sendResponse(Boolean reponse) {
        //incrementing local clock because sending response if not null
        if (reponse != null) {
            this.localClockCounter += 1;
        }
        Response res = new Response(this.localClockCounter, this.ID, reponse);
        return res;
    }

    public void processResponse(Response response){
        //if localClockCounter of sender is > then localClockCounter of recipient
        //recipient clock will be updated to assure consistency
        this.localClockCounter = Math.max(this.localClockCounter, response.recordedClockCounter);

        //incrementing local lock after synchronization
        // because message was received
        this.localClockCounter += 1;

        if(response.answer!=null && response.answer){
            this.goAheadResponses[response.responserID] =1;
        }
    }

    public void enterCS(int currentTime){
        this.isInCS = true;
        this.wantsToGetInCS = false;
        this.csExitTime = currentTime + 4;
        this.goAheadResponses = new int[sizeOfNetwork];
        this.goAheadResponses[this.ID] =1;
        System.out.println("Node " + ID + " ENTERED CS with request " + lastSentRequest+" at simulated time point "+currentTime+" with local clock "+this.localClockCounter);
    }

    public boolean checkIfIShoulfEnterCS(){
        boolean isSucessful = true;
        for(int goAhead:this.goAheadResponses){
            if (goAhead==0){
                isSucessful=false;
            }
        }
        return isSucessful;
    }

    public void checkIfShouldExitCS(int currentTime, Network networkRef) {
        if (isInCS && currentTime >= csExitTime) {
            isInCS = false;
            csExitTime = -1;
            System.out.println("Node " + ID + " EXITED CS at time " + currentTime+" with local clock "+this.localClockCounter+" and last recorded request time"+this.lastSentRequest);

            // Send approvals to waiting nodes
            for (int waitingNodeId : listOfRequests) {
                System.out.println("NODE "+this.ID+" granting the permission to enter CS (after eaving CS) to: "+waitingNodeId);
                Node waitingNode = networkRef.getNode(waitingNodeId);
                Response response = sendResponse(true);
                waitingNode.processResponse(response);
            }
            this.listOfRequests.clear();
            this.goAheadResponses = new int[sizeOfNetwork];
            this.goAheadResponses[this.ID] =1;
        }
    }





    @Override
    public String toString() {
        return "{" + "<" +
                localClockCounter + ", " +
                ID + ">, " +
                listOfRequests + ", " +
                Arrays.toString(goAheadResponses) +
                "}";
    }
}

