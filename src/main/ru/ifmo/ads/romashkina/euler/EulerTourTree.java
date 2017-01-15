package ru.ifmo.ads.romashkina.euler;

import ru.ifmo.ads.romashkina.graph.Graph;
import ru.ifmo.ads.romashkina.graph.GraphUtility;
import ru.ifmo.ads.romashkina.graph.Vertex;
import ru.ifmo.ads.romashkina.treap.ImplicitTreap;

import java.util.List;

/*
 * 0. Научиться задавать при чтении из файла(ов) несколько деревьев
 * 1. Написать функцию, которая строит декартово дерево по дереву-графу (в файле задавай дерево):
 *    + считать граф
 *    + найти эйлеров путь (список вершин)
 *    + создать список декартовых деревьев размера 1 для каждой вершины
 *    + заполнить у вершин вхождения в дерево
 *    + затем смёржить все одиночные деревья в одно большое дерево (лучше написать вспомогательную функцию: мёрж списка деревьев)
 * 2. Написать функцию link, которая добавляет ребро между двумя вершинами
 *    + принимает две вершины (Vertex)
 *    + получает ноды дерева, соответвующие вхождениям вершины
 *    + для первой:
 *         + разрешаешь дерево по ноде
 *         + добавляешь первую вершину графа в начало второго дерева
 *    + для второй:
 *         + разрезаешь дерево по ноде
 *         + добавить в начало второго дерева узел с вершиной, по которой разрезаешь и обновить ссылку у вершины дерева на новосозданную
 *         + удаляешь первый узел из первого элемента пары, обновив у вершины узла ссылку на последний узел дерева
 *    + затем смёржить куски в правильном порядке (3 мёржа)
 *    + обратить внимание на частные случаи: когда графы из одной вершины, когда вторая вершина в начале пути
 * 3. Написать функцию areConnected: проверка того, что две вершины связаны
 * 4. Напиши тесты (ручные): задай руками несколько эйлеровых обходов разных графов, построй по ним деревья и проверь, что ответ совпадает
 *    + примеры: листочек, конспект, лекция Маврина
 *    + обязательно на крайние случаи (графы из одной вершины)
 */
public class EulerTourTree {
    private ImplicitTreap<Vertex> tree;
    private List<Vertex> tour;

    EulerTourTree(Graph graph) {
        this.tour = GraphUtility.findEulerPath(graph.getRandomVertex(), graph.getVertexNum());
        this.tree = ImplicitTreap.makeFromArray(this.tour);
    }

    @Override
    public String toString() {
        StringBuilder t = new StringBuilder();
        for (Vertex v : tour) {
            t.append(v.getLabel()).append(" ");
        }
        return "ET Tree{ \n" +
                "tree = " + tree +
                "\ntour = " + t.toString() +
                "\n}";
    }
}
