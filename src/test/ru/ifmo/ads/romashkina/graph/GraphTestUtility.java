package ru.ifmo.ads.romashkina.graph;

import ru.ifmo.ads.romashkina.euler.EulerTourTree;
import ru.ifmo.ads.romashkina.treap.ImplicitTreap;
import ru.ifmo.ads.romashkina.utils.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringJoiner;

import static org.junit.Assert.assertEquals;
import static ru.ifmo.ads.romashkina.euler.EulerTourTree.cut;
import static ru.ifmo.ads.romashkina.euler.EulerTourTree.link;
import static ru.ifmo.ads.romashkina.euler.EulerUtility.findEulerPathForVertex;
import static ru.ifmo.ads.romashkina.treap.ImplicitTreap.makeValueList;
import static ru.ifmo.ads.romashkina.treap.ImplicitTreap.mergeTreapList;

public class GraphTestUtility {

    public static ImplicitTreap<TreapEdge> makeEulerPathTreeFromList(List<Vertex> vertices) {
        List<ImplicitTreap<TreapEdge>> edgeTreaps = new ArrayList<>(vertices.size());
        for (int i = 1; i < vertices.size(); i++) {
            Vertex previousVertex = vertices.get(i - 1);
            TreapEdge treapEdge = new TreapEdge(previousVertex, vertices.get(i));
            ImplicitTreap<TreapEdge> tree = new ImplicitTreap<>(EulerTourTree.random.nextLong(), treapEdge);
            treapEdge.setLink(tree);
            previousVertex.addTreapEdge(treapEdge);

            edgeTreaps.add(tree);
        }
        return mergeTreapList(edgeTreaps);
    }

    public static ImplicitTreap<TreapEdge> makeEulerPathTree(Graph graph, Vertex u) {
        return findEulerPathForVertex(u, graph.getVertexNum());
    }

    public static ImplicitTreap<TreapEdge> makeEulerPathTreeRandomVertex(Graph graph) {
        return  makeEulerPathTree(graph, graph.getRandomVertex());
    }

    public static ImplicitTreap<TreapEdge> makeEulerFromString(String l) {
        return makeEulerPathTreeFromList(createVerticesFromLabels(l.split(" ")));
    }

    public static List<Vertex> createVerticesFromLabels(String[] labels) {
        HashMap<String, Vertex> vertexMap = new HashMap<>();
        List<Vertex> vertices = new ArrayList<>();
        for (int i = 0; i < labels.length; i++) {
            String label = labels[i];
            Vertex v;
            if (vertexMap.containsKey(label)) {
                v = vertexMap.get(label);
            } else {
                v = new Vertex(label);
                vertexMap.put(label, v);
            }
            vertices.add(v);
        }
        return vertices;
    }

    public static String labelsFromVertices(List<Vertex> vertices) {
        StringJoiner labels = new StringJoiner(" ");
        for (Vertex v : vertices) {
            labels.add(v.getLabel());
        }
        return labels.toString();
    }

    public static String labelsFromEdges(List<TreapEdge> treapEdges) {

        StringJoiner labels = new StringJoiner(" ");

        for (TreapEdge e : treapEdges) {
            labels.add(e.getFrom().getLabel());
        }
        if (treapEdges.size() > 0) {
            labels.add(treapEdges.get(treapEdges.size() - 1).getTo().getLabel());
        }
        return labels.toString();
    }

    public static String labelsFromTour(ImplicitTreap<TreapEdge> tour) {
        return labelsFromEdges(makeValueList(tour));
    }

    public static ImplicitTreap<TreapEdge> handLinkTest(String l1, String l2, int v1, int v2) {
        List<Vertex> eulerTour1 = createVerticesFromLabels(l1.split(" "));
        List<Vertex> eulerTour2 = createVerticesFromLabels(l2.split(" "));
        makeEulerPathTreeFromList(eulerTour1);
        makeEulerPathTreeFromList(eulerTour2);
        return  link(eulerTour1.get(v1), eulerTour2.get(v2));
    }

    public static String handLinkTestToString(String l1, String l2, int v1, int v2) {
        return  labelsFromTour(handLinkTest(l1, l2, v1, v2));
    }

    public static void handCutTest(int edge, String l, String result1, String result2) {
        List<Vertex> eulerTour1 = createVerticesFromLabels(l.split(" "));
        makeEulerPathTreeFromList(eulerTour1);
        Pair<ImplicitTreap<TreapEdge>> cutResult = cut(eulerTour1.get(edge), eulerTour1.get(edge + 1));
        assertEquals(result1, labelsFromTour(cutResult.getFirst()));
        assertEquals(result2, labelsFromTour(cutResult.getSecond()));
    }
}
