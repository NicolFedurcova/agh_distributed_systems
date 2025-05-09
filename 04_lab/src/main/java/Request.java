import java.util.LinkedList;
import java.util.Queue;

public class Request {
    //true == granting request, false == requesting request
    private boolean isGranting;
    //who send the request o tp whom is granting request intended
    private int requester;
    private Queue<Integer> waitingQueue = new LinkedList<>();

    public Request(boolean isGranting, int requester) {
        this.isGranting = isGranting;
        this.requester = requester;
    }

    public boolean isGranting() {
        return isGranting;
    }

    public void setGranting(boolean granting) {
        isGranting = granting;
    }

    public int getRequester() {
        return requester;
    }

    public void setRequester(int requester) {
        this.requester = requester;
    }

    public Queue<Integer> getWaitingQueue() {
        return waitingQueue;
    }

    public void setWaitingQueue(Queue<Integer> queue){
        this.waitingQueue.addAll(queue);
    }

    public void addToWaitingQueue(Integer requesterID){
        this.waitingQueue.add(requesterID);
    }


    @Override
    public String toString(){
        String gr = "<<type:";
        if(isGranting){
            gr+="granting to ";
        }else{
            gr += "requesting from ";
        }
        return gr+this.requester+" with queue:"+this.waitingQueue+">>";
    }
}
