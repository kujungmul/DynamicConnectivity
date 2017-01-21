package ru.ifmo.ads.romashkina.euler;

import ru.ifmo.ads.romashkina.graph.Vertex;
import ru.ifmo.ads.romashkina.treap.ImplicitTreap;
import ru.ifmo.ads.romashkina.utils.Pair;

import java.util.Random;

import static ru.ifmo.ads.romashkina.graph.GraphUtility.addOrientedEdge;
import static ru.ifmo.ads.romashkina.graph.GraphUtility.removeOrientedEdge;
import static ru.ifmo.ads.romashkina.treap.ImplicitTreap.*;

/*
 * 
 */
public class EulerTourTree {
    public static Random random = new Random(123456);

    private static ImplicitTreap<Vertex> updateAfterVertex(Vertex vertex, ImplicitTreap<Vertex> afterVertex, ImplicitTreap<Vertex> vertexNew) {
        if (fullSize(afterVertex) > 0) {
            Vertex nextAfterVertex = getValueByIndex(afterVertex, 1);
            if (nextAfterVertex != null) vertex.getEdgeTo(nextAfterVertex).setFromNode(vertexNew);
        }
        return merge(vertexNew, afterVertex);
    }

    public static ImplicitTreap<Vertex> link(Vertex v, Vertex u) {
        if (areConnected(u, v)) return null;

        // разрезаем обход первого эйлерового дерева по вершине v; обновляем ссылку у ребра v -> x на новый клон v
        Pair<ImplicitTreap<Vertex>> vPair = split(v.getIn());
        ImplicitTreap<Vertex> beforeV = vPair.getFirst();
        ImplicitTreap<Vertex> afterV  = vPair.getSecond();
        ImplicitTreap<Vertex> vNew = new ImplicitTreap<>(random.nextLong(), v);
        afterV = updateAfterVertex(v, afterV, vNew);

        // разрезаем обход второго эйлерового дерева по вершине u; обновляем ссылку у ребра u -> y на новый клон u
        Pair<ImplicitTreap<Vertex>> uPair = split(u.getIn());
        ImplicitTreap<Vertex> beforeU = uPair.getFirst();
        ImplicitTreap<Vertex> afterU = uPair.getSecond();
        ImplicitTreap<Vertex> uNew = new ImplicitTreap<>(random.nextLong(), u);
        afterU = updateAfterVertex(u, afterU, uNew);

        u.setIn(uNew); // Обновить у вершины ссылку на ноду на случай если u -- первая вершина пути
        beforeU = remove(beforeU, 0); // удалить первую вершину эйлеровго пути

        // обновить ссылку на from у ребра между первой (удалённой) вершиной в пути на её последнее вхождение
        if (fullSize(beforeU) > 1) {
            ImplicitTreap<Vertex> lastInAfterU = getTreapByIndex(afterU, fullSize(afterU));
            Vertex lastVertexInAfterU = lastInAfterU.getValue();
            getValueByIndex(beforeU, 1).getEdgeTo(lastVertexInAfterU).setFromNode(lastInAfterU); // теперь не падает!!!

        }

        addOrientedEdge(v, u);
        v.getEdgeTo(u).setLinksToNodes(getTreapByIndex(beforeV, fullSize(beforeV)), uNew);  // uNew == new first in afterU (clone)
        ImplicitTreap<Vertex> lastU= getTreapByIndex(beforeU, fullSize(beforeU));
        u.getEdgeTo(v).setLinksToNodes(lastU == null ? getTreapByIndex(afterU, 1) : lastU, vNew);  // vNew == new first in afterV (clone)

        ImplicitTreap<Vertex> vu = merge(beforeV, afterU);
        ImplicitTreap<Vertex> uv = merge(beforeU, afterV);
        return merge(vu, uv);
    }

    public static Pair<ImplicitTreap<Vertex>> cut(Vertex v, Vertex u) {
        if (!v.hasEdge(u)) return null;


        ImplicitTreap<Vertex> vInVU = v.getEdgeTo(u).getFromNode();
        ImplicitTreap<Vertex> uInUV = u.getEdgeTo(v).getFromNode();
        if (findIndex(vInVU) > findIndex(uInUV)) {
            return cut(u, v);
        }

        ImplicitTreap<Vertex> part1 = split(vInVU).getFirst();
        Pair<ImplicitTreap<Vertex>> other = split(uInUV);
        ImplicitTreap<Vertex> part2 = other.getFirst();
        ImplicitTreap<Vertex> part3 = other.getSecond();

        v.setIn(vInVU);
        part3 = remove(part3, 0);
        if (fullSize(part3) > 0) {
            v.getEdgeTo(getValueByIndex(part3, 1)).setFromNode(vInVU);
        }

        removeOrientedEdge(v, u);
        return new Pair<>(merge(part1, part3), part2);
    }

    public static boolean areConnected(Vertex v, Vertex u) {
        return getRoot(v.getIn()) == getRoot(u.getIn());
    }

}
