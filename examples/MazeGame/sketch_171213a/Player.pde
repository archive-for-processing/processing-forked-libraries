
/**
 * 
 * 
 * 
 * This class draws the player and sets the players parameters 
 * such as collision detection and velocity of player.
 *
 * @author AmazingGroup
 */

class Player {
  private PVector velocity;
  private float maxVelocity; // maximal possible velocity
  private PVector position; // position of the player in pixel
  private boolean up; // key control, up pressed
  private boolean down; // key control, down pressed
  private boolean right; // key control, right pressed
  private boolean left; // key control, left pressed
  private float acceleration; // Change of velocity with respect to time
  private float radius; // radius of player icon
  private float deceleration; // Change of velocity with respect to time

  public Player(int cellSize) {
    velocity = new PVector(0, 0); // set initial velocity to zero
    radius = cellSize / 3; // set radius
    // Sets max velocity dependent on cell size, formula arrived from experiments
    maxVelocity = 0.012f * cellSize + 1.4195f;
    acceleration = 0.5f; // set acceleration
    deceleration = 0.7f; // set deceleration
  }

  public void drawPlayer() {
    fill(255, 255, 0);
    ellipse(position.x, position.y, radius, radius);
  }

  // process key control
  public void move() {

    if (up) {
      velocity.y = velocity.y - acceleration;
    }
    if (down) {
      velocity.y = velocity.y + acceleration;
    }
    if (left) {
      velocity.x = velocity.x - acceleration;
    }
    if (right) {
      velocity.x = velocity.x + acceleration;
    }
    if (!left && !right) {
      velocity.x = (velocity.x * deceleration);
    }
    if (!up && !down) {
      velocity.y = (velocity.y * deceleration);
    }
    velocity.limit(maxVelocity);
    position.add(velocity);

  }

  // check collision with horizontal walls
  void collisionHorizontal(Wall wall) {
    if (wall.isSolid()) {
      if (velocity.y > 0) {
        if (position.y + radius >= wall.getPosY() && position.y < wall.getPosY() + wall.getWallHeight()) {
          if (position.x > wall.getPosX() && position.x < wall.getPosX() + wall.getWallWidth()) {
            position.y = wall.getPosY() + wall.getWallHeight() - radius;
          }
        }
      }
      if (velocity.y < 0) {
        if (position.y - radius >= wall.getPosY()
            && position.y <= wall.getPosY() + wall.getWallHeight() + radius) {
          if (position.x > wall.getPosX() && position.x < wall.getPosX() + wall.getWallWidth()) {
            position.y = wall.getPosY() + wall.getWallHeight() + radius + 2;
          }
        }
      }
    }
  }

  // check collision with vertical walls
  void collisionVertical(Wall wall) {
    if (wall.isSolid()) {
      if (velocity.x > 0) {
        if (position.x + radius >= wall.getPosX() && position.x < wall.getPosX() + wall.getWallWidth()) {
          if (position.y > wall.getPosY() && position.y < wall.getPosY() + wall.getWallHeight()) {
            position.x = wall.getPosX() + wall.getWallWidth() - radius;
          }
        }
      }
      if (velocity.x < 0) {
        if (position.x - radius >= wall.getPosX()
            && position.x <= wall.getPosX() + wall.getWallWidth() + radius) {
          if (position.y > wall.getPosY() && position.y < wall.getPosY() + wall.getWallHeight()) {
            position.x = wall.getPosX() + wall.getWallWidth() + radius + 2;
          }
        }
      }
    }
  }

  // check whether player escaped the maze
  public boolean goalReached(int size) {

    if (position.x > size + 10 || position.y > size + 10) {
      fill(0, 255, 0);
      rect(0, 0, 200, 200);

      return true;
    } else {
      return false;
    }

  }

  /**
   * 
   * Getter and Setter methods
   * 
   */

  // set initial position of player in pixel
  public void setStartPosition(float posW, float posH) {
    position = new PVector(0, 0);
    position.x = posW;
    position.y = posH;
  }

  // set maximum velocity of player
  void setMaxVelocity(float f) {
    maxVelocity = f;
  }

  public float getPositionX() {
    return position.x;
  }

  public float getPositionY() {
    return position.y;
  }

  public void setDown(boolean down) {
    this.down = down;
  }

  public void setUp(boolean up) {
    this.up = up;
  }

  public void setRight(boolean right) {
    this.right = right;
  }

  public void setLeft(boolean left) {
    this.left = left;
  }
}