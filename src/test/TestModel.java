package test;

import train.Pair;
import utils.Log;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

/**
 * Created by gongyu on 2015/4/26.
 * Test Channel Model.
 */
public class TestModel {
    private String noisy_channel_file;
    private String dic_file;

    private double probEqual;
    private int mostDis;
    private int contextNum;
    private int topNum;

    // Constructor for test model
    public TestModel(String noisy_channel_file, String dic_file,
                     double probEqual, int mostDis, int contextNum, int topNum) {
        this.noisy_channel_file = noisy_channel_file;
        this.dic_file = dic_file;
        this.probEqual = probEqual;
        this.mostDis = mostDis;
        this.contextNum = contextNum;
        this.topNum = topNum;
    }

    // Constructor for prune model
    public TestModel(double probEqual, int mostDis, int contextNum, int topNum) {
        this.probEqual = probEqual;
        this.mostDis = mostDis;
        this.contextNum = contextNum;
        this.topNum = topNum;
    }

    // Test for prune model
    public double testForPrune(HashSet<String> dicSet, HashMap<String, Double> channelMap,
                               double smoothVal) throws Exception {
        List<Pair<String>> testData = new GenerateTestData().loadTestData("test_data.txt");
        PredictModel pm = new PredictModel(dicSet, channelMap, probEqual, mostDis, smoothVal, contextNum);
        int total = testData.size();
        int cnt = 0;

        for(Pair<String> pair : testData){
            List<PredictWord> word_list = pm.predict(pair.val2, topNum);
            boolean flag = false;
            for(PredictWord word : word_list){
                if(word.w.equals(pair.val1)){
                    flag = true;
                    break;
                }
            }
            if(flag){
                cnt += 1;
            }
        }
        return (double) cnt / total;
    }

    // Test for test model
    public double test() throws Exception{
        Log log = new Log();
        log.open("test_result.txt");

        List<Pair<String>> testData = new GenerateTestData().loadTestData("test_data.txt");
        log.log("test data = " + 5000 + "\t" + "most dis. = " + mostDis);

        PredictModel pm = new PredictModel(dic_file, noisy_channel_file, probEqual, mostDis, contextNum);
        log.log("equal prob. = " + probEqual + "\t"
                + "most dis. = " + mostDis + "\t"
                + "smooth prob. = " + pm.getSmoothVal() + "\t"
                + "context num. = " + contextNum);
        log.log("==========");

        int total = testData.size();
        System.out.println("Total: " + total);
        int cnt = 0, sum = 0;

        for(Pair<String> pair : testData){
            List<PredictWord> word_list = pm.predict(pair.val2, topNum);
            boolean flag = false;
            for(PredictWord word : word_list){
                if(word.w.equals(pair.val1)){
                    flag = true;
                    break;
                }
            }
            if(flag){
                cnt += 1; sum += 1;
                System.out.println("Bingo!" + "\t" + cnt + "/" + sum);
                log.log("Bingo!" + "\t" + pair.val2 + "\t" + pair.val1 + "\t" + word_list.toString());
            }else{
                sum += 1;
                System.out.println("Shit!" + "\t" + pair.val2 + "\t" + pair.val1);
                log.log("Shit!" + "\t" + pair.val2 + "\t" + pair.val1 + "\t" + word_list.toString());
            }
        }
        System.out.println("Cnt: " + cnt);
        log.log("==========");
        log.log("precision: " + (double) cnt / total + "\t" + cnt + "/" + sum);

        log.close();
        return (double) cnt / total;
    }
}
