package com.ibm.bluej.util.common;


/*
Copyright (c) 2012 IBM Corp.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

/*
 * Created on Oct 15, 2009
 * 
 */

import java.io.*;
import java.util.*;

public class IdentitySet<T> implements Set<T>, Serializable {
        private static final long serialVersionUID = 1L;
        private IdentityHashMap<T, Boolean> map;
        
        public IdentitySet() {
                 map = new IdentityHashMap<T, Boolean>();
        }
        
        public boolean add(T o) {
                if (o == null) {
                        throw new Error("Tried to add a null element to an IdentitySet");
                }
                Boolean ret = map.put(o, Boolean.TRUE);
                if (ret == null) {
                        return true;
                }
                return false;
        }

        public boolean addAll(Collection<? extends T> c) {
                boolean changed = false;
                for (T x : c) {
                        changed |= add(x);
                }
                return changed;
        }

public void clear() {
    map.clear();
}

public boolean contains(Object o) {
    return map.containsKey(o);
}

public boolean containsAll(Collection<?> c) {
    boolean allContained = true;
    for (Object x : c) {
            allContained &= contains(x);
    }
    return allContained;
}

public boolean isEmpty() {
    return map.isEmpty();
}

public Iterator<T> iterator() {
    return map.keySet().iterator();
}

public boolean remove(Object o) {
    return (map.remove(o) != null);
}

public boolean removeAll(Collection<?> c) {
    boolean changed = false;
    for (Object x : c) {
            changed |= remove(x);
    }
    return changed;
}

public boolean retainAll(Collection<?> c) {
    throw new UnsupportedOperationException();
    //return false;
}

public int size() {
    return map.size();
}

public Object[] toArray() {
    return map.keySet().toArray();
}

public <T> T[] toArray(T[] a) {
    return map.keySet().toArray(a);
}

}