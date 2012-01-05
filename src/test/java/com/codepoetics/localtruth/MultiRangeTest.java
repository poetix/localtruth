package com.codepoetics.localtruth;

import static org.hamcrest.MatcherAssert.assertThat;


import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import com.google.common.collect.Range;
import com.google.common.collect.Ranges;

public class MultiRangeTest {
    
    @SuppressWarnings("unchecked")
	@Test public void
    the_union_of_two_multiranges_is_a_multirange_containing_the_simplified_set_of_their_combined_ranges() {
    	RangeSet<Integer> multirange1 = RangeSet.containing(Ranges.closed(0, 1), Ranges.closed(9, 10));
    	RangeSet<Integer> multirange2 = RangeSet.containing(Ranges.closed(1, 2), Ranges.closed(8, 9));
    	
    	assertThat(multirange1.union(multirange2), aMultiRangeWithTheRanges(Ranges.closed(0, 2), Ranges.closed(8, 10)));
    }
    
    @SuppressWarnings("unchecked")
	@Test public void
    the_intersection_of_two_multiranges_is_a_multirange_containing_the_intersections_of_their_ranges() {
    	RangeSet<Integer> multirange1 = RangeSet.containing(Ranges.closed(0, 3), Ranges.closed(7, 10));
    	RangeSet<Integer> multirange2 = RangeSet.containing(Ranges.closed(2, 5), Ranges.closed(6, 9));
    	
    	assertThat(multirange1.intersection(multirange2), aMultiRangeWithTheRanges(Ranges.closed(2, 3), Ranges.closed(7, 9)));
    }

	private Matcher<RangeSet<Integer>> aMultiRangeWithTheRanges(final Range<Integer>...rangeArgs) {
		return new TypeSafeMatcher<RangeSet<Integer>>() {
			@Override
			public void describeTo(Description description) {
				description.appendText("A multirange containing the ranges ");
				description.appendValueList("[", ",", "]", rangeArgs);
			}
			
			@Override
			public boolean matchesSafely(RangeSet<Integer> item) {
				return Matchers.<Range<Integer>>hasItems(rangeArgs).matches(item.ranges());
			}
		};
	}
}