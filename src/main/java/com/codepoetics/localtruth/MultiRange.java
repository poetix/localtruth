package com.codepoetics.localtruth;

import static com.google.common.base.Predicates.notNull;
import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;

import java.util.Collection;
import java.util.Set;

import com.google.common.base.Function;
import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.google.common.collect.Sets;

public class MultiRange<T extends Comparable<T>> {
    
    private final Set<Range<T>> ranges;
    
    public static <T extends Comparable<T>> MultiRange<T> empty() {
        return new MultiRange<T>(ImmutableSet.<Range<T>>of());
    }
    
    public static <T extends Comparable<T>> MultiRange<T> containing(Range<T>...ranges) {
        return containing(Lists.newArrayList(ranges));
    }
    
    public static <T extends Comparable<T>> MultiRange<T> containing(Collection<Range<T>> ranges) {
    	RangeSetSimplifier<T> simplifier = RangeSetSimplifier.newSimplifier();
    	Set<Range<T>> simplifiedRanges = simplifier.toDiscreteRanges(ranges);
    	return new MultiRange<T>(simplifiedRanges);
    }
    
    private MultiRange(Set<Range<T>> ranges) {
        this.ranges = ranges;
    }
    
    public MultiRange<T> union(MultiRange<T> other) {
    	RangeSetSimplifier<T> simplifier = RangeSetSimplifier.newSimplifier();
    	Set<Range<T>> simplifiedRanges = simplifier.mergeToDiscreteRanges(ranges, other.ranges);
    	return new MultiRange<T>(simplifiedRanges);
    }
    
    public MultiRange<T> intersection(MultiRange<T> other) {
    	Iterable<Range<T>> intersections = filter(concat(transform(ranges, intersectionWith(other.ranges))),
    			notNull());
    	return new MultiRange<T>(Sets.newHashSet(intersections));
    }
    
    private Function<Range<T>, Iterable<Range<T>>> intersectionWith(final Iterable<Range<T>> otherRanges) {
    	return new Function<Range<T>, Iterable<Range<T>>>() {
			@Override public Iterable<Range<T>> apply(Range<T> range) {
				return Iterables.transform(otherRanges, intersectionWith(range));
			}
		};
    }

    private Function<Range<T>, Range<T>> intersectionWith(final Range<T> range) {
    	return new Function<Range<T>, Range<T>>() {
			@Override public Range<T> apply(Range<T> otherRange) {
				if (range.isConnected(otherRange)) {
					return range.intersection(otherRange);
				}
				return null;
			}
		};
    }
    
    public Collection<Range<T>> ranges() {
        return this.ranges;
    }
    
    @Override public String toString() {
    	Joiner joiner = Joiner.on(", ");
    	return "Multirange: " + joiner.join(ranges);
    }
}