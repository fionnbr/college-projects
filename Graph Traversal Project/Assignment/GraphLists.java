// Simple weighted graph representation 
// Uses an Adjacency Linked Lists, suitable for sparse graphs

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;

class Heap
{
    private int[] a;	   // heap array
    private int[] hPos;	   // hPos[h[k]] == k
    private int[] dist;    // dist[v] = priority of v

    private int N;         // heap size
   
    // The heap constructor gets passed from the Graph:
    //    1. maximum heap size
    //    2. reference to the dist[] array
    //    3. reference to the hPos[] array
    public Heap(int maxSize, int[] _dist, int[] _hPos) 
    {
        N = 0;
        a = new int[maxSize + 1];
        dist = _dist;
        hPos = _hPos;
    }


    public boolean isEmpty() 
    {
        return N == 0;
    }


    public void siftUp( int k) 
    {
        int v = a[k];

        // code yourself
        // must use hPos[] and dist[] arrays
        while (k > 1 && dist[v] < dist[a[k / 2]]) {
            a[k] = a[k / 2];
            hPos[a[k]] = k;
            k = k / 2;
        }
        a[k] = v;
        hPos[v] = k;
    }


    public void siftDown( int k) 
    {
        int v, j;
       
        v = a[k];  
        
        // code yourself 
        // must use hPos[] and dist[] arrays

        while (2 * k <= N) {
            j = 2 * k;
            if (j < N && dist[a[j + 1]] < dist[a[j]]) j++;
            if (dist[v] <= dist[a[j]]) break;
            a[k] = a[j];
            hPos[a[k]] = k;
            k = j;
        }
        a[k] = v;
        hPos[v] = k;
    }


    public void insert( int x) 
    {
        a[++N] = x;
        siftUp(N);
    }


    public int remove() 
    {   
        int v = a[1];
        hPos[v] = 0; // v is no longer in heap
        a[N+1] = 0;  // put null node into empty spot
        
        a[1] = a[N--];
        siftDown(1);
        
        return v;
    }

}

class Graph {
    class Node {
        public int vert;
        public int wgt;
        public Node next;
    }
    
    // V = number of vertices
    // E = number of edges
    // adj[] is the adjacency lists array
    private int V, E;
    private Node[] adj;
    private Node z;
    private int[] mst;
    
    // used for traversing graph
    private int[] visited;
    private int id;
    
    
    // default constructor
    public Graph(String graphFile)  throws IOException
    {
        int u, v;
        int e, wgt;
        Node t;

        FileReader fr = new FileReader(graphFile);
		BufferedReader reader = new BufferedReader(fr);
	           
        String splits = " +";  // multiple whitespace as delimiter
		String line = reader.readLine();        
        String[] parts = line.split(splits);
        System.out.println("Parts[] = " + parts[0] + " " + parts[1]);
        
        V = Integer.parseInt(parts[0]);
        E = Integer.parseInt(parts[1]);
        
        // create sentinel node
        z = new Node(); 
        z.next = z;
        
        // create adjacency lists, initialised to sentinel node z       
        visited = new int[V+1];
        adj = new Node[V+1];        
        for(v = 1; v <= V; ++v)
            adj[v] = z;               
        
       // read the edges
        System.out.println("Reading edges from text file");
        for(e = 1; e <= E; ++e)
        {
            line = reader.readLine();
            parts = line.split(splits);
            u = Integer.parseInt(parts[0]);
            v = Integer.parseInt(parts[1]); 
            wgt = Integer.parseInt(parts[2]);
            
            System.out.println("Edge " + toChar(u) + "--(" + wgt + ")--" + toChar(v));   

           
            
            // write code to put edge into adjacency matrix  

            // insert node v into u's adjacency list
             t = new Node();
             t.vert = v;
             t.wgt = wgt;
             t.next = adj[u];
             adj[u] = t;
 
             // insert node u into v's adjacency list (since the graph is undirected)
             t = new Node();
             t.vert = u;
             t.wgt = wgt;
             t.next = adj[v];
             adj[v] = t;
        }	       
    }
   
