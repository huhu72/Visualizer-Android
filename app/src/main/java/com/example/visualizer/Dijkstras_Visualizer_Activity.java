package com.example.visualizer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;


import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.gridlayout.widget.GridLayout;

import java.util.HashMap;

class CustomTextView {
    TextView textView;
    int left;
    int right;
    int top;
    int bot;
    int x, y;
    boolean isWall = false;

    public CustomTextView(TextView tv) {
        this.textView = tv;
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

    public void setCord(int[] coord) {
        this.x = coord[0];
        this.y = coord[1];
    }
}

public class Dijkstras_Visualizer_Activity extends AppCompatActivity implements View.OnTouchListener {

    ConstraintLayout layout;
    ConstraintSet set = new ConstraintSet();
    int rowSize = 5 * 2;
    int colSize = 5 * 2;
    int boxHeight;
    int boxWidth;
    int xOffset, yOffset;
    HashMap<String, CustomTextView> grid = new HashMap<>();
    GridLayout gridContainerView;
    boolean setStartNode = false;
    boolean hasStartNode = false;

    private boolean setEndNode = false;
    private boolean hasEndNode = false;

    private boolean setWallNodes;

    private boolean resetNodes = false;
    private boolean areNodesSet = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dijkstras_visualizer);
        layout = (ConstraintLayout) findViewById(R.id.activity_dijkstras_visualizer);
        set.clone(layout);
        gridContainerView = (GridLayout) findViewById(R.id.gridContainer);
        gridContainerView.setAlignmentMode(GridLayout.ALIGN_BOUNDS);
        gridContainerView.setColumnCount(colSize);
        gridContainerView.setRowCount(rowSize);
        gridContainerView.setOnTouchListener(this);
        // System.out.println("Child " + gridContainerView.getChildCount());

        gridContainerView.post(new Runnable() {
            @Override
            public void run() {
                System.out.println("imageview width:" + gridContainerView.getWidth() + " height:" + gridContainerView.getHeight());
                calculateBoxSize(gridContainerView.getWidth(), gridContainerView.getHeight());/*
                for(int i = 0; i < layout.getChildCount(); i++){
                    System.out.println(layout.getChildAt(i).getTag());
                }*/
                createGrid();
                System.out.println("Create grid done");
                System.out.println("container: " + gridContainerView.getY());
            }
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

    private void createGrid() {

        int counter = 1;
        for (int i = 0; i < rowSize; i++) {
            for (int j = 0; j < colSize; j++) {
                TextView box = new TextView(this);
                box.setBackground(getDrawable(R.drawable.round_corner_delete));
                box.setId(TextView.generateViewId());
                String tag = "box-" + counter;
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
                grid.put(tag, new CustomTextView(box));
                counter++;
                grid.get(tag).textView.post(() -> {
                    int[] coord = new int[2];
                    grid.get(tag).textView.getLocationOnScreen(coord);
                    grid.get(tag).setTop(coord);
                    if (tag.equals("box-1")) {
                        this.xOffset = coord[0];
                        this.yOffset = coord[1];
                    }
                    int right = grid.get(tag).textView.getRight() + xOffset;
                    int bot = grid.get(tag).textView.getBottom() + yOffset;

                    grid.get(tag).setRectBounds(right, bot);
/*                    if ( rowIndx == 0 && colIndx == 1){
                        System.out.println("Creation left: " + grid.get(tag).left);
                        System.out.println("Creation Right: " + grid.get(tag).right);
                        System.out.println("Creation Top: " + grid.get(tag).top);
                        System.out.println("Creation Bottom: " + grid.get(tag).bot);
                    }*/
                });

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
/*        for( CustomTextView ctv[] : grid ){
            for( CustomTextView tv: ctv){
                int coord[] = new int[2];
                tv.textView.getLocationOnScreen(coord);
                tv.setCord(coord);
                System.out.println(tv.textView.getTag());
                System.out.println("For each loop x: " + tv.x);
                System.out.println("For each loop y: " + tv.y);
            }
        }*/
/*        grid[1][0].textView.post(() -> {
            int[] coord = new int[2];
            grid[1][0].textView.getLocationOnScreen(coord);
            grid[1][0].setCord(coord);
            System.out.println("Runnable X: " + grid[1][0].x);
            System.out.println("Runnable y: " + grid[1][0].y);
        });*/
        /*System.out.println("view x : " + grid[0][1].x);
        System.out.println("view y : " + grid[0][1].y);*/
   /*     grid[0][0].textView.post(() -> {
            int left = grid[0][0].textView.getLeft();
            int right = grid[0][0].textView.getRight();
            int top = grid[0][0].textView.getTop();
            int bot = grid[0][0].textView.getBottom();
            grid[0][1].setRectBounds(left,top,right,bot);
                System.out.println("Runnable left: " + grid[0][0].textView.getLeft());
                System.out.println("Runnable Right: " + grid[0][0].textView.getRight());
                System.out.println("Runnable Top: " + grid[0][0].textView.getTop());
                System.out.println("Runnable Bottom: " + grid[0][0].textView.getBottom());
        });
*/
    }

    private void calculateBoxSize(int containerWidth, int containerHeight) {
        int totalWidthSpacing = (rowSize - 1) * getDPDimensions(3);
        System.out.println("total Width spacing: " + totalWidthSpacing);
        boxWidth = (containerWidth - totalWidthSpacing) / rowSize;
        System.out.println("box Width : " + boxWidth);
        int totalHeightSpacing = (colSize - 1) * getDPDimensions(3);
        boxHeight = (containerHeight - totalHeightSpacing) / colSize;
        System.out.println("box Height : " + boxHeight);
    }

    private int getDPDimensions(int value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (setStartNode && !hasStartNode) {
                v.setBackground(getDrawable(R.drawable.round_corner_start));
                this.setStartNode = false;
                this.hasStartNode = true;
            }
            if (setEndNode && !hasEndNode) {
                v.setBackground(getDrawable(R.drawable.round_corner_end));
                this.setEndNode = false;
                this.hasEndNode = true;
            }
        }
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            if (setWallNodes) {
                CustomTextView box = grid.get(v.getTag());
                changeBoxColor(box, getDrawable(R.drawable.round_corner_wall));
            }
            if (resetNodes) {
                CustomTextView box = grid.get(v.getTag());
                changeBoxColor(box, getDrawable(R.drawable.round_corner_delete));
            }
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {

            grid.forEach((key, box) -> {
                Rect viewBounds = new Rect(box.left, box.top, box.right, box.bot);
                if (viewBounds.contains((int) event.getRawX(), (int) event.getRawY()) && v.getTag() != box.textView.getTag()) {
                    if (setWallNodes) {
                        changeBoxColor(box, getDrawable(R.drawable.round_corner_wall));
                    } else {
                        changeBoxColor(box, getDrawable(R.drawable.round_corner_delete));
                    }
                }
            });

        }
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            System.out.println(event.getX() + "         " + event.getRawY());
        }
        return true;
    }

    public void changeBoxColor(CustomTextView box, Drawable color) {
        box.textView.setBackground(color);
        box.isWall = true;
        grid.put(box.textView.getTag().toString(), box);
    }

    public void setStartNode(View v) {
        this.setStartNode = true;
    }

    public void setEndNode(View v) {
        this.setEndNode = true;
    }

    public void setWallNodes(View v) {
        this.setWallNodes = true;
    }

    public void resetNodes(View v) {
        this.resetNodes = true;
        this.setWallNodes = false;
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