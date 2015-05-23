package test;

import java.util.Collections;
import java.util.List;
import train.Pair;

/**
 * Created by gongyu on 2015/4/25.
 * Calculate edit distance and Return the alignment.
 */

public class EditDistance {
    class Pos {
        int x, y;
        char ch1, ch2;

        public Pos() {

        }

        public void setPos(int x, int y, char ch1, char ch2) {
            this.x = x;
            this.y = y;
            this.ch1 = ch1;
            this.ch2 = ch2;
        }
    }

    public int edit(String word, String source, List<Pair<Character>> pos_list) {
        int m = word.length();
        int n = source.length();

        int[][] record = new int[m + 1][n + 1];
        Pos[][] pos_record = new Pos[m + 1][n + 1];
        for (int i = 0; i <= m; i++)
            for (int j = 0; j <= n; j++)
                pos_record[i][j] = new Pos();

        for (int i = 1; i <= n; i++) {
            record[0][i] = i;
            pos_record[0][i].setPos(0, i - 1, '#', source.charAt(i - 1));
        }
        for (int i = 1; i <= m; i++) {
            record[i][0] = i;
            pos_record[i][0].setPos(i - 1, 0, word.charAt(i - 1), '#');
        }

        for (int i = 1; i <= m; i++) {
            for(int j = 1; j <= n; j++){
                int tmp = 0;
                if(word.charAt(i - 1) != source.charAt(j - 1))
                    tmp = 1;

                if(record[i - 1][j] + 1 < record[i][j - 1] + 1) {
                    if (record[i - 1][j] + 1 < record[i - 1][j - 1] + tmp)
                        pos_record[i][j].setPos(i - 1, j, word.charAt(i - 1), '#');
                    else
                        pos_record[i][j].setPos(i - 1, j - 1, word.charAt(i - 1), source.charAt(j - 1));
                }
                else {
                    if (record[i][j - 1] + 1 < record[i - 1][j - 1] + tmp)
                        pos_record[i][j].setPos(i, j - 1, '#', source.charAt(j - 1));
                    else
                        pos_record[i][j].setPos(i - 1, j - 1, word.charAt(i - 1), source.charAt(j - 1));
                }
                record[i][j] = Math.min(Math.min(record[i - 1][j] + 1, record[i][j - 1] + 1),
                        record[i - 1][j - 1] + tmp);
            }
        }

        int i = m, j = n;
        while ((i > 0) || (j > 0)) {
            Pos pos = pos_record[i][j];
            i = pos.x;
            j = pos.y;
            pos_list.add(new Pair<>(pos.ch1, pos.ch2));
        }

        Collections.reverse(pos_list);
        return record[m][n];
    }
}
