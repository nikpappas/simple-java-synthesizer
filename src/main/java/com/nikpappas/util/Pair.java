package com.nikpappas.util;

public class Pair<T1, T2> {
    public final T1 _1;
    public final T2 _2;

    private Pair(T1 _1, T2 _2){
        this._1 = _1;
        this._2 = _2;
    }

    public static <E1, E2> Pair<E1, E2> of(E1 _1, E2 _2){
        return new Pair<>(_1,_2);
    }

}
