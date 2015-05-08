package cootek.spell.main;

import cootek.spell.bean.Pair;
import cootek.spell.model.NoisyChannelModel;
import cootek.spell.tool.EditDistance;
import cootek.spell.tool.LoadDictionary;
import cootek.spell.bean.PredictWord;

import java.util.*;

/**
 * Created by gongyu on 2015/4/26.
 * Given a typed word (key) and top k, return PredictWord list.
 */
public class PredictModel {
    private String DIC_FILE;
    private String NOISY_CHANNEL_FILE;

    private double probEqual;
    private int mostDis;
    private double smoothVal;
    private int contextNum;

    public PredictModel(String DIC_FILE, String NOISY_CHANNEL_FILE,
                        double probEqual, int mostDis, double smoothVal, int contextNum) {
        this.DIC_FILE = DIC_FILE;
        this.NOISY_CHANNEL_FILE = NOISY_CHANNEL_FILE;
        this.probEqual = probEqual;
        this.mostDis = mostDis;
        this.smoothVal = smoothVal;
        this.contextNum = contextNum;
    }

    public List<PredictWord> predict(String source, int top_k) throws Exception{
        HashSet<String> dicSet = new LoadDictionary().loadDictionary(DIC_FILE);
        HashMap<String, Double> channelMap = new NoisyChannelModel().loadNoisyChannelData(NOISY_CHANNEL_FILE);

        List<PredictWord> list = new ArrayList<>();
        for(String word : dicSet){
            List<Pair<Character>> pos_list = new ArrayList<>();
            int dis = new EditDistance().edit(word, source, pos_list);
            if(dis > mostDis) continue;

            double prob = calChannelNoisyProb(pos_list, channelMap);
            list.add(new PredictWord(word, prob));
        }
        Collections.sort(list);

        if(top_k < 0 || list.size() <= top_k) return list; //If top_k < 0 return the whole list
        else return list.subList(0, top_k);
    }

    private double calChannelNoisyProb(List<Pair<Character>> pos_list, HashMap<String, Double> channelMap){
        double[] record = new double[pos_list.size() + 1];
        for(int i = 0; i <= pos_list.size(); i++) record[i] = 0.0;

        for(int pos = 1; pos <= pos_list.size(); pos++){
            double maxVal = -10000000.0;

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
                    val = -10000000.0;
                }else{
                    if(wordStr.toString().equals(sourceStr.toString())){
                        val = record[pos - slide - 1] + Math.log(probEqual);
                    }else{
                        String tmp = wordStr.append("\t").append(sourceStr).toString();
                        if(channelMap.containsKey(tmp)) {
                            val = record[pos - slide - 1] + channelMap.get(tmp);
                        }else{
                            val = record[pos - slide - 1] + Math.log(smoothVal);
                        }
                    }
                }
                if(val > maxVal) maxVal = val;
            }

            record[pos] = maxVal;
        }
        return record[pos_list.size()];
    }
}
