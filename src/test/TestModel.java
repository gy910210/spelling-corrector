package test;

import train.Pair;

import java.util.List;

/**
 * Created by gongyu on 2015/4/26.
 * Test Channel Model.
 */
public class TestModel {
    private String noisy_channel_file;
    private String dic_file;

    private double probEqual;
    private int mostDis;
    private double smoothVal;
    private int contextNum;
    private int topNum;

    public TestModel(String noisy_channel_file, String dic_file,
                     double probEqual, int mostDis, int contextNum, double smoothVal, int topNum) {
        this.noisy_channel_file = noisy_channel_file;
        this.dic_file = dic_file;
        this.probEqual = probEqual;
        this.mostDis = mostDis;
        this.contextNum = contextNum;
        this.smoothVal = smoothVal;
        this.topNum = topNum;
    }

    public double test() throws Exception{
        Log log = new Log();
        log.open("test_result.txt");

        List<Pair<String>> testData = new GenerateTestData().loadTestData("test_data.txt");
        log.log("test data = " + 5000 + "\t" + "most dis. = " + mostDis);

        PredictModel pm = new PredictModel(dic_file, noisy_channel_file, probEqual, mostDis, smoothVal, contextNum);
        log.log("equal prob. = " + probEqual + "\t"
                + "most dis. = " + mostDis + "\t"
                + "smooth prob. = " + smoothVal + "\t"
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
