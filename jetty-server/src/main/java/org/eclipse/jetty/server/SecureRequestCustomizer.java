//
//  ========================================================================
//  Copyright (c) 1995-2015 Mort Bay Consulting Pty. Ltd.
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
//

package org.eclipse.jetty.server;

import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLSession;
import javax.servlet.ServletRequest;

import org.eclipse.jetty.http.BadMessageException;
import org.eclipse.jetty.http.HttpScheme;
import org.eclipse.jetty.io.ssl.SslConnection;
import org.eclipse.jetty.io.ssl.SslConnection.DecryptedEndPoint;
import org.eclipse.jetty.util.TypeUtil;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.util.ssl.SniX509ExtendedKeyManager;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.ssl.X509;

/**
 * <p>Customizer that extracts the attribute from an {@link SSLContext}
 * and sets them on the request with {@link ServletRequest#setAttribute(String, Object)}
 * according to Servlet Specification Requirements.</p>
 */
public class SecureRequestCustomizer implements HttpConfiguration.Customizer
{
    private static final Logger LOG = Log.getLogger(SecureRequestCustomizer.class);

    /**
     * The name of the SSLSession attribute that will contain any cached information.
     */
    public static final String CACHED_INFO_ATTR = CachedInfo.class.getName();

    private boolean _sniHostCheck;


    public SecureRequestCustomizer()
    {
        this(true);
    }

    public SecureRequestCustomizer(boolean sniHostCheck)
    {
        _sniHostCheck=sniHostCheck;
    }

    @Override
    public void customize(Connector connector, HttpConfiguration channelConfig, Request request)
    {
        if (request.getHttpChannel().getEndPoint() instanceof DecryptedEndPoint)
        {
            request.setScheme(HttpScheme.HTTPS.asString());
            request.setSecure(true);
            SslConnection.DecryptedEndPoint ssl_endp = (DecryptedEndPoint)request.getHttpChannel().getEndPoint();
            SslConnection sslConnection = ssl_endp.getSslConnection();
            SSLEngine sslEngine=sslConnection.getSSLEngine();
            customize(sslEngine,request);
        }
    }

    /**
     * <p>Customizes the request attributes to be set for SSL requests.</p>
     * <p>The requirements of the Servlet specs are:</p>
     * <ul>
     * <li> an attribute named "javax.servlet.request.ssl_session_id" of type
     * String (since Servlet Spec 3.0).</li>
     * <li> an attribute named "javax.servlet.request.cipher_suite" of type
     * String.</li>
     * <li> an attribute named "javax.servlet.request.key_size" of type Integer.</li>
     * <li> an attribute named "javax.servlet.request.X509Certificate" of type
     * java.security.cert.X509Certificate[]. This is an array of objects of type
     * X509Certificate, the order of this array is defined as being in ascending
     * order of trust. The first certificate in the chain is the one set by the
     * client, the next is the one used to authenticate the first, and so on.
     * </li>
     * </ul>
     *
     * @param request HttpRequest to be customized.
     */
    public void customize(SSLEngine sslEngine, Request request)
    {
        request.setScheme(HttpScheme.HTTPS.asString());
        SSLSession sslSession = sslEngine.getSession();

        if (_sniHostCheck)
        {
            String name = request.getServerName();
            @SuppressWarnings("unchecked")
            X509 x509 = (X509)sslSession.getValue(SniX509ExtendedKeyManager.SNI_X509);

            if (x509!=null && !x509.matches(name))
            {
                LOG.warn("Host {} does not match SNI {}",name,x509);
                throw new BadMessageException(400,"Host does not match SNI");
            }

            if (LOG.isDebugEnabled())
                LOG.debug("Host {} matched SNI {}",name,x509);
        }

        try
        {
            String cipherSuite=sslSession.getCipherSuite();
            Integer keySize;
            X509Certificate[] certs;
            String idStr;

            CachedInfo cachedInfo=(CachedInfo)sslSession.getValue(CACHED_INFO_ATTR);
            if (cachedInfo!=null)
            {
                keySize=cachedInfo.getKeySize();
                certs=cachedInfo.getCerts();
                idStr=cachedInfo.getIdStr();
            }
            else
            {
                keySize=SslContextFactory.deduceKeyLength(cipherSuite);
                certs=SslContextFactory.getCertChain(sslSession);
                byte[] bytes = sslSession.getId();
                idStr = TypeUtil.toHexString(bytes);
                cachedInfo=new CachedInfo(keySize,certs,idStr);
                sslSession.putValue(CACHED_INFO_ATTR,cachedInfo);
            }

            if (certs!=null)
                request.setAttribute("javax.servlet.request.X509Certificate",certs);

            request.setAttribute("javax.servlet.request.cipher_suite",cipherSuite);
            request.setAttribute("javax.servlet.request.key_size",keySize);
            request.setAttribute("javax.servlet.request.ssl_session_id", idStr);
        }
        catch (Exception e)
        {
            LOG.warn(Log.EXCEPTION,e);
        }
    }

    @Override
    public String toString()
    {
        return String.format("%s@%x",this.getClass().getSimpleName(),hashCode());
    }

    /**
     * Simple bundle of information that is cached in the SSLSession. Stores the
     * effective keySize and the client certificate chain.
     */
    private static class CachedInfo
    {
        private final X509Certificate[] _certs;
        private final Integer _keySize;
        private final String _idStr;

        CachedInfo(Integer keySize, X509Certificate[] certs,String idStr)
        {
            this._keySize=keySize;
            this._certs=certs;
            this._idStr=idStr;
        }

        X509Certificate[] getCerts()
        {
            return _certs;
        }

        Integer getKeySize()
        {
            return _keySize;
        }

        String getIdStr()
        {
            return _idStr;
        }
    }
}
