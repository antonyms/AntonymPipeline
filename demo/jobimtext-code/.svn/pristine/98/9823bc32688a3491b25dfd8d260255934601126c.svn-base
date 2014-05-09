package com.ibm.bluej.util.common;

import java.lang.reflect.*;
import java.util.*;

public class FunctionalUtil {
	
	public static class InstFunST implements FunST {
		public InstFunST(Object inst, String methodName) {
			for (Method m : inst.getClass().getMethods()) {
				if (m.getName().equals(methodName) && m.getParameterTypes().length == 1 && !Modifier.isStatic(m.getModifiers())) {
					method = m;
					break;
				}
			}
			if (method == null) {
				throw new Error("No non-static 1 arg method named: "+methodName+" in "+inst.getClass());
			}
		}
		
		private Object inst;
		private Method method;
		public Object f(Object o) {
			try {
				return method.invoke(inst, o);
			} catch (Exception e) {
				throw new Error(e);
			}
		}
	}
	
	public static class StaticFunST implements FunST {
		public StaticFunST(Class clazz, String methodName) {
			for (Method m : clazz.getDeclaredMethods()) {
				if (m.getName().equals(method) && m.getParameterTypes().length == 1 && Modifier.isStatic(m.getModifiers())) {
					method = m;
					break;
				}
			}
			if (method == null) {
				throw new Error("No static 1 arg method named: "+methodName+" in "+clazz);
			}			
		}
		
		private Method method;
		public Object f(Object o) {
			try {
				return method.invoke(null, o);
			} catch (Exception e) {
				throw new Error(e);
			}
		}
	}
	
	public static <S,T> FunST<S,T> fromMap(final Map<S,T> m) {
		return new FunST<S,T>() {
			public T f(S o) {
				return m.get(o);
			}
		};
	}
	
	public static <VI,V2,V1 extends VI,VO extends V2> V2[] apply(V1[] source, V2[] dest, FunST<VI,VO> applier) {
		for (int i = 0; i < source.length; ++i)
			dest[i] = applier.f(source[i]);
		return dest;
	}
	
	public static <K1, K2, V, K1E extends K1> HashMap<K2,V> applyKey(Map<K1E,V> map, FunST<K1,K2> applier) {
		HashMap<K2,V> out = new HashMap<K2,V>();
		for (Map.Entry<K1E, V> e : map.entrySet()) {
			out.put(applier.f(e.getKey()), e.getValue());
		}
		return out;
	}
	
	public static <V1, V2, MV extends V1> ArrayList<V2> apply(MV[] list, FunST<V1,V2> applier) {
		ArrayList<V2> l = new ArrayList<V2>();
		for (MV i : list) {
			l.add(applier.f(i));
		}
		return l;
	}
	
	public static <V1, V2, MV extends V1, C extends Iterable<MV>> ArrayList<V2> apply(C list, FunST<V1,V2> applier) {
		ArrayList<V2> l = new ArrayList<V2>();
		for (MV i : list) {
			l.add(applier.f(i));
		}
		return l;
	}
	
	public static <K, V, V2, MV extends V> HashMap<K,V2> apply(Map<K,MV> map, FunST<V,V2> applier) {
		HashMap<K,V2> out = new HashMap<K,V2>();
		for (Map.Entry<K, MV> e : map.entrySet()) {
			out.put(e.getKey(), applier.f(e.getValue()));
		}
		return out;
	}
	
	public static <K1, K2, V, V2, MV extends V, M extends Map<K2,MV>> HashMap<K1,HashMap<K2,V2>> apply2(Map<K1,M> map, FunST<V,V2> applier) {
		HashMap<K1,HashMap<K2,V2>> out = new HashMap<K1,HashMap<K2,V2>>();
		for (Map.Entry<K1, M> e : map.entrySet()) {
			out.put(e.getKey(), apply(e.getValue(), applier));
		}
		return out;
	}
	
}
