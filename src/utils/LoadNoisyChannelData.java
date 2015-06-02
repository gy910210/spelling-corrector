package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

/**
 * Created by gongyu on 2015/5/11.
 */
public class LoadNoisyChannelData {
    public HashMap<String, Double> loadNoisyChannelData(String channel_file) throws Exception{
        BufferedReader br = new BufferedReader(new FileReader(channel_file));
        HashMap<String, Double> channelMap = new HashMap<>();
        String line;

        while((line = br.readLine()) != null){
            if(line.trim().equals("")) continue;
            String[] strs = line.trim().split("\t");
            channelMap.put(strs[0] + "\t" + strs[1], Double.parseDouble(strs[2]));
        }
        br.close();
        return channelMap;
    }
}
