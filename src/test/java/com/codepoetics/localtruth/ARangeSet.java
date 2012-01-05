package com.codepoetics.localtruth;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;

public final class ARangeSet {
    public static <T extends Comparable<T>> Matcher<RangeSet<T>> withTheRanges(final Range<T>...rangeArgs) {
        return new TypeSafeMatcher<RangeSet<T>>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("A multirange containing the ranges ");
                description.appendValueList("[", ",", "]", rangeArgs);
            }
            
            @Override
            public boolean matchesSafely(RangeSet<T> item) {
                return Matchers.<Range<T>>hasItems(rangeArgs).matches(item.ranges());
            }
        };
    }
    
    public static <T extends Comparable<T>> Matcher<RangeSet<T>> containingTheElements(final T...elements) {
        return new TypeSafeMatcher<RangeSet<T>>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("A multirange containing the elements ");
                description.appendValueList("[", ",", "]", elements);
            }
            
            @Override
            public boolean matchesSafely(RangeSet<T> item) {
                return Iterables.all(Lists.newArrayList(elements), item);
            }
        };
    }
    
    public static <T extends Comparable<T>> Matcher<RangeSet<T>> containingNoneOfTheElements(final T...elements) {
        return new TypeSafeMatcher<RangeSet<T>>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("A multirange containing none of the elements ");
                description.appendValueList("[", ",", "]", elements);
            }
            
            @Override
            public boolean matchesSafely(RangeSet<T> item) {
                return !Iterables.any(Lists.newArrayList(elements), item);
            }
        };
    }
}