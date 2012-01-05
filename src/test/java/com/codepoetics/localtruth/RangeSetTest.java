package com.codepoetics.localtruth;

import static org.hamcrest.MatcherAssert.assertThat;


import org.junit.Test;

import com.google.common.collect.Ranges;

public class RangeSetTest {
    
    @SuppressWarnings("unchecked")
	@Test public void
    the_union_of_two_rangesets_is_a_rangeset_containing_the_coalesced_set_of_their_combined_ranges() {
    	RangeSet<Integer> multirange1 = RangeSets.containing(Ranges.closed(0, 1), Ranges.closed(9, 10));
    	RangeSet<Integer> multirange2 = RangeSets.containing(Ranges.closed(1, 2), Ranges.closed(8, 9));
    	
    	assertThat(multirange1.union(multirange2), ARangeSet.withTheRanges(Ranges.closed(0, 2), Ranges.closed(8, 10)));
    }
    
    @SuppressWarnings("unchecked")
	@Test public void
    the_intersection_of_two_rangesets_is_a_rangeset_containing_the_intersections_of_their_ranges() {
    	RangeSet<Integer> multirange1 = RangeSets.containing(Ranges.closed(0, 3), Ranges.closed(7, 10));
    	RangeSet<Integer> multirange2 = RangeSets.containing(Ranges.closed(2, 5), Ranges.closed(6, 9));
    	
    	assertThat(multirange1.intersection(multirange2), ARangeSet.withTheRanges(Ranges.closed(2, 3), Ranges.closed(7, 9)));
    }
}