package cootek.spell.tool;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;

/**
 * Created by gongyu on 2015/4/26.
 * Load the dictionary.
 */
public class LoadDictionary {
    public HashSet<String> loadDictionary(String dic_file) throws Exception{
        BufferedReader br = new BufferedReader(new FileReader(dic_file));
        HashSet<String> dicSet = new HashSet<>();
        String line;
        while((line = br.readLine()) != null){
            if(line.trim().equals("")) continue;
            dicSet.add(line.trim());
        }
        br.close();
        return dicSet;
    }
}
