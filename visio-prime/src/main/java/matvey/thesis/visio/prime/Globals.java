/*
 * Date: Jan 10, 2004
 * Copyright (c) 2004 Matvey Kazakov.
 *
 * $Id$
 */
package matvey.thesis.visio.prime;

import java.util.Arrays;

/**
 * <code>Globals</code> class
 *
 * @author Matvey Kazakov
 */
public class Globals {

    // Состояние автомата
    public int state;
    // Массив флагов
    public boolean a[];
    // Длина массива
    public int n;
    // Служебные переменные
    public int i, j, q;
    public Automaton auto;

    public Globals(int n) {
        this.n = n;
		this.a = new boolean[n+1];
        auto = new Automaton(this);
        init();
    }

	public void init() {
        Arrays.fill(a, true);
        a[0] = a[1] = false;
		auto.init();
	}


}

/*
 * $Log$
 */