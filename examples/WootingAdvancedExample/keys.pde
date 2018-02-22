public enum LayoutType { 
  ANSI, ISO
};

public class SingleKey {
  public float xpos, ypos, w, h;
  public KeyValue keyName;

  public SingleKey(float xpos, float ypos, float w, float h, KeyValue keyName)
  {
    this.xpos = xpos;
    this.ypos = ypos;
    this.w = w;
    this.h = h;
    this.keyName = keyName;
  }
}

public class Layout {
  HashMap<KeyValue, SingleKey> keys;
  
  public HashMap getKeys()
  {
    return keys;
  }


  public Layout(LayoutType type)
  {
    keys = new HashMap<KeyValue, SingleKey>();
    
    switch(type)
    {
      case ANSI:
        keys.put(KeyValue.Enter, new SingleKey(12.75, 2, 2.25, 1, KeyValue.Enter));
        keys.put(KeyValue.Backslash, new SingleKey(13.5, 1, 1.5, 1, KeyValue.Backslash));
        keys.put(KeyValue.ModifierLeftShift, new SingleKey(0, 3, 2.25, 1, KeyValue.ModifierLeftShift));
        break;
      case ISO:
        keys.put(KeyValue.Enter, new SingleKey(13.75, 1, 1.25, 2, KeyValue.Enter));
        keys.put(KeyValue.Backslash, new SingleKey(12.75, 2, 1, 1, KeyValue.Backslash));
        keys.put(KeyValue.ModifierLeftShift, new SingleKey(0, 3, 1.25, 1, KeyValue.ModifierLeftShift));
        keys.put(KeyValue.ModifierNone2, new SingleKey(1.25, 3, 1, 1,  KeyValue.ModifierNone2));
        break;    
    }
    
    keys.put(KeyValue.Tilde, new SingleKey(0, 0, 1, 1, KeyValue.Tilde));
    keys.put(KeyValue.Number1, new SingleKey(1, 0, 1, 1, KeyValue.Number1));
    keys.put(KeyValue.Number2, new SingleKey(2, 0, 1, 1, KeyValue.Number2));
    keys.put(KeyValue.Number3, new SingleKey(3, 0, 1, 1, KeyValue.Number3));
    keys.put(KeyValue.Number4, new SingleKey(4, 0, 1, 1, KeyValue.Number4));
    keys.put(KeyValue.Number5, new SingleKey(5, 0, 1, 1, KeyValue.Number5));
    keys.put(KeyValue.Number6, new SingleKey(6, 0, 1, 1, KeyValue.Number6));
    keys.put(KeyValue.Number7, new SingleKey(7, 0, 1, 1, KeyValue.Number7));
    keys.put(KeyValue.Number8, new SingleKey(8, 0, 1, 1, KeyValue.Number8));
    keys.put(KeyValue.Number9, new SingleKey(9, 0, 1, 1, KeyValue.Number9));
    keys.put(KeyValue.Number0, new SingleKey(10, 0, 1, 1, KeyValue.Number0));
    keys.put(KeyValue.Underscore, new SingleKey(11, 0, 1, 1, KeyValue.Underscore));
    keys.put(KeyValue.Plus, new SingleKey(12, 0, 1, 1, KeyValue.Plus));
    keys.put(KeyValue.Backspace, new SingleKey(13, 0, 2, 1, KeyValue.Backspace));
    keys.put(KeyValue.Tab, new SingleKey(0, 1, 1.5, 1, KeyValue.Tab));
    keys.put(KeyValue.Q, new SingleKey(1.5, 1, 1, 1, KeyValue.Q));
    keys.put(KeyValue.W, new SingleKey(2.5, 1, 1, 1, KeyValue.W));
    keys.put(KeyValue.E, new SingleKey(3.5, 1, 1, 1, KeyValue.E));
    keys.put(KeyValue.R, new SingleKey(4.5, 1, 1, 1, KeyValue.R));
    keys.put(KeyValue.T, new SingleKey(5.5, 1, 1, 1, KeyValue.T));
    keys.put(KeyValue.Y, new SingleKey(6.5, 1, 1, 1, KeyValue.Y));
    keys.put(KeyValue.U, new SingleKey(7.5, 1, 1, 1, KeyValue.U));
    keys.put(KeyValue.I, new SingleKey(8.5, 1, 1, 1, KeyValue.I));
    keys.put(KeyValue.O, new SingleKey(9.5, 1, 1, 1, KeyValue.O));
    keys.put(KeyValue.P, new SingleKey(10.5, 1, 1, 1, KeyValue.P));
    keys.put(KeyValue.OpenBracket, new SingleKey(11.5, 1, 1, 1, KeyValue.OpenBracket));
    keys.put(KeyValue.CloseBracket, new SingleKey(12.5, 1, 1, 1, KeyValue.CloseBracket));
    keys.put(KeyValue.CapsLock, new SingleKey(0, 2, 1.75, 1, KeyValue.CapsLock));
    keys.put(KeyValue.A, new SingleKey(1.75, 2, 1, 1, KeyValue.A));
    keys.put(KeyValue.S, new SingleKey(2.75, 2, 1, 1, KeyValue.S));
    keys.put(KeyValue.D, new SingleKey(3.75, 2, 1, 1, KeyValue.D));
    keys.put(KeyValue.F, new SingleKey(4.75, 2, 1, 1, KeyValue.F));
    keys.put(KeyValue.G, new SingleKey(5.75, 2, 1, 1, KeyValue.G));
    keys.put(KeyValue.H, new SingleKey(6.75, 2, 1, 1, KeyValue.H));
    keys.put(KeyValue.J, new SingleKey(7.75, 2, 1, 1, KeyValue.J));
    keys.put(KeyValue.K, new SingleKey(8.75, 2, 1, 1, KeyValue.K));
    keys.put(KeyValue.L, new SingleKey(9.75, 2, 1, 1, KeyValue.L));
    keys.put(KeyValue.Colon, new SingleKey(10.75, 2, 1, 1, KeyValue.Colon));
    keys.put(KeyValue.Quote, new SingleKey(11.75, 2, 1, 1, KeyValue.Quote));
    keys.put(KeyValue.Z, new SingleKey(2.25, 3, 1, 1, KeyValue.Z));
    keys.put(KeyValue.X, new SingleKey(3.25, 3, 1, 1, KeyValue.X));
    keys.put(KeyValue.C, new SingleKey(4.25, 3, 1, 1, KeyValue.C));
    keys.put(KeyValue.V, new SingleKey(5.25, 3, 1, 1, KeyValue.V));
    keys.put(KeyValue.B, new SingleKey(6.25, 3, 1, 1, KeyValue.B));
    keys.put(KeyValue.N, new SingleKey(7.25, 3, 1, 1, KeyValue.N));
    keys.put(KeyValue.M, new SingleKey(8.25, 3, 1, 1, KeyValue.M));
    keys.put(KeyValue.Comma, new SingleKey(9.25, 3, 1, 1, KeyValue.Comma));
    keys.put(KeyValue.Dot, new SingleKey(10.25, 3, 1, 1, KeyValue.Dot));
    keys.put(KeyValue.Slash, new SingleKey(11.25, 3, 1, 1, KeyValue.Slash));
    keys.put(KeyValue.ModifierRightShift, new SingleKey(12.25, 3, 2.75, 1, KeyValue.ModifierRightShift));
    keys.put(KeyValue.ModifierLeftCtrl, new SingleKey(0, 4, 1.5, 1, KeyValue.ModifierLeftCtrl));
    keys.put(KeyValue.ModifierLeftUi, new SingleKey(1.5, 4, 1, 1, KeyValue.ModifierLeftUi));
    keys.put(KeyValue.ModifierLeftAlt, new SingleKey(2.5, 4, 1.5, 1, KeyValue.ModifierLeftAlt));
    keys.put(KeyValue.Spacebar, new SingleKey(4, 4, 6, 1, KeyValue.Spacebar));
    keys.put(KeyValue.ModifierRightAlt, new SingleKey(10, 4, 1.5, 1, KeyValue.ModifierRightAlt));
    keys.put(KeyValue.ModifierRightUi, new SingleKey(11.5, 4, 1, 1, KeyValue.ModifierRightUi));
    keys.put(KeyValue.ModifierNone, new SingleKey(12.5, 4, 1, 1, KeyValue.ModifierNone));
    keys.put(KeyValue.ModifierRightCtrl, new SingleKey(13.5, 4, 1.5, 1, KeyValue.ModifierRightCtrl));
    keys.put(KeyValue.Escape, new SingleKey(0, -1.5, 1, 1, KeyValue.Escape));
    keys.put(KeyValue.F1, new SingleKey(2, -1.5, 1, 1, KeyValue.F1));
    keys.put(KeyValue.F2, new SingleKey(3, -1.5, 1, 1, KeyValue.F2));
    keys.put(KeyValue.F3, new SingleKey(4, -1.5, 1, 1, KeyValue.F3));
    keys.put(KeyValue.F4, new SingleKey(5, -1.5, 1, 1, KeyValue.F4));
    keys.put(KeyValue.F5, new SingleKey(6.5, -1.5, 1, 1, KeyValue.F5));
    keys.put(KeyValue.F6, new SingleKey(7.5, -1.5, 1, 1, KeyValue.F6));
    keys.put(KeyValue.F7, new SingleKey(8.5, -1.5, 1, 1, KeyValue.F7));
    keys.put(KeyValue.F8, new SingleKey(9.5, -1.5, 1, 1, KeyValue.F8));
    keys.put(KeyValue.F9, new SingleKey(11, -1.5, 1, 1, KeyValue.F9));
    keys.put(KeyValue.F10, new SingleKey(12, -1.5, 1, 1, KeyValue.F10));
    keys.put(KeyValue.F11, new SingleKey(13, -1.5, 1, 1, KeyValue.F11));
    keys.put(KeyValue.F12, new SingleKey(14, -1.5, 1, 1, KeyValue.F12));
    keys.put(KeyValue.Printscreen, new SingleKey(15.5, -1.5, 1, 1, KeyValue.Printscreen));
    keys.put(KeyValue.Pause, new SingleKey(16.5, -1.5, 1, 1, KeyValue.Pause));
    keys.put(KeyValue.None, new SingleKey(17.5, -1.5, 1, 1, KeyValue.None));
    keys.put(KeyValue.Insert, new SingleKey(15.5, 0, 1, 1, KeyValue.Insert));
    keys.put(KeyValue.Home, new SingleKey(16.5, 0, 1, 1, KeyValue.Home));
    keys.put(KeyValue.PageUp, new SingleKey(17.5, 0, 1, 1, KeyValue.PageUp));
    keys.put(KeyValue.Delete, new SingleKey(15.5, 1, 1, 1, KeyValue.Delete));
    keys.put(KeyValue.End, new SingleKey(16.5, 1, 1, 1, KeyValue.End));
    keys.put(KeyValue.PageDown, new SingleKey(17.5, 1, 1, 1, KeyValue.PageDown));
    keys.put(KeyValue.Up, new SingleKey(16.5, 3, 1, 1, KeyValue.Up));
    keys.put(KeyValue.Left, new SingleKey(15.5, 4, 1, 1, KeyValue.Left));
    keys.put(KeyValue.Down, new SingleKey(16.5, 4, 1, 1, KeyValue.Down));
    keys.put(KeyValue.Right, new SingleKey(17.5, 4, 1, 1, KeyValue.Right));
  }
}