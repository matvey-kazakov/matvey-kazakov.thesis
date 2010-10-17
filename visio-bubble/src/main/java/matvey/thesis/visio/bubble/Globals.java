/*
 * Date: Jan 10, 2004
 * Copyright (c) 2004 Matvey Kazakov.
 *
 * $Id$
 */
package matvey.thesis.visio.bubble;

/**
 * <code>Globals</code> class
 *
 * @author Matvey Kazakov
 */
public class Globals {

    // Состояние автомата
    public int state;
    // Сортируемый массив
    public int a[];
    // Длина массива
    public int n;
    // Служебные переменные
    public int i, j;

    public Globals(int[] a) {
        init(a);
    }

    public void init(int[] a) {
        n = a.length;
        this.a = new int[a.length];
        for (int k = 0; k < a.length; k++) {
            this.a[k] = a[k];
        }
        state = Automaton.STATE_START;
        i = 0;
        j = 0;
    }


}

/*
 * $Log$
 */