package com.codepoetics.localtruth;

import java.util.Collection;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableSet;
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
    
    public static <T extends Comparable<T>> RangeSet<T> intersection(final RangeSet<T> self, final RangeSet<T> other) {
        Iterable<Range<T>> intersections = filter(concat(transform(self.ranges(),
                                                                   intersectionWith(other.ranges()))),
                                                  notNull());
        return new RangeSet<T>(Sets.newHashSet(intersections));
    }
    
    private static <T extends Comparable<T>> Function<Range<T>, Iterable<Range<T>>> intersectionWith(final Iterable<Range<T>> otherRanges) {
        return new Function<Range<T>, Iterable<Range<T>>>() {
            @Override public Iterable<Range<T>> apply(Range<T> range) {
                return transform(otherRanges, intersectionWith(range));
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
	
	private static <T extends Comparable<T>> RangeSet<T> coalesce(final Collection<Range<T>> ranges, final Collection<Range<T>> coalesced) {
	    Collection<Range<T>> result = Lists.newLinkedList(coalesced);
		for (Range<T> range : ranges) {
		    result = coalesceWith(range, result);
		}
		return new RangeSet<T>(result);
	}
	
	private static <T extends Comparable<T>> Collection<Range<T>> coalesceWith(final Range<T> range, final Collection<Range<T>> coalesced) {
	    Range<T> accumulator = range;
	    Collection<Range<T>> result = Lists.newLinkedList();
	    for (Range<T> other : coalesced) {
            if (accumulator.isConnected(other)) {
                accumulator = accumulator.span(other);
            } else {
                result.add(other);
            }
	    }
	    result.add(accumulator);
	    return result;
    }
}