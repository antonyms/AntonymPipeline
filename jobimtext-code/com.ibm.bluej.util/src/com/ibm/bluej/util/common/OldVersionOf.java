package com.ibm.bluej.util.common;

/**
 * Used by RefactoringObjectInputStream
 * When breaking serialization compatibility for a class Foo
 * 1) copy the old version to a new class name FooV1.
 * 2) have FooV1 implement OldVersionOf Foo
 * 3) change Foo in the way desired and write the convert function for FooV1
 * 4) update the serialVersionId in Foo
 * 5) create a mapping in serializedMappings.properties: com.ibm.Foo:oldSerialVersionId -> com.ibm.FooV1
 * @author mrglass
 *
 * @param <T> the class that it is an old version of
 */
public interface OldVersionOf<T> {
	public T convert();
}
