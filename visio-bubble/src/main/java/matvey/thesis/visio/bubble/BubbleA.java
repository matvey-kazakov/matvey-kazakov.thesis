package matvey.thesis.visio.bubble;

public class BubbleA {

    public static final int STATE_INIT   = 0;
    public static final int STATE_NEXT   = 1;
    public static final int STATE_EMERGE = 2;
    public static final int STATE_SWAP   = 3;

    private static int state = STATE_INIT;
    private static int a[] = {4, 3, 7, 1, 9, -2};
    private static int n = a.length;
    private static int i = 0;
    private static int j = 0;

    public static void main(String[] args) {
        do {
            nextStep();
        } while (state != STATE_INIT);
        out();
    }

    private static void nextStep() {
        switch(state) {
            case STATE_INIT:
                i = 0;
                state = STATE_NEXT;
                break;

            case STATE_NEXT:
                j = 0;
                i++;

                if (i >= n) {
                    state = STATE_INIT;
                } else {
                    state = STATE_EMERGE;
                }
                break;

            case STATE_EMERGE:
                j++;

                if (j >= n - i + 1) {
                    state = STATE_NEXT;
                } else if (a[j-1] > a [j]) {
                    state = STATE_SWAP;
                }
                break;

            case STATE_SWAP:
                out();                          // Пошаговый вывод
                swap(j,j-1);
                state = STATE_EMERGE;
                break;
        }
    }

    private static void swap (int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    private static void out () {
        for (int i = 0; i < n; i++) {
            System.out.print("" + a[i] +" ");
        }
        System.out.println();
    }

}