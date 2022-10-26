package com.example.visualizer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;


import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Looper;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.gridlayout.widget.GridLayout;

import com.example.visualizer.algorithms.Dijkstra;

import java.util.Stack;

public class Dijkstras_Visualizer_Activity extends AppCompatActivity implements View.OnTouchListener {

    ConstraintLayout layout;
    ConstraintSet set = new ConstraintSet();
    int rowSize = 5 ;
    int colSize = 5 ;
    int boxHeight;
    int boxWidth;
    int xOffset, yOffset;
    // HashMap<String, Node> grid = new HashMap<>();
    Node[][] grid = new Node[rowSize][colSize];
    GridLayout gridContainerView;
    boolean setStartNode = false;
    boolean hasStartNode = false;

    private boolean setEndNode = false;
    private boolean hasEndNode = false;

    private boolean setWallNodes;

    private boolean resetNodes = false;

    Node endNode;
    private Node[] visitedNodesInOrder;
    private Stack<Node> nodesInShortestPathOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dijkstras_visualizer);
        layout = findViewById(R.id.activity_dijkstras_visualizer);
        set.clone(layout);
        gridContainerView = findViewById(R.id.gridContainer);
        gridContainerView.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        gridContainerView.setColumnCount(colSize);
        gridContainerView.setRowCount(rowSize);
        // System.out.println("Child " + gridContainerView.getChildCount());

        gridContainerView.post(() -> {
            System.out.println("imageview width:" + gridContainerView.getWidth() + " height:" + gridContainerView.getHeight());
            calculateBoxSize(gridContainerView.getWidth(), gridContainerView.getHeight());
            /*
            for(int i = 0; i < layout.getChildCount(); i++){
                System.out.println(layout.getChildAt(i).getTag());
            }*/
            createGrid();
            System.out.println("Create grid done");
            System.out.println("container: " + gridContainerView.getY());
        });