    // convert vertex into char for pretty printing
    private char toChar(int u)
    {  
        return (char)(u + 64);
    }
    
    // method to display the graph representation
    public void display() {
        int v;
        Node n;
        
        for(v=1; v<=V; ++v){
            System.out.print("\nadj[" + toChar(v) + "] ->" );
            for(n = adj[v]; n != z; n = n.next) 
                System.out.print(" |" + toChar(n.vert) + " | " + n.wgt + "| ->");    
        }
        System.out.println("");
    }


    public void DF(int s) {
        visited = new int[V + 1];
        id = 0;

        System.out.println("\nDepth-first");
        dfsVisit(s);
    }
    
    private void dfsVisit(int u) {
        visited[u] = ++id;
        System.out.print(toChar(u) + " ");
    
        for (Node t = adj[u]; t != z; t = t.next) {
            if (visited[t.vert] == 0) {
                dfsVisit(t.vert);
            }
        }
    }

    public void breadthFirst(int s) {
        visited = new int[V + 1];
        Queue<Integer> q = new LinkedList<>();

        visited[s] = 1;
        System.out.println("\nBreadth-first");
        System.out.print(toChar(s) + " ");
        q.add(s);

        while (!q.isEmpty()) {
            int u = q.remove();
            for (Node t = adj[u]; t != z; t = t.next) {
                int v = t.vert;
                if (visited[v] == 0) {
                    visited[v] = 1;
                    System.out.print(toChar(v) + " ");
                    q.add(v);
                }
            }
        }
            
    }

    
	public void MST_Prim(int s)
    {
        int v, u;
        int wgt, wgt_sum = 0;
        int[]  dist, parent, hPos;
        Node t;

        //code here
        
        dist = new int[V + 1];
        parent = new int[V + 1];
        hPos = new int[V + 1];
        mst = new int[V + 1];

        for (v = 1; v <= V; ++v) {
            dist[v] = Integer.MAX_VALUE;
            parent[v] = 0;
            hPos[v] = 0;
        }

        // Set starting vertex distance to 0
        dist[s] = 0;

        // Create a heap with dist and hPos arrays
        Heap h = new Heap(V, dist, hPos);
        h.insert(s);

        System.out.println("\n\nPrim's MST");
        int j = 0; //displays the current step

        // While the heap is not empty
        while (!h.isEmpty()) {
            // Remove the vertex with the smallest distance (minimum weight)
            
            /* 
            //prints current values in heap, dist and parent
            System.err.println("Step "+ j + ": ");
            j++;
            System.out.println("\n\nHeap contents:");
            for (int i = 1; i <= V; i++) {
                if (hPos[i] != 0)
                    System.out.print(toChar(i) + "(" + dist[i] + ") ");
            }
            System.out.println();

            System.out.println("\ndist[]:");
            for (int i = 1; i <= V; i++) {
                if (dist[i] == Integer.MAX_VALUE)
                    System.out.print(toChar(i) + "= Max ");
                else
                    System.out.print(toChar(i) + "=" + dist[i] + " ");
            }
            System.out.println();

            System.out.println("\nparent[]:");
            for (int i = 1; i <= V; i++) {
                if (parent[i] != 0)
                    System.out.print(toChar(i) + "<-" + toChar(parent[i]) + " ");
                else
                    System.out.print(toChar(i) + "<- Null ");
            }
            System.out.println();
            */
            
            v = h.remove();
            System.out.print(toChar(v) + " ");
            wgt_sum += dist[v];
            mst[v] = parent[v];

             

            // Explore all adjacent vertices
            for (t = adj[v]; t != z; t = t.next) {
                u = t.vert;
                wgt = t.wgt;
        
                // If the edge (v, u) offers a smaller weight to u, update the distance
                if (wgt < dist[u] && dist[u] > 0) {
                    dist[u] = wgt;
                    parent[u] = v;
    
                    // If vertex u is not in the heap, insert it
                    if (hPos[u] == 0) {
                        h.insert(u);
                    } else {
                        // If u is already in the heap, update its position
                        h.siftUp(hPos[u]);
                    }
                }
            }
        }
       

        
        // Output the total weight of the MST
        System.out.println("\nMST total weight: " + wgt_sum);

        showMST();
                    
    }
    
    
    public void showMST()
    {
            System.out.print("\n\nMinimum Spanning tree parent array is:\n");
            for(int v = 1; v <= V; ++v)
                System.out.println(toChar(v) + " -> " + toChar(mst[v]));
            System.out.println("");
    }

