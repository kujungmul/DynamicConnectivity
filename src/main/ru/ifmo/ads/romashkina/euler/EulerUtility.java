package ru.ifmo.ads.romashkina.euler;

import ru.ifmo.ads.romashkina.graph.TreapEdge;
import ru.ifmo.ads.romashkina.graph.Graph;
import ru.ifmo.ads.romashkina.graph.Vertex;
import ru.ifmo.ads.romashkina.treap.ImplicitTreap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

import static ru.ifmo.ads.romashkina.graph.GraphUtility.readGraph;
import static ru.ifmo.ads.romashkina.treap.ImplicitTreap.mergeTreapList;

public class EulerUtility {

    public static List<Graph> readGraphList(String fileName) {
        List<Graph> graphs;

        try (FileReader fInput = new FileReader(fileName);
             BufferedReader inputBuf = new BufferedReader(fInput)
        ) {
            graphs = new ArrayList<>();
            String row;
            while ((row = inputBuf.readLine()) != null) {
                graphs.add(new Graph(readGraph(row)));
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return graphs;
    }

    public static ImplicitTreap<TreapEdge> findEulerPathForVertex(Vertex u, int vertexNum) {
        Deque<Vertex> tour = new ArrayDeque<>();
        tour.addLast(u);
        List<Vertex> resVertex = new ArrayList<>();
        List<ImplicitTreap<TreapEdge>> result = new ArrayList<>();
        Map<Vertex, Integer> vNums = new HashMap<>();
        final int[] counter = new int[1];
        int[] edgeCounters = new int[vertexNum];

        while (!tour.isEmpty()) {
            Vertex w = tour.peekLast();
            int wNum = vNums.computeIfAbsent(w, value -> counter[0]++);
            int i = edgeCounters[wNum];

            if (i < w.getEdgesNumber()) {
                Vertex to = w.getEdges().keySet().toArray(new Vertex[w.getEdgesNumber()])[i];
                tour.addLast(to);
                edgeCounters[wNum]++;
            }

            if (w.equals(tour.peekLast())) {
                tour.pollLast();
                resVertex.add(w);

                int curResVertexSize = resVertex.size() - 1;
                if (curResVertexSize > 0) {
                    TreapEdge treapEdge = resVertex.get(curResVertexSize - 1).getTreapEdgeTo(w);
                    ImplicitTreap<TreapEdge> e = new ImplicitTreap<>(EulerTourTree.random.nextLong(), treapEdge);
                    treapEdge.setLink(e);
                    result.add(e);
                }
            }
        }

        return mergeTreapList(result);
    }

    public static void findEulerPathForGraph(Graph graph) {
        int n = graph.getVertexNum();
        for (Vertex v : graph.getVertexMap().values()) {

            if (v.getRandomTreapEdge() != null && v.getRandomTreapEdge().getLink() == null) {
                findEulerPathForVertex(v, n);
            }
        }
    }
}
