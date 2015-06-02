package prune;

import test.TestModel;
import utils.LoadDictionary;
import utils.LoadNoisyChannelData;
import utils.Log;

import java.util.*;

/**
 * Created by gy910 on 2015/5/25.
 */
public class PruneModel {
    private HashMap<String, Double> channelMap;
    private HashSet<String> dicSet;

    private double probEqual;
    private int mostDis;
    private int contextNum;
    private int topNum;

    public PruneModel(String dic_file, String noisy_channel_file,
                        double probEqual, int mostDis, int contextNum, int topNum) throws Exception {
        this.probEqual = probEqual;
        this.mostDis = mostDis;
        this.contextNum = contextNum;
        this.topNum = topNum;

        dicSet = new LoadDictionary().loadDictionary(dic_file);
        channelMap = new LoadNoisyChannelData().loadNoisyChannelData(noisy_channel_file);
    }

    public void prune(int thr) throws Exception {
        Log log = new Log();
        log.open("prune_data.txt");

        TestModel tm = new TestModel(probEqual, mostDis, contextNum, topNum);

        List<Map.Entry<String, Double>> list = new LinkedList<>(channelMap.entrySet());
        Collections.sort(list, (o1, o2) -> o1.getValue().compareTo(o2.getValue()));

        Vector<Double> removedItemVec = new Vector<>();
        for (int i = 1; i <= thr; i++) {
            for (int j = 1; j <=10000; j++) {
                double tmp = list.remove(0).getValue();
                removedItemVec.add(tmp);
            }

            System.out.println(list.size());

            double smoothVal = calSmoothVal(removedItemVec);

            HashMap<String, Double> channel_new = new HashMap<>();
            for (Map.Entry<String, Double> entry : list) {
                channel_new.put(entry.getKey(), entry.getValue());
            }

            double precision = tm.testForPrune(dicSet, channel_new, smoothVal);
            System.out.println(removedItemVec.size() + "\t" + precision + "\t" + smoothVal);
            log.log(removedItemVec.size() + "\t" + list.size() + "\t" + precision + "\t" + smoothVal);
        }
        log.close();
    }

    private double calSmoothVal(Vector<Double> removedItemVec) {
        // The average.
        double sum = 0.0;
        for (double item : removedItemVec) {
            sum += item;
        }
        return sum / removedItemVec.size();
    }
}
