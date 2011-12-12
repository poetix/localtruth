package com.codepoetics.localtruth;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import com.google.common.collect.Ranges;

import static org.hamcrest.MatcherAssert.assertThat;

public class MultiRangeTest {
    
    public static class MultiRange<T extends Comparable<T>> {
        
        private final Set<Range<T>> ranges;
        
        public static <T extends Comparable<T>> MultiRange<T> empty() {
            return new MultiRange<T>(ImmutableSet.<Range<T>>of());
        }
        
        public static <T extends Comparable<T>> MultiRange<T> containing(Range<T>...ranges) {
            return new MultiRange<T>(simplify(ranges));
        }
        
        private static <T extends Comparable<T>> Set<Range<T>> simplify(Range<T>...ranges) {
            ImmutableSet.Builder<Range<T>> results = ImmutableSet.builder();
            Collection<Range<T>> remainder = Lists.newLinkedList();
            Iterator<Range<T>> iterator = Lists.newArrayList(ranges).iterator();
            while(iterator.hasNext()) {
                Range<T> accumulator = iterator.next();
                while (iterator.hasNext()) {
                    Range<T> other = iterator.next();
                    if (accumulator.isConnected(other)) {
                        accumulator = accumulator.span(other);
                    } else {
                        remainder.add(other);
                    }
                }
                results.add(accumulator);
                iterator = remainder.iterator();
                remainder = Lists.newLinkedList();
            }
            
            return results.build();
        }
        
        private MultiRange(Set<Range<T>> ranges) {
            this.ranges = ranges;
        }
        
        public Collection<Range<T>> ranges() {
            return this.ranges;
        }
    }
    
    @Test public void
    an_empty_multirange_contains_no_ranges() {
        MultiRange<Integer> multiRange = MultiRange.empty();
        assertThat(multiRange.ranges(), Matchers.<Range<Integer>>empty());
    }
    
    @SuppressWarnings("unchecked")
    @Test public void
    adjacent_ranges_are_concatenated() {
        MultiRange<Integer> multiRange = MultiRange.containing(Ranges.closed(0, 1), Ranges.closed(1, 2), Ranges.closed(3, 4), Ranges.closed(4, 5));
        assertThat(multiRange.ranges(), Matchers.<Range<Integer>>contains(Ranges.closed(0, 2), Ranges.closed(3, 5)));
    }
    
    @SuppressWarnings("unchecked")
    @Test public void
    overlapping_ranges_are_concatenated() {
        MultiRange<Integer> multiRange = MultiRange.containing(Ranges.closed(0, 2), Ranges.closed(1, 3));
        assertThat(multiRange.ranges(), Matchers.<Range<Integer>>contains(Ranges.closed(0, 3)));
    }
    
    @SuppressWarnings("unchecked")
    @Test public void
    non_touching_open_ranges_are_not_concatenated() {
        MultiRange<Integer> multiRange = MultiRange.containing(Ranges.open(0, 1), Ranges.open(1, 2));
        assertThat(multiRange.ranges(), Matchers.<Range<Integer>>containsInAnyOrder(Ranges.open(0, 1), Ranges.open(1, 2)));
    }
    

}
