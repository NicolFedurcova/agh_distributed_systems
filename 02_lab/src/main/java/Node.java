import java.util.ArrayList;
import java.util.Arrays;

public class Node {
    //based on Lampold Clock model
    public int localClockCounter = 0;
    public int ID;

    public ArrayList<Integer> listOfRequests = new ArrayList<>();
    public int[] goAheadResponses;

    public boolean wantsToGetInCS = false;
    public Request lastSentRequest;
    public boolean isInCS = false;

    public Node(int ID, int sizeOfNetwork) {
        this.ID = ID;
        this.goAheadResponses = new int[sizeOfNetwork];
    }

    public Node(int ID, int localClockCounter, int sizeOfNetwork) {
        this.ID = ID;
        this.localClockCounter = localClockCounter;
        this.goAheadResponses = new int[sizeOfNetwork];
    }

    public Boolean sendRequestForCS(Node destinationNode) {
        //incrementing local lock before sending the request
        this.localClockCounter += 1;

        Request request = new Request(this.localClockCounter, this.ID);
        this.wantsToGetInCS = true;
        this.lastSentRequest = request;

        //send reques by calling receiveRequest function in destination node
        return destinationNode.receiveRequest(request);
    }

    public Boolean receiveRequest(Request request) {
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

    public Boolean sendResponse(Boolean reponse) {
        //incrementing local clock because sending response
        if (reponse != null) {
            this.localClockCounter += 1;
        }
        return reponse;
    }

    public void processResponse(Boolean response, int responserID){
        if(response){
            this.goAheadResponses[responserID] =1;
        }
    }

    public void enterCS(){
        this.isInCS = true;
        this.wantsToGetInCS = false;
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

