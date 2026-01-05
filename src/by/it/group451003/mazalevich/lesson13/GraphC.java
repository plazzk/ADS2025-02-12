package by.it.group451003.mazalevich.lesson13;

import java.util.*;

public class GraphC {

    public static void main(String[] args) {
        Map<String, ArrayList<String>> graph = new HashMap<>();
        Map<String, ArrayList<String>> reversedGraph = new HashMap<>();
        Set<String> vertices = new HashSet<>();
        Stack<String> stack = new Stack<>();
        Set<String> visited = new HashSet<>();

        // Чтение и парсинг входных данных
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        String[] edges = input.split(",\\s*");
        for (String edge : edges) {
            String[] parts = edge.split("->");
            String from = parts[0].trim();
            String to = parts[1].trim();

            vertices.add(from);
            vertices.add(to);

            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
            reversedGraph.computeIfAbsent(to, k -> new ArrayList<>()).add(from);
        }

        // Сортировка списков смежности для детерминированного обхода
        for (ArrayList<String> list : graph.values()) {
            Collections.sort(list);
        }
        for (ArrayList<String> list : reversedGraph.values()) {
            Collections.sort(list);
        }

        // Первый DFS: заполнение стека порядком завершения
        for (String vertex : vertices) {
            if (!visited.contains(vertex)) {
                dfs1(graph, vertex, visited, stack);
            }
        }

        // Второй DFS: поиск компонент сильной связности
        visited.clear();
        List<String> components = new ArrayList<>();
        while (!stack.isEmpty()) {
            String vertex = stack.pop();
            if (!visited.contains(vertex)) {
                StringBuilder component = new StringBuilder();
                dfs2(reversedGraph, vertex, visited, component);

                // Сортировка вершин внутри компоненты
                char[] chars = component.toString().toCharArray();
                Arrays.sort(chars);
                components.add(new String(chars));
            }
        }

        // Вывод компонент
        for (String component : components) {
            System.out.println(component);
        }
    }

    private static void dfs1(Map<String, ArrayList<String>> graph,
                             String vertex, Set<String> visited, Stack<String> stack) {
        visited.add(vertex);

        if (graph.containsKey(vertex)) {
            for (String neighbor : graph.get(vertex)) {
                if (!visited.contains(neighbor)) {
                    dfs1(graph, neighbor, visited, stack);
                }
            }
        }

        stack.push(vertex);
    }

    private static void dfs2(Map<String, ArrayList<String>> graph,
                             String vertex, Set<String> visited, StringBuilder component) {
        visited.add(vertex);
        component.append(vertex);

        if (graph.containsKey(vertex)) {
            for (String neighbor : graph.get(vertex)) {
                if (!visited.contains(neighbor)) {
                    dfs2(graph, neighbor, visited, component);
                }
            }
        }
    }
}