package cootek.spell.tool;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

/**
 * Created by gongyu on 2015/5/5.
 * Format the training data as (word, key) pairs from original input.
 */
public class FormatTrainData {
    public void dumpTrainData(String input_file, String train_file, String corpus_file) throws Exception{
        BufferedReader br = new BufferedReader(new FileReader(input_file));
        BufferedWriter bw = new BufferedWriter(new FileWriter(train_file));
        BufferedWriter bwAll = new BufferedWriter(new FileWriter(corpus_file));

        String line;
        while ((line = br.readLine()) != null) {
            if(line.trim().equals("")) continue;

            JsonParser parser = new JsonParser();
            JsonObject jsonObj = parser.parse(line.trim()).getAsJsonObject();

            String cor_type = jsonObj.get("cor_type").getAsString();
            String match_type = jsonObj.get("match_type").getAsString();
            String word = jsonObj.get("word").getAsString();
            String key = jsonObj.get("key").getAsString();
            String cnt = jsonObj.get("cnt").getAsString();

            bwAll.write(word + "\t" + cnt);
            bwAll.newLine();
            bwAll.flush();

            if (cor_type.equals("cor") || cor_type.equals("spe_cor")) continue;

            if (match_type.equals("precise")) {
                bw.write(word + "\t" + key + "\t" + cnt);
                bw.newLine();
            }else if (match_type.equals("predict")) {
                if (cor_type.equals("")) {
                    bw.write(word + "\t" + word + "\t" + cnt);
                    bw.newLine();
                }else if (cor_type.equals("spe")) {
                    JsonObject jsonSpeObj = jsonObj.get("spell_info").getAsJsonObject();
                    StringBuffer error_word = new StringBuffer(jsonSpeObj.get("spell_in").getAsString());
                    String spell_out = jsonSpeObj.get("spell_out").getAsString();
                    for (int i = 0; i < word.length(); i++) {
                        if (i < spell_out.length()) continue;
                            else error_word.append(word.charAt(i));
                    }
                    bw.write(word + "\t" + error_word.toString() + "\t" + cnt);
                    bw.newLine();
                }
            }
            bw.flush();
        }

        br.close();
        bw.close();
        bwAll.close();
    }
}
