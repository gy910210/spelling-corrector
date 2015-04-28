package cootek.spell.test;

import cootek.spell.tool.LoadDictionary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by gongyu on 2015/4/27.
 */
public class FormatTest {
    public HashMap<String, HashSet<String>> loadTestData(String rawFile, String wordsFile)
            throws Exception{
        HashSet<String> dicSet = new LoadDictionary().loadDictionary(wordsFile);
        BufferedReader br = new BufferedReader(new FileReader(rawFile));

        HashMap<String, HashSet<String>> map = new HashMap<>();
        String line;
        while((line = br.readLine()) != null){
            if(line.trim().equals("")) continue;
            String[] strs = line.trim().split(": ");
            if(!dicSet.contains(strs[0].toLowerCase())) continue;

            String[] source_list = strs[1].split(", ");
            for(String source : source_list){
                if(source.contains("*"))
                    source = source.substring(0, source.length() - 2);

                HashSet<String> set;
                if(!map.containsKey(source.toLowerCase())) set = new HashSet<>();
                    else set = map.get(source.toLowerCase());
                set.add(strs[0].toLowerCase());
                map.put(source.toLowerCase(), set);
            }
        }
        br.close();
        return map;
    }
}
