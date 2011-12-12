package com.codepoetics.localtruth;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Range;

public final class RangeSetSimplifier<T extends Comparable<T>> {
	
	public static <T extends Comparable<T>> RangeSetSimplifier<T> newSimplifier() {
		return new RangeSetSimplifier<T>();
	}
	
	public Set<Range<T>> simplify(Collection<Range<T>> ranges) {
		ImmutableSet.Builder<Range<T>> result = ImmutableSet.builder();
		Collection<Range<T>> remainder = new LinkedList<Range<T>>(ranges);
		Iterator<Range<T>> iterator = remainder.iterator();
		
        while(iterator.hasNext()) {
            result.add(mergeConnected(iterator, getNewAccumulator(iterator)));
    		iterator = remainder.iterator();
        }
        
        return result.build();
    }

	private Range<T> mergeConnected(Iterator<Range<T>> iterator, Range<T> accumulator) {
		while (iterator.hasNext()) {
		    accumulator = mergeIfConnected(iterator, accumulator);
		}
		return accumulator;
	}

	private Range<T> getNewAccumulator(Iterator<Range<T>> iterator) {
		Range<T> accumulator = iterator.next();
		iterator.remove();
		return accumulator;
	}

	private Range<T> mergeIfConnected(Iterator<Range<T>> iterator, Range<T> accumulator) {
		Range<T> other = iterator.next();
		if (accumulator.isConnected(other)) {
			iterator.remove();
		    return accumulator.span(other);
		}
		return accumulator;
	}    	
}