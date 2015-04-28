package cootek.spell.bean;

/**
 * Created by gongyu on 2015/4/26.
 */
public class PredictWord implements Comparable<PredictWord>{
    public String w;
    public double prob;

    @Override
    public int compareTo(PredictWord o) {
        if(this.prob < o.prob) return 1;
        else if(this.prob > o.prob) return -1;
        else return 0;
    }

    public PredictWord(String w, double prob) {
        this.w = w;
        this.prob = prob;
    }

    @Override
    public String toString() {
        return "(" + w + "," + String.valueOf(prob) + ")";
    }
}
