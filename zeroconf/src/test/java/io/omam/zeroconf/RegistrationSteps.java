/*
Copyright 2018 Cedric Liegeois

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
package io.omam.zeroconf;

import static io.omam.zeroconf.ZeroconfAssert.assertAttributesEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import cucumber.api.java.After;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

/**
 * Steps to tests service resolution.
 */
@SuppressWarnings("javadoc")
public final class RegistrationSteps {

    private final Engines engines;

    private final Exceptions exceptions;

    private Optional<Service> zcs;

    public RegistrationSteps(final Engines someEngines, final Exceptions someExceptions) {
        engines = someEngines;
        exceptions = someExceptions;
        zcs = Optional.empty();
    }

    @After
    public final void after() {
        zcs = Optional.empty();
    }

    @Given("^the following service has been registered with \"(Zeroconf|JmDNS)\":$")
    public final void givenServiceRegistered(final String engine, final List<ServiceDetails> service)
            throws IOException {
        assertEquals(1, service.size());
        if (engine.equals("Zeroconf")) {
            zcs = Optional.of(engines.zc().register(engines.toZc(service.get(0)), false));
        } else {
            engines.jmdns().registerService(engines.toJmdns(service.get(0)));
        }
    }

    @Then("^the following registered service shall be returned:$")
    public final void thenServiceReturned(final List<ServiceDetails> service) {
        assertEquals(1, service.size());
        final ServiceDetails expected = service.get(0);
        assertTrue(zcs.isPresent());
        final Service actual = zcs.get();
        assertEquals(expected.instanceName(), actual.instanceName());
        assertEquals(expected.registrationType(), actual.registrationType());
        assertEquals(expected.port(), actual.port());
        assertEquals(expected.priority(), actual.priority());
        assertTrue(actual.attributes().isPresent());
        assertAttributesEquals(engines.toZc(expected.text()), actual.attributes().get());
        assertEquals(expected.weight(), actual.weight());
    }

    @When("^the following service is registered with \"Zeroconf\"( not)? allowing instance name change:$")
    public final void whenServiceRegistered(final String nameChangeNotAllowed,
            final List<ServiceDetails> service) {
        try {
            final boolean allowNameChange = nameChangeNotAllowed == null;
            zcs = Optional.of(engines.zc().register(engines.toZc(service.get(0)), allowNameChange));
        } catch (final IOException e) {
            zcs = Optional.empty();
            exceptions.thrown(e);
        }
    }

}
