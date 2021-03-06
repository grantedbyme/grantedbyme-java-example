/*
 * =BEGIN MIT LICENSE
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 GrantedByMe
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 * =END MIT LICENSE
 */
package com.grantedbyme.example;

import grantedbyme.GrantedByMe;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.apache.log4j.LogManager;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.Integer;
import java.lang.Long;

public class ServletAjax extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doProcess(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doProcess(request, response);
    }

    public void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");

        String operation = request.getParameter("operation");
        int challengeType = Integer.valueOf(request.getParameter("challenge_type"));
        Boolean isSuccess = false;
        JSONObject result = null;
        JSONObject defaultResult = null;
        try {
            defaultResult = (JSONObject) new JSONParser().parse("{\"success\": false, \"error\": 0}");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (operation != null) {
            _log(operation);
            GrantedByMe sdk = ServletUtils.getSDK(this);
            //sdk.setDebugMode(true);
            // process operation
            String challenge = null;
            if (operation.equals("getChallenge")) {
                result = sdk.getChallenge(challengeType);
            } else if (operation.equals("getChallengeState")) {
                challenge = request.getParameter("challenge");
                result = sdk.getChallengeState(challenge);
                if(challengeType == GrantedByMe.CHALLENGE_AUTHORIZE) {
                    handleGetAccountState(result, challenge, sdk);
                } else if(challengeType == GrantedByMe.CHALLENGE_AUTHENTICATE) {
                    handleGetSessionState(result);
                } else if(challengeType == GrantedByMe.CHALLENGE_PROFILE) {
                    handleGetRegisterState(result, challenge, sdk);
                }
            }
        }
        if (result == null) {
            result = defaultResult;
        }

        PrintWriter out = response.getWriter();
        out.print(result.toJSONString());
        out.close();
    }

    private void _log(String message) {
        LogManager.getRootLogger().info("[ServletAjax]: " + message);
    }

    private void handleGetRegisterState(JSONObject result, String challenge, GrantedByMe sdk) {
        Boolean isSuccess = (Boolean) result.get("success");
        if (isSuccess) {
            Long status = (Long) result.get("status");
            _log("status: " + Long.toString(status));
            // wait until scanned QR code
            if (status == GrantedByMe.STATUS_VALIDATED) {
                // generate authenticator_secret used by GBM service for authentication
                String authenticator_secret = GrantedByMe.generateAuthenticatorSecret();
                // link challenge with authenticator_secret
                JSONObject linkResult = sdk.linkAccount(challenge, authenticator_secret);
                // process link result
                Boolean isLinkSuccess = (Boolean) linkResult.get("success");
                if (isLinkSuccess) {
                    // get registration profile data
                    JSONObject data = (JSONObject) result.get("data");
                    if (data != null) {
                        String email = (String) data.get("email");
                        String first_name = (String) data.get("first_name");
                        String last_name = (String) data.get("last_name");
                        _log("email: " + email);
                        _log("first_name: " + first_name);
                        _log("last_name: " + last_name);
                        // TODO: register user with authenticator_secret and profile data
                    }
                } else {
                    Long linkError = (Long) linkResult.get("error");
                    _log("linkError: " + Long.toString(linkError));
                }
            }
        } else {
            Long error = (Long) result.get("error");
            _log("error: " + Long.toString(error));
        }
    }

    private void handleGetAccountState(JSONObject result, String challenge, GrantedByMe sdk) {
        Boolean isSuccess = (Boolean) result.get("success");
        if (isSuccess) {
            Long status = (Long) result.get("status");
            _log("status: " + Long.toString(status));
            // wait until scanned QR code
            if (status == GrantedByMe.STATUS_VALIDATED) {
                // generate authenticator_secret used by GBM service for authentication
                String authenticator_secret = GrantedByMe.generateAuthenticatorSecret();
                // link current challenge with authenticator_secret
                JSONObject linkResult = sdk.linkAccount(challenge, authenticator_secret);
                // process link result
                Boolean isLinkSuccess = (Boolean) linkResult.get("success");
                if (isLinkSuccess) {
                    // TODO: link authenticator_secret with current logged-in user id
                } else {
                    Long linkError = (Long) linkResult.get("error");
                    _log("linkError: " + Long.toString(linkError));
                }
            }
        } else {
            Long error = (Long) result.get("error");
            _log("error: " + Long.toString(error));
        }
    }

    private void handleGetSessionState(JSONObject result) {
        Boolean isSuccess = (Boolean) result.get("success");
        if (isSuccess) {
            Long status = (Long) result.get("status");
            _log("status: " + Long.toString(status));
            // wait until scanned QR code
            if (status == GrantedByMe.STATUS_VALIDATED) {
                String authenticator_secret = (String) result.get("authenticator_secret");
                // TODO: login user by authenticator_secret
            }
        } else {
            Long error = (Long) result.get("error");
            _log("error: " + Long.toString(error));
        }
    }
}
