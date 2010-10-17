package matvey.thesis.visio.bubble;

public class BubbleAFormal {

    public static final int STATE_START = 0;
    public static final int STATE_INIT1 = 1;
    public static final int STATE_INIT2 = 2;
    public static final int STATE_SWAP = 3;
    public static final int STATE_EMERGE = 4;
    public static final int STATE_NEXT = 5;

    private static int state = STATE_START;
    private static int a[] = {4, 3, 7, 1, 9, -2};
    private static int n = a.length;
    private static int i = 0;
    private static int j = 0;

    public static void main(String[] args) {
        do {
            nextStep();
        } while (state != STATE_START);
        out();
    }

    private static void nextStep() {
        switch (state) {
            case STATE_START:
                state = STATE_INIT1;
                break;

            case STATE_INIT1:
                i = 1;
                if (i < n) {
                    state = STATE_INIT2;
                } else {
                    state = STATE_START;
                }
                break;

            case STATE_INIT2:
                j = 1;
                if (j < n - i + 1 && a[j - 1] > a[j]) {
                    state = STATE_SWAP;
                } else if (j < n - i + 1 && a[j - 1] <= a[j]) {
                    state = STATE_EMERGE;
                } else {
                    state = STATE_NEXT;
                }
                break;

            case STATE_SWAP:
                out();                          // Пошаговый вывод
                swap(j, j - 1);
                state = STATE_EMERGE;
                break;

            case STATE_EMERGE:
                j++;

                if (j < n - i + 1 && a[j - 1] > a[j]) {
                    state = STATE_SWAP;
                } else if (j < n - i + 1 && a[j - 1] <= a[j]) {
                    state = STATE_EMERGE;
                } else {
                    state = STATE_NEXT;
                }
                break;

            case STATE_NEXT:
                i++;

                if (i < n) {
                    state = STATE_INIT2;
                } else {
                    state = STATE_START;
                }
                break;

        }
    }

    private static void swap(int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    private static void out() {
        for (int i = 0; i < n; i++) {
            System.out.print("" + a[i] + " ");
        }
        System.out.println();
    }

}