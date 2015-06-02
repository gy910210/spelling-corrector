package utils;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * Created by gongyu on 2015/4/28.
 * Save the log information.
 */
public class Log {
    public BufferedWriter bw;

    public void open (String file) {
        try {
            this.bw = new BufferedWriter(new FileWriter(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void log(String content) {
        try {
            this.bw.write(content);
            this.bw.newLine();
            this.bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            this.bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
