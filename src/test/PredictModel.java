package test;

import train.Pair;
import utils.LoadDictionary;
import utils.LoadNoisyChannelData;

import java.util.*;

/**
 * Created by gongyu on 2015/4/26.
 * Given a typed word (key) and top k, return PredictWord list.
 */
public class PredictModel {
    private double probEqual;
    private int mostDis;
    private double smoothVal;
    private int contextNum;

    private HashSet<String> dicSet;
    private HashMap<String, Double> channelMap;

    // Constructor for test model
    public PredictModel(String dic_file, String noisy_channel_file,
                        double probEqual, int mostDis, int contextNum) throws Exception {
        this.probEqual = probEqual;
        this.mostDis = mostDis;
        this.contextNum = contextNum;

        dicSet = new LoadDictionary().loadDictionary(dic_file);
        channelMap = new LoadNoisyChannelData().loadNoisyChannelData(noisy_channel_file);
        this.smoothVal = new GetMapSmallest().getMapSmallest(channelMap, 10);
    }

    // Constructor for prune model
    public PredictModel(HashSet<String> dicSet, HashMap<String, Double> channelMap,
                        double probEqual, int mostDis, double smoothVal, int contextNum) throws Exception {
        this.probEqual = probEqual;
        this.mostDis = mostDis;
        this.smoothVal = smoothVal;
        this.contextNum = contextNum;

        this.dicSet = dicSet;
        this.channelMap = channelMap;
    }

    public List<PredictWord> predict(String source, int top_k) throws Exception {
        List<PredictWord> list = new ArrayList<>();
        for (String word : dicSet) {
            // rules
            if (word.length() <= 1) continue;
            if (source.charAt(0) != word.charAt(0)) continue;
            if (word.equals(source)) continue;

            List<Pair<Character>> pos_list = new ArrayList<>();
            int dis = new EditDistance().edit(word, source, pos_list);
            if (dis > mostDis) continue;

            double prob = calChannelNoisyProb(pos_list, channelMap);
            list.add(new PredictWord(word, prob));
        }
        Collections.sort(list);

        if (top_k < 0 || list.size() <= top_k) return list; //If top_k < 0 return the whole list
        else return list.subList(0, top_k);
    }

    private double calChannelNoisyProb(List<Pair<Character>> pos_list, HashMap<String, Double> channelMap) {
        double[] record = new double[pos_list.size() + 1];
        for (int i = 0; i <= pos_list.size(); i++) record[i] = 0.0;

        for (int pos = 1; pos <= pos_list.size(); pos++) {
            double maxVal = -10000000.0;

            for (int slide = 0; slide <= contextNum; slide++) {
                if (pos - slide <= 0) break;

                StringBuilder wordStr = new StringBuilder("");
                StringBuilder sourceStr = new StringBuilder("");

                for (int i = pos - slide - 1; i < pos; i++) {
                    if (pos_list.get(i).val1 != '#')
                        wordStr.append(pos_list.get(i).val1);
                    if (pos_list.get(i).val2 != '#')
                        sourceStr.append(pos_list.get(i).val2);
                }

                double val;

                if ((wordStr.toString().trim().equals("") || sourceStr.toString().trim().equals(""))) {
                    val = -10000000.0;
                } else {
                    if (wordStr.toString().equals(sourceStr.toString())) {
                        val = record[pos - slide - 1] + Math.log(probEqual);
                    } else {
                        String tmp = wordStr.append("\t").append(sourceStr).toString();
                        if (channelMap.containsKey(tmp)) {
                            val = record[pos - slide - 1] + channelMap.get(tmp);
                        } else {
                            val = record[pos - slide - 1] + smoothVal;
                        }
                    }
                }
                if (val > maxVal) maxVal = val;
            }
            record[pos] = maxVal;
        }
        return record[pos_list.size()];
    }

    public double getSmoothVal() { return this.smoothVal; }
}