/*
        DisplayMetrics displayMetrics = new DisplayMetrics();

        // on below line we are getting metrics for display using window manager.
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        // on below line we are getting height
        // and width using display metrics.
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        // on below line we are setting height and width to our text view.
        System.out.println("Width : " + String.valueOf(width) + "\n" + "Height : " + String.valueOf(height));
*/

    }

    @SuppressLint("ClickableViewAccessibility")
    private void createGrid() {

        int counter = 1;
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                TextView box = new TextView(this);
                box.setBackground(AppCompatResources.getDrawable(this, R.drawable.round_corner_delete));
                box.setId(TextView.generateViewId());
                String tag = "box-" + counter + "." + i + "-" + j;
                box.setTag(tag);
                box.setText(String.valueOf(counter));
                box.setGravity(Gravity.CENTER);
                box.setAutoSizeTextTypeUniformWithConfiguration(
                        1, 17, 1, TypedValue.COMPLEX_UNIT_DIP);
                box.setOnTouchListener(this);
                ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(boxWidth, boxHeight);
                if (i == 0 && j == 0) {
                    //Row 0 Column  = 0
                    layoutParams.leftMargin = getDPDimensions(0);
                    layoutParams.topMargin = getDPDimensions(0);
                } else if (i != 0 && j == 0) {
                    //Row > 0 Column = 0
                    layoutParams.leftMargin = getDPDimensions(0);
                    layoutParams.topMargin = getDPDimensions(3);
                } else {
                    layoutParams.leftMargin = getDPDimensions(3);
                    layoutParams.topMargin = getDPDimensions(0);
                }
                box.setLayoutParams(layoutParams);
                //  System.out.println("Grid Child: " + gridContainerView.getChildCount());
                gridContainerView.addView(box);
                Node node = new Node(i, j, box);
                counter++;
                node.textView.post(() -> {
                    int[] coord = new int[2];
                    node.textView.getLocationOnScreen(coord);
                    node.setTop(coord);
                    if (tag.equals("box-1.0-0")) {
                        this.xOffset = coord[0];
                        this.yOffset = coord[1];
                    }
                    int right = node.textView.getRight() + xOffset;
                    int bot = node.textView.getBottom() + yOffset;

                    node.setRectBounds(right, bot);
              /*      if ( rowIndx == 0 && colIndx == 1){
                        System.out.println("Creation left: " + grid[0][1].left);
                        System.out.println("Creation Right: " + grid[0][1].right);
                        System.out.println("Creation Top: " + grid[0][1].top);
                        System.out.println("Creation Bottom: " + grid[0][1].bot);
                    }*/
                });
                grid[i][j] = node;

                // System.out.println("Created " + node.textView.getTag());
            }

        }
 /*       for( int i = 0; i < rowSize; i ++){
            for( int j = 0; j< colSize; j++){
                final int rowIndx = i;
                final int colIndx = j;
                grid[rowIndx][colIndx].textView.post(() -> {
                    int[] coord = new int[2];
                    grid[rowIndx][colIndx].textView.getLocationOnScreen(coord);
                    grid[rowIndx][colIndx].setCord(coord);
                    System.out.println(grid[0][0].y);
                });
            }
        }*/


    }

    private void calculateBoxSize(int containerWidth, int containerHeight) {
        int totalWidthSpacing = (rowSize - 1) * getDPDimensions(3);
       // System.out.println("total Width spacing: " + totalWidthSpacing);
        boxWidth = (containerWidth - totalWidthSpacing) / rowSize;
       // System.out.println("box Width : " + boxWidth);
        int totalHeightSpacing = (colSize - 1) * getDPDimensions(3);
        boxHeight = (containerHeight - totalHeightSpacing) / colSize;
        //System.out.println("box Height : " + boxHeight);
    }

    private int getDPDimensions(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
    }


    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        String[] nodePos = v.getTag().toString().split("\\.");
        nodePos = nodePos[1].split("-");
        int row = Integer.parseInt(nodePos[0]);
        int col = Integer.parseInt((nodePos[1]));
        Node node = grid[row][col];
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (setStartNode && !hasStartNode) {
                node.textView.setBackground(AppCompatResources.getDrawable(this, R.drawable.round_corner_start));
                if (node.isEndNode) {
                    node.isEndNode = false;
                    hasEndNode = false;
                }
                node.isStartNode = true;
                setStartNode = false;
                hasStartNode = true;
                node.isWall = false;
                node.distance = 0;
            }
            if (setEndNode && !hasEndNode) {
                node.textView.setBackground(AppCompatResources.getDrawable(this, R.drawable.round_corner_end));
                if (node.isStartNode) {
                    node.isStartNode = false;
                    hasStartNode = false;
                }
                node.isEndNode = true;
                node.isWall = false;
                hasEndNode = true;
                this.endNode = node;


            }
            grid[row][col] = node;
           // System.out.println(node.toString());
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            if (setWallNodes && !node.isStartNode && !node.isEndNode) {
                node.isWall = true;
                changeBoxColor(node, AppCompatResources.getDrawable(this, R.drawable.round_corner_wall));
            }
            if (resetNodes) {
                if (node.isStartNode) {
                    hasStartNode = false;
                    node.distance = Integer.MAX_VALUE;
                }
                if (node.isEndNode) {
                    hasEndNode = false;
                    this.endNode = null;
                }
                changeBoxColor(resetNode(node), AppCompatResources.getDrawable(this, R.drawable.round_corner_delete));
            }
           // System.out.println(node.toString());
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            for (Node[] gridRow : grid) {
                for (Node boxNode : gridRow) {
                    Rect viewBounds = new Rect(boxNode.left, boxNode.top, boxNode.right, boxNode.bot);
                    if (viewBounds.contains((int) event.getRawX(), (int) event.getRawY())) {
                        if (setWallNodes && !boxNode.isStartNode && !boxNode.isEndNode) {
                            boxNode.isWall = true;
                            changeBoxColor(boxNode, AppCompatResources.getDrawable(this, R.drawable.round_corner_wall));
                           // System.out.println(boxNode);
                        } else if (resetNodes) {
                            resetNode(boxNode);
                            changeBoxColor(boxNode, AppCompatResources.getDrawable(this, R.drawable.round_corner_delete));
                           // System.out.println(boxNode);
                        }
                    }
                }
            }

 /*           grid.forEach((key, box) -> {
                Rect viewBounds = new Rect(box.left, box.top, box.right, box.bot);
                if (viewBounds.contains((int) event.getRawX(), (int) event.getRawY()) && v.getTag() != box.textView.getTag()) {
                    if (setWallNodes && !box.isStartNode && !box.isEndNode) {
                        changeBoxColor(box, getDrawable(R.drawable.round_corner_wall));
                    } else if (box.isEndNode || box.isStartNode) {
                    } else {
                        changeBoxColor(box, getDrawable(R.drawable.round_corner_delete));
                    }
                }
            });*/

        }
    /*    if (event.getAction() == MotionEvent.ACTION_MOVE) {
            System.out.println(event.getRawX() + "         " + event.getRawY());
        }*/
        return true;
    }

    public Node resetNode(Node node) {
        node.isWall = false;
        node.isStartNode = false;
        node.isEndNode = false;
        return node;
    }

    public void changeBoxColor(Node node, Drawable color) {
        node.textView.setBackground(color);
        grid[node.row][node.col] = node;
    }

    public void setStartNode(View v) {
        this.setStartNode = true;
        setEndNode = false;
        setWallNodes = false;
        resetNodes = false;
    }

    public void setEndNode(View v) {
        this.setEndNode = true;
        setStartNode = false;
        setWallNodes = false;
        resetNodes = false;
    }

    public void setWallNodes(View v) {
        this.setWallNodes = true;
        setEndNode = false;
        setStartNode = false;
        resetNodes = false;
    }

    public void resetNodes(View v) {
        this.resetNodes = true;
        setEndNode = false;
        setStartNode = false;
        setWallNodes = false;
    }

    public void runAlgorithm(View v){
        Dijkstra dijkstra = new Dijkstra(this.grid,endNode);
        this.visitedNodesInOrder = dijkstra.getVisitedNodesInOrder();
        for( Node n : visitedNodesInOrder){
            System.out.println(n.textView.getTag());
        }
        this.nodesInShortestPathOrder = dijkstra.getNodesInShortestPathOrder(endNode);
        System.out.println("_____________________________________________Shortest Path____________________________________");
        for( Node n : nodesInShortestPathOrder){
            System.out.println(n.textView.getTag());
        }
        //this.animateAlgorithm(visitedNodesInOrder,nodesInShortestPathOrder);

    }

    private void animateAlgorithm(Node[] visitedNodesInOrder, Stack<Node> nodesInShortestPathOrder) {
        for( int i = 0; i <= visitedNodesInOrder.length; i++){
            final int index = i;
            if (i == visitedNodesInOrder.length){
                new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animateShortestPath(nodesInShortestPathOrder);
                    }
                },10*i);
            }
            new android.os.Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Node node = visitedNodesInOrder[index];
                    grid[node.row][node.col].textView.setBackground(getDrawable(R.drawable.round_corner_path_view));
                }
            }, 10*i);
        }
    }
    private void animateShortestPath(Stack<Node> nodesInShortestPathOrder){

    }
}
/*
 <View
        android:id="@+id/gridContainer"
        android:layout_width="0dp"
        android:layout_height="500dp"
        android:layout_marginStart="15dp"
        android:layout_marginTop="110dp"
        android:layout_marginEnd="15dp"
        android:layout_marginBottom="30dp"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintEnd_toEndOf="parent"
        app:layout_constraintHorizontal_bias="0.0"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:layout_constraintVertical_bias="0.0" />
 */