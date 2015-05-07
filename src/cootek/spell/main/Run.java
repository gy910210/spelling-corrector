package cootek.spell.main;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Created by gongyu on 2015/5/5.
 */
public class Run {
    public static void main(String[] args){
        JsonParser parser = new JsonParser();
        BufferedReader brParameter;
        try {
            // Parse parameter
            System.out.println("parse parameter ...");
            brParameter = new BufferedReader(new FileReader("parameter"));
            StringBuffer sb = new StringBuffer("");
            String line;
            while ((line = brParameter.readLine()) != null) {
                if (line.trim().equals("")) continue;
                sb.append(line.trim());
            }
            JsonObject jsonObj = parser.parse(sb.toString()).getAsJsonObject();
            String tmp_dir = jsonObj.get("tmp_dir").getAsString();
            String channel_file = jsonObj.get("channel_file").getAsString();
            String input_file = jsonObj.get("input_file").getAsString();
            String words_file = jsonObj.get("words_file").getAsString();
            String test_file = jsonObj.get("test_file").getAsString();
            double equal_prob = jsonObj.get("equal_prob").getAsDouble();
            double smooth_prob = jsonObj.get("smooth_prob").getAsDouble();
            int most_dis = jsonObj.get("most_dis").getAsInt();
            int context_num = jsonObj.get("context_num").getAsInt();
            int top_num = jsonObj.get("top_num").getAsInt();

            // Train model
            System.out.println("train model ...");
            TrainModel trainModel = new TrainModel(tmp_dir);
            trainModel.train(input_file, channel_file, context_num);

            // Test model
            System.out.println("test model ...");
            TestModel testModel = new TestModel(channel_file, test_file, words_file,
                    equal_prob, most_dis, context_num, smooth_prob, top_num);
            testModel.test();

        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
