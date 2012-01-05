package com.codepoetics.localtruth;

import java.util.Collection;
import java.util.Iterator;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.google.common.collect.Sets;

import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;

public final class RangeSets {
    
    private RangeSets() { }

    public static <T extends Comparable<T>> RangeSet<T> empty() {
        return new RangeSet<T>(ImmutableSet.<Range<T>>of());
    }
    
    public static <T extends Comparable<T>> RangeSet<T> containing(Range<T>...ranges) {
        return containing(Lists.newArrayList(ranges));
    }
    
    public static <T extends Comparable<T>> RangeSet<T> containing(Collection<Range<T>> ranges) {
        Collection<Range<T>> coalesced = Lists.newLinkedList();
        return coalesce(ranges, coalesced);
    }
	
	public static <T extends Comparable<T>> RangeSet<T> union(RangeSet<T> rangeSetA, RangeSet<T> rangeSetB) {
	    if (rangeSetA.size() < rangeSetB.size()) {
	        return coalesce(rangeSetA.ranges(), rangeSetB.ranges());
	    }
	    return coalesce(rangeSetB.ranges(), rangeSetA.ranges());
	}
    
    public static <T extends Comparable<T>> RangeSet<T> intersection(RangeSet<T> self, RangeSet<T> other) {
        Iterable<Range<T>> intersections = filter(concat(transform(self.ranges(),
                                                                   intersectionWith(other.ranges()))),
                                                  notNull());
        return new RangeSet<T>(Sets.newHashSet(intersections));
    }
    
    private static <T extends Comparable<T>> Function<Range<T>, Iterable<Range<T>>> intersectionWith(final Iterable<Range<T>> otherRanges) {
        return new Function<Range<T>, Iterable<Range<T>>>() {
            @Override public Iterable<Range<T>> apply(Range<T> range) {
                return Iterables.transform(otherRanges, intersectionWith(range));
            }
        };
    }

    private static <T extends Comparable<T>> Function<Range<T>, Range<T>> intersectionWith(final Range<T> range) {
        return new Function<Range<T>, Range<T>>() {
            @Override public Range<T> apply(Range<T> otherRange) {
                if (range.isConnected(otherRange)) {
                    return range.intersection(otherRange);
                }
                return null;
            }
        };
    }
	
	private static <T extends Comparable<T>> RangeSet<T> coalesce(Collection<Range<T>> ranges, Collection<Range<T>> coalesced) {
	    Collection<Range<T>> mutableCoalesced = Lists.newLinkedList(coalesced);
		for (Range<T> range : ranges) {
		    coalesceWith(range, mutableCoalesced);
		}
		return new RangeSet<T>(mutableCoalesced);
	}
	
	private static <T extends Comparable<T>> void coalesceWith(Range<T> range, Collection<Range<T>> coalesced) {
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
}