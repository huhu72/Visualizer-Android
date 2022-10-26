package com.example.visualizer.algorithms;

import com.example.visualizer.Node;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Stack;

public class Dijkstra {
    Node[][] grid;
    Node endNode;
    ArrayList<Node> visitedNodesInOrder = new ArrayList<>();
    //ArrayList<Node> unvisitedNotes;
    PriorityQueue<Node> unvisitedNodes;
    Node start;

    public Dijkstra(Node[][] grid, Node start,Node end) {
        this.grid = grid;
        this.endNode = end;
        this.start = start;
        start.distance = 0;
        this.unvisitedNodes = getAllNodes(this.grid);

    }

    private PriorityQueue<Node> getAllNodes(Node[][] grid) {
        PriorityQueue<Node> nodes = new PriorityQueue<>(grid.length,Comparator.comparingInt(nodeA -> nodeA.distance));
        for (Node[] row : grid) {
            for (Node node : row) {
                nodes.add(node);
            }
        }
        return nodes;
    }
    /*private ArrayList<Node> getAllNodes(Node[][] grid) {
        ArrayList<Node> nodes = new ArrayList<>();
        for (Node[] row : grid) {
            for (Node node : row) {
                nodes.add(node);
            }
        }
        return nodes;
    }
     public Node[] getVisitedNodesInOrder() {
        while (unvisitedNodes.size() != 0) {
            sortNodesByDistance();
            //   System.out.println("----------Loop started----------");
            //   System.out.println("Unvisited Nodes: " + unvisitedNodes);
            Node closestNode = unvisitedNodes.remove(0);
            //   System.out.println("Closest node: " + closestNode);
            if (closestNode.isWall) continue;
            if (closestNode.distance == Integer.MAX_VALUE)
                return this.visitedNodesInOrder.toArray(new Node[visitedNodesInOrder.size()]);
            closestNode.isVisited = true;
            visitedNodesInOrder.add(closestNode);
            if (closestNode.textView.getTag().equals(endNode.textView.getTag())) return this.visitedNodesInOrder.toArray(new Node[visitedNodesInOrder.size()]);
            //   System.out.println("Updating closes node");
            updateUnvisitedNeighbors(closestNode, grid);
            //  System.out.println("closses nodes updated");
        }
        return  this.visitedNodesInOrder.toArray(new Node[visitedNodesInOrder.size()]);
    }  private void sortNodesByDistance() {
       this.unvisitedNodes.sort((nodeA, nodeB) -> nodeA.distance - nodeB.distance);
    }*/
    public Node[] getVisitedNodesInOrder() {
        while (unvisitedNodes.size() != 0) {
           // sortNodesByDistance();
            System.out.println("----------Loop started----------");
            System.out.println("Unvisited Nodes: " + unvisitedNodes);
            Node closestNode = unvisitedNodes.remove();
            System.out.println("Closest node: " + closestNode);
            if (closestNode.isWall) continue;
            if (closestNode.distance == Integer.MAX_VALUE)
                return this.visitedNodesInOrder.toArray(new Node[visitedNodesInOrder.size()]);
            closestNode.isVisited = true;
            visitedNodesInOrder.add(closestNode);
            if (closestNode.textView.getTag().equals(endNode.textView.getTag())) return this.visitedNodesInOrder.toArray(new Node[visitedNodesInOrder.size()]);
         //   System.out.println("Updating closes node");
            updateUnvisitedNeighbors(closestNode, grid);
          //  System.out.println("closses nodes updated");
        }
        return  this.visitedNodesInOrder.toArray(new Node[visitedNodesInOrder.size()]);
    }

  /*  private void sortNodesByDistance() {
       this.unvisitedNodes.sort((nodeA, nodeB) -> nodeA.distance - nodeB.distance);
    }*/

    private void updateUnvisitedNeighbors(Node node, Node[][] grid) {
        ArrayList<Node> unvisitedNeighbors = getUnvisitedNeighbors(node, grid);
        for (Node neighbor : unvisitedNeighbors) {
            neighbor.distance = node.distance + 1;
            neighbor.previousNode = node;
            unvisitedNodes.remove(neighbor);
            unvisitedNodes.add(neighbor);
        }
        //System.out.println("UnvisitedNeighbors: " + unvisitedNeighbors);
    }

    private ArrayList<Node> getUnvisitedNeighbors(Node node, Node[][] grid) {
        ArrayList<Node> neighbors = new ArrayList<>();
        int col = node.col;
        int row = node.row;
        //Adds the node above the current node
        if (row > 0) neighbors.add(grid[row - 1][col]);
        //Adds the node below the current node if its not in the last row
        if (row < grid.length - 1) neighbors.add(grid[row + 1][col]);
        //Adds the node to the left of the current node if its not in the first column
        if (col > 0) neighbors.add(grid[row][col - 1]);
        //Adds the node to the right of the current node if its not in the last column
        if (col < grid[0].length - 1) neighbors.add(grid[row][col + 1]);
        neighbors.removeIf(neighbor -> neighbor.isVisited);
        return neighbors;
    }

    public Stack<Node> getNodesInShortestPathOrder(Node end) {
        Stack<Node> nodeInShortestPathOrder = new Stack<>();
        Node currentNode = end;
        while (currentNode != null) {
            nodeInShortestPathOrder.add(currentNode);
            currentNode = currentNode.previousNode;
        }
        return nodeInShortestPathOrder;
    }
}
//NOTE: For arraylist usage
  /*private ArrayList<Node> getAllNodes(Node[][] grid) {
        ArrayList<Node> nodes = new ArrayList<>();
        for (Node[] row : grid) {
            for (Node node : row) {
                nodes.add(node);
            }
        }
        return nodes;
    }
     public Node[] getVisitedNodesInOrder() {
        while (unvisitedNodes.size() != 0) {
            sortNodesByDistance();
            //   System.out.println("----------Loop started----------");
            //   System.out.println("Unvisited Nodes: " + unvisitedNodes);
            Node closestNode = unvisitedNodes.remove(0);
            //   System.out.println("Closest node: " + closestNode);
            if (closestNode.isWall) continue;
            if (closestNode.distance == Integer.MAX_VALUE)
                return this.visitedNodesInOrder.toArray(new Node[visitedNodesInOrder.size()]);
            closestNode.isVisited = true;
            visitedNodesInOrder.add(closestNode);
            if (closestNode.textView.getTag().equals(endNode.textView.getTag())) return this.visitedNodesInOrder.toArray(new Node[visitedNodesInOrder.size()]);
            //   System.out.println("Updating closes node");
            updateUnvisitedNeighbors(closestNode, grid);
            //  System.out.println("closses nodes updated");
        }
        return  this.visitedNodesInOrder.toArray(new Node[visitedNodesInOrder.size()]);
    }  private void sortNodesByDistance() {
       this.unvisitedNodes.sort((nodeA, nodeB) -> nodeA.distance - nodeB.distance);
    }*/