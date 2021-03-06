/*
 * Copyright 2013 Red Hat Inc. and/or its affiliates and other contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.switchyard.security.credential.extractor;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;
import org.switchyard.common.io.pull.StringPuller;
import org.switchyard.security.credential.AuthorizationTokenCredential;
import org.switchyard.security.credential.Credential;
import org.switchyard.security.credential.NameCredential;
import org.switchyard.security.credential.PasswordCredential;

/**
 * AuthorizationHeaderCredentialExtractor tests.
 *
 * @author David Ward &lt;<a href="mailto:dward@jboss.org">dward@jboss.org</a>&gt; &copy; 2012 Red Hat Inc.
 */
public class AuthorizationHeaderCredentialExtractorTests {

    private static final String BASE_PATH = "/org/switchyard/security/credential/extractor/AuthorizationHeaderCredentialExtractorTests-";
    private static final String BASIC_TXT = BASE_PATH + "Basic.txt";
    private static final String BASIC2_TXT = BASE_PATH + "Basic2.txt";

    private static final String DIGEST_TXT = BASE_PATH + "Digest.txt";
    private static final String DIGEST2_TXT = BASE_PATH + "Digest2.txt";
    private static final String BEARER_TXT = BASE_PATH + "Bearer.txt";

    @Test
    public void testBasic() throws Exception {
        String source = new StringPuller().pull(BASIC_TXT, AuthorizationHeaderCredentialExtractorTests.class);
        Set<Credential> creds = new AuthorizationHeaderCredentialExtractor().extract(source);
        boolean foundName = false;
        boolean foundPassword = false;
        for (Credential cred : creds) {
            if (cred instanceof NameCredential) {
                foundName = true;
                String name = ((NameCredential)cred).getName();
                Assert.assertEquals("Aladdin", name);
            } else if (cred instanceof PasswordCredential) {
                foundPassword = true;
                String password = new String(((PasswordCredential)cred).getPassword());
                Assert.assertEquals("open sesame", password);
            }
        }
        if (!foundName) {
            Assert.fail("name not found");
        }
        if (!foundPassword) {
            Assert.fail("password not found");
        }
    }

    // SWITCHYARD-2953
    @Test
    public void testBasic2() throws Exception {
        String source = new StringPuller().pull(BASIC2_TXT, AuthorizationHeaderCredentialExtractorTests.class);
        Set<Credential> creds = new AuthorizationHeaderCredentialExtractor().extract(source);
        boolean foundName = false;
        boolean foundPassword = false;
        for (Credential cred : creds) {
            if (cred instanceof NameCredential) {
                foundName = true;
                String name = ((NameCredential)cred).getName();
                Assert.assertEquals("Aladdin", name);
            } else if (cred instanceof PasswordCredential) {
                foundPassword = true;
                String password = new String(((PasswordCredential)cred).getPassword());
                Assert.assertEquals("open sesame", password);
            }
        }
        if (!foundName) {
            Assert.fail("name not found");
        }
        if (!foundPassword) {
            Assert.fail("password not found");
        }
    }

    @Test
    public void testDigest() throws Exception {
        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // https://issues.jboss.org/browse/SWITCHYARD-1082
        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        String source = new StringPuller().pull(DIGEST_TXT, AuthorizationHeaderCredentialExtractorTests.class);
        Set<Credential> creds = new AuthorizationHeaderCredentialExtractor().extract(source);
        boolean foundName = false;
        for (Credential cred : creds) {
            if (cred instanceof NameCredential) {
                foundName = true;
                String name = ((NameCredential)cred).getName();
                Assert.assertEquals("Mufasa", name);
            }
        }
        if (!foundName) {
            Assert.fail("name not found");
        }
        // TODO: complete per SWITCHYARD-1082
    }

    // SWITCHYARD-2953
    @Test
    public void testDigest2() throws Exception {
        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // https://issues.jboss.org/browse/SWITCHYARD-1082
        // !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        String source = new StringPuller().pull(DIGEST2_TXT, AuthorizationHeaderCredentialExtractorTests.class);
        Set<Credential> creds = new AuthorizationHeaderCredentialExtractor().extract(source);
        boolean foundName = false;
        for (Credential cred : creds) {
            if (cred instanceof NameCredential) {
                foundName = true;
                String name = ((NameCredential)cred).getName();
                Assert.assertEquals("Mufasa", name);
            }
        }
        if (!foundName) {
            Assert.fail("name not found");
        }
        // TODO: complete per SWITCHYARD-1082
    }

    @Test
    public void testBearer() throws Exception {
        String source = new StringPuller().pull(BEARER_TXT, AuthorizationHeaderCredentialExtractorTests.class);
        Set<Credential> creds = new AuthorizationHeaderCredentialExtractor().extract(source);
        Assert.assertEquals(1, creds.size());
        Credential credential = creds.iterator().next();
        Assert.assertEquals(AuthorizationTokenCredential.class, credential.getClass());
        AuthorizationTokenCredential authzTokenCredential = (AuthorizationTokenCredential)credential;
        Assert.assertEquals("Bearer", authzTokenCredential.getSchema());
        Assert.assertEquals("mytoken-0123456789", authzTokenCredential.getToken());
    }
}
