package train;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * Created by gongyu on 2015/5/11.
 */

public class TrainModel {
    public void trainNoisyChannelModel(String dicFile, String trainFile, String modelFile,
                                       int contextNum, String transferFreq) throws Exception {
        ReadRaw rr = new ReadRaw();
        rr.readRaw(dicFile, trainFile, contextNum, transferFreq);

        BufferedWriter bw = new BufferedWriter(new FileWriter(modelFile));
        for (String error : rr.errorDataMap.keySet()) {
            String[] strs = error.split("\t");
            double log_prob = Math.log((double) rr.errorDataMap.get(error) / rr.corpusCountMap.get(strs[0]));
            bw.write(error + "\t" + String.valueOf(log_prob));
            bw.newLine();
            bw.flush();
        }
        bw.close();
    }
}
