import elementaryGUI.*;

/*
The following project shows:
 -how to use a Label
 -its main functions
 -using UI for customizing components in your project
 */

UI style;
Label name, surname, age;

void settings() {
  size(320, 60, P2D);
}

void setup() {
  style = new UI(this) {
    @Override
      public void displayBackground(Component component) {
      fill(this.getBackground());
      noStroke();
      rect(0, 0, component.getWidth(), component.getHeight(), 5);

      noFill();
      stroke(this.getForeground());
      strokeWeight(2.5);
      rect(5, 5, component.getWidth() - 10, component.getHeight() - 10, 5);
    }
  };
  style.setColors(#FFFFFF, #995555);

  name = new Label(this);
  name.setBounds(5, 5, 100, 50);
  name.setText("Bob");
  name.setUI(style);

  surname = new Label(this);
  surname.setBounds(110, 5, 100, 50);
  surname.setText("Bin");
  surname.setUI(style);

  age = new Label(this);
  age.setBounds(215, 5, 100, 50);
  age.setText("37");
  age.setUI(style);
}

void draw() {
  background(#550000);
  name.display();
  surname.display();
  age.display();
}
