package com.codepoetics.localtruth;

import java.util.Collection;

import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.google.common.collect.Range;

public class RangeSet<T extends Comparable<T>> implements Predicate<T> {
    
    private final Collection<Range<T>> ranges;
    
    RangeSet(Collection<Range<T>> ranges) {
        this.ranges = ranges;
    }
    
    public int size() {
        return ranges.size();
    }
    
    public Collection<Range<T>> ranges() {
        return this.ranges;
    }
    
    public RangeSet<T> union(RangeSet<T> other) {
        return RangeSets.union(this, other);
    }
    
    public RangeSet<T> intersection(RangeSet<T> other) {
        return RangeSets.intersection(this, other);
    }
    
    @Override public String toString() {
    	Joiner joiner = Joiner.on(", ");
    	return "RangeSet: " + joiner.join(ranges);
    }

    @Override
    public boolean apply(T element) {
        return contains(element);
    }
    
    public boolean contains(T element) {
        for (Range<T> range : ranges) {
            if (range.apply(element)) {
                return true;
            }
        }
        return false;
    }
}