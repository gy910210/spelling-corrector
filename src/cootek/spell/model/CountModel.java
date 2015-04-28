package cootek.spell.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;

/**
 * Created by gongyu on 2015/4/25.
 */
public class CountModel {
    private HashMap<String, Integer> countMap;

    public void init(String error_file) throws Exception{
        BufferedReader br = new BufferedReader(new FileReader(error_file));
        countMap = new HashMap<>();

        String line;
        while((line = br.readLine()) != null){
            if(line.trim().equals("")) continue;
            String[] strs = line.trim().split("\t");
            String[] pairs = strs[0].trim().split(" | ");
            // System.out.println(pairs[0]);
            if(!countMap.containsKey(pairs[0])) countMap.put(pairs[0], 0);
        }
        System.out.println("key count: " + countMap.keySet().size());
        br.close();
    }

    public void dumpCountInfo(String train_file, String count_file) throws Exception{
        BufferedReader br = new BufferedReader(new FileReader(train_file));
        BufferedWriter bw = new BufferedWriter(new FileWriter(count_file));

        int cnt = 1;
        String line;
        while((line = br.readLine()) != null){
            if(cnt % 10000 == 0) System.out.println("cnt: " + cnt);
            if(line.trim().equals("")) continue;
            String[] strs = line.trim().split("\t");
            for(String key : countMap.keySet()){
                int count = kmp(strs[0], key) * Integer.parseInt(strs[2]) + countMap.get(key);
                countMap.put(key, count);
            }
            cnt += 1;
        }
        br.close();

        for(String key : countMap.keySet()){
            bw.write(key + "\t" + countMap.get(key));
            bw.newLine();
            bw.flush();
        }
        bw.close();
    }

    private int[] compute_prefix_function(String P)
    {
        int m = P.length();
        int[] next = new int[m + 1];
        next[0] = -1;
        int k = -1, j = 0;
        while(j < m) {
            if(k == -1 || P.charAt(j) == P.charAt(k)) {
                k++; j++;
                next[j] = k;
            } else k = next[k];
        }
        return next;
    }

    public int kmp(String target, String pattern){
        int ans = 0;
        int n = target.length();
        int m = pattern.length();
        int[] next = compute_prefix_function(pattern);
        int i = 0, j = 0;

        while(i < n) {
            if(j == -1 || target.charAt(i) == pattern.charAt(j)) {
                i++; j++;
            } else j = next[j];

            if(j == m) {
                ans++;
                j = next[j];
            }
        }
        return ans;
    }

    public static void main(String[] args) throws Exception{
        CountModel countModel = new CountModel();
        // System.out.println(countModel.kmp("abcbc", "ab"));
        countModel.init("error_data.txt");
        countModel.dumpCountInfo("training_data.txt", "count_data.txt");
    }
}
