package test;

import java.util.*;

/**
 * Created by gy910 on 2015/6/2.
 */
public class GetMapSmallest {
    public double getMapSmallest(HashMap<String, Double> map, int k) {
        List<Map.Entry<String, Double>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, (o1, o2) -> o1.getValue().compareTo(o2.getValue()));
        double sum = 0.0;
        for (int i = 0; i < k; i++) {
            sum += list.get(i).getValue();
        }

        return sum / k;
    }
}
