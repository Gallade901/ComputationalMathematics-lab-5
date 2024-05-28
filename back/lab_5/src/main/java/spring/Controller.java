package spring;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import util.Points;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/app-controller")
@CrossOrigin(origins = "*")
public class Controller {
    Main main = new Main();

    @PostMapping("/points")
    public ResponseEntity points(@RequestBody HashMap pointsR) {
        HashMap<String, Object> points = pointsR;
        double max = Double.MIN_VALUE;
        double min = Double.MAX_VALUE;
        double[] x = new double[points.size()/2]; // Массив для хранения ключей
        double[] y = new double[points.size()/2]; // Массив для хранения значений
        int index = 0;
        boolean flag = true;
        for (Map.Entry<String, Object> entry : points.entrySet()) {
            Object value = entry.getValue();
            if (value instanceof Integer) {
                if (flag) {
                    y[index] = ((Integer) value).doubleValue(); // Convert Integer to double
                    flag = false;
                } else {
                    x[index] = ((Integer) value).doubleValue(); // Convert Integer to double
                    flag = true;
                    index++;
                }
            } else if (value instanceof Double) {
                if (flag) {
                    y[index] = (Double) value; // Directly assign Double
                    flag = false;
                } else {
                    x[index] = (Double) value; // Directly assign Double
                    flag = true;
                    index++;
                }
            }
        }
        for (int i = 0; i < x.length; i++) {
            if (x[i] > max) {
                max = x[i];
            }
            if (x[i] < min) {
                min = x[i];
            }
        }
        Points points1 = new Points(x, y);
        ArrayList ans = main.calculateMethods(points1, max, min);
        return ResponseEntity.ok(ans);
    }

    @GetMapping("/functions")
    public ResponseEntity functions() {
        return ResponseEntity.ok("");
    }

    @GetMapping("/welcome")
    public ResponseEntity welcome() {
        return ResponseEntity.ok("welcome");
    }
}
