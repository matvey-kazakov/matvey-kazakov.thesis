/*
 * Date: Oct 22, 2010
 * Copyright (c) 2001-2010 Flextronics Corp. All Rights Reserved.
 */
package matvey.thesis.visio.multi;

/**
 * <code>AutomatonFR</code>
 *
 * @author Matvey Kazakov
 */
public class AutomatonMain extends Automaton {

	public AutomatonMain(Globals g) {
		super(g, new int[] { 1, 2, 3, 4, 11, 21, 31, 41 }, new int[] { 5, 6 }, 3);
	}

	@Override
	protected void makeAutomationStep() {
		switch (state()) {
		case 0: // Начальное состояние
			goToAndPerformAction(11);
			break;

		case 11: // Заполнение строки
			if (g.aFR.stopped()) {
				goToAndPerformAction(21);
			} else {
				goToAndPerformAction(1);
			}
			break;

		case 1: // Заполнение строки
			if (g.aFR.stopped()) {
				goToAndPerformAction(21);
			} else {
				goToAndPerformAction(1);
			}
			break;

		case 21: // Заполнение столбца
			if (g.aFC.stopped()) {
				goToAndPerformAction(31);
			} else {
				goToAndPerformAction(2);
			}
			break;

		case 2: // Заполнение столбца
			if (g.aFC.stopped()) {
				goToAndPerformAction(31);
			} else {
				goToAndPerformAction(2);
			}
			break;

		case 31: // Заполнение таблицы
			if (!g.aT.stopped()) {
				goToAndPerformAction(3);
			} else if (g.T[g.K][g.N] == 1) {
				goToAndPerformAction(41);
			} else {
				goToAndPerformAction(5);
			}
			break;

		case 3: // Заполнение таблицы
			if (!g.aT.stopped()) {
				goToAndPerformAction(3);
			} else if (g.T[g.K][g.N] == 1) {
				goToAndPerformAction(41);
			} else {
				goToAndPerformAction(5);
			}
			break;

		case 41: // Поиск результата
			if (g.aR.stopped()) {
				goToAndPerformAction(6);
			} else {
				goToAndPerformAction(4);
			}
			break;

		case 4: // Поиск результата
			if (g.aR.stopped()) {
				goToAndPerformAction(6);
			} else {
				goToAndPerformAction(4);
			}
			break;
		}
	}

	@Override
	protected void performAction() {
		switch (state()) {
		case 11: // Заполнение строки
			initInner(g.aFR);
			break;

		case 1:  // Заполнение строки
			g.aFR.step();
			break;

		case 21: // Заполнение столбца
			initInner(g.aFC);
			break;

		case 2: // Заполнение столбца
			g.aFC.step();
			break;

		case 31: // Заполнение таблицы
			initInner(g.aT);
			break;

		case 3: // Заполнение таблицы
			g.aT.step();
			break;

		case 41: // Поиск результата
			initInner(g.aR);
			break;

		case 4: // Поиск результата
			g.aR.step();
			break;

		case 5: // Финиш - нет
			g.result = false;
			break;

		case 6: // Финиш - да
			g.result = true;
			break;
		}
	}

}
