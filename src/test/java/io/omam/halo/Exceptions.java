/*
Copyright 2018 - 2020 Cedric Liegeois

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.

    * Redistributions in binary form must reproduce the above
      copyright notice, this list of conditions and the following
      disclaimer in the documentation and/or other materials provided
      with the distribution.

    * Neither the name of the copyright holder nor the names of other
      contributors may be used to endorse or promote products derived
      from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package io.omam.halo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayDeque;
import java.util.Queue;

import io.cucumber.java.After;
import io.cucumber.java.en.Then;

/**
 * Steps pertaining to testing whether expected {@link Exception} have been thrown.
 */
@SuppressWarnings("javadoc")
public final class Exceptions {

    private final Queue<Exception> exs;

    /**
     * Constructor.
     */
    public Exceptions() {
        exs = new ArrayDeque<>();
    }

    @After
    public final void after() {
        assertTrue(exs.isEmpty(), "Unasserted exceptions: " + exs);
    }

    @Then("a {string} shall be thrown with message containing {string}")
    public final void thenExceptionThrow(final String exceptionClass, final String exceptionMessage) {
        final Exception ex = exs.poll();
        assertNotNull(ex);
        assertEquals(exceptionClass, ex.getClass().getName());
        final String[] sequences = exceptionMessage.split("\\(\\.\\.\\.\\)");
        for (final String sequence : sequences) {
            final String s = sequence.trim();
            assertTrue(ex.getMessage().contains(s),
                    "Expected message to contain [" + s + "] but was [" + ex.getMessage() + "]");
        }
    }

    final void thrown(final Exception exception) {
        exs.add(exception);
    }

}
