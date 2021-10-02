package java_8_in_action.functionalprograming;

public class TreeExample {
	public static void main(String[] args) {
		Tree a = update("a", 100, null);
		
		System.out.println(a);
		System.out.println();
		
		a = update ("b", 200, a);
		System.out.println(a);
		System.out.println();
		
		a = update ("c", 300, a);
		System.out.println(a);
		System.out.println();
		
		Tree aa = fupdate("a", 100, null);
		System.out.println(aa);
		
		aa = fupdate("b", 300, aa);
		System.out.println(aa);
	}
	
	// 함수형으로 구현하기
	// 아래 코드는 if-then-else 대신 하나의 조건문을 사용했는데 이렇게 해서 위 코드가 부작용이 없는 하나의 표현식임을 강조헀다.
	static Tree fupdate (String k, int newVal, Tree t) {
		return (t == null) ? 
				new Tree (k, newVal, null, null) :
					k.equals(t.getKey()) ?
							new Tree (k, newVal, t.getLeft(), t.getRight()) :
					k.compareTo(t.getKey()) < 0 ?
							new Tree(t.getKey(), t.getVal(), fupdate(k, newVal, t.getLeft()), t.getRight()) :
							new Tree(t.getKey(), t.getVal(), t.getLeft(), fupdate(k, newVal, t.getRight()));
							
	}
	
	static Tree update (String k, int newVal, Tree t) {
		if (t == null) {
			new Tree (k, newVal, null, null);
		} 
		else if (k.equals(t.getKey())) {
			t.setVal(newVal);
		}
		else if (k.compareTo(t.getKey()) < 0) {
			t.setLeft(update (k, newVal, t.getLeft()));
		} 
		else {
			t.setRight(update (k, newVal, t.getRight()));
		}
		return t;
	}
}

class Tree {
	private String key;
	private int val;
	private Tree left, right;
	public Tree (String k, int v, Tree l, Tree r) {
		key = k;
		val = v;
		left = l;
		right = r;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public int getVal() {
		return val;
	}
	public void setVal(int val) {
		this.val = val;
	}
	public Tree getLeft() {
		return left;
	}
	public void setLeft(Tree left) {
		this.left = left;
	}
	public Tree getRight() {
		return right;
	}
	public void setRight(Tree right) {
		this.right = right;
	}
	
	@Override
	public String toString() {
		return "key = " + key + " val = " + val + " left = " + left + " right = " + right;
	}
	
}

class TreeProcessor {
	public static int lookup (String k, int defaultVal, Tree t) {
		if (t == null) return defaultVal;
		
		if (k.equals(t.getKey())) {
			return t.getVal();
		}
		
		return lookup (k, defaultVal, k.compareTo(t.getKey()) < 0 ? t.getLeft() : t.getRight());
	}
}