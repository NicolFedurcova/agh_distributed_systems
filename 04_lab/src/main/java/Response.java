public class Response {
    //this is just a wrapper class
    private int neighbourID;
    private Request request;

    public Response(int neighbourID, Request request) {
        this.neighbourID = neighbourID;
        this.request = request;
    }

    public int getNeighbourID() {
        return neighbourID;
    }

    public void setNeighbourID(int neighbourID) {
        this.neighbourID = neighbourID;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }
}
