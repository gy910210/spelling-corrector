package main;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import prune.PruneModel;
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
            StringBuilder sb = new StringBuilder("");
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
            int most_dis = jsonObj.get("most_dis").getAsInt();
            int context_num = jsonObj.get("context_num").getAsInt();
            int top_num = jsonObj.get("top_num").getAsInt();
            String train = jsonObj.get("train").getAsString();
            String test = jsonObj.get("test").getAsString();
            String prune = jsonObj.get("prune").getAsString();

            String transfer_freq = jsonObj.get("transfer_freq").getAsString();

            // Train model
            if (train.equals("yes")) {
                System.out.println("train model ...");
                TrainModel trainModel = new TrainModel();
                trainModel.trainNoisyChannelModel(dic_file, train_file, model_file,
                        context_num, transfer_freq);
            }

            // Test model
            if (test.equals("yes")) {
                System.out.println("test model ...");
                TestModel testModel = new TestModel(model_file, dic_file,
                        equal_prob, most_dis, context_num, top_num);
                testModel.test();
            }

            // Prune model
            if (prune.equals("yes")) {
                System.out.println("prune model ...");
                PruneModel pm = new PruneModel(dic_file, model_file, equal_prob,
                        most_dis, context_num, top_num);
                pm.prune(20);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
