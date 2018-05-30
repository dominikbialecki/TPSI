public class ConnectionData {
    ConnectionData() {
        url = "";
        requests = 0L;
        dataSend = 0L;
        dataGot = 0L;
    }

    String url;
    long requests, dataSend, dataGot;


    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(url);
        sb.append(",");
        sb.append(requests);
        sb.append(",");
        sb.append(dataSend);
        sb.append(",");
        sb.append(dataGot);


        return sb.toString();
    }
}
