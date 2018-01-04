package com.test;

public class TestPrint {

	public static void main(String args[]) {
		printYhSj(5);
		
    }
	
	
	public static void  printYhSj(int a){
        int b[][] = new int[a][];
        for (int i = 1; i <= a; i++) {
            b[i-1] = new int[i];
        }
        for (int j = 0; j < a; j++) {
            for (int k = 0; k <= j; k++) {
                if (j == 0 || k == 0 || k == j)// 分支
                {
                    b[j][k] = 1;// 等于1的
                    continue;
                } else {
                    b[j][k] = b[j-1][k-1] + b[j-1][k];// 计算值
                }
            }
        }
        for (int m = 0; m < a; m++) {
            for (int n = 0; n <= m; n++) {
                System.out.print(b[m][n] + " ");// 循环输出
            }
            System.out.println(" ");
        }
	}
}
