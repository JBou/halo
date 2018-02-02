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
package io.omam.halo;

import static io.omam.halo.MulticastDns.TYPE_SRV;

import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

/**
 * DNS SRV (service) record.
 * <p>
 * According to <a href=
 * "https://developer.apple.com/library/content/documentation/Cocoa/Conceptual/NetServices/Introduction.html#//apple_ref/doc/uid/TP40002445-SW1">Apple
 * Bonjour</a>, both the priority and weight are always {@code 0}.
 */
@SuppressWarnings("javadoc")
final class SrvRecord extends DnsRecord {

    private final short port;

    private final String server;

    SrvRecord(final String aName, final short aClass, final Duration aTtl, final Instant now, final short aPort,
            final String aServer) {
        super(aName, TYPE_SRV, aClass, aTtl, now);
        Objects.requireNonNull(aServer);
        port = aPort;
        server = aServer;
    }

    @Override
    public final String toString() {
        return "SrvRecord [name="
            + name()
            + ", type="
            + type()
            + ", class="
            + clazz()
            + ", ttl="
            + ttl()
            + ", server="
            + server
            + ", port="
            + port
            + "]";
    }

    @Override
    protected final void write(final MessageOutputStream mos) {
        /* priority and weight are always 0. */
        mos.writeShort(0);
        mos.writeShort(0);
        mos.writeShort(port);
        mos.writeName(server);
    }

    final short port() {
        return port;
    }

    final String server() {
        return server;
    }

}
