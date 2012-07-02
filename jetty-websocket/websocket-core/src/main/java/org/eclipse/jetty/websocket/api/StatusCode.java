package org.eclipse.jetty.websocket.api;

/**
 * The <a href="https://tools.ietf.org/html/rfc6455#section-7.4">RFC 6455 specified status codes</a>.
 */
public class StatusCode
{
    /**
     * 1000 indicates a normal closure, meaning that the purpose for which the connection was established has been fulfilled.
     * <p>
     * See <a href="https://tools.ietf.org/html/rfc6455#section-7.4.1">RFC 6455, Section 7.4.1 Defined Status Codes</a>.
     */
    public final static int NORMAL = 1000;
    /**
     * 1001 indicates that an endpoint is "going away", such as a server going down or a browser having navigated away from a page.
     * <p>
     * See <a href="https://tools.ietf.org/html/rfc6455#section-7.4.1">RFC 6455, Section 7.4.1 Defined Status Codes</a>.
     */
    public final static int SHUTDOWN = 1001;
    /**
     * 1002 indicates that an endpoint is terminating the connection due to a protocol error.
     * <p>
     * See <a href="https://tools.ietf.org/html/rfc6455#section-7.4.1">RFC 6455, Section 7.4.1 Defined Status Codes</a>.
     */
    public final static int PROTOCOL = 1002;
    /**
     * 1003 indicates that an endpoint is terminating the connection because it has received a type of data it cannot accept (e.g., an endpoint that understands
     * only text data MAY send this if it receives a binary message).
     * <p>
     * See <a href="https://tools.ietf.org/html/rfc6455#section-7.4.1">RFC 6455, Section 7.4.1 Defined Status Codes</a>.
     */
    public final static int BAD_DATA = 1003;
    /**
     * Reserved. The specific meaning might be defined in the future.
     * <p>
     * See <a href="https://tools.ietf.org/html/rfc6455#section-7.4.1">RFC 6455, Section 7.4.1 Defined Status Codes</a>.
     */
    public final static int UNDEFINED = 1004;
    /**
     * 1005 is a reserved value and MUST NOT be set as a status code in a Close control frame by an endpoint. It is designated for use in applications expecting
     * a status code to indicate that no status code was actually present.
     * <p>
     * See <a href="https://tools.ietf.org/html/rfc6455#section-7.4.1">RFC 6455, Section 7.4.1 Defined Status Codes</a>.
     */
    public final static int NO_CODE = 1005;
    /**
     * 1006 is a reserved value and MUST NOT be set as a status code in a Close control frame by an endpoint. It is designated for use in applications expecting
     * a status code to indicate that the connection was closed abnormally, e.g., without sending or receiving a Close control frame.
     * <p>
     * See <a href="https://tools.ietf.org/html/rfc6455#section-7.4.1">RFC 6455, Section 7.4.1 Defined Status Codes</a>.
     */
    public final static int NO_CLOSE = 1006;
    /**
     * 1007 indicates that an endpoint is terminating the connection because it has received data within a message that was not consistent with the type of the
     * message (e.g., non-UTF-8 [<a href="https://tools.ietf.org/html/rfc3629">RFC3629</a>] data within a text message).
     * <p>
     * See <a href="https://tools.ietf.org/html/rfc6455#section-7.4.1">RFC 6455, Section 7.4.1 Defined Status Codes</a>.
     */
    public final static int BAD_PAYLOAD = 1007;
    /**
     * 1008 indicates that an endpoint is terminating the connection because it has received a message that violates its policy. This is a generic status code
     * that can be returned when there is no other more suitable status code (e.g., 1003 or 1009) or if there is a need to hide specific details about the
     * policy.
     * <p>
     * See <a href="https://tools.ietf.org/html/rfc6455#section-7.4.1">RFC 6455, Section 7.4.1 Defined Status Codes</a>.
     */
    public final static int POLICY_VIOLATION = 1008;
    /**
     * 1009 indicates that an endpoint is terminating the connection because it has received a message that is too big for it to process.
     * <p>
     * See <a href="https://tools.ietf.org/html/rfc6455#section-7.4.1">RFC 6455, Section 7.4.1 Defined Status Codes</a>.
     */
    public final static int MESSAGE_TOO_LARGE = 1009;
    /**
     * 1010 indicates that an endpoint (client) is terminating the connection because it has expected the server to negotiate one or more extension, but the
     * server didn't return them in the response message of the WebSocket handshake. The list of extensions that are needed SHOULD appear in the /reason/ part
     * of the Close frame. Note that this status code is not used by the server, because it can fail the WebSocket handshake instead.
     * <p>
     * See <a href="https://tools.ietf.org/html/rfc6455#section-7.4.1">RFC 6455, Section 7.4.1 Defined Status Codes</a>.
     */
    public final static int REQUIRED_EXTENSION = 1010;
    /**
     * 1011 indicates that a server is terminating the connection because it encountered an unexpected condition that prevented it from fulfilling the request.
     * <p>
     * See <a href="https://tools.ietf.org/html/rfc6455#section-7.4.1">RFC 6455, Section 7.4.1 Defined Status Codes</a>.
     */
    public final static int SERVER_ERROR = 1011;
    /**
     * 1015 is a reserved value and MUST NOT be set as a status code in a Close control frame by an endpoint. It is designated for use in applications expecting
     * a status code to indicate that the connection was closed due to a failure to perform a TLS handshake (e.g., the server certificate can't be verified).
     * <p>
     * See <a href="https://tools.ietf.org/html/rfc6455#section-7.4.1">RFC 6455, Section 7.4.1 Defined Status Codes</a>.
     */
    public final static int FAILED_TLS_HANDSHAKE = 1015;
}
