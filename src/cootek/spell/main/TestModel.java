package cootek.spell.main;

import java.util.List;
import cootek.spell.bean.PredictWord;
import cootek.spell.bean.Pair;
import cootek.spell.tool.Log;
import cootek.spell.tool.GenerateTestData;

/**
 * Created by gongyu on 2015/4/26.
 * Test Channel Model.
 */
public class TestModel {
    private String NOISY_CHANNEL_FILE;
    private String WORDS_FILE;
    private String TEST_FILE;

    private double probEqual;
    private int mostDis;
    private double smoothVal;
    private int contextNum;
    private int topNum;

    public TestModel(String NOISY_CHANNEL_FILE, String TEST_FILE, String WORDS_FILE,
                     double probEqual, int mostDis, int contextNum, double smoothVal, int topNum) {
        this.NOISY_CHANNEL_FILE = NOISY_CHANNEL_FILE;
        this.TEST_FILE = TEST_FILE;
        this.WORDS_FILE = WORDS_FILE;
        this.probEqual = probEqual;
        this.mostDis = mostDis;
        this.contextNum = contextNum;
        this.smoothVal = smoothVal;
        this.topNum = topNum;
    }

    public double test() throws Exception{
        Log log = new Log();
        log.open("test_result.txt");

        // Random select 1000 test data
        List<Pair<String>> testData = new GenerateTestData().loadTestData(TEST_FILE, WORDS_FILE, 1000, mostDis);
        log.log("test data = " + 1000 + "\t" + "most dis. = " + mostDis);

        PredictModel pm = new PredictModel(WORDS_FILE, NOISY_CHANNEL_FILE, probEqual, mostDis, smoothVal, contextNum);
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
