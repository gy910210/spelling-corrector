package cootek.spell.tool;

import cootek.spell.bean.Pair;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

/**
 * Created by gongyu on 2015/4/27.
 * Randomly generate test data from 'train_data.txt'
 */
public class GenerateTestData {
    private HashSet<Pair<String>> loadTestData(String testFile, String wordsFile) throws Exception{
        BufferedReader br = new BufferedReader(new FileReader(testFile));
        HashSet<String> dicSet = new LoadDictionary().loadDictionary(wordsFile);

        HashSet<Pair<String>> list = new HashSet<>();
        String line;
        while((line = br.readLine()) != null){
            String[] strs = line.trim().split("\t");
            if(strs[0].toLowerCase().equals(strs[1].toLowerCase()))
                continue;
            if((strs[0].length() <= 1) || (strs[1].length() <= 1))
                continue;
            if(!dicSet.contains(strs[0].toLowerCase()))
                continue;

            Pair<String> pair = new Pair<>(strs[0].toLowerCase(), strs[1].toLowerCase());
            list.add(pair);
        }
        br.close();
        return list;
    }

    public List<Pair<String>> loadTestData(String testFile, String wordsFile,
                                           int sampleNum, int mostDis) throws Exception {
        HashSet<Pair<String>> testDataSet = loadTestData(testFile, wordsFile);
        Object[] testDataArray = testDataSet.toArray();
        List<Pair<String>> list = new ArrayList<>();

        int cnt = 0;
        for(int i = 0; i < testDataArray.length; i++){
            if(cnt == sampleNum) break;

            int index = new Random().nextInt(testDataArray.length - i) + i;
            Pair<String> pair = (Pair<String>) testDataArray[index];
            List<Pair<Character>> pos_list = new ArrayList<>();
            int dis = new EditDistance().edit(pair.val1, pair.val2, pos_list);

            if(dis <= mostDis){
                list.add(pair);
                cnt += 1;
            }
            // swap
            Pair<String> tmp = (Pair<String>) testDataArray[i];
            testDataArray[i] = testDataArray[index];
            testDataArray[index] = tmp;
        }

        return list;
    }
}
