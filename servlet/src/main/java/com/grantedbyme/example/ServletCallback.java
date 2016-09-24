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

import grantedbyme.CryptoUtil;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.apache.log4j.LogManager;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.System;
import java.security.Security;
import java.util.HashMap;

public class ServletCallback extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doProcess(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doProcess(request, response);
    }

    public void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json; charset=UTF-8");
        // setup default response
        HashMap<String, Object> resultHashMap = new HashMap<>();
        resultHashMap.put("success", false);
        resultHashMap.put("error", 0);
        JSONObject result = null;
        // get request parameters
        String reqSignature = request.getParameter("signature");
        String reqPayload = request.getParameter("payload");
        String reqMessage = request.getParameter("message");
        // process request
        if (reqSignature != null && reqPayload != null) {
            // read private key
            String privateKey = null;
            InputStream privateKeyInputStream = getClass().getResourceAsStream("/private_key.pem");
            try {
                privateKey = IOUtils.toString(privateKeyInputStream);
            } finally {
                privateKeyInputStream.close();
            }
            // read server key
            String serverKey = null;
            InputStream serverKeyInputStream = getClass().getResourceAsStream("/server_key.pem");
            try {
                serverKey = IOUtils.toString(serverKeyInputStream);
            } finally {
                serverKeyInputStream.close();
            }
            // _log(serverKey);
            // initialize BouncyCastle security provider
            Security.insertProviderAt(new org.bouncycastle.jce.provider.BouncyCastleProvider(), 0);
            // decrypt request
            HashMap<String, Object> params = new HashMap<>();
            params.put("signature", reqSignature);
            params.put("payload", reqPayload);
            params.put("message", reqMessage);
            /*System.out.println("Signature: " + reqSignature);
            System.out.println("Payload: " + reqPayload);
            System.out.println("Message: " + reqMessage);*/
            JSONObject requestJSON = CryptoUtil.decryptAndVerify(new JSONObject(params), serverKey, privateKey);
            if (requestJSON != null) {
                _log(requestJSON.toJSONString());
                if(requestJSON.containsKey("operation")) {
                    String operation = (String) requestJSON.get("operation");
                    if(operation.equals("ping")) {
                        resultHashMap.put("success", true);
                    } else if(operation.equals("unlink_account")){
                        if(requestJSON.containsKey("authenticator_secret_hash")) {
                            // TODO: implement
                        }
                        resultHashMap.put("success", false);
                    } else if(operation.equals("rekey_account")){
                        if(requestJSON.containsKey("authenticator_secret_hash")) {
                            // TODO: implement
                        }
                        resultHashMap.put("success", false);
                    } else if(operation.equals("revoke_challenge")){
                        if(requestJSON.containsKey("challenge")) {
                            // TODO: implement
                        }
                        resultHashMap.put("success", false);
                    } else {
                        // operation not handled
                    }
                }
            }
            result = CryptoUtil.encryptAndSign(new JSONObject(resultHashMap), serverKey, privateKey);
        }
        // write out response
        PrintWriter out = response.getWriter();
        if(result != null) {
            out.print(result.toJSONString());
        } else {
            out.print(new JSONObject(resultHashMap).toJSONString());
        }
        out.close();
    }

    private void _log(String message) {
        LogManager.getRootLogger().info("[ServletCallback]: " + message);
    }
}
