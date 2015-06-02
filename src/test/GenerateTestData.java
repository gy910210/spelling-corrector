package test;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import train.Pair;
import utils.LoadDictionary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.*;

/**
 * Created by gongyu on 2015/5/11.
 */

public class GenerateTestData {
    private HashSet<Pair<String>> generateTestData(String testFile, String wordsFile,
                                           int sampleNum, int mostDis) throws Exception {
        List<Pair<String>> list = new ArrayList<>();
        HashSet<String> dicSet = new LoadDictionary().loadDictionary(wordsFile);
        BufferedReader br = new BufferedReader(new FileReader(testFile));

        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().equals("")) continue;

            JsonParser parser = new JsonParser();
            JsonObject jsonObj = parser.parse(line.trim()).getAsJsonObject();

            String cor_type = jsonObj.get("cor_type").getAsString();
            String match_type = jsonObj.get("match_type").getAsString();
            String word = jsonObj.get("word").getAsString().toLowerCase();
            int cnt = jsonObj.get("cnt").getAsInt();

            if (cor_type.equals("cor") || cor_type.equals("")
                    || !dicSet.contains(word) || cor_type.equals("spe_cor")) {
                continue;
            }

            if (match_type.equals("predict") && cor_type.equals("spe")) continue;

            JsonObject spellInfo = jsonObj.get("spell_info").getAsJsonObject();
            String spell_in = spellInfo.get("spell_in").getAsString().toLowerCase();
            String spell_out = spellInfo.get("spell_out").getAsString().toLowerCase();

            Pair<String> pair;
            if (match_type.equals("precise")) {
                pair = new Pair<>(spell_out, spell_in);
            } else {
                StringBuilder error_word = new StringBuilder(spell_in);
                for (int i = 0; i < word.length(); i++) {
                    if (i < spell_out.length()) continue;
                    else error_word.append(word.charAt(i));
                }
                pair = new Pair<>(word, error_word.toString());
            }

            List<Pair<Character>> pos_list = new ArrayList<>();
            int dis = new EditDistance().edit(pair.val1, pair.val2, pos_list);
            if (dis > mostDis) continue;

            while (cnt-- > 0) {
                list.add(pair);
            }
        }
        br.close();

        System.out.println(list.size());

        HashSet<Pair<String>> testSet = new HashSet<>();
        while (testSet.size() < sampleNum) {
            System.out.println(testSet.size());
            int index = new Random().nextInt(list.size());
            testSet.add(list.get(index));
        }
        return testSet;
    }

    public void dumpTestData(String testFile, String wordsFile,
                             int sampleNum, int mostDis) throws Exception {
        HashSet<Pair<String>> testSet = generateTestData(testFile, wordsFile, sampleNum, mostDis);
        BufferedWriter bw = new BufferedWriter(new FileWriter("test_data.txt"));

        for (Pair<String> pair : testSet) {
            bw.write(pair.val1 + "\t" + pair.val2);
            bw.newLine();
            bw.flush();
        }
        bw.close();
    }

    public List<Pair<String>> loadTestData(String testFile) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(testFile));
        List<Pair<String>> list = new ArrayList<>();

        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().equals("")) continue;
            String[] strs = line.trim().split("\t");
            list.add(new Pair<>(strs[0].trim().toLowerCase(), strs[1].trim().toLowerCase()));
        }
        br.close();
        return list;
    }

    public static void main(String[] args) throws Exception {
        new GenerateTestData().dumpTestData("final.out", "words.txt", 5000, 2);
    }
}
