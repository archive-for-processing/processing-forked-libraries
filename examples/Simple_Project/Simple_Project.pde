import elementaryGUI.*;

UI materialUI;
Panel surprise;
Button showSurprise, close, clickME;
Label greetings;

void settings() {
  size(400, 400);
}

void setup() {
  materialUI = new UI(this) {
    @Override 
      public void displayBackground(Component component) {
      pushMatrix();
      fill(this.getBackground(), 100);
      noStroke();
      rect(0, 0, component.getWidth(), component.getHeight());
      popMatrix();
    }
  };
  materialUI.setColors(#FF9999, #551010);

  surprise = new Panel(this);
  surprise.setBounds(20, 20, 360, 360);
  surprise.setUI(materialUI);

  showSurprise = new Button(this) {
    @Override
      public void executeClickedTasks() {
      surprise.setVisible(true);
      this.setVisible(false);
    }
  };
  showSurprise.setBounds(width*0.5-100, height*0.5-50, 200, 50);
  showSurprise.setText("Show surprise!");
  showSurprise.setUI(materialUI);

  close = new Button(this) {
    @Override
      public void executeClickedTasks() {
      surprise.setVisible(false);
      showSurprise.setVisible(true);
      greetings.setVisible(false);
    }
  };
  close.setBounds(surprise.getWidth() - 60, 10, 50, 25);
  close.setText("close");
  close.setUI(materialUI);

  clickME = new Button(this) {
    @Override
      public void executeClickedTasks() {
      greetings.setVisible(true);
      this.setVisible(false);
    }
  };
  clickME.setBounds(surprise.getWidth()*0.5-100, surprise.getHeight()*0.5-50, 200, 50);
  clickME.setText("Click me!");
  clickME.setUI(materialUI);

  greetings = new Label(this);
  greetings.setBounds(width*0.5-100, height*0.5-50, 200, 50);
  greetings.setText("Hello library!");
  greetings.setUI(materialUI);
  greetings.setVisible(false);

  surprise.add(close);
  surprise.add(clickME);
  surprise.setVisible(false);
}

void draw() {
  background(#FFFFFF);
  showSurprise.display();
  surprise.display();
  greetings.display();
}
