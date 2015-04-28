package cootek.spell.main;

import cootek.spell.bean.Pair;
import cootek.spell.bean.PredictWord;
import cootek.spell.test.FormatTest;
import cootek.spell.test.TestData;
import cootek.spell.tool.Log;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by gongyu on 2015/4/26.
 */
public class TestModel {
    public double moreTest(String rawFile, String wordsFile) throws Exception{
        HashMap<String, HashSet<String>> testData = new FormatTest().loadTestData(rawFile, wordsFile);
        PredictModel pm = new PredictModel(1.0, 2, 0.00005, 2);

        int total = testData.keySet().size();
        System.out.println("Total: " + total);
        int cnt = 0;
        int sum = 0;
        for(String source : testData.keySet()){
            List<PredictWord> word_list = pm.predict(source, 3);
            boolean flag = false;
            for(PredictWord word : word_list){
                if(testData.get(source).contains(word.w)){
                    flag = true;
                    break;
                }
            }
            if(flag == true){
                cnt += 1; sum += 1;
                System.out.println("Bingo!" + "\t" + cnt + "/" + sum);
            }else{
                sum += 1;
                System.out.println("False!");
            }
        }
        System.out.println("Cnt: " + cnt);
        return (double) cnt / total;
    }

    public double test(String testFile, String wordsFile) throws Exception{
        // HashSet<Pair<String>> testData = new TestData().loadTestData(testFile, wordsFile);
        Log log = new Log();
        log.open("log.txt");

        List<Pair<String>> testData = new TestData().loadTestData(testFile, wordsFile, 1000, 2);
        log.log("test data = " + 1000 + "\t" + "most dis. = " + 2);

        PredictModel pm = new PredictModel(1.0, 2, 0.00000005, 2);
        log.log("equal prob. = " + 1.0 + "\t"
                + "most dis. = " + 2 + "\t"
                + "smooth prob. = " + 0.00000005 + "\t"
                + "context num. = " + 2);
        log.log("==========");

        int total = testData.size();
        System.out.println("Total: " + total);
        int cnt = 0, sum = 0;

        for(Pair<String> pair : testData){
            List<PredictWord> word_list = pm.predict(pair.val2, 3);
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

    public static void main(String[] args) throws Exception{
        System.out.println(new TestModel().test("training_data.txt", "words.txt"));
    }
}
