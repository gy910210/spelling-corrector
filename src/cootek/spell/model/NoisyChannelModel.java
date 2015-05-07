package cootek.spell.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

/**
 * Created by gongyu on 2015/4/25.
 * Generate and save Channel Model Count(alpha) / Count(alpha -> beta)
 * Load the Channel Model
 */
public class NoisyChannelModel {
    public void dumpNoisyChannelData(String error_file, String count_file, String channel_file)
            throws Exception{
        BufferedReader brError = new BufferedReader(new FileReader(error_file));
        BufferedReader brCount = new BufferedReader(new FileReader(count_file));
        BufferedWriter bw = new BufferedWriter(new FileWriter(channel_file));

        HashMap<String, Integer> countMap = new HashMap<>();
        String line;
        while((line = brCount.readLine()) != null){
            if(line.trim().equals("")) continue;
            String[] strs = line.trim().split("\t");
            countMap.put(strs[0], Integer.parseInt(strs[1]));
        }
        brCount.close();

        while((line = brError.readLine()) != null){
            if(line.trim().equals("")) continue;
            String[] strs = line.trim().split("\t");
            double logProb = Math.log((double) Integer.parseInt(strs[2].trim()) / countMap.get(strs[0]));
            bw.write(strs[0].trim() + "\t" + strs[1].trim() + "\t" + String.valueOf(logProb));
            bw.newLine();
            bw.flush();
        }
        brError.close();
        bw.close();
    }

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
