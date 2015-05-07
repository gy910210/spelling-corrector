package cootek.spell.main;

import java.io.File;
import java.util.Arrays;
import cootek.spell.model.ErrorModel;
import cootek.spell.model.CountModel;
import cootek.spell.model.NoisyChannelModel;
import cootek.spell.tool.FormatTrainData;
import java.util.HashSet;

/**
 * Created by gongyu on 2015/5/5.
 * Train Channel Model
 */
public class TrainModel {
    private String DIR_PATH; // middle dir (user input)
    private final String ERROR_DATA_FILE = "error_data.txt"; // middle file 'error_data.txt'
    private final String COUNT_DATA_FILE = "count_data.txt"; // middle file 'count_data.txt'
    private final String TRAIN_DATA_FILE = "train_data.txt"; // middle file 'train_data.txt'
    private final String CORPUS_DATA_FILE = "corpus_data.txt"; // middle file 'corpus_data.txt'

    public TrainModel(String DIR_PATH) {
        this.DIR_PATH = DIR_PATH;
    }

    public void train(String input_file, String channel_file, int context_num) throws Exception{
        File dir = new File(DIR_PATH);
        HashSet<String> fileSet = new HashSet<>();
        fileSet.addAll(Arrays.asList(dir.list()));

        System.out.println("... dump train_data");
        if (!fileSet.contains(TRAIN_DATA_FILE)) {
            new FormatTrainData().dumpTrainData(input_file, DIR_PATH + TRAIN_DATA_FILE, DIR_PATH + CORPUS_DATA_FILE);
        }

        System.out.println("... dump error_data");
        if (!fileSet.contains(ERROR_DATA_FILE)) {
            new ErrorModel().dumpErrorData(DIR_PATH + TRAIN_DATA_FILE, DIR_PATH + ERROR_DATA_FILE, context_num);
        }

        System.out.println("... dump count_data");
        if (!fileSet.contains(COUNT_DATA_FILE)) {
            CountModel countModel = new CountModel();
            countModel.init(DIR_PATH + ERROR_DATA_FILE);
            countModel.dumpCountData(DIR_PATH + CORPUS_DATA_FILE, DIR_PATH + COUNT_DATA_FILE);
        }

        System.out.println("... dump channel_data");
        new NoisyChannelModel().dumpNoisyChannelData(DIR_PATH + ERROR_DATA_FILE,
                DIR_PATH + COUNT_DATA_FILE, channel_file);
    }
}
