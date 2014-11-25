package com.heng.test.check;
import java.util.*;
/**
 * Created by zhangheng07 on 2014/11/5.
 */

public class Main {
    private  int n, m;
    private  int[][] G;// 邻接矩阵
    private  int[] indegree;// 顶点的入度
    private  Queue< Integer> que;
    private  int index;//保存最后的结果，如果等于n表示合法，否则No
    public Main(int n,int m,int[] indegree,int[][] G){
        this.n=n;
        this.m=m;
        this.indegree=indegree;
        this.G=G;
    }
    private  void topSort() {
        que = new PriorityQueue< Integer>();
        index = 0;
        for (int i = 0; i < n; i++) {
            if (indegree[i] == 0)//入度为0的顶点入队
                que.add(i);
        }
        while (!que.isEmpty()) {
            int v = que.poll();//出队
            index++;
            for (int i = 0; i < n; i++) {//删除顶点v及以v为尾的弧。
                if (G[v][i] == 1) {
                    indegree[i]--;
                    if (indegree[i] == 0)
                        que.add(i);
                }
            }
        }
        if (index ==n)
            System.out.println("YES");
        else//有环
            System.out.println("NO");
    }
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n,m;
        int[][] G;
        int indegree[];
        while (sc.hasNext()) {
            n = sc.nextInt();
            m = sc.nextInt();
            if (n == 0)
                break;
            G = new int[n][n];
            indegree = new int[n];
            while (m-- > 0) {
                int u = sc.nextInt();
                int v = sc.nextInt();
                if (G[u][v] == 0) {//注意重边
                    G[u][v] = 1;//u是v的master
                    indegree[v]++;
                }
            }
            Main ma=new Main(n,m,indegree,G);
            ma.topSort();
        }
    }
}