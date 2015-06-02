package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashSet;

/**
 * Created by gongyu on 2015/4/26.
 * Load the dictionary.
 */
public class LoadDictionary {
    public HashSet<String> loadDictionary(String dicFile) throws Exception{
        HashSet<String> dicSet = new HashSet<>();
        BufferedReader br = new BufferedReader(new FileReader(dicFile));
        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().equals("")) continue;
            String[] strs = line.split(" ");
            dicSet.add(strs[1].trim().toLowerCase().replace("-", "").replace("'", ""));
        }
        br.close();
        return dicSet;
    }
}
