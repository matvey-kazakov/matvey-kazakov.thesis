/*
 * Date: Oct 23, 2010
 * Copyright (c) 2001-2010 Flextronics Corp. All Rights Reserved.
 */
package matvey.thesis.visio.multi;

import java.util.Arrays;

/**
 * <code>Automaton1</code>
 *
 * @author Matvey Kazakov
 */
public abstract class Automaton {

	protected final Globals g;
	private int state;
	private final int level;
	private Automaton inner = null;

	protected abstract void makeAutomationStep();
	protected abstract void performAction();

	private final int[] hiddenStates;
	private final int[] finishStates;

	public Automaton(Globals g, int[] hiddenStates, int[] finishStates, int level) {
		this.g = g;
		this.hiddenStates = hiddenStates;
		this.finishStates = finishStates;
		this.level = level;
		state = 0;
	}

	/**
	 * Инициализируем автомат
	 */
	public final void init() {
		state = 0;
		skipHiddenStates();
	}

	public final void initInner(Automaton a) {
		this.inner = a;
		a.init();
	}

	/**
	 * Один шаг визуализатора состоит из нескольких переходов автомата.
	 */
	public final void step() {
		makeAutomationStep();
		skipHiddenStates();
	}

	/**
	 * Пропуск неинтересных состояний
	 */
	private void skipHiddenStates() {
		while (!stopped() && (isHidden() || level < g.skipLevel)) {
			if (inner != null && inner.stopped()) {
				inner = null;
			}
			if (g.skipLevel <= level) {
				g.skipLevel = 0;
			}
			makeAutomationStep();
		}
	}
	private boolean isHidden() {
		return (inner != null && inner.isHidden() || inner == null)
			&& Arrays.binarySearch(hiddenStates, state) >= 0;
	}

	/**
	 * Проверяет, что автомат уже остановился.
	 */
	public final boolean stopped() {
		return Arrays.binarySearch(finishStates, state) >= 0;
	}

	/**
	 * Производит переход автомата и действие в вершине
	 * @param newstate	новое состояние
	 */
	protected final void goToAndPerformAction(int newstate) {
		state = newstate;
		performAction();
	}

	/**
	 * Возвращает текущее состояние автомата
	 * @return
	 */
	public int state() {
		return state;
	}

}
