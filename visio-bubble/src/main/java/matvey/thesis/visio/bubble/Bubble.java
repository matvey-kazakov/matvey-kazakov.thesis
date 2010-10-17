package matvey.thesis.visio.bubble;

public class Bubble {

    private static int a[] = {4, 3, 7, 1, 9, -2};
    private static int n = a.length;

    /**
     * Реализует "пузырьковую" сортировку
     * @param args
     */
    public static void main(String[] args) {
        for (int i = 1; i < n; i++) {
            for (int j = 1; j < n - i + 1; j++) {
                if (a[j - 1] > a[j]) {
                    out();                          // Пошаговый вывод
                    swap(j, j - 1);
                }
            }
        }
        out();
    }

    /**
     * Обменивает значения элементов с индексами i и j
     */
    private static void swap(int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    /**
     * Выводит текущее состояние массива a.
     */
    private static void out() {
        for (int i = 0; i < n; i++) {
            System.out.print("" + a[i] + " ");
        }
        System.out.println();
    }

}