package train;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by gongyu on 2015/5/11.
 */

public class ReadRaw {

    public HashSet<String> dicSet;
    public HashMap<String, Double> errorDataMap;
    public HashMap<String, Double> corpusCountMap;

    public void readRaw(String dicFile, String trainFile, int contextNum, String transferFreq) throws Exception {
        loadDictionary(dicFile);
        loadTrainData(trainFile, contextNum, transferFreq);
    }

    public void loadTrainData(String trainFile, int contextNum, String transferFreq) throws Exception {
        errorDataMap = new HashMap<>();
        corpusCountMap = new HashMap<>();
        BufferedReader br = new BufferedReader(new FileReader(trainFile));
        String line;

        while ((line = br.readLine()) != null) {
            if (line.trim().equals("")) continue;

            JsonParser parser = new JsonParser();
            JsonObject jsonObj = parser.parse(line.trim()).getAsJsonObject();

            String cor_type = jsonObj.get("cor_type").getAsString();
            String match_type = jsonObj.get("match_type").getAsString();
            String word = jsonObj.get("word").getAsString().toLowerCase();
            int cnt = Integer.parseInt(jsonObj.get("cnt").getAsString());

            if (!dicSet.contains(word) || cor_type.equals("spe_cor")) continue;
            if (match_type.equals("predict") && cor_type.equals("spe")) continue;

            List<String> wordSliceList = getWordSliceList(word, contextNum + 1);
            for (String slice : wordSliceList) {
                double count;
                if (!corpusCountMap.containsKey(slice)) {
                    count = (double) cnt;
                } else {
                    count = corpusCountMap.get(slice) + cnt;
                }
                corpusCountMap.put(slice, count);
            }

            if (cor_type.equals("") || cor_type.equals("cor")) continue;

            JsonObject spellInfo = jsonObj.get("spell_info").getAsJsonObject();
            String spell_in = spellInfo.get("spell_in").getAsString().toLowerCase();
            String spell_out = spellInfo.get("spell_out").getAsString().toLowerCase();
            String spell_type = spellInfo.get("spell_type").getAsString();
            int spell_pos = spellInfo.get("spell_pos").getAsInt();

            List<Pair<Character>> posList = null;
            if (match_type.equals("precise")) {
                posList = alignPos(spell_out, spell_in, spell_type, spell_pos);
            } else if (match_type.equals("predict")) {
                StringBuilder error_word = new StringBuilder(spell_in);
                for (int i = 0; i < word.length(); i++) {
                    if (i < spell_out.length()) continue;
                    else error_word.append(word.charAt(i));
                }
                posList = alignPos(word, error_word.toString(), spell_type, spell_pos);
            }

            List<String> errorList = splitError(posList, contextNum);
            for (String error : errorList) {
                if (!errorDataMap.containsKey(error)) {
                    errorDataMap.put(error, (double) cnt);
                } else {
                    double count = errorDataMap.get(error) + (double) cnt;
                    errorDataMap.put(error, count);
                }
            }
        }
        br.close();

        switch (transferFreq) {
            case "no":
                for (String slice : corpusCountMap.keySet()) {
                    corpusCountMap.put(slice, corpusCountMap.get(slice));
                }
                for (String error : errorDataMap.keySet()) {
                    errorDataMap.put(error, errorDataMap.get(error));
                }
                break;
            case "log":
                for (String slice : corpusCountMap.keySet()) {
                    corpusCountMap.put(slice, Math.log(1 + corpusCountMap.get(slice)));
                }
                for (String error : errorDataMap.keySet()) {
                    errorDataMap.put(error, Math.log(1 + errorDataMap.get(error)));
                }
                break;
            case "loglog":
                for (String slice : corpusCountMap.keySet()) {
                    corpusCountMap.put(slice, Math.log(1 + Math.log(1 + corpusCountMap.get(slice))));
                }
                for (String error : errorDataMap.keySet()) {
                    errorDataMap.put(error, Math.log(1 + Math.log(1 + errorDataMap.get(error))));
                }
                break;
        }
    }

    private void loadDictionary(String dicFile) throws Exception {
        BufferedReader br = new BufferedReader(new FileReader(dicFile));
        dicSet = new HashSet<>();
        String line;
        while ((line = br.readLine()) != null) {
            if (line.trim().equals("")) continue;
            String[] strs = line.split(" ");
            dicSet.add(strs[1].trim().toLowerCase().replace("-", "").replace("'", ""));
        }
        br.close();
    }

    private List<String> getWordSliceList(String word, int windowNum) {
        List<String> wordSliceList = new ArrayList<>();
        for (int w = 1; w <= windowNum; w++) {
            for (int i = 0; i <= word.length() - w; i++) {
                wordSliceList.add(word.substring(i, i + w));
            }
        }
        return wordSliceList;
    }

    private List<String> splitError(List<Pair<Character>> posList, int contextNum) {
        List<String> errorList = new ArrayList<>();

        for (int pos = 0; pos < posList.size(); pos++) {
            if (posList.get(pos).val1 == posList.get(pos).val2) continue;

            if ((posList.get(pos).val1 != '#') && (posList.get(pos).val2 != '#'))
                errorList.add(posList.get(pos).val1 + "\t" + posList.get(pos).val2);

            for (int slide = 1; slide <= contextNum; slide++) {
                int head = Math.max(0, pos - slide);
                for (int i = head; i <= pos; i++) {
                    if(i + slide >= posList.size()) continue;
                    StringBuilder slice_w = new StringBuilder("");
                    StringBuilder slice_s = new StringBuilder("");

                    for (int ii = 0; ii <= slide; ii++) {
                        if (posList.get(i+ii).val1 != '#') slice_w.append(posList.get(i+ii).val1);
                        if (posList.get(i+ii).val2 != '#') slice_s.append(posList.get(i+ii).val2);
                    }
                    errorList.add(slice_w.append("\t").append(slice_s).toString());
                }
            }
        }
        return errorList;
    }

    private List<Pair<Character>> alignPos(String word, String key, String spell_type, int spell_pos) {
        List<Pair<Character>> posList = new ArrayList<>();
        int range = Math.max(key.length(), word.length());

        for (int i = 0; i < range; i++) {
            switch (spell_type) {
                case "__rep__":
                    if (i == spell_pos) {
                        posList.add(new Pair<>(word.charAt(i), key.charAt(i)));
                    } else {
                        posList.add(new Pair<>(word.charAt(i), word.charAt(i)));
                    }
                    break;
                case "__ins__":
                    if (i == spell_pos) {
                        posList.add(new Pair<>(word.charAt(i), '#'));
                    } else {
                        posList.add(new Pair<>(word.charAt(i), word.charAt(i)));
                    }
                    break;
                case "__del__":
                    if (i == spell_pos) {
                        posList.add(new Pair<>('#', key.charAt(i)));
                    } else {
                        posList.add(new Pair<>(key.charAt(i), key.charAt(i)));
                    }
                    break;
                case "__tra__":
                    if (i == spell_pos) {
                        posList.add(new Pair<>(word.charAt(i), key.charAt(i)));
                        i += 1;
                        posList.add(new Pair<>(word.charAt(i), key.charAt(i)));
                    } else {
                        posList.add(new Pair<>(word.charAt(i), word.charAt(i)));
                    }
                    break;
            }
        }
        return posList;
    }
}