    public void SPT_Dijkstra(int s)
    {
        int v, u;
        int d;
        int[] dist, parent, hPos;
        Node t;

        dist = new int[V+1];
        parent = new int[V+1];
        hPos = new int[V+1];


        for (v=1; v<=V; ++v) {
            dist[v] = Integer.MAX_VALUE;
            parent[v] = 0; //null
            hPos[v] = 0; 
        }

        Heap pq = new Heap(V, dist, hPos); //p queue starts empty
        v=s;
        dist[s] = 0;
        pq.insert(s);
        //int j = 0; //to display which step when displaying heap, dist and parent
        
        System.out.println("\nDijkstra's SPT\n");
        while (!pq.isEmpty()) {
            /* 
            System.out.println("Step "+ j + ": ");
            j++;
            System.out.println("\n\nHeap contents:");
            for (int i = 1; i <= V; i++) {
                if (hPos[i] != 0)
                    System.out.print(toChar(i) + "(" + dist[i] + ") ");
            }
            System.out.println();

            System.out.println("\ndist[]:");
            for (int i = 1; i <= V; i++) {
                if (dist[i] == Integer.MAX_VALUE)
                    System.out.print(toChar(i) + "= Max ");
                else
                    System.out.print(toChar(i) + "=" + dist[i] + " ");
            }
            System.out.println();

            System.out.println("\nparent[]:");
            for (int i = 1; i <= V; i++) {
                if (parent[i] != 0)
                    System.out.print(toChar(i) + "<-" + toChar(parent[i]) + " ");
                else
                    System.out.print(toChar(i) + "<- Null ");
            }
            System.out.println();
            */
            

            System.out.print(toChar(v) + " "); //current vertice to show the order of traversal
            for (t = adj[v]; t != z; t = t.next){ //each u next to v is examined
                
                u = t.vert;
                d = t.wgt;

                if (dist[v] + d < dist[u]) {

                    dist[u] = dist[v] + d;
                    if (hPos[u] == 0) {
                        pq.insert(u); 
                    }
                    else {
                        pq.siftUp(hPos[u]);
                    }
                    parent[u] = v;

                }    
            }

            v = pq.remove();
        }
        
        //path tree
        System.err.println("\n");
        for(v = 1; v <= V; ++v) {
                System.out.println("Vertex " + toChar(v) + " -> Parent: " + toChar(parent[v]) + ", Distance " + dist[v]);
            }
        System.out.println("");
        
        
    }

}

public class GraphLists {
    public static void main(String[] args) throws IOException
    {   
        /*
        set variables for testing purposes
        int s = 12;
        String fname = "wGraph1.txt";  
           
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the filename: ");
        String fname = scanner.nextLine();

        System.out.print("Enter the starting vertex: ");
        int s = scanner.nextInt();
        */ 
        int s = 12;
        String fname = "wGraph1.txt";  

        Graph g = new Graph(fname);
       
        g.display();

        g.DF(s);
        g.breadthFirst(s);
        g.MST_Prim(s);
        g.SPT_Dijkstra(s);               
    }
}
