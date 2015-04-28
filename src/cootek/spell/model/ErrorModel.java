package cootek.spell.model;

import cootek.spell.bean.Pair;
import cootek.spell.tool.EditDistance;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by gongyu on 2015/4/25.
 */
public class ErrorModel {

    public List<String> splitError(String word, String source, int contex_num){
        List<String> split_list = new ArrayList<>();
        List<Pair<Character>> pos_list = new ArrayList<>();
        new EditDistance().edit(word, source, pos_list);
        // System.out.println(pos_list);

        for(int pos = 0; pos < pos_list.size(); pos++){
            if(pos_list.get(pos).val1 == pos_list.get(pos).val2) continue;

            split_list.add(pos_list.get(pos).val1 + " | " + pos_list.get(pos).val2);

            for(int slide = 1; slide <= contex_num; slide++){
                int head = Math.max(0, pos - slide);
                for(int i = head; i <= pos; i++){
                    if(i + slide >= pos_list.size()) continue;
                    StringBuffer slice_w = new StringBuffer("");
                    StringBuffer slice_s = new StringBuffer("");

                    for(int ii = 0; ii <= slide; ii++){
                        if(pos_list.get(i+ii).val1 != '#') slice_w.append(pos_list.get(i+ii).val1);
                        if(pos_list.get(i+ii).val2 != '#') slice_s.append(pos_list.get(i+ii).val2);
                    }

                    split_list.add(slice_w.append(" | ").append(slice_s).toString());
                }
            }
        }
        return split_list;
    }

    public void dumpErrorData(String train_file, String error_file, int context_num) throws Exception{
        BufferedReader br = new BufferedReader(new FileReader(train_file));
        BufferedWriter bw = new BufferedWriter(new FileWriter(error_file));

        System.out.println("read and split error data ...");
        HashMap<String, Integer> error_map = new HashMap<>();
        String line;
        while((line = br.readLine()) != null) {
            if(line.trim().equals("")) continue;

            String[] strs = line.trim().split("\t");
            if(strs[0].equals(strs[1])) continue;

            // System.out.println(strs[0] + "\t" + strs[1]);
            List<String> split_list = splitError(strs[0], strs[1], context_num);
            for(String error : split_list){
                if(!error_map.containsKey(error)) error_map.put(error, Integer.parseInt(strs[2]));
                else error_map.put(error, error_map.get(error) + Integer.parseInt(strs[2]));
            }
        }

        System.out.println("dump error data ...");
        for(String key : error_map.keySet()){
            bw.write(key + "\t" + error_map.get(key));
            bw.newLine();
            bw.flush();
        }

        br.close();
        bw.close();
    }

    public static void main(String[] args) throws Exception{
        System.out.println(new cootek.spell.model.ErrorModel().splitError("prepare", "perpare", 2));
        // new ErrorModel().dumpErrorData("training_data.txt", "error_data.txt", 2);
    }
}
