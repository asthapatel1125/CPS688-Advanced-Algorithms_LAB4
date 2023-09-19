
package lab4;


import java.util.*;
public class MaxBipartiteMatching {

	//BFS: Each applicant visits each job posting 
    public static boolean bfs(int[][] rGraph, int s, int t, int[] parent) {
        int V = rGraph.length; //num of vertices
        boolean[] visited = new boolean[V]; //visited nodes
        
        /*The first loop reads in the interests of each applicant for each job. 
         * If an applicant is interested in a particular job (interest = 1), 
         * then an edge is added from the source vertex (0) to the applicant vertex (i), 
         * and an edge is added from the applicant vertex (i) to the job vertex (j+1). 
         * The +1 is necessary because the 0th vertex is reserved for the source vertex.
         */
         
        Queue<Integer> queue = new LinkedList<>(); //adjacency list
        queue.add(s);//stores nodes to be processed
        visited[s] = true;
        parent[s] = -1; //store parent nodes of the vertex during the BFS algorithm

        while (!queue.isEmpty()) {
            int u = queue.poll(); //searching the tree level by level 
            for (int v = 0; v < V; v++) {
                if (!visited[v] && rGraph[u][v] > 0) {
                    queue.add(v);
                    parent[v] = u;
                    visited[v] = true;
                }
            }
        }

        return visited[t];
    }


    
    public static int fordFulkerson(int[][] graph, int s, int t) {
        int V = graph.length;
        int[][] rGraph = new int[V][V];
     // Create a copy of the input graph to hold the residual graph.
        
        for (int i = 0; i < V; i++) {
            System.arraycopy(graph[i], 0, rGraph[i], 0, V);
        }

        int[] parent = new int[V];
        int maxFlow = 0;
        
        // While there exists an augmenting path in the residual graph,
        // update the residual graph and add the flow to the maximum flow.
        while (bfs(rGraph, s, t, parent)) {
            int pathFlow = Integer.MAX_VALUE;
            for (int v = t; v != s; v = parent[v]) {
                int u = parent[v];
                pathFlow = Math.min(pathFlow, rGraph[u][v]); //minimum b
            }
            
            //job to applicant
            //update graph by subtracting the bottleneck capacity from the forward edges to the backward edges
            //means finding the maximum number of applicants to the same job.
            for (int v = t; v != s; v = parent[v]) {
                int u = parent[v];
                rGraph[u][v] -= pathFlow;
                rGraph[v][u] += pathFlow;
            }
            //update  the total flow (total number of applicants to the same job)
            maxFlow += pathFlow;
        }

        return maxFlow;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Read the input.
        int M = scanner.nextInt();
        int[][] graph = new int[M + 2][M + 2];

        // Add edges from source to applicants.
        for (int i = 1; i <= M; i++) {
            for (int j = 1; j <= M; j++) {
                int interest = scanner.nextInt();
                if (interest == 1) {
                    graph[0][i] = 1;
                    graph[i][j + 1] = 1;
                }
            }
        }

        // Add edges from jobs to target.
        for (int j = 1; j <= M; j++) {
            graph[j][M + 1] = 1;
        }

        int maxFlow = fordFulkerson(graph, 0, M + 1);
        System.out.println("The maximum number of applicants matching for the jobs is " + maxFlow);

        scanner.close();
    }
}



