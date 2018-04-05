import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class BlackList {
    BlackList(){ getBlackList(); }

    private LinkedList<String> blackList = new LinkedList<>();


    public LinkedList<String> getBlackList() {
        String blackListFile = "D:\\Studia_mgr\\1 semestr\\Systemy Internetowe\\zad4\\src\\main\\resources\\blacklist.txt";
        String line;
        BufferedReader br;

        try {
            br = new BufferedReader(new FileReader(blackListFile));
            while ((line = br.readLine()) != null) {
                if (!blackList.contains(line)){
                    blackList.add(line);
                }
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return blackList;
    }

    public boolean contains(String urlToCheck){
        for (String url : blackList){
            if (urlToCheck.contains(url)){
                return true;
            }
        }
        return false;
    }
}
