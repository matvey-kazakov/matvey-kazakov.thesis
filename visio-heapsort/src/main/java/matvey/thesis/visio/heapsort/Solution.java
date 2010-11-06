// $Id: Solution.java,v 1.1 2006/02/18 06:37:45 matvey Exp $
/**
 * Copyright (c) Matvey Kazakov 2005.
 */
package matvey.thesis.visio.heapsort;

/**
 * Performs solution of the heap-sort algorithm
 * @author Matvey Kazakov
 */
public class Solution {
    // array of elements
    private int[] a;
    // size of heap
    private int hsize;
    // size of unsorted piece
    private int usize;

    /**
     * Runs solution.
     */
    public static void main(String[] args) {
        Solution solution = new Solution();
        solution.solve();
        solution.out();
    }

    /**
     * Constructor that fills array with default values
     */
    public Solution() {
        a = new int[]{4, 1, 23, 3, 2, 16, 20, 9, 10, 21, 14, 8, 7, 15, 11};
    }

    /**
     * ���������� ��������� ������������� ����������.
     */
    private void solve() {
        // ������ �������� � ������ �������
        hsize = a.length;
        // ���������� ������, ������� ���� "��������"
        usize = a.length / 2;
        while (true) {
            if (usize > 0) {
                // ������ ���� - ��������������
                usize--;
            } else if (hsize > 1){
                // ������ ���� - ����� ������� �������� � ��������� ���������
                // � ���������� ������� ��������.
                hsize--;
                swap(0, hsize);
            } else {
                // �����. ��� ������� �������������
                break;
            }
            // �������� � usize:
            // - �� ������ ����� - ��������� "������������" �������
            // - �� ������ ����� - ������ ������� (usize = 0)
            int c = usize;
            while (true) {
                // "�����������". �������� �������� �� �������� � ��������
                int cmax = max(c, left(c), right(c));
                // ���� ������� �� �������� ������������
                if (cmax != c) {
                    // �� ���������� ��� � ���������� � ���������� "������"
                    swap(c, cmax);
                    c = cmax;
                } else {
                    // ����� ��������� � ���������� ����
                    break;
                }
            }
        }
    }

    /**
     * Returns index of right child
     * @param c index of the node whose child is searched
     * @return index of the right child
     */
    private int right(int c) {
        return 2 * c + 2;
    }

    /**
     * Returns index of left child
     * @param c index of the node whose child is searched
     * @return index of the left child
     */
    private int left(int c) {
        return 2 * c + 1;
    }

    /**
     * Prints out sorted array
     */
    private void out() {
        for (int i = 0; i < a.length; i++) {
            System.out.print(a[i]);
            if (i < a.length - 1) {
                System.out.print(",");
            }
        }
        System.out.println();
    }

/**
     * Searches for maximum among three elements
     * @param i first element index
     * @param l second element index
     * @param r third element index
     * @return index of maximum element
     */
    private int max(int i, int l, int r) {
        int largest = i;
        if (l < hsize && a[l] >= a[largest]) {
            largest = l;
        }
        if (r < hsize && a[r] >= a[largest]) {
            largest = r;
        }
        return largest;
    }

    /**
     * Swaps two elements
     * @param i first element
     * @param largest second element
     */
    private void swap(int i, int largest) {
        int t = a[i];
        a[i] = a[largest];
        a[largest] = t;
    }
}

/*
 * $Log: Solution.java,v $
 * Revision 1.1  2006/02/18 06:37:45  matvey
 * HeapSort is committed
 *
 */