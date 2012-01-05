package com.codepoetics.localtruth;

import java.util.Collection;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.google.common.collect.Range;
import com.google.common.collect.Ranges;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.MatcherAssert.assertThat;

public class RangeSetsTest {
	
    @Test public void
    an_empty_list_of_ranges_is_simplified_to_an_empty_set() {
        Collection<Range<Integer>> ranges = newArrayList();
        
		assertThat(RangeSets.coalesce(ranges).size(), Matchers.is(0));
    }
	
	@SuppressWarnings("unchecked")
    @Test public void
    adjacent_ranges_are_coalesced() {
        Collection<Range<Integer>> ranges = newArrayList(
        		Ranges.closed(0, 3),
        		Ranges.closed(3, 5));
        
		assertThat(RangeSets.coalesce(ranges), ARangeSet.withTheRanges(Ranges.closed(0, 5)));
    }
    
    @SuppressWarnings("unchecked")
    @Test public void
    overlapping_ranges_are_coalesced() {
    	Collection<Range<Integer>> ranges = newArrayList(
    			Ranges.closed(0, 2),
    			Ranges.closed(1, 3));
    	
    	assertThat(RangeSets.coalesce(ranges), ARangeSet.withTheRanges(Ranges.closed(0, 3)));
    }
    
    @SuppressWarnings("unchecked")
    @Test public void
    ranges_contained_in_other_ranges_are_absorbed() {
    	Collection<Range<Integer>> ranges = newArrayList(
    			Ranges.closed(0, 4),
    			Ranges.closed(1, 3));
    	
    	assertThat(RangeSets.coalesce(ranges), ARangeSet.withTheRanges(Ranges.closed(0, 4)));
    }
    
    @SuppressWarnings("unchecked")
    @Test public void
    multiple_connected_ranges_are_coalesced() {
    	Collection<Range<Integer>> ranges = newArrayList(
    			Ranges.closed(0, 1),
    			Ranges.closed(3, 4),
    			Ranges.closed(1, 2),
    			Ranges.closed(4, 5),
    			Ranges.closed(2, 3));
    	
    	assertThat(RangeSets.coalesce(ranges), ARangeSet.withTheRanges(Ranges.closed(0, 5)));
    }
    
    @SuppressWarnings("unchecked")
    @Test public void
    non_touching_open_ranges_are_not_coalesced() {
    	Collection<Range<Integer>> ranges = newArrayList(
    			Ranges.open(0, 1),
    			Ranges.open(1, 2));
    	
        assertThat(RangeSets.coalesce(ranges), ARangeSet.withTheRanges(Ranges.open(0, 1), Ranges.open(1, 2)));
    }
    
    @SuppressWarnings("unchecked")
    @Test public void
    discrete_groups_of_connected_ranges_are_coalesced_separately() {
    	Collection<Range<Integer>> ranges = newArrayList(
    			Ranges.closed(0, 1),
    			Ranges.closed(9, 10),
    			Ranges.closed(8, 9),
    			Ranges.closed(1, 2));
    	
        assertThat(RangeSets.coalesce(ranges), ARangeSet.withTheRanges(Ranges.closed(0, 2), Ranges.closed(8, 10)));
    }
}
