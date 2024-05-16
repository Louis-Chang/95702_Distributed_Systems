package ds.cmu.task1;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

public class RequestInfoWrapper extends HttpServletRequestWrapper {

    private final long timestamp;

    public RequestInfoWrapper(HttpServletRequest request) {
        super(request);
        this.timestamp = System.currentTimeMillis();
    }

    public String getUserAgent() {
        return getHeader("User-Agent");
    }

    public long getTimestamp() {
        return timestamp;
    }

}

