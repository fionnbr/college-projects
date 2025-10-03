// Simple weighted graph representation 
// Uses an Adjacency Linked Lists, suitable for sparse graphs

import java.io.*;
import java.util.Scanner;
//removed any used of toChar because of size of graph, not enough characters
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
            
            System.out.println("Edge " + u + "--(" + wgt + ")--" + v);   

           
            
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
            System.out.print("\nadj[" + v + "] ->" );
            for(n = adj[v]; n != z; n = n.next) 
                System.out.print(" |" + n.vert + " | " + n.wgt + "| ->");    
        }
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
            parent[v] = 0;
            hPos[v] = 0;
        }

        Heap pq = new Heap(V, dist, hPos);
        v=s;
        dist[s] = 0;
        pq.insert(s);


        while (!pq.isEmpty()) {
            
            for (t = adj[v]; t != z; t = t.next){
                
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
        

        System.out.println("\nSPT\n");
        for(v = 1; v <= V; ++v) {
            System.out.println("Vertex " + v + " -> Parent: " + parent[v] + ", Distance " + dist[v]);
        }
        System.out.println("");
        
        
    }

}

public class LargeGraph {
    public static void main(String[] args) throws IOException
    {   
        /* 
        int s = 12;
        String fname = "rome_roads.txt";  
             */         
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the filename: ");
        String fname = scanner.nextLine();

        System.out.print("Enter the starting vertex: ");
        int s = scanner.nextInt();
          

        //gets time and memory before graph is read and traversed
        long startTime = System.nanoTime();

        Runtime runtime = Runtime.getRuntime();
        long memoryBefore = runtime.totalMemory() - runtime.freeMemory();

        Graph g = new Graph(fname);
        
        g.display();

        g.SPT_Dijkstra(s);     
        
        //gets the time the program needed to run and the total amount of memory used and prints 
        long endTime = System.nanoTime();
        long duration = endTime - startTime;

        long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
        long memoryUsed = memoryAfter - memoryBefore;

        System.out.println("\n\nExecution Time: " + duration + " nanoseconds");
        System.out.println("\nMemory Used: " + memoryUsed + " bytes");
    }
}
