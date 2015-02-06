import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class project_1 extends PApplet {

//Some of this code provided from http://www.openprocessing.org/sketch/95216


//size of cells altogether
int cellSize = 10;

//how likely is it for a cell to be alive?
float probabilityAliveStart = 15;

//minimum amount of neighbors
int neighborsMin = 2;

//maximum amount of neighbors
int neighborsMax = 3;
// Variables for timer
int interval = 1000;
int lastRecordedTime = 0;


//colors for alive/dead cells
int alive = color(0, 17, 255);
int dead = color(255);

//arrays of cells
int[][] cells;

//buffer to record the state of cells
int[][] cellsBuffer;

// Pause
boolean pause = false;

int startX = 300/cellSize;
int startY = 300/cellSize;

public void setup() {
  size(300, 300);

  //setup arrays
  cells = new int[startX][startY];
  cellsBuffer = new int[startX][startY];

  //background grid
  stroke(255);

  //initial cells
  for (int x=0; x<startX; x++) {
    for (int y=0; y<startY; y++) {
      float state = random (100);
      if (state > probabilityAliveStart) {
        state = 0;
      } else {
        state = 1;
      }
      cells[x][y] = PApplet.parseInt(state); // Save state of each cell
    }
  }
  background(0); // Fill in black in case cells don't cover all the windows
  print("Press R to restart with standard parameters");
  print("\nPress C to change parameters");
  print("\nPress X to restart with current parameters");
  print("\nPress SPACE to pause");
}

public void draw() {
  //draw grid
  for (int x=0; x<startX; x++) {
    for (int y=0; y<startY; y++) {
      if (cells[x][y] == 1) {
        fill(alive);
      } else {
        fill(dead);
      }
      rect(x*cellSize, y*cellSize, cellSize, cellSize);
    }
  }

  //iterate if timer ticks
  if (millis()-lastRecordedTime>interval) {
    if (!pause) {
      iteration();
      lastRecordedTime = millis();
    }
  }
}

public void iteration() {
  //save cells to buffer
  for (int x=0; x<startX; x++) {
    for ( int y=0; y<startY; y++) {
      cellsBuffer[x][y] = cells[x][y];
    }
  }
  neighborCheck(neighborsMin, neighborsMax);
}

public void neighborCheck(int a, int b) { 
  // Visit each cell:
  for (int x=0; x<startX; x++) {
    for (int y=0; y<startY; y++) {
      // And visit all the neighbours of each cell
      int neighbours = 0; // We'll count the neighbours
      for (int xx=x-1; xx<=x+1; xx++) {
        for (int yy=y-1; yy<=y+1; yy++) { 
          if (((xx>=0)&&(xx<startX))&&((yy>=0)&&(yy<startY))) { // Make sure you are not out of bounds
            if (!((xx==x)&&(yy==y))) { // Make sure to to check against self
              if (cellsBuffer[xx][yy]==1) {
                neighbours ++; // Check alive neighbours and count them
              }
            } // End of if
          } // End of if
        } // End of yy loop
      } //End of xx loop
      // We've checked the neigbours: apply rules!
      if (cellsBuffer[x][y]==1) { // The cell is alive: kill it if necessary
        if (neighbours < a || neighbours > b) {
          cells[x][y] = 0; // Die unless it has a or b neighbors
        }
      } else { // The cell is dead: make it live if necessary     
        if (neighbours == b ) {
          cells[x][y] = 1; // Only if it has b neighbours
        }
      } // End of if
    } // End of y loop
  } // End of x loop
} // End of function

public void keyPressed() {
  //restart cells with default parameters
  if (key=='r' || key == 'R') {
    probabilityAliveStart = 15;
    neighborsMin = 2;
    neighborsMax = 3;
    interval = 1000;
    for (int x=0; x<startX; x++) {
      for (int y=0; y<startY; y++) {
        float state = random (100);
        if (state > probabilityAliveStart) {
          state = 0;
        } else {
          state = 1;
        }
        cells[x][y] = PApplet.parseInt(state); // Save state of each cell
      }
    }
  }
  // Gives input for the user to change rules if they wish to
  if (key == 'c' || key == 'C') {
    print("\nPress P to restart and change Probability of living cells at start to random number");
    print("\nPress N to restart and change the number of neighbors needed to live");
    print("\nPress UP and DOWN to increase and decrease the speed of the sim");
  }
  //changes the probability that the cell is alive is at the start to a random number
  if (key == 'p' || key == 'P') {

    float Probability = random(100);
    probabilityAliveStart = Probability;
    print("\nNew probability is" + " " +  Probability);
    for (int x=0; x<startX; x++) {
      for (int y=0; y<startY; y++) {
        float state = random (100);
        if (state > probabilityAliveStart) {
          state = 0;
        } else {
          state = 1;
        }
        cells[x][y] = PApplet.parseInt(state); // Save state of each cell
      }
    }
  }

  //changes the number of neighbors needed for the cell to live, die, and reincarnate
  if (key == 'n' || key == 'N') {
    neighborsMin = 0;
    neighborsMax = 0;

    while (neighborsMin >= neighborsMax) {
      float Min = random(1, 8);
      float Max = random(1, 8);
      neighborsMin = PApplet.parseInt(Min);
      neighborsMax = PApplet.parseInt(Max);
    }

    print("\n The mininum number of Neighbors is" + " " + neighborsMin);
    print("\n The maximum number of Neighbors is" + " " + neighborsMax);

    for (int x=0; x<startX; x++) {
      for (int y=0; y<startY; y++) {
        float state = random (100);
        if (state > probabilityAliveStart) {
          state = 0;
        } else {
          state = 1;
        }
        cells[x][y] = PApplet.parseInt(state); // Save state of each cell
      }
    }

    //save cells to buffer
    //initialize cells
    for (int x=0; x<startX; x++) {
      for ( int y=0; y<startY; y++) {
        cellsBuffer[x][y] = cells[x][y];
      }
    }
    neighborCheck(neighborsMin, neighborsMax);
  }

  //Restarts cells with current parameters
  if (key == 'x' || key == 'X') {
    for (int x=0; x<startX; x++) {
      for (int y=0; y<startY; y++) {
        float state = random (100);
        if (state > probabilityAliveStart) {
          state = 0;
        } else {
          state = 1;
        }
        cells[x][y] = PApplet.parseInt(state); // Save state of each cell
      }
    }
  }

  //Speed up or slow down the cells
  if (key == CODED) {
    if (keyCode == DOWN) {
      interval += 100; 
      print("\nSpeed decreased to" + " " + interval + " " + "ms");
    } else if (keyCode == UP) {
      if (interval >= 100) {
        interval -= 100;
      }
      print("\nSpeed increased to" + " " + interval + " " + "ms");
    } else {
      print ("\nSpeed Maxed");
    }
  } 


  //toggles pause on and off
  if (key==' ') { // On/off of pause
    pause = !pause;
    if (pause == true) {
      print("\nSim Paused");
    } else print("\nSim Unpaused");
  }
}

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "project_1" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
