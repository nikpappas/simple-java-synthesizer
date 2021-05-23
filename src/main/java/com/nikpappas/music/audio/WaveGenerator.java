package com.nikpappas.music.audio;

import static com.nikpappas.music.audio.SineSynth.MAX_VOL;
import static java.lang.Math.*;

public interface WaveGenerator {

    WaveGenerator SINE = (angle, time) -> Long.valueOf(
            round(sin(angle * time) * MAX_VOL))
            .byteValue();


    WaveGenerator SAT_SINE = (angle, time) -> Long.valueOf(
            round(saturate(sin(angle * time), 0.6) * MAX_VOL))
            .byteValue();

    WaveGenerator SQUARE = (angle, time) -> Long.valueOf(
            round(squareTheSin(sin(angle * time)) * MAX_VOL))
            .byteValue();

    WaveGenerator TRI = (angle, time) -> {
        double res = ((2*(angle*time) % (2.0 * PI)) / PI)-1;
        if (res > 1) {
            res = 2-res;
        }
        return Long.valueOf(
                round(res * MAX_VOL))
                .byteValue();
    };

    WaveGenerator SAW = (angle, time) -> {
        double res = ((angle*time) % (2.0 * PI)) / PI;
        if (res > 1) {
            res -= 2;
        }
        return Long.valueOf(
                round(res * MAX_VOL))
                .byteValue();
    };



    byte generate(double angle, double time);


    static double squareTheSin(double val) {
        return val > 0 ? 1.0 : -1.0;

    }

    static double saturate(double val, double threshold) {
        if(abs(val)>threshold){
            return squareTheSin(val);
        }
        return val;
    }

}
