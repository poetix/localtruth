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
        
        assertThat(RangeSets.containing(ranges).size(), Matchers.is(0));
    }
    
    @SuppressWarnings("unchecked")
    @Test public void
    adjacent_ranges_are_coalesced() {
        Collection<Range<Integer>> ranges = newArrayList(
                Ranges.closed(0, 3),
                Ranges.closed(3, 5));
        
        assertThat(RangeSets.containing(ranges), ARangeSet.withTheRanges(Ranges.closed(0, 5)));
    }
    
    @SuppressWarnings("unchecked")
    @Test public void
    overlapping_ranges_are_coalesced() {
        Collection<Range<Integer>> ranges = newArrayList(
                Ranges.closed(0, 2),
                Ranges.closed(1, 3));
        
        assertThat(RangeSets.containing(ranges), ARangeSet.withTheRanges(Ranges.closed(0, 3)));
    }
    
    @SuppressWarnings("unchecked")
    @Test public void
    ranges_contained_in_other_ranges_are_absorbed() {
        Collection<Range<Integer>> ranges = newArrayList(
                Ranges.closed(0, 4),
                Ranges.closed(1, 3));
        
        assertThat(RangeSets.containing(ranges), ARangeSet.withTheRanges(Ranges.closed(0, 4)));
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
        
        assertThat(RangeSets.containing(ranges), ARangeSet.withTheRanges(Ranges.closed(0, 5)));
    }
    
    @SuppressWarnings("unchecked")
    @Test public void
    non_touching_open_ranges_are_not_coalesced() {
        Collection<Range<Integer>> ranges = newArrayList(
                Ranges.open(0, 1),
                Ranges.open(1, 2));
        
        assertThat(RangeSets.containing(ranges), ARangeSet.withTheRanges(Ranges.open(0, 1), Ranges.open(1, 2)));
    }
    
    @SuppressWarnings("unchecked")
    @Test public void
    discrete_groups_of_connected_ranges_are_coalesced_separately() {
        Collection<Range<Integer>> ranges = newArrayList(
                Ranges.closed(0, 1),
                Ranges.closed(9, 10),
                Ranges.closed(8, 9),
                Ranges.closed(1, 2));
        
        assertThat(RangeSets.containing(ranges), ARangeSet.withTheRanges(Ranges.closed(0, 2), Ranges.closed(8, 10)));
    }
    
    @SuppressWarnings("unchecked")
    @Test public void
    the_union_of_two_rangesets_is_a_rangeset_containing_the_coalesced_set_of_their_combined_ranges() {
        RangeSet<Integer> rangeSet1 = RangeSets.containing(Ranges.closed(0, 1), Ranges.closed(9, 10));
        RangeSet<Integer> rangeSet2 = RangeSets.containing(Ranges.closed(1, 2), Ranges.closed(8, 9));
        
        assertThat(RangeSets.union(rangeSet1, rangeSet2), ARangeSet.withTheRanges(Ranges.closed(0, 2), Ranges.closed(8, 10)));
    }
    
    @SuppressWarnings("unchecked")
    @Test public void
    the_intersection_of_two_rangesets_is_a_rangeset_containing_the_intersections_of_their_ranges() {
        RangeSet<Integer> rangeSet1 = RangeSets.containing(Ranges.closed(0, 3), Ranges.closed(7, 10));
        RangeSet<Integer> rangeSet2 = RangeSets.containing(Ranges.closed(2, 5), Ranges.closed(6, 9));
        
        assertThat(RangeSets.intersection(rangeSet1, rangeSet2), ARangeSet.withTheRanges(Ranges.closed(2, 3), Ranges.closed(7, 9)));
    }
}
