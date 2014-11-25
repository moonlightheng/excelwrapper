package com.heng.test.check;
import java.util.*;
/**
 * Created by zhangheng07 on 2014/11/5.
 */
public class AdjacencyList {


        private int n, m;//顶点个数，边的数目
        private int[] indegree;//indegree[i]表示顶点i的入度
        private int index;
        private List< ArrayList< Integer>> G;//邻接表
        private  Queue< Integer> que;
        public AdjacencyList(int n,int m,int[] indegree,List< ArrayList< Integer>> G){
            this.n=n;
            this.m=m;
            this.indegree=indegree;
            this.G=G;
        }
        private  void topSort() {
            index = 0;
            que = new PriorityQueue< Integer>();
            for(int i = 0;i< n;i++)
                if(indegree[i]==0) que.add(i);//统计有多少个入度为0的
            while(!que.isEmpty()){
                int v = que.poll();//出队并输出
                index++;
                for(int i :G.get(v)){
                    indegree[i]--;//模拟删除以v为起点的边
                    if(indegree[i]==0) que.add(i);
                }
            }
            if (index == n)
                System.out.println("YES");
            else
                System.out.println("NO");
        }
        public static void main(String[] args) {
            Scanner sc = new Scanner(System.in);
            int n,m;
            List< ArrayList< Integer>> G;
            int[] indegree;
            while (sc.hasNext()) {
                n = sc.nextInt();
                m = sc.nextInt();
                if (n == 0)
                    break;
                indegree = new int[n];
                G = new ArrayList< ArrayList< Integer>>();//初始化邻接表
                for(int i = 0;i< n;i++)
                    G.add(new ArrayList< Integer>());
                while (m-- > 0) {
                    int u = sc.nextInt();
                    int v = sc.nextInt();
                    if (!G.get(u).contains(v)) {// 注意重边
                        G.get(u).add(v);
                        indegree[v]++;
                    }
                }
                AdjacencyList a=new AdjacencyList(n,m,indegree,G);
                a.topSort();
            }
        }
    }