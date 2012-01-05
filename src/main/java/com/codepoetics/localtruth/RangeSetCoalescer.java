package com.codepoetics.localtruth;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.google.common.collect.Sets;

public final class RangeSetCoalescer<T extends Comparable<T>> {
	
	public static <T extends Comparable<T>> RangeSetCoalescer<T> newSimplifier() {
		return new RangeSetCoalescer<T>();
	}

	public Set<Range<T>> coalesce(Collection<Range<T>> ranges) {
	    Collection<Range<T>> coalesced = Lists.newLinkedList();
	    return coalesce(ranges, coalesced);
	}
	
	private Set<Range<T>> coalesce(Collection<Range<T>> ranges, Collection<Range<T>> coalesced) {
		for (Range<T> range : ranges) {
		    coalesceWith(range, coalesced);
		}
		return Sets.newHashSet(coalesced);
	}
	
	private void coalesceWith(Range<T> range, Collection<Range<T>> coalesced) {
	    Range<T> accumulator = range;
	    Iterator<Range<T>> iterator = coalesced.iterator();
	    while (iterator.hasNext()) {
	        Range<T> other = iterator.next();
            if (accumulator.isConnected(other)) {
                iterator.remove();
                accumulator = accumulator.span(other);
            }
	    }
	    coalesced.add(accumulator);
    }
	
	public Set<Range<T>> mergeCoalesced(Collection<Range<T>> rangeSetA, Collection<Range<T>> rangeSetB) {
		if (rangeSetA.size() < rangeSetB.size()) {
			return coalesce(rangeSetA, rangeSetB);
		}
		return coalesce(rangeSetB, rangeSetA);
	}
}