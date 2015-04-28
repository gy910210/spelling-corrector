package cootek.spell.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

/**
 * Created by gongyu on 2015/4/25.
 */
public class ChannelNoisyModel {
    public void dumpChannelNoisyModel(String error_file, String count_file, String channel_file)
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
            String[] pairs = strs[0].trim().split(" | ");
            // if(pairs[0].equals('#') || pairs[1].equals('#')) continue;
            double prob = (double) Integer.parseInt(strs[1].trim()) / countMap.get(pairs[0]);
            bw.write(strs[0].trim() + "\t" + String.valueOf(prob));
            bw.newLine();
            bw.flush();
        }
        brError.close();
        bw.close();
    }

    public HashMap<String, Double> loadNoisyChannelModel(String channel_file) throws Exception{
        BufferedReader br = new BufferedReader(new FileReader(channel_file));
        HashMap<String, Double> channelMap = new HashMap<>();
        String line;

        while((line = br.readLine()) != null){
            if(line.trim().equals("")) continue;
            String[] strs = line.trim().split("\t");
            channelMap.put(strs[0], Double.parseDouble(strs[1]));
        }
        br.close();
        return channelMap;
    }


    public static void main(String[] args) throws Exception{
        new ChannelNoisyModel().dumpChannelNoisyModel("error_data.txt", "count_data.txt", "channel_data.txt");
    }
}
