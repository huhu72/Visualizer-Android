package com.example.visualizer;

import android.widget.TextView;

import androidx.annotation.NonNull;

public class Node {
    public TextView textView;
    int left;
    int right;
    int top;
    int bot;
    public int row, col;
    public boolean isStartNode = false;
    public boolean isEndNode = false;
    public int distance = Integer.MAX_VALUE;
    public boolean isVisited = false;
    public boolean isWall = false;
    public Node previousNode = null;

    public Node(int row, int col, TextView box) {
        this.row = row;
        this.col = col;
        this.textView = box;
    }

    public void setRectBounds(int right, int bot) {
        this.right = right;
        this.bot = bot;
    }

    //Index 0 represents the x coordinate with respect to the screen since and index 1 represents y.
    //This is needed because we are using raw x and y for our motion event
    public void setTop(int[] offsets) {
        this.left = offsets[0];
        this.top = offsets[1];
    }

    @NonNull
    @Override
    public String toString() {
        return textView.getTag().toString();
    }
}
