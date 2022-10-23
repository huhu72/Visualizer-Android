package com.example.visualizer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;


import android.graphics.Rect;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.gridlayout.widget.GridLayout;

class CustomTextViewArray{
    TextView tv;
    int x;
    int y;
    public CustomTextViewArray(TextView tv){
        this.tv = tv;
    }
    public void setCord(int[] cord){
        this.x = cord[0];
        this.y = cord[1];
    }
}
public class Dijkstras_Visualizer_Activity extends AppCompatActivity implements View.OnTouchListener {

    ConstraintLayout layout;
    ConstraintSet set = new ConstraintSet();
    int rowSize = 25;
    int colSize = 25;
    CustomTextViewArray[][] grid = new CustomTextViewArray[rowSize][colSize];
    int boxHeight;
    int boxWidth;
    GridLayout gridContainerView;
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
        // System.out.println("Child " + gridContainerView.getChildCount());

        gridContainerView.post(new Runnable() {
            @Override
            public void run() {
                System.out.println("imageview width:" + gridContainerView.getWidth() + " height:" + gridContainerView.getHeight());
                calculateBoxSize(gridContainerView.getWidth(),gridContainerView.getHeight());
                System.out.println(layout.getChildAt(0).getTag());
                createGrid();
            }
        });
        DisplayMetrics displayMetrics = new DisplayMetrics();

        // on below line we are getting metrics for display using window manager.
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        // on below line we are getting height
        // and width using display metrics.
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        // on below line we are setting height and width to our text view.
        System.out.println("Width : " + String.valueOf(width) + "\n" + "Height : " + String.valueOf(height));

    }

    private void createGrid() {

        int counter = 1;
        for ( int i = 0; i < rowSize; i++ ){
            for (int j = 0; j < colSize; j++){
                TextView box = new TextView(this);
                box.setBackgroundColor(getColor(R.color.custom_grey));
                box.setId(TextView.generateViewId());
                box.setTag("box-" + counter);
                box.setText(String.valueOf(counter));
                box.setAutoSizeTextTypeUniformWithConfiguration(
                        1, 17, 1, TypedValue.COMPLEX_UNIT_DIP);
                box.setOnTouchListener(this);
                int[] cord = new int[2];
                box.getLocationOnScreen(cord);
                System.out.println("-----------------------------------------------------" + cord[0]);
                grid[i][j] = new CustomTextViewArray(box);
                ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(boxWidth, boxHeight);
                if ( i == 0 && j ==0 ){
                    //Row 0 Column  = 0
                    layoutParams.leftMargin=getDPDimensions(0);
                    layoutParams.topMargin = getDPDimensions(0);
                }else if (i != 0 && j == 0){
                    //Row > 0 Column = 0
                    layoutParams.leftMargin=getDPDimensions(0);
                    layoutParams.topMargin = getDPDimensions(1);
                }else{
                    layoutParams.leftMargin=getDPDimensions(1);
                    layoutParams.topMargin = getDPDimensions(0);
                }
                box.setLayoutParams(layoutParams);
                System.out.println("Grid Child: " + gridContainerView.getChildCount());
                gridContainerView.addView(box);
                counter++;
            }

        }
       /* View box;
        int counter = 1;
        for( int i = 0; i < rowSize; i++){
            for (int j = 0; j < colSize; j++){
                box = new View(this);
                box.setId(View.generateViewId());
                box.setBackgroundColor(getResources().getColor(R.color.custom_grey));
                box.setTag("box-" + counter);
                box.setLayoutParams(new ConstraintLayout.LayoutParams(80,68));
                layout.addView(box);
                grid[i][j] = box;
                if ( i == 0 && j ==0 ){
                    //Row 0 Column  = 0
                    set.connect(box.getId(), ConstraintSet.LEFT, layout.getChildAt(0).getId(), ConstraintSet.LEFT, 0);
                    set.connect(box.getId(), ConstraintSet.TOP, layout.getChildAt(0).getId(), ConstraintSet.TOP, 0);
                }else if (i != 0 && j == 0){
                    //Row > 0 Column = 0
                    set.connect(box.getId(), ConstraintSet.LEFT, grid[i-1][j].getId(), ConstraintSet.LEFT, 0);
                    set.connect(box.getId(), ConstraintSet.TOP, grid[i-1][j].getId(), ConstraintSet.BOTTOM, getDPDimensions(1));
                }else{
                    set.connect(box.getId(), ConstraintSet.LEFT, grid[i][j-1].getId(), ConstraintSet.RIGHT, getDPDimensions(1));
                    set.connect(box.getId(), ConstraintSet.TOP, grid[i][j-1].getId(), ConstraintSet.TOP, 0);
                }
                set.constrainWidth(box.getId(), boxWidth);
                set.constrainHeight(box.getId(), boxHeight);
                set.applyTo(layout);
                counter ++;
            }
        }
*/
    }

    private void calculateBoxSize(int containerWidth, int containerHeight) {
        int totalWidthSpacing = (rowSize-1)*getDPDimensions(1);
        System.out.println("total Width spacing: " + totalWidthSpacing);
        boxWidth = (containerWidth-totalWidthSpacing)/rowSize;
        System.out.println("box Width : " + boxWidth);
        int totalHeightSpacing = (colSize-1)*getDPDimensions(1);
        boxHeight = (containerHeight-totalHeightSpacing)/colSize;
    }

    private int getDPDimensions(int value){
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            System.out.println(v.getTag() + " preesed");
            for( CustomTextViewArray ctv[] : grid){
                for( CustomTextViewArray CTV: ctv){
                    int cord[] = new int[2];
                    CTV.tv.getLocationOnScreen(cord);
                    CTV.setCord(cord);
                }
            }
            v.setBackgroundColor(getColor(R.color.white));
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE){
            System.out.println(v.getTag() + " moved");
            System.out.println(event.getRawX());
            TextView hoveredView;
            Rect r1 = new Rect((int)event.getRawX(),(int)event.getRawY(),1,1);
            //v.dispatchTouchEvent(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), (int)MotionEvent.ACTION_CANCEL, 0, 0, 0));
            for(CustomTextViewArray[] TV: grid){
                for(CustomTextViewArray tv: TV){
                    if( r1.intersect(tv.x,tv.y,1,1)){
                        System.out.println(r1.intersect(tv.x,tv.y,1,1));
                    }
                }
            }
            System.out.println("called up");

        }
        if (event.getAction() == MotionEvent.ACTION_UP){
            System.out.println(v.getTag() + " released");
     /*      for(TextView[] TV: grid){
               for(TextView tv: TV){
                    System.out.println(tv.getTag());
               }
           }*/

            System.out.println(grid[0][1].x);
        }
        return true;
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