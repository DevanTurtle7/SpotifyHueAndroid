/**
 * A custom JSON request, where the return type is a JSON array and the body is a JSON object.
 *
 * @author Devan Kavalchek
 */

package com.devankav.spotifyhue.requests;

import androidx.annotation.Nullable;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class JsonArrayBodyRequest extends JsonRequest<JSONArray> {

    /**
     * The constructor
     * @param method The JSON method (get, post, put, etc.)
     * @param url The URL the request is being made to
     * @param requestBody The body of the request
     * @param listener // The listener
     * @param errorListener // The error listener
     */
    public JsonArrayBodyRequest(int method, String url, JSONObject requestBody, Response.Listener<JSONArray> listener, @Nullable Response.ErrorListener errorListener) {
        super(method, url, requestBody == null ? null : requestBody.toString(), listener, errorListener);
    }

    @Override
    protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            return Response.success(new JSONArray(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }
}
