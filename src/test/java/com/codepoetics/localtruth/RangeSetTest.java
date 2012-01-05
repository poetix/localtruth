package com.codepoetics.localtruth;

import org.junit.Test;

import com.google.common.collect.Ranges;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class RangeSetTest {
    
    @SuppressWarnings("unchecked")
    @Test public void
    a_range_set_contains_an_element_if_any_of_its_ranges_contains_the_element() {
        RangeSet<Integer> rangeSet = RangeSets.containing(Ranges.closed(0, 2), Ranges.closed(4, 6));
        
        assertThat(rangeSet.contains(1), is(true));
        assertThat(rangeSet.contains(5), is(true));
    }
    
    @SuppressWarnings("unchecked")
    @Test public void
    a_range_set_does_not_contain_an_element_if_none_of_its_ranges_contains_the_element() {
        RangeSet<Integer> rangeSet = RangeSets.containing(Ranges.closed(0, 2), Ranges.closed(4, 6));
        
        assertThat(rangeSet.contains(3), is(false));
    }
    

}