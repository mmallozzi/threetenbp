/*
 * Copyright (c) 2012, Stephen Colebourne & Michael Nascimento Santos
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
package javax.time.builder;

import javax.time.Duration;
import javax.time.LocalDate;
import javax.time.LocalDateTime;
import javax.time.LocalTime;
import javax.time.calendrical.DateTimeRuleRange;

/**
 * A calendar system.
 * 
 * @author Stephen Colebourne
 */
public interface Chrono {

    String getName();

    //-----------------------------------------------------------------------
    DateTimeRuleRange getRange(DateTimeField field);

    DateTimeRuleRange getRange(DateTimeField field, LocalDate date, LocalTime time);

    //-----------------------------------------------------------------------
    int getValue(DateTimeField field, LocalDate date, LocalTime time);

    //-----------------------------------------------------------------------
    LocalDate setDate(DateTimeField field, LocalDate date, int newValue);

    LocalTime setTime(DateTimeField field, LocalTime time, int newValue);

    LocalDateTime setDateTime(DateTimeField field, LocalDateTime dateTime, int newValue);

    //-----------------------------------------------------------------------
    LocalDate setDateLenient(DateTimeField field, LocalDate date, int newValue);

    LocalTime setTimeLenient(DateTimeField field, LocalTime time, int newValue);

    LocalDateTime setDateTimeLenient(DateTimeField field, LocalDateTime dateTime, int newValue);

    //-----------------------------------------------------------------------
    LocalDate addDate(DateTimeField field, LocalDate date, int amount);

    LocalDate rollDate(DateTimeField field, LocalDate date, int roll);

    //-----------------------------------------------------------------------
    Duration getEstimatedDuration(PeriodUnit unit);

    Duration getDurationBetween(LocalDate date1, LocalTime time1, LocalDate date2, LocalTime time2);

    long getPeriodBetween(PeriodUnit unit, LocalDate date1, LocalTime time1, LocalDate date2, LocalTime time2);

}
