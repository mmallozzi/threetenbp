/*
 * Copyright (c) 2011 Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package javax.time.calendar;

import static javax.time.calendar.ISODateTimeRule.DAY_OF_MONTH;
import static javax.time.calendar.ISODateTimeRule.EPOCH_DAY;
import static javax.time.calendar.ISODateTimeRule.MONTH_OF_QUARTER;
import static javax.time.calendar.ISODateTimeRule.MONTH_OF_YEAR;
import static javax.time.calendar.ISODateTimeRule.QUARTER_OF_YEAR;
import static javax.time.calendar.ISODateTimeRule.YEAR;
import static javax.time.calendar.ISODateTimeRule.ZERO_EPOCH_MONTH;
import static javax.time.calendar.ISOPeriodUnit.QUARTERS;
import static org.testng.Assert.assertEquals;

import java.io.Serializable;

import javax.time.MathUtils;
import javax.time.i18n.CopticChronology;

/**
 * The rules of date and time for zero-epoch-quarter.
 * <p>
 * This class is final, immutable and thread-safe.
 *
 * @author Stephen Colebourne
 */
public final class ZeroEpochQuarterDateTimeRule extends DateTimeRule implements Serializable {
    // This is a test to ensure that the longrules mechanism works
    public static void main(String[] args) {
        assertEquals(DAY_OF_MONTH.valueRange(12, DAY_OF_MONTH), DateTimeRuleRange.of(1, 28, 31));
        assertEquals(EPOCH_DAY.valueRange(12, DAY_OF_MONTH), DateTimeRuleRange.of(1, 31));
        assertEquals(EPOCH_DAY.valueRange(34, DAY_OF_MONTH), DateTimeRuleRange.of(1, 28));
        assertEquals(EPOCH_DAY.valueRange(64, DAY_OF_MONTH), DateTimeRuleRange.of(1, 31));
        assertEquals(EPOCH_DAY.valueRange(94, DAY_OF_MONTH), DateTimeRuleRange.of(1, 30));
        assertEquals(INSTANCE.valueRange(0, DAY_OF_MONTH), DateTimeRuleRange.of(1, 28, 31));
        assertEquals(INSTANCE.valueRange(1, DAY_OF_MONTH), DateTimeRuleRange.of(1, 30, 31));
        assertEquals(INSTANCE.valueRange(2, DAY_OF_MONTH), DateTimeRuleRange.of(1, 30, 31));
        assertEquals(INSTANCE.valueRange(3, DAY_OF_MONTH), DateTimeRuleRange.of(1, 30, 31));
        assertEquals(INSTANCE.valueRange(4, DAY_OF_MONTH), DateTimeRuleRange.of(1, 28, 31));
        //assertEquals(INSTANCE.valueRange(12, DAY_OF_MONTH), DateTimeRuleRange.of(1, 29, 31));
        assertEquals(INSTANCE.extract(4, DAY_OF_MONTH), Long.MIN_VALUE);
        assertEquals(INSTANCE.extract(4, MONTH_OF_QUARTER), Long.MIN_VALUE);
        assertEquals(INSTANCE.extract(4, MONTH_OF_YEAR), Long.MIN_VALUE);
        assertEquals(INSTANCE.extract(4, QUARTER_OF_YEAR), 1);
        System.out.println("OK");
    }

    /**
     * Singleton.
     */
    public static final DateTimeRule INSTANCE = new ZeroEpochQuarterDateTimeRule();;

    /**
     * Serialization version.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Restricted constructor.
     */
    private ZeroEpochQuarterDateTimeRule() {
        super("ZeroEpochQuarter", QUARTERS, null,
                DateTimeRuleRange.of(Year.MIN_YEAR * 4L, Year.MAX_YEAR * 4L), MONTH_OF_YEAR);
    }

    /**
     * Deserialize singletons.
     * 
     * @return the resolved value, not null
     */
    private Object readResolve() {
        return INSTANCE;
    }

    //-----------------------------------------------------------------------
    @Override
    protected DateTimeRuleRange valueRange(long zeq, DateTimeRule requiredRule) {
        if (requiredRule.equals(DAY_OF_MONTH)) {
            long qoy = qoyFromZeq(zeq);
            if (qoy == 1) {
                long year = yFromZeq(zeq);
                return DateTimeRuleRange.of(1, CopticChronology.isLeapYear(year) ? 29 : 28, 31);
            } else {
                return DateTimeRuleRange.of(1, 30, 31);
            }
        }
        return super.valueRange(zeq, requiredRule);
    }

    //-----------------------------------------------------------------------
    @Override
    protected long extract(long zeq, DateTimeRule requiredRule) {
        if (this == requiredRule) {
            return zeq;
        }
        if (requiredRule instanceof ISODateTimeRule) {
            long result = QUARTER_OF_YEAR.extract(qoyFromZeq(zeq), requiredRule);
            if (result != Long.MIN_VALUE) {
                return result;
            }
            result = YEAR.extract(yFromZeq(zeq), requiredRule);
            if (result != Long.MIN_VALUE) {
                return result;
            }
            return Long.MIN_VALUE;
        }
        return requiredRule.extractFrom(this, zeq);
    }

    @Override
    protected long extractFrom(DateTimeRule valueRule, long value) {
        long zem = valueRule.extract(value, ZERO_EPOCH_MONTH);
        if (zem != Long.MIN_VALUE) {
            return extractFromZem(zem);
        }
        return Long.MIN_VALUE;
    }

    //-----------------------------------------------------------------------
    private static long yFromZeq(long zeq) {
        return MathUtils.floorDiv(zeq, 4);
    }

    private static int qoyFromZeq(long zeq) {
        return MathUtils.floorMod(zeq, 4) + 1;
    }

    private long extractFromZem(long zem) {
        return MathUtils.floorDiv(zem, 3);
    }

    //-----------------------------------------------------------------------
    @Override
    public int compareTo(DateTimeRule other) {
        if (other == INSTANCE) {
            return 0;
        }
        return super.compareTo(other);
    }

    @Override
    public boolean equals(Object obj) {
        return (obj == INSTANCE);
    }

    @Override
    public int hashCode() {
        return ZeroEpochQuarterDateTimeRule.class.hashCode();
    }

}
