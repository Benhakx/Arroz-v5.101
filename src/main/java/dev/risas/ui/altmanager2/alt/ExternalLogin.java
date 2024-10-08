package dev.risas.ui.altmanager2.alt;

import com.sun.net.httpserver.HttpServer;
import fr.litarvan.openauth.microsoft.HttpClient;
import fr.litarvan.openauth.microsoft.MicrosoftAuthResult;
import fr.litarvan.openauth.microsoft.model.response.MinecraftProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Session;
import org.apache.commons.lang3.RandomUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.*;

public class ExternalLogin {

    private MicrosoftAuthResult response;
    private final GuiAltLogin screen;

    public ExternalLogin(GuiAltLogin screen) {
        this.screen = screen;
    }

    public MicrosoftAuthResult getAccessToken() throws Exception {
        HttpClient http = new HttpClient();
        int random = RandomUtils.nextInt(8080, 8999);
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(random), 0);
        String authUrl = "https://login.live.com/oauth20_authorize.srf?client_id=2fa2913a-ebf9-4bb7-b16a-c3e7e437b5bc&response_type=code&redirect_uri=http://localhost:" + random + "/&scope=XboxLive.signin%20offline_access";

        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection stringSelection = new StringSelection(authUrl);
        clip.setContents(stringSelection, stringSelection);

        httpServer.createContext("/", exchange -> {
            String code = exchange.getRequestURI().toString();
            code = code.substring(code.lastIndexOf('=') + 1);

            try {
                JSONObject tokenMap = new JSONObject();
                tokenMap.put("client_id", "2fa2913a-ebf9-4bb7-b16a-c3e7e437b5bc");
                tokenMap.put("code", code);
                tokenMap.put("grant_type", "authorization_code");
                tokenMap.put("redirect_uri", "http://localhost:" + random + "/");
                tokenMap.put("client_secret", "yax8Q~qfPbqyR_6Qmau3J93dVFuO5cvk4w4uQcLk");

                String oauth = http.readPost("https://login.live.com/oauth20_token.srf", toMap(tokenMap), "application/x-www-form-urlencoded");
                String access_token = new JSONObject(oauth).getString("access_token");

                JSONObject authObj = new JSONObject();
                authObj.put("RelyingParty", "http://auth.xboxlive.com");
                authObj.put("TokenType", "JWT");

                JSONObject propObj = new JSONObject();
                propObj.put("RpsTicket", "d=" + access_token);
                propObj.put("SiteName", "user.auth.xboxlive.com");
                propObj.put("AuthMethod", "RPS");
                authObj.put("Properties", propObj);

                JSONObject xbl = new JSONObject(http.readPost("https://user.auth.xboxlive.com/user/authenticate", authObj, "application/json", "application/json"));
                String xbl_token = xbl.getString("Token");

                JSONObject authObj2 = new JSONObject();
                authObj2.put("RelyingParty", "rp://api.minecraftservices.com/");
                authObj2.put("TokenType", "JWT");

                JSONObject propObj2 = new JSONObject();
                propObj2.put("SandboxId", "RETAIL");
                propObj2.put("UserTokens", new String[]{ xbl_token });
                authObj2.put("Properties", propObj2);

                JSONObject xsts = new JSONObject(http.readPost("https://xsts.auth.xboxlive.com/xsts/authorize", authObj2, "application/json", "application/json"));
                String xsts_token = xsts.getString("Token");
                String xsts_uhs = xsts.getJSONObject("DisplayClaims").getJSONArray("xui").getJSONObject(0).getString("uhs");

                JSONObject xboxAuth = new JSONObject();
                xboxAuth.put("identityToken", String.format("XBL3.0 x=%s;%s", xsts_uhs, xsts_token));
                xboxAuth.put("ensureLegacyEnabled", "true");

                JSONObject resp = new JSONObject(http.readPost("https://api.minecraftservices.com/authentication/login_with_xbox", xboxAuth, "application/json"));

                MinecraftProfile profile = http.getJson(
                        "https://api.minecraftservices.com/minecraft/profile",
                        resp.getString("access_token"), MinecraftProfile.class
                );

                response = new MicrosoftAuthResult(profile, resp.getString("access_token"));

                Minecraft.getMinecraft().session = new Session(response.getProfile().getName(), response.getProfile().getId(), response.getAccessToken(), "legacy");
                screen.setStatus(EnumChatFormatting.GREEN + "Logged in as " + response.getProfile().getName());

                String success = "Successfully logged into account, now you can close this window.";
                exchange.sendResponseHeaders(200, success.length());
                OutputStream responseBody = exchange.getResponseBody();
                responseBody.write(success.getBytes(StandardCharsets.UTF_8));
                responseBody.close();

                httpServer.stop(2);
            } catch (Exception ex) {
                ex.printStackTrace();
                httpServer.stop(2);
            }
        });

        httpServer.setExecutor(null);
        httpServer.start();
        return response;
    }

    private Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<>();
        Iterator<String> keys = object.keys();

        while(keys.hasNext()) {
            String key = keys.next();
            Object value = object.get(key);

            if (value instanceof JSONArray)
                value = toList((JSONArray) value);
            else if (value instanceof JSONObject)
                value = toMap((JSONObject) value);

            map.put(key, value);
        }

        return map;
    }

    private List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<>();

        for(int i = 0; i < array.length(); i++) {
            Object value = array.get(i);

            if (value instanceof JSONArray)
                value = toList((JSONArray) value);
            else if (value instanceof JSONObject)
                value = toMap((JSONObject) value);

            list.add(value);
        }

        return list;
    }
}