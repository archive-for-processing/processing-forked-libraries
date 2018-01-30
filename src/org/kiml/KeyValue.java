package org.kiml;

import java.util.HashMap;

/**
 * @author Shinhoo Park
 *
 */

public enum KeyValue {
	Escape(0), F1(1), F2(2), F3(3), F4(4), F5(5), F6(6), F7(7), F8(8), F9(9), F10(10), F11(11), F12(12), 
	Printscreen(13), Pause(14), None(15), Tilde(16), Number1(17), Number2(18), Number3(19), Number4(20), 
	Number5(21), Number6(22), Number7(23), Number8(24), Number9(25), Number0(26), Underscore(27), Plus(28), 
	Backspace(29), Insert(30), Home(31), Tab(32), Q(33), W(34), E(35), R(36), T(37), Y(38), U(39), I(40), 
	O(41), P(42), OpenBracket(43), CloseBracket(44), Backslash(45), Delete(46), End(47), CapsLock(48), 
	A(49), S(50), D(51), F(52), G(53), H(54), J(55), K(56), L(57), Colon(58), Quote(59), Enter(60), 
	PageUp(61), PageDown(62), Up(63), ModifierLeftShift(64), Z(65), X(66), C(67), V(68), B(69), N(70),
	M(71), Comma(72), Dot(73), Slash(74), ModifierRightShift(75), Left(76), Down(77), Right(78), 
	ModifierRightCtrl(79), ModifierLeftCtrl(80), ModifierLeftUi(81), ModifierLeftAlt(82), Spacebar(83), 
	ModifierRightAlt(84), ModifierRightUi(85), ModifierNone(86), ModifierNone2(87);

	private int value;
	private static HashMap<Integer, KeyValue> keyArray;

	private KeyValue(int v) {
		this.value = v;
	}

	public static KeyValue getKey(int k) {
		if (keyArray == null) {
			initMapping();
		}
		return keyArray.get(k);
	}
	
	public int getValue() {
		return value;
	}

	private static void initMapping() {
		keyArray = new HashMap<Integer, KeyValue>();
		for (KeyValue s : values()) {
			keyArray.put(s.value, s);
		}
	}
	
	@Override
	public String toString() {
		return this.name();
	}
}
