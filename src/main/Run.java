package main;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import test.TestModel;
import train.TrainModel;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Created by gongyu on 2015/5/5.
 */

public class Run {
    public static void main(String[] args) {
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
            String model_file = jsonObj.get("model_file").getAsString();
            String train_file = jsonObj.get("train_file").getAsString();
            String dic_file = jsonObj.get("dic_file").getAsString();

            double equal_prob = jsonObj.get("equal_prob").getAsDouble();
            double smooth_prob = jsonObj.get("smooth_prob").getAsDouble();
            int most_dis = jsonObj.get("most_dis").getAsInt();
            int context_num = jsonObj.get("context_num").getAsInt();
            int top_num = jsonObj.get("top_num").getAsInt();
            String train = jsonObj.get("train").getAsString();

            // Train model
            if (train.equals("yes")) {
                System.out.println("train model ...");
                TrainModel trainModel = new TrainModel();
                trainModel.trainNoisyChannelModel(dic_file, train_file, model_file, context_num);
            }

            // Test model
            System.out.println("test model ...");
            TestModel testModel = new TestModel(model_file, dic_file,
                    equal_prob, most_dis, context_num, smooth_prob, top_num);
            testModel.test();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
