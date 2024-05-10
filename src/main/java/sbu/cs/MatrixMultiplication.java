package sbu.cs;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MatrixMultiplication {

    // You are allowed to change all code in the BlockMultiplier class
    public static class BlockMultiplier implements Runnable
    {
        public List<List<Integer>> tempMatrixProduct;
        public List<List<Integer>> m1, m2;
        Scanner in = new Scanner(System.in);
        int part;
        public BlockMultiplier(int part, List<List<Integer>> m1, List<List<Integer>> m2) {
            this.part = part;
            this.m1 = m1;
            this.m2 = m2;
        }

        @Override
        public void run() {
            int p, q, r;
            p = m1.size();
            q = m1.get(0).size();
            r = m2.get(0).size();
            System.out.println(p + " " + q + " " + r);

            int start_m1, end_m1, start_m2, end_m2;
            if(part == 1){
                start_m1 = 0;
                end_m1 = p/2;
                start_m2 = 0;
                end_m2 = r/2;
            }else if(part == 2){
                start_m1 = 0;
                end_m1 = p/2;
                start_m2 = r/2;
                end_m2 = r;
            }else if(part == 3){
                start_m1 = p/2;
                end_m1 = p;
                start_m2 = 0;
                end_m2 = r/2;
            }else {
                start_m1 = p/2;
                end_m1 = p;
                start_m2 = r/2;
                end_m2 = r;
            }

            List<List<Integer>> result = new ArrayList<>();

            for(int i = start_m1; i < end_m1; i++){
                List<Integer> result_row = new ArrayList<>();
                List<Integer> row = new ArrayList<>();
                for(int ii = 0; ii < q; ii++)
                    row.add(m1.get(i).get(ii));

                for(int j = start_m2; j < end_m2; j++){
                    List<Integer> col = new ArrayList<>();
                    for(int jj = 0; jj < q; jj++)
                        col.add(m2.get(jj).get(j));

                    int sum = 0;
                    for(int k = 0; k < q; k++)
                        sum += row.get(k) * col.get(k);
                    result_row.add(sum);
                }

                result.add(result_row);
            }

            tempMatrixProduct = result;
        }
    }// sample comment

    public static List<List<Integer>> ParallelizeMatMul(List<List<Integer>> matrix_A, List<List<Integer>> matrix_B) {
        BlockMultiplier b1 = new BlockMultiplier(1, matrix_A, matrix_B);
        BlockMultiplier b2 = new BlockMultiplier(2, matrix_A, matrix_B);
        BlockMultiplier b3 = new BlockMultiplier(3, matrix_A, matrix_B);
        BlockMultiplier b4 = new BlockMultiplier(4, matrix_A, matrix_B);

        Thread t1 = new Thread(b1);
        Thread t2 = new Thread(b2);
        Thread t3 = new Thread(b3);
        Thread t4 = new Thread(b4);

        t1.start();
        t2.start();
        t3.start();
        t4.start();

        try {
            t1.join();
            t2.join();
            t3.join();
            t4.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }

        List<List<Integer>> result = new ArrayList<>();
        for(int i = 0; i < matrix_A.size()/2; i++){
            List<Integer> line = new ArrayList<>();
            for(int j = 0; j < matrix_B.get(0).size()/2; j++)
                line.add(b1.tempMatrixProduct.get(i).get(j));
            for(int j = 0; j < matrix_B.get(0).size()/2; j++)
                line.add(b2.tempMatrixProduct.get(i).get(j));
            result.add(line);
        }
        for(int i = 0; i < matrix_A.size()/2; i++){
            List<Integer> line = new ArrayList<>();
            for(int j = 0; j < matrix_B.get(0).size()/2; j++)
                line.add(b3.tempMatrixProduct.get(i).get(j));
            for(int j = 0; j < matrix_B.get(0).size()/2; j++)
                line.add(b4.tempMatrixProduct.get(i).get(j));
            result.add(line);
        }

        return result;
    }

    public static void main(String[] args) {

        List<List<Integer>> a = new ArrayList<>();
        List<List<Integer>> b = new ArrayList<>();
        List<Integer> l1 = new ArrayList<>();
        l1.add(1);l1.add(1);l1.add(1);l1.add(1);
        a.add(l1);a.add(l1);
        List<Integer> l2 = new ArrayList<>();
        l2.add(1);l2.add(1);
        b.add(l2);b.add(l2);b.add(l2);b.add(l2);

        List<List<Integer>> r = new ArrayList<>();
        r = ParallelizeMatMul(a,b);
        for(List<Integer> i : r) {
            for (int j : i)
                System.out.print(j + " ");
            System.out.println();
        }

    }
}
