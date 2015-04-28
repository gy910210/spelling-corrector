package cootek.spell.main;

import cootek.spell.bean.Pair;
import cootek.spell.bean.PredictWord;
import cootek.spell.model.ChannelNoisyModel;
import cootek.spell.tool.EditDistance;
import cootek.spell.tool.LoadDictionary;

import java.util.*;

/**
 * Created by gongyu on 2015/4/26.
 */
public class PredictModel {
    private final String DIC_FILE = "words.txt";
    private final String CHANNEL_NOISY_FILE = "channel_data.txt";

    private double probEqual;
    private int mostDis;
    private double smoothVal;
    private int contextNum;

    public PredictModel(double probEqual, int mostDis, double smoothVal, int contexNum) {
        this.probEqual = probEqual;
        this.mostDis = mostDis;
        this.smoothVal = smoothVal;
        this.contextNum = contexNum;
    }

    //If top_k < 0 return the whole list
    public List<PredictWord> predict(String source, int top_k) throws Exception{
        HashSet<String> dicSet = new LoadDictionary().loadDictionary(DIC_FILE);
        HashMap<String, Double> channelMap = new ChannelNoisyModel().loadNoisyChannelModel(CHANNEL_NOISY_FILE);

        List<PredictWord> list = new ArrayList<>();
        for(String word : dicSet){
            List<Pair<Character>> pos_list = new ArrayList<>();
            int dis = new EditDistance().edit(word, source, pos_list);
            if(dis > mostDis) continue;
            // System.out.println(word);
            // System.out.println(pos_list);

            double prob = calChannelNoisyProb(pos_list, channelMap);
            list.add(new PredictWord(word, prob));
        }
        Collections.sort(list);

        if(top_k < 0 || list.size() <= top_k) return list;
        else return list.subList(0, top_k);
    }

    public double calChannelNoisyProb(List<Pair<Character>> pos_list, HashMap<String, Double> channelMap){
        double[] record = new double[pos_list.size() + 1];
        for(int i = 0; i <= pos_list.size(); i++) record[i] = 0.0;

        for(int pos = 1; pos <= pos_list.size(); pos++){
            double maxVal = -10000.0;

            for(int slide = 0; slide <= contextNum; slide++){
                if(pos - slide <= 0) break;
                StringBuffer wordStr = new StringBuffer("");
                StringBuffer sourceStr = new StringBuffer("");

                for(int i = pos - slide - 1; i < pos; i++){
                    if(pos_list.get(i).val1 != '#')
                        wordStr.append(pos_list.get(i).val1);
                    if(pos_list.get(i).val2 != '#')
                        sourceStr.append(pos_list.get(i).val2);
                }

                double val;

                if((slide == 0) && (wordStr.toString().equals("") || sourceStr.toString().equals(""))){
                    // val = record[pos - 1] + Math.log(smoothVal);
                    val = -10000.0;
                }else{
                    if(wordStr.toString().equals(sourceStr.toString())){
                        // System.out.println(wordStr.toString() + " | " + sourceStr.toString() +
                        //         "\t" + Math.log(probEqual));
                        val = record[pos - slide - 1] + Math.log(probEqual);
                    }else{
                        String tmp = wordStr.append(" | ").append(sourceStr).toString();
                        if(channelMap.containsKey(tmp)) {
                            // System.out.println(tmp + "\t" + Math.log(channelMap.get(tmp)));
                            val = record[pos - slide - 1] + Math.log(channelMap.get(tmp));
                        }else{
                            // System.out.println(tmp + "\t" + Math.log(smoothVal));
                            val = record[pos - slide - 1] + Math.log(smoothVal);
                        }
                    }
                }
                // System.out.println("Val: " + val);
                if(val > maxVal) maxVal = val;
            }

            record[pos] = maxVal;
        }
        return record[pos_list.size()];
    }

    public static void main(String[] args) throws Exception{
        PredictModel pm = new PredictModel(1.0, 2, 0.0000005, 2);
        System.out.println(pm.predict("mwe", 20));
    }
}
