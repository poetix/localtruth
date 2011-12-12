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
	
	public Set<Range<T>> toDiscreteRanges(Collection<Range<T>> ranges) {
		ImmutableSet.Builder<Range<T>> result = ImmutableSet.builder();
		Collection<Range<T>> remainder = new LinkedList<Range<T>>(ranges);
		Iterator<Range<T>> iterator = remainder.iterator();
		
        while(iterator.hasNext()) {
        	Range<T> accumulator = iterator.next();
    		iterator.remove();
            result.add(mergeAllConnected(iterator, accumulator));
    		iterator = remainder.iterator();
        }
        
        return result.build();
    }
	
	public Set<Range<T>> mergeToDiscreteRanges(Collection<Range<T>> rangeSetA, Collection<Range<T>> rangeSetB) {
		if (rangeSetA.size() < rangeSetB.size()) {
			return _mergeToDiscreteRanges(rangeSetA, rangeSetB);
		}
		return _mergeToDiscreteRanges(rangeSetB, rangeSetA);
	}
	
	private Set<Range<T>> _mergeToDiscreteRanges(Collection<Range<T>> smaller, Collection<Range<T>> larger) {
		ImmutableSet.Builder<Range<T>> result = ImmutableSet.builder();
		Collection<Range<T>> remainder = new LinkedList<Range<T>>(larger);
		
		for (Range<T> newRange : smaller) {
			result.add(mergeAllConnected(remainder.iterator(), newRange));
		}
        result.addAll(remainder);
        return result.build();
    }

	private Range<T> mergeAllConnected(Iterator<Range<T>> iterator, Range<T> accumulator) {
		while (iterator.hasNext()) {
		    accumulator = mergeIfConnected(iterator, accumulator);
		}
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