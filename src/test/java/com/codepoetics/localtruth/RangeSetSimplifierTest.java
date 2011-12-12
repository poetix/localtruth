package com.codepoetics.localtruth;

import static com.google.common.collect.Lists.newArrayList;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.Collection;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;

import com.google.common.collect.Range;
import com.google.common.collect.Ranges;

public class RangeSetSimplifierTest {
	
	private final RangeSetSimplifier<Integer> simplifier = RangeSetSimplifier.newSimplifier();
	
    @Test public void
    an_empty_list_of_ranges_is_simplified_to_an_empty_set() {
        Collection<Range<Integer>> ranges = newArrayList();
        
		assertThat(simplifier.simplify(ranges).size(), Matchers.is(0));
    }
	
	@SuppressWarnings("unchecked")
    @Test public void
    adjacent_ranges_are_concatenated() {
        Collection<Range<Integer>> ranges = newArrayList(
        		Ranges.closed(0, 3),
        		Ranges.closed(3, 5));
        
		assertThat(simplifier.simplify(ranges), aSetOfRangesContaining(Ranges.closed(0, 5)));
    }
    
    @SuppressWarnings("unchecked")
    @Test public void
    overlapping_ranges_are_concatenated() {
    	Collection<Range<Integer>> ranges = newArrayList(
    			Ranges.closed(0, 2),
    			Ranges.closed(1, 3));
    	
    	assertThat(simplifier.simplify(ranges), aSetOfRangesContaining(Ranges.closed(0, 3)));
    }
    
    @SuppressWarnings("unchecked")
    @Test public void
    non_touching_open_ranges_are_not_concatenated() {
    	Collection<Range<Integer>> ranges = newArrayList(
    			Ranges.open(0, 1),
    			Ranges.open(1, 2));
    	
        assertThat(simplifier.simplify(ranges), aSetOfRangesContaining(Ranges.open(0, 1), Ranges.open(1, 2)));
    }
    
    @SuppressWarnings("unchecked")
    @Test public void
    discrete_groups_of_connected_ranges_are_concatenated_separately() {
    	Collection<Range<Integer>> ranges = newArrayList(
    			Ranges.closed(0, 1),
    			Ranges.closed(9, 10),
    			Ranges.closed(8, 9),
    			Ranges.closed(1, 2));
    	
        assertThat(simplifier.simplify(ranges), aSetOfRangesContaining(Ranges.closed(0, 2), Ranges.closed(8, 10)));
    }
    
    private Matcher<Iterable<Range<Integer>>> aSetOfRangesContaining(Range<Integer>...ranges) {
    	return Matchers.<Range<Integer>>hasItems(ranges);
    }
}
